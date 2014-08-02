package com.images3.core.models.imageplant;

import java.util.ArrayList;
import java.util.List;

import com.images3.DuplicateTemplateNameException;
import com.images3.ResizingConfig;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;

public class TemplateFactoryService {
    
    private TemplateAccess templateAccess;
    
    public TemplateFactoryService(TemplateAccess templateAccess) {
        this.templateAccess = templateAccess;
    }

    public TemplateEntity generateTemplate(ImagePlantRoot imagePlant, String name, 
            ResizingConfig resizingConfig) {
        if (templateAccess.isDuplicatedTemplateName(imagePlant.getId(), name)) {
            throw new DuplicateTemplateNameException(name);
        }
        String id = templateAccess.generateTemplateId(imagePlant.getObjectSegment());
        TemplateOS objectSegment = new TemplateOS(
                imagePlant.getId(), id, name, false, true, resizingConfig);
        TemplateEntity template = reconstituteTemplate(imagePlant, objectSegment);
        template.markAsNew();
        return template;
    }
    
    public TemplateEntity reconstituteTemplate(ImagePlantRoot imagePlant, 
            TemplateOS objectSegment) {
        if (null == objectSegment) {
            return null;
        }
        return new TemplateEntity(imagePlant, objectSegment);
    }
    
    public List<Template> reconstituteTemplates(ImagePlantRoot imagePlant, List<TemplateOS> objectSegments) {
        List<Template> templates = new ArrayList<Template>(objectSegments.size());
        for (TemplateOS os: objectSegments) {
            if (null == os) {
                throw new NullPointerException();
            }
            templates.add(reconstituteTemplate(imagePlant, os));
        }
        return templates;
    }
    
}
