package com.images3.core.models.imageplant;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.images3.common.ResizingConfig;
import com.images3.common.ResizingUnit;
import com.images3.common.TemplateIdentity;
import com.images3.core.Template;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.core.models.imageplant.ImagePlantRoot;
import com.images3.core.models.imageplant.TemplateEntity;
import com.images3.core.models.imageplant.TemplateFactoryService;
import com.images3.core.models.imageplant.TemplateRepositoryService;

import org.gogoup.dddutils.pagination.PaginatedResult;

public class TemplateRepositoryServiceTest {

    private static final String IMAGE_PLANT_ID = "IMAGE_PLANT_ID";
    private static final  String TEMPLATE_NAME = "TEMPLATE_NAME";
    private static final  boolean TEMPLATE_ISARCHIVED = false;
    private static final  boolean TEMPLATE_ISREMOVABLE = true;
    private static final  ResizingConfig TEMPLATE_RESIZE_CONFIG = 
            new ResizingConfig(ResizingUnit.PIXEL, 100, 100, true);
    
    private TemplateAccess templateAccess;
    private ImagePlantRoot imagePlant;
    private ImagePlantOS imagePlantOS;
    private TemplateOS objectSegment;
    private TemplateEntity template;
    private TemplateFactoryService templateFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setup() {
        setupTemplateAccess();
        setupImagePlantOS();
        setupImagePlant();
        setupTemplateOS();
        setupTemplate();
        setupTemplateFactory();
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
    
    private void setupTemplate() {
        template = Mockito.mock(TemplateEntity.class);
        Mockito.when(template.getObjectSegment()).thenReturn(objectSegment);
        Mockito.when(template.getImagePlant()).thenReturn(imagePlant);
    }
    
    private void setupTemplateFactory() {
        templateFactory = Mockito.mock(TemplateFactoryService.class);
    }
    
    private void setupTemplateOS() {
        objectSegment = Mockito.mock(TemplateOS.class);
        Mockito.when(objectSegment.getId()).thenReturn(new TemplateIdentity(IMAGE_PLANT_ID, TEMPLATE_NAME));
        Mockito.when(objectSegment.isArchived()).thenReturn(TEMPLATE_ISARCHIVED);
        Mockito.when(objectSegment.isRemovable()).thenReturn(TEMPLATE_ISREMOVABLE);
        Mockito.when(objectSegment.getResizingConfig()).thenReturn(TEMPLATE_RESIZE_CONFIG);
    }
    
    @Test
    public void testStoreTemplate_NewTemplate() {
        Mockito.when(template.isNew()).thenReturn(true);
        Mockito.when(template.isDirty()).thenReturn(true);
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        repository.storeTemplate(template);
        
        Mockito.verify(templateAccess).insertTemplate(objectSegment);
        Mockito.verify(templateAccess, Mockito.never()).updateTemplate(objectSegment);
        Mockito.verify(template).cleanMarks();
        Mockito.verify(templateFactory).reconstituteTemplate(imagePlant, objectSegment);
    }
    
    @Test
    public void testStoreTemplate_UpdateTemplate() {
        Mockito.when(template.isDirty()).thenReturn(true);
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        repository.storeTemplate(template);
        
        Mockito.verify(templateAccess, Mockito.never()).insertTemplate(objectSegment);
        Mockito.verify(templateAccess).updateTemplate(objectSegment);
        Mockito.verify(template).cleanMarks();
        Mockito.verify(templateFactory).reconstituteTemplate(imagePlant, objectSegment);
    }
    
    @Test
    public void testStoreTemplate_VoidTemplate() {
        expectedException.expect(IllegalStateException.class);
        Mockito.when(template.isNew()).thenReturn(true);
        Mockito.when(template.isDirty()).thenReturn(true);
        Mockito.when(template.isVoid()).thenReturn(true);
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        repository.storeTemplate(template);
    }
    
    @Test
    public void testFindTemplateById() {
        TemplateEntity oldTemplate = Mockito.mock(TemplateEntity.class);
        Mockito.when(
                templateAccess.selectTemplateById(
                        new TemplateIdentity(IMAGE_PLANT_ID, TEMPLATE_NAME))).thenReturn(objectSegment);
        Mockito.when(templateFactory.reconstituteTemplate(imagePlant, objectSegment)).thenReturn(oldTemplate);
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        repository.findTemplateByName(imagePlant, TEMPLATE_NAME);
        
        Mockito.verify(templateAccess).selectTemplateById(new TemplateIdentity(IMAGE_PLANT_ID, TEMPLATE_NAME));
        Mockito.verify(templateFactory).reconstituteTemplate(imagePlant, objectSegment);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllTemplates() {
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        PaginatedResult<List<TemplateOS>> osResult = Mockito.mock(PaginatedResult.class);
        Mockito.when(templateAccess.selectTemplatesByImagePlantId(IMAGE_PLANT_ID, null)).thenReturn(osResult);
        Mockito.when(osResult.isGetAllResultsSupported()).thenReturn(true);
        Mockito.when(osResult.getAllResults()).thenReturn(objectSegments);
        
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        PaginatedResult<List<Template>> result = repository.findAllTemplatesByImagePlant(imagePlant);
        result.getAllResults();
        
        Mockito.verify(templateAccess).selectTemplatesByImagePlantId(IMAGE_PLANT_ID, null);
        Mockito.verify(templateFactory).reconstituteTemplates(imagePlant, objectSegments);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindAllTemplates_ForPages() {
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        PaginatedResult<List<TemplateOS>> osResult = Mockito.mock(PaginatedResult.class);
        Mockito.when(osResult.getFirstPageCursor()).thenReturn(1);
        Mockito.when(templateAccess.selectTemplatesByImagePlantId(IMAGE_PLANT_ID, null)).thenReturn(osResult);
        Mockito.when(osResult.getResult(Mockito.eq(1))).thenReturn(objectSegments);
        
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        PaginatedResult<List<Template>> result = repository.findAllTemplatesByImagePlant(imagePlant);
        Object pageCursor = result.getFirstPageCursor();
        result.getResult(pageCursor);
        
        assertEquals(pageCursor, 1);
        Mockito.verify(templateAccess).selectTemplatesByImagePlantId(IMAGE_PLANT_ID, null);
        Mockito.verify(templateFactory).reconstituteTemplates(imagePlant, objectSegments);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindActiveTemplates() {
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        PaginatedResult<List<TemplateOS>> osResult = Mockito.mock(PaginatedResult.class);
        Mockito.when(templateAccess.selectTemplatesByImagePlantId(IMAGE_PLANT_ID, false)).thenReturn(osResult);
        Mockito.when(osResult.isGetAllResultsSupported()).thenReturn(true);
        Mockito.when(osResult.getAllResults()).thenReturn(objectSegments);
        
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        PaginatedResult<List<Template>> result = repository.findActiveTemplatesByImagePlant(imagePlant);
        result.getAllResults();
        
        Mockito.verify(templateAccess).selectTemplatesByImagePlantId(IMAGE_PLANT_ID, false);
        Mockito.verify(templateFactory).reconstituteTemplates(imagePlant, objectSegments);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindActiveTemplates_ForPages() {
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        PaginatedResult<List<TemplateOS>> osResult = Mockito.mock(PaginatedResult.class);
        Mockito.when(osResult.getFirstPageCursor()).thenReturn(1);
        Mockito.when(osResult.getNextPageCursor()).thenReturn(2);
        Mockito.when(templateAccess.selectTemplatesByImagePlantId(IMAGE_PLANT_ID, false)).thenReturn(osResult);
        Mockito.when(osResult.getResult(Mockito.eq(1))).thenReturn(objectSegments);
        
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        PaginatedResult<List<Template>> result = repository.findActiveTemplatesByImagePlant(imagePlant);
        Object pageCursor = result.getFirstPageCursor();
        result.getResult(pageCursor);
        assertEquals(pageCursor, 1);
        
        pageCursor = result.getNextPageCursor();
        assertEquals(pageCursor, 2);
        
        Mockito.verify(templateAccess).selectTemplatesByImagePlantId(IMAGE_PLANT_ID, false);
        Mockito.verify(templateFactory).reconstituteTemplates(imagePlant, objectSegments);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindArchivedTemplates() {
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        PaginatedResult<List<TemplateOS>> osResult = Mockito.mock(PaginatedResult.class);
        Mockito.when(templateAccess.selectTemplatesByImagePlantId(IMAGE_PLANT_ID, true)).thenReturn(osResult);
        Mockito.when(osResult.isGetAllResultsSupported()).thenReturn(true);
        Mockito.when(osResult.getAllResults()).thenReturn(objectSegments);
        
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        PaginatedResult<List<Template>> result = repository.findArchivedTemplatesByImagePlant(imagePlant);
        result.getAllResults();
        
        Mockito.verify(templateAccess).selectTemplatesByImagePlantId(IMAGE_PLANT_ID, true);
        Mockito.verify(templateFactory).reconstituteTemplates(imagePlant, objectSegments);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindArchivedTemplates_ForPages() {
        List<TemplateOS> objectSegments = new ArrayList<TemplateOS>();
        objectSegments.add(objectSegment);
        PaginatedResult<List<TemplateOS>> osResult = Mockito.mock(PaginatedResult.class);
        Mockito.when(osResult.getFirstPageCursor()).thenReturn(1);
        Mockito.when(templateAccess.selectTemplatesByImagePlantId(IMAGE_PLANT_ID, true)).thenReturn(osResult);
        Mockito.when(osResult.getResult(Mockito.eq(1))).thenReturn(objectSegments);
        
        TemplateRepositoryService repository = new TemplateRepositoryService(templateAccess, templateFactory);
        PaginatedResult<List<Template>> result = repository.findArchivedTemplatesByImagePlant(imagePlant);
        Object pageCursor = result.getFirstPageCursor();
        result.getResult(pageCursor);
        
        assertEquals(pageCursor, 1);
        Mockito.verify(templateAccess).selectTemplatesByImagePlantId(IMAGE_PLANT_ID, true);
        Mockito.verify(templateFactory).reconstituteTemplates(imagePlant, objectSegments);
    }
    
}
