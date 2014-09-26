package com.images3.core.models.imageplant;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.images3.common.ResizingConfig;
import com.images3.common.ResizingUnit;
import com.images3.common.TemplateIdentity;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.exceptions.DuplicateTemplateNameException;
import com.images3.exceptions.IllegalResizingDimensionsException;
import com.images3.exceptions.IllegalTemplateNameException;
import com.images3.exceptions.IllegalTemplateNameLengthException;

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
        checkForPercentResizing(resizingConfig);
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
        checkForPercentResizing(resizingConfig);
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
    
    private void checkForPercentResizing(ResizingConfig resizingConfig) {
        int width = resizingConfig.getWidth();
        int height = resizingConfig.getHeight();
        if (resizingConfig.getUnit() == ResizingUnit.PERCENT) {
            if (width <= 0 || width > 100) {
                throw new IllegalResizingDimensionsException(
                        width, height, "Percent of width need to be 1 to 100.");
            }
            if (height <= 0 || height > 100) {
                throw new IllegalResizingDimensionsException(
                        width, height, "Percent of height need to be 1 to 100.");
            }
        }
    }
    
    private void validateTempalteName(String name) {
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
