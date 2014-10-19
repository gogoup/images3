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

import com.images3.common.DirtyMark;
import com.images3.common.ResizingConfig;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;

public class TemplateEntity extends DirtyMark implements Template {
    
    private ImagePlantRoot imagePlant;
    private TemplateOS objectSegment;
    
    public TemplateEntity(ImagePlantRoot imagePlant, TemplateOS objectSegment) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
    }
    
    public TemplateOS getObjectSegment() {
        return objectSegment;
    }
    
    @Override
    public ImagePlant getImagePlant() {
        return imagePlant;
    }

    @Override
    public String getName() {
        return getObjectSegment().getId().getTemplateName();
    }

    @Override
    public boolean isArchived() {
        return getObjectSegment().isArchived();
    }

    @Override
    public void setArchived(boolean isArchived) {
        getObjectSegment().setArchived(isArchived);
        markAsDirty();
    }

    @Override
    public boolean isRemovable() {
        return getObjectSegment().isRemovable();
    }
    
    public void setNotRemovable() {
        getObjectSegment().setRemovable(false);
        markAsDirty();
    }

    @Override
    public ResizingConfig getResizingConfig() {
        return getObjectSegment().getResizingConfig();
    }

}
