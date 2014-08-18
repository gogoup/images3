package com.images3.core.models.imageplant;

import java.util.List;

import com.images3.TemplateIdentity;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public class TemplateRepositoryService implements PaginatedResultDelegate<List<Template>> {
    
    private TemplateAccess templateAccess;
    private TemplateFactoryService templateFactory;
    
    public TemplateRepositoryService(TemplateAccess templateAccess,
            TemplateFactoryService templateFactory) {
        this.templateAccess = templateAccess;
        this.templateFactory = templateFactory;
    }

    public Template storeTemplate(TemplateEntity template) {
        checkIfVoid(template);
        TemplateOS objectSegment = template.getObjectSegment();
        if (template.isNew()) {
            templateAccess.insertTemplate(objectSegment);
        } else if (template.isDirty()) {
            templateAccess.updateTemplate(objectSegment);
        }
        template.cleanMarks();
        return templateFactory.reconstituteTemplate(
                (ImagePlantRoot) template.getImagePlant(), objectSegment);
    }
    
    public void removeTemplate(TemplateEntity template) {
        checkIfVoid(template);
        TemplateOS objectSegment = template.getObjectSegment();
        templateAccess.deleteTemplate(objectSegment);
        template.markAsVoid();
    }
    
    public Template findTemplateByName(ImagePlantRoot imagePlant, String name) {
        TemplateOS objectSegment = templateAccess.selectTemplateById(
                new TemplateIdentity(imagePlant.getId(), name));
        TemplateEntity entity = templateFactory.reconstituteTemplate(
                imagePlant, objectSegment);
        return entity;
    }
    
    public PaginatedResult<List<Template>> findAllTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), null);
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, osResult}){};
    }
    
    public PaginatedResult<List<Template>> findActiveTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), false);
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, osResult}){};
    }
    
    public PaginatedResult<List<Template>> findArchivedTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), true);
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, osResult}){};
    }
    
    private List<Template> getAllTemplatesByImagePlant(ImagePlantRoot imagePlant, 
            PaginatedResult<List<TemplateOS>> osResult,  Object pageCursor) {
        List<TemplateOS> objectSegments = null;
        if (null == pageCursor) {
            objectSegments = osResult.getAllResults();
        } else {
            objectSegments = osResult.getResult(pageCursor);
        }
        return templateFactory.reconstituteTemplates(imagePlant, objectSegments);
    }
    
    private void checkIfVoid(TemplateEntity template) {
        if (template.isVoid()) {
            throw new IllegalStateException(template.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Template> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getAllTemplatesByImagePlant".equals(methodName)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0]; 
            PaginatedResult<List<TemplateOS>> osResult = (PaginatedResult<List<TemplateOS>>) arguments[1];
            return getAllTemplatesByImagePlant(imagePlant, osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<Template> result) {
        if ("getAllTemplatesByImagePlant".equals(tag)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[1];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(tag);
    }
    
}
