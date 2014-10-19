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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.images3.common.ResizingConfig;
import com.images3.common.ResizingUnit;
import com.images3.common.TemplateIdentity;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.models.imageplant.ImagePlantRoot;
import com.images3.core.models.imageplant.TemplateEntity;


public class TemplateEntityTest {
    
    private static final String IMAGE_PLANT_ID = "IMAGE_PLANT_ID";
    private static final String NAME = "TEMPLATE_NAME";
    private static final boolean ISARCHIVED = false;
    private static final boolean ISREMOVABLE = true;
    
    private ResizingConfig resizingConfig;
    private ImagePlantRoot imagePlant;
    private TemplateOS objectSegment;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setup() {
        resizingConfig = SetupHelper.setupResizingConfig(ResizingUnit.PIXEL, 100, 100, true);
        setupImagePlant();
        objectSegment = SetupHelper.setupTemplateOS(
                new TemplateIdentity(IMAGE_PLANT_ID, NAME), ISARCHIVED, ISREMOVABLE, resizingConfig);
    }
    
    private void setupImagePlant() {
        imagePlant = Mockito.mock(ImagePlantRoot.class);
    }
    
    @Test
    public void testTemplateEntityValues() {
        TemplateEntity template = new TemplateEntity(imagePlant, objectSegment);
        assertTrue(template.getObjectSegment().equals(objectSegment));
        assertEquals(template.getImagePlant(), imagePlant);
        assertEquals(template.getName(), NAME);
        assertEquals(template.isArchived(), ISARCHIVED);
        assertEquals(template.isRemovable(), ISREMOVABLE);
        assertEquals(template.getResizingConfig(), resizingConfig);
    }
    
}
