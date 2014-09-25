package com.images3.core.models.imageplant;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.images3.common.DuplicateTemplateNameException;
import com.images3.common.IllegalTemplateNameException;
import com.images3.common.IllegalTemplateNameLengthException;
import com.images3.common.ResizingConfig;
import com.images3.common.TemplateIdentity;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;

public class TemplateFactoryService {
    
    private static final boolean DEFAULT_ISARCHIVED = true;
    private static final boolean DEFAULT_ISREMOVABLE = true;
    private static final int TEMPLATE_NAME_MIN_LENGTH = 1;
    private static final int TEMPLATE_NAME_MAX_LENGTH = 100;
    private static final Pattern TEMPLATE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[a-zA-Z0-9_-]+[a-zA-Z0-9]$");
    
    private TemplateAccess templateAccess;
    
    public TemplateFactoryService(TemplateAccess templateAccess) {
        this.templateAccess = templateAccess;
    }
    
    public TemplateEntity generateMasterTemplate(ImagePlantRoot imagePlant, 
            ResizingConfig resizingConfig) {
        TemplateEntity entity = generateTemplate(
                imagePlant, 
                TemplateEntity.MASTER_TEMPLATE_NAME,
                resizingConfig);
        entity.setArchived(false);
        entity.setNotRemovable();
        return entity;
    }

    public TemplateEntity generateTemplate(ImagePlantRoot imagePlant, String name, 
            ResizingConfig resizingConfig) {
        validateTempalteName(name);
        checkForDuplicateTemplateName(imagePlant.getId(), name);
        TemplateOS objectSegment = new TemplateOS(
                new TemplateIdentity(imagePlant.getId(), name), 
                DEFAULT_ISARCHIVED, 
                DEFAULT_ISREMOVABLE, 
                resizingConfig);
        TemplateEntity template = reconstituteTemplate(imagePlant, objectSegment);
        template.markAsNew();
        return template;
    }
    
    private void validateTempalteName(String name) {
        System.out.println("HERE======>TempalteName: " + name);
        if (null == name) {
            throw new NullPointerException("Template name");
        }
        int length = name.trim().length();
        if (length < TEMPLATE_NAME_MIN_LENGTH
                || length > TEMPLATE_NAME_MAX_LENGTH) {
            throw new IllegalTemplateNameLengthException(
                    name, TEMPLATE_NAME_MIN_LENGTH, TEMPLATE_NAME_MAX_LENGTH);
        }
        if (!TEMPLATE_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalTemplateNameException(name, "0-9, a-z, dash (-) and underscore (_).");
        }
    }
    
    private void checkForDuplicateTemplateName(String imagePlantId, String name) {
        if (templateAccess.isDuplicatedTemplateName(imagePlantId, name)) {
            throw new DuplicateTemplateNameException(name);
        }
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
