package com.images3.core.models.imageplant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.images3.ResizingConfig;
import com.images3.ResizingUnit;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.models.imageplant.ImagePlantRoot;
import com.images3.core.models.imageplant.TemplateEntity;


public class TemplateEntityTest {
    
    private static final String IMAGE_PLANT_ID = "IMAGE_PLANT_ID";
    private static final  String ID = "TEMPLATE_ID";
    private static final  String NAME = "TEMPLATE_NAME";
    private static final  boolean ISARCHIVED = false;
    private static final  boolean ISREMOVABLE = true;
    private static final  ResizingConfig RESIZE_CONFIG = new ResizingConfig(ResizingUnit.PIXEL, 100, 100, true);
    
    private ImagePlantRoot imagePlant;
    private TemplateOS objectSegment;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setup() {
        setupImagePlant();
        setupTemplateOS();
    }
    
    private void setupImagePlant() {
        imagePlant = Mockito.mock(ImagePlantRoot.class);
    }
    
    private void setupTemplateOS() {
        objectSegment = Mockito.mock(TemplateOS.class);
        Mockito.when(objectSegment.getImagePlantId()).thenReturn(IMAGE_PLANT_ID);
        Mockito.when(objectSegment.getId()).thenReturn(ID);
        Mockito.when(objectSegment.getName()).thenReturn(NAME);
        Mockito.when(objectSegment.isArchived()).thenReturn(ISARCHIVED);
        Mockito.when(objectSegment.isRemovable()).thenReturn(ISREMOVABLE);
        Mockito.when(objectSegment.getResizingConfig()).thenReturn(RESIZE_CONFIG);
    }
    
    @Test
    public void testTemplateEntity() {
        TemplateEntity template = new TemplateEntity(imagePlant, objectSegment);
        assertTrue(template.getObjectSegment().equals(objectSegment));
        assertEquals(template.getImagePlant(), imagePlant);
        assertEquals(template.getId(), ID);
        assertEquals(template.getName(), NAME);
        assertEquals(template.isArchived(), ISARCHIVED);
        assertEquals(template.isRemovable(), ISREMOVABLE);
        assertEquals(template.getResizingConfig(), RESIZE_CONFIG);
    }
    
}
