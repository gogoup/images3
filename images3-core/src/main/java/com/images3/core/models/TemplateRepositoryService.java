package com.images3.core.models;

import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.core.Template;
import com.images3.core.infrastructure.data.TemplateOS;
import com.images3.core.infrastructure.data.spi.TemplateAccess;

public class TemplateRepositoryService {
    
    private TemplateAccess templateAccess;
    private TemplateFactoryService templateFactory;
    
    public TemplateRepositoryService(TemplateAccess templateAccess,
            TemplateFactoryService templateFactory) {
        this.templateAccess = templateAccess;
        this.templateFactory = templateFactory;
    }

    public TemplateEntity storeTemplate(TemplateEntity template) {
        checkIfVoid(template);
        TemplateOS objectSegment = template.getObjectSegment();
        if (template.isNew()) {
            templateAccess.insertTemplate(objectSegment);
        } else if (template.isDirty()) {
            templateAccess.updateTemplate(objectSegment);
        }
        template.markAsVoid();
        return templateFactory.reconstituteTemplate(
                (ImagePlantRoot) template.getImagePlant(), objectSegment);
    }
    
    public void removeTemplate(TemplateEntity template) {
        checkIfVoid(template);
        TemplateOS objectSegment = template.getObjectSegment();
        templateAccess.deleteTemplate(objectSegment);
        template.markAsVoid();
    }
    
    public Template findTemplateById(ImagePlantRoot imagePlant, String id) {
        TemplateOS objectSegment = templateAccess.selectTemplateById(imagePlant.getId(), id);
        return templateFactory.reconstituteTemplate(
                imagePlant, objectSegment);
    }
    
    public PaginatedResult<List<Template>> findAllTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, null}){};
    }
    
    public PaginatedResult<List<Template>> findActiveTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, false}){};
    }
    
    public PaginatedResult<List<Template>> findArchivedTemplatesByImagePlant(ImagePlantRoot imagePlant) {
        return new PaginatedResult<List<Template>>(
                this, "getAllTemplatesByImagePlant", new Object[]{imagePlant, true}){};
    }
    
    protected List<Template> getAllTemplatesByImagePlant(ImagePlantRoot imagePlant, Boolean isArchived, 
            Object pageCursor) {
        PaginatedResult<List<TemplateOS>> osResult =
                templateAccess.selectTemplatesByImagePlantId(imagePlant.getId(), isArchived);
        List<TemplateOS> objectSegments = osResult.getResult(pageCursor);
        return templateFactory.reconstituteTemplates(imagePlant, objectSegments);
    }
    
    
    private void checkIfVoid(TemplateEntity template) {
        if (template.isVoid()) {
            throw new IllegalStateException(template.toString());
        }
    }
    
}
