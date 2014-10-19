/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
    
    private static final boolean DEFAULT_ISARCHIVED = false;
    private static final boolean DEFAULT_ISREMOVABLE = true;
    private static final int TEMPLATE_NAME_MIN_LENGTH = 3;
    private static final int TEMPLATE_NAME_MAX_LENGTH = 100;
    private static final Pattern TEMPLATE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[a-zA-Z0-9_-]+[a-zA-Z0-9]$");
    private static final int RESIZING_DIMENSION_PERCENT_MIN = 1;
    private static final int RESIZING_DIMENSION_PERCENT_MAX = 100;
    
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
        TemplateEntity entity = reconstituteTemplate(imagePlant, objectSegment);
        entity.markAsNew();
        return entity;
    }
    
    private void checkForPercentResizing(ResizingConfig resizingConfig) {
        int width = resizingConfig.getWidth();
        int height = resizingConfig.getHeight();
        if (resizingConfig.getUnit() == ResizingUnit.PERCENT) {
            if (width < RESIZING_DIMENSION_PERCENT_MIN 
                    || width > RESIZING_DIMENSION_PERCENT_MAX) {
                String message = "Set width between " + RESIZING_DIMENSION_PERCENT_MIN
                        + " and " + RESIZING_DIMENSION_PERCENT_MAX + " percent.";
                throw new IllegalResizingDimensionsException(
                        RESIZING_DIMENSION_PERCENT_MIN, 
                        RESIZING_DIMENSION_PERCENT_MAX, 
                        resizingConfig.getUnit(), message);
            }
            if (height < RESIZING_DIMENSION_PERCENT_MIN 
                    || height > RESIZING_DIMENSION_PERCENT_MAX) {
                String message = "Set height between " + RESIZING_DIMENSION_PERCENT_MIN
                        + " and " + RESIZING_DIMENSION_PERCENT_MAX + " percent.";
                throw new IllegalResizingDimensionsException(
                        RESIZING_DIMENSION_PERCENT_MIN, 
                        RESIZING_DIMENSION_PERCENT_MAX, 
                        resizingConfig.getUnit(), message);
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
            String message = "Use " + TEMPLATE_NAME_MIN_LENGTH
                    + " to " + TEMPLATE_NAME_MAX_LENGTH + " characters";
            throw new IllegalTemplateNameLengthException(
                    name, TEMPLATE_NAME_MIN_LENGTH, TEMPLATE_NAME_MAX_LENGTH, message);
        }
        if (!TEMPLATE_NAME_PATTERN.matcher(name).matches()) {
            String message = "May only contain numbers (0-9), "
                    + "letters (a-z), dash (-) and underscore (_).";
            throw new IllegalTemplateNameException(
                    name, "^[a-zA-Z0-9]+[a-zA-Z0-9_-]+[a-zA-Z0-9]$", message);
        }
    }
    
    private void checkForDuplicateTemplateName(String imagePlantId, String name) {
        if (templateAccess.isDuplicatedTemplateName(imagePlantId, name)) {
            String message = "Name \'" + name + "\' has been taken.";
            throw new DuplicateTemplateNameException(name, message);
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
