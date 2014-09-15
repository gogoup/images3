package com.images3.core.models.imageplant;

import java.util.List;

import com.images3.common.NoSuchEntityFoundException;
import com.images3.common.TemplateIdentity;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;

import org.gogoup.dddutils.pagination.AutoPaginatedResultDelegate;
import org.gogoup.dddutils.pagination.PaginatedResult;

public class TemplateRepositoryService extends AutoPaginatedResultDelegate<List<Template>> {
    
    private TemplateAccess templateAccess;
    private TemplateFactoryService templateFactory;
    
    public TemplateRepositoryService(TemplateAccess templateAccess,
            TemplateFactoryService templateFactory) {
        super(1, "getAllTemplatesByImagePlant");
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
        TemplateOS objectSegment = template.getObjectSegment();
        templateAccess.deleteTemplate(objectSegment);
        template.markAsVoid();
    }
    
    public void removeTemplates(ImagePlantRoot imagePlant) {
        templateAccess.deleteTemplatesByImagePlantId(imagePlant.getId());
    }
    
    public Template findTemplateByName(ImagePlantRoot imagePlant, String name) {
        TemplateOS objectSegment = templateAccess.selectTemplateById(
                new TemplateIdentity(imagePlant.getId(), name));
        TemplateEntity entity = templateFactory.reconstituteTemplate(
                imagePlant, objectSegment);
        if (null == entity) {
            throw new NoSuchEntityFoundException("Template", name);
        }
        return entity;
    }
    
    public PaginatedResult<List<Template>> findAllTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), null);
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, osResult});
    }
    
    public PaginatedResult<List<Template>> findActiveTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), false);
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, osResult});
    }
    
    public PaginatedResult<List<Template>> findArchivedTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), true);
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, osResult});
    }
   
    private void checkIfVoid(TemplateEntity template) {
        if (template.isVoid()) {
            throw new IllegalStateException(template.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Template> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getAllTemplatesByImagePlant".equals(tag)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0]; 
            PaginatedResult<List<TemplateOS>> osResult = (PaginatedResult<List<TemplateOS>>) arguments[1];
            List<TemplateOS> objectSegments = osResult.getResult(pageCursor);
            return templateFactory.reconstituteTemplates(imagePlant, objectSegments);
        }
        throw new UnsupportedOperationException(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Template> fetchAllResults(String tag, Object[] arguments) {
        if ("getAllTemplatesByImagePlant".equals(tag)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0]; 
            PaginatedResult<List<TemplateOS>> osResult = (PaginatedResult<List<TemplateOS>>) arguments[1];
            List<TemplateOS> objectSegments = osResult.getAllResults();
            return templateFactory.reconstituteTemplates(imagePlant, objectSegments);
        }
        throw new UnsupportedOperationException(tag);
    }
    
}
