package com.images3.core.models.imageplant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.images3.DuplicateTemplateNameException;
import com.images3.ResizingConfig;
import com.images3.ResizingUnit;
import com.images3.core.Template;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.core.models.imageplant.ImagePlantRoot;
import com.images3.core.models.imageplant.TemplateEntity;
import com.images3.core.models.imageplant.TemplateFactoryService;

public class TemplateFactoryServiceTest {

    private static final String IMAGE_PLANT_ID = "IMAGE_PLANT_ID";
    private static final  String TEMPLATE_ID = "TEMPLATE_ID";
    private static final  String TEMPLATE_NAME = "TEMPLATE_NAME";
    private static final  boolean TEMPLATE_ISARCHIVED = false;
    private static final  boolean TEMPLATE_ISREMOVABLE = true;
    private static final  ResizingConfig TEMPLATE_RESIZE_CONFIG = 
            new ResizingConfig(ResizingUnit.PIXEL, 100, 100, true);
    
    private TemplateAccess templateAccess;
    private ImagePlantRoot imagePlant;
    private ImagePlantOS imagePlantOS;
    private TemplateOS objectSegment;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setup() {
        setupImagePlantOS();
        setupImagePlant();
        setupTemplateOS();
        setupTemplateAccess();
    }
    
    private void setupTemplateAccess() {
        templateAccess = Mockito.mock(TemplateAccess.class);
    }
    
    private void setupImagePlant() {
        imagePlant = Mockito.mock(ImagePlantRoot.class);
        Mockito.when(imagePlant.getObjectSegment()).thenReturn(imagePlantOS);
        Mockito.when(imagePlant.getId()).thenReturn(IMAGE_PLANT_ID);
    }
    
    private void setupImagePlantOS() {
        imagePlantOS = Mockito.mock(ImagePlantOS.class);
        
    }
    
    private void setupTemplateOS() {
        objectSegment = Mockito.mock(TemplateOS.class);
        Mockito.when(objectSegment.getImagePlantId()).thenReturn(IMAGE_PLANT_ID);
        Mockito.when(objectSegment.getId()).thenReturn(TEMPLATE_ID);
        Mockito.when(objectSegment.getName()).thenReturn(TEMPLATE_NAME);
        Mockito.when(objectSegment.isArchived()).thenReturn(TEMPLATE_ISARCHIVED);
        Mockito.when(objectSegment.isRemovable()).thenReturn(TEMPLATE_ISREMOVABLE);
        Mockito.when(objectSegment.getResizingConfig()).thenReturn(TEMPLATE_RESIZE_CONFIG);
    }
    
    @Test
    public void testGenerateTemplate() {
        Mockito.when(
                templateAccess.isDuplicatedTemplateName(
                        Mockito.eq(IMAGE_PLANT_ID), Mockito.eq(TEMPLATE_NAME))).thenReturn(false);
        TemplateFactoryService factory = new TemplateFactoryService(templateAccess);
        TemplateEntity template = factory.generateTemplate(imagePlant, TEMPLATE_NAME, TEMPLATE_RESIZE_CONFIG);
        
        assertTrue(null!=template);
        assertTrue(template.isNew());
        Mockito.verify(templateAccess).generateTemplateId(Mockito.eq(imagePlantOS));
    }
    
    @Test
    public void testGenerateTemplate_DuplicateNameException() {
        expectedException.expect(DuplicateTemplateNameException.class);
        Mockito.when(
                templateAccess.isDuplicatedTemplateName(
                        Mockito.eq(IMAGE_PLANT_ID), Mockito.eq(TEMPLATE_NAME))).thenReturn(true);
        TemplateFactoryService factory = new TemplateFactoryService(templateAccess);
        factory.generateTemplate(imagePlant, TEMPLATE_NAME, TEMPLATE_RESIZE_CONFIG);
    }
    
    @Test
    public void testReconstituteTemplate() {
        TemplateFactoryService factory = new TemplateFactoryService(templateAccess);
        TemplateEntity template = factory.reconstituteTemplate(imagePlant, objectSegment);
        
        assertTrue(null!=template);
        assertEquals(template.getImagePlant(), imagePlant);
        assertEquals(template.getObjectSegment(), objectSegment);
    }
    
    @Test
    public void testReconstituteTemplate_ReturnNullValue() {
        TemplateFactoryService factory = new TemplateFactoryService(templateAccess);
        TemplateEntity template = factory.reconstituteTemplate(imagePlant, null);
        
        assertTrue(null==template);
    }
    
    @Test
    public void testReconstituteTemplates() {
        TemplateFactoryService factory = new TemplateFactoryService(templateAccess);
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        List<Template> templates = factory.reconstituteTemplates(imagePlant, objectSegments);
        
        assertEquals(templates.size(), 1);
        assertTrue(TemplateEntity.class.isInstance(templates.get(0)));
    }
    
    @Test
    public void testReconstituteTemplates_NullObjectSegment() {
        expectedException.expect(NullPointerException.class);
        TemplateFactoryService factory = new TemplateFactoryService(templateAccess);
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(null);
        factory.reconstituteTemplates(imagePlant, objectSegments);
    }
    
}
