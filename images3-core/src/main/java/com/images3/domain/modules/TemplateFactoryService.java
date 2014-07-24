package com.images3.domain.modules;

import com.images3.domain.ImageSize;
import com.images3.infrastructure.data.TemplateOS;
import com.images3.infrastructure.data.spi.TemplateAccess;

public class TemplateFactoryService {
    
    private TemplateAccess templateAccess;
    
    public TemplateFactoryService(TemplateAccess templateAccess) {
        this.templateAccess = templateAccess;
    }

    public TemplateEntity generateTemplate(ImagePlantRoot imagePlant, String name, 
            ImageSize scalingSize, boolean isKeepProportions) {
        if (templateAccess.isDuplicatedTemplateName(imagePlant.getId(), name)) {
            throw new IllegalArgumentException("Duplicated name exists, " + name);
        }
        String id = templateAccess.generateTemplateId(imagePlant.getObjectSegment());
        TemplateOS objectSegment = new TemplateOS(imagePlant.getId(), id, name,
                scalingSize, isKeepProportions, false, true);
        TemplateEntity template = reconstituteTemplate(imagePlant, objectSegment, scalingSize);
        template.markAsNew();
        return template;
    }
    
    public TemplateEntity reconstituteTemplate(ImagePlantRoot imagePlant, TemplateOS objectSegment, 
            ImageSize scalingSize) {
        if (null == objectSegment) {
            return null;
        }
        return new TemplateEntity(imagePlant, objectSegment, scalingSize);
    }
    
}
