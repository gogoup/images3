package com.images3.core.models;

import java.util.ArrayList;
import java.util.List;

import com.images3.common.ResizingConfig;
import com.images3.core.Template;
import com.images3.core.infrastructure.data.TemplateOS;
import com.images3.core.infrastructure.data.spi.TemplateAccess;

public class TemplateFactoryService {
    
    private TemplateAccess templateAccess;
    
    public TemplateFactoryService(TemplateAccess templateAccess) {
        this.templateAccess = templateAccess;
    }

    public TemplateEntity generateTemplate(ImagePlantRoot imagePlant, String name, 
            ResizingConfig filterConfig) {
        if (templateAccess.isDuplicatedTemplateName(imagePlant.getId(), name)) {
            throw new IllegalArgumentException("Duplicated name exists, " + name);
        }
        String id = templateAccess.generateTemplateId(imagePlant.getObjectSegment());
        TemplateOS objectSegment = new TemplateOS(
                imagePlant.getId(), id, name, false, true, filterConfig);
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
            templates.add(reconstituteTemplate(imagePlant, os));
        }
        return templates;
    }
    
}
