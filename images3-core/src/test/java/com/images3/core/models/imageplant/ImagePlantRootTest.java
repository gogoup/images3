package com.images3.core.models.imageplant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.images3.AmazonS3Bucket;
import com.images3.DuplicatedImagePlantNameException;
import com.images3.ImageDimension;
import com.images3.ImageFormat;
import com.images3.ImageIdentity;
import com.images3.ImageMetadata;
import com.images3.ResizingConfig;
import com.images3.ResizingUnit;
import com.images3.TemplateIdentity;
import com.images3.UnremovableTemplateException;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.TemplateOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

public class ImagePlantRootTest {
    
    private static final String IMAGE_PLANT_ID = "IMAGE_PLANT_ID";
    private static final String IMAGE_PLANT_NAME = "IMAGE_PLANT_NAME";
    private static final Date IMAGE_PLANT_CREATION_TIME = new Date();
    private static final String BUCKET_ACCESS_KEY = "BUCKET_ACCESS_KEY";
    private static final String BUCKET_SECRET_KEY = "BUCKET_SECRET_KEY";
    private static final String BUCKET_NAME = "BUCKET_NAME";
    private static final String TEMPLATE_NAME = "TEMPLATE_NAME";
    private static final boolean TEMPLATE_ISARCHIVED = false;
    private static final boolean TEMPLATE_ISREMOVABLE = true;
    private static final ResizingUnit RESIZE_UNIT = ResizingUnit.PIXEL;
    private static final int RESIZE_WIDTH = 100;
    private static final int RESIZE_HEIGHT = 200;
    private static final boolean RESIZE_KEEP_PROPORTIONS = true;
    
    private ResizingConfig resizingConfig;
    private AmazonS3Bucket amazonS3Bucket;
    private ImagePlantOS objectSegment;
    private ImagePlantAccess imagePlantAccess;
    private ImageFactoryService imageFactory;
    private ImageRepositoryService imageRepository;
    private TemplateFactoryService templateFactory;
    private TemplateRepositoryService templateRepository;
    private TemplateOS templateOS;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setup() {
        resizingConfig = SetupHelper.setupResizingConfig(
                RESIZE_UNIT, RESIZE_WIDTH, RESIZE_HEIGHT, RESIZE_KEEP_PROPORTIONS);
        amazonS3Bucket = SetupHelper.setupAmazonS3Bucket(
                BUCKET_ACCESS_KEY, BUCKET_SECRET_KEY, BUCKET_NAME);
        objectSegment = SetupHelper.setupImagePlantOS(
                IMAGE_PLANT_ID, IMAGE_PLANT_NAME, IMAGE_PLANT_CREATION_TIME, amazonS3Bucket);
        setupImagePlantAccess();
        setupImageFactoryService();
        setupImageRepositoryService();
        setupTemplateFactoryService();
        setupTemplateRepositoryService();
        
        templateOS = SetupHelper.setupTemplateOS(
                new TemplateIdentity(IMAGE_PLANT_ID, TEMPLATE_NAME), 
                TEMPLATE_ISARCHIVED, TEMPLATE_ISREMOVABLE, resizingConfig);
        
    }
    
    private void setupImagePlantAccess() {
        imagePlantAccess = Mockito.mock(ImagePlantAccess.class);
    }
    
    private void setupImageFactoryService() {
        imageFactory = Mockito.mock(ImageFactoryService.class);
    }
    
    private void setupImageRepositoryService() {
        imageRepository = Mockito.mock(ImageRepositoryService.class);
    }
    
    private void setupTemplateFactoryService() {
        templateFactory = Mockito.mock(TemplateFactoryService.class);
    }
    
    private void setupTemplateRepositoryService() {
        templateRepository = Mockito.mock(TemplateRepositoryService.class);
    }
    
    private ImagePlantRoot createImagePlant() {
        ImagePlantRoot imagePlant = new ImagePlantRoot(objectSegment, imagePlantAccess, 
                imageFactory, imageRepository, templateFactory, templateRepository);
        return imagePlant;
    }
    
    
    @Test
    public void testImagePlantRootValues() {
        ImagePlantRoot imagePlant = createImagePlant();
        
        assertTrue(imagePlant.getObjectSegment().equals(objectSegment));
        assertEquals(imagePlant.getId(), IMAGE_PLANT_ID);
        assertEquals(imagePlant.getName(), IMAGE_PLANT_NAME);
        assertEquals(imagePlant.getCreationTime(), IMAGE_PLANT_CREATION_TIME);
        assertEquals(imagePlant.getAmazonS3Bucket(), amazonS3Bucket);
        assertTrue(!imagePlant.isNew());
        assertTrue(!imagePlant.isDirty());
        assertTrue(!imagePlant.isVoid());
    }
    
    @Test
    public void testUpdateName() {
        Mockito.when(imagePlantAccess.isDuplicatedImagePlantName("ImagePlantName2")).thenReturn(false);
        ImagePlantRoot imagePlant = createImagePlant();
        imagePlant.updateName("ImagePlantName2");
        
        Mockito.verify(imagePlantAccess, Mockito.atMost(1)).isDuplicatedImagePlantName("ImagePlantName2");
        Mockito.verify(objectSegment, Mockito.atMost(1)).setName("ImagePlantName2");
        assertTrue(!imagePlant.isNew());
        assertTrue(imagePlant.isDirty());
        assertTrue(!imagePlant.isVoid());
    }
    
    @Test
    public void testUpdateName_DuplicateName() {
        expectedException.expect(DuplicatedImagePlantNameException.class);
        Mockito.when(imagePlantAccess.isDuplicatedImagePlantName("ImagePlantName2")).thenReturn(true);
        ImagePlantRoot imagePlant = createImagePlant();
        imagePlant.updateName("ImagePlantName2");
        
        Mockito.verify(imagePlantAccess, Mockito.atMost(1)).isDuplicatedImagePlantName("ImagePlantName2");
    }
    
    @Test
    public void testUpdateName_NoChange() {
        ImagePlantRoot imagePlant = createImagePlant();
        imagePlant.updateName(IMAGE_PLANT_NAME);
        
        Mockito.verify(imagePlantAccess, Mockito.never()).isDuplicatedImagePlantName(Mockito.anyString());
        Mockito.verify(objectSegment, Mockito.never()).setName(Mockito.anyString());
    }
    
    @Test
    public void testCreateTemplate() {
        ImagePlantRoot imagePlant = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant, templateOS);
        Mockito.when(
                templateFactory.generateTemplate(
                        imagePlant, TEMPLATE_NAME, resizingConfig)).thenReturn(template);
        Template newTemplate = imagePlant.createTemplate(TEMPLATE_NAME, resizingConfig);
        assertTrue(newTemplate.equals(template));
        Mockito.verify(templateFactory).generateTemplate(imagePlant, TEMPLATE_NAME, resizingConfig);
    }
    
    @Test
    public void testUpdateTemplate() {
        ImagePlantRoot imagePlant = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant, templateOS);
        imagePlant.updateTemplate(template);
    }
    
    @Test
    public void testUpdateTemplate_InvalidTemplate() {
        expectedException.expect(IllegalArgumentException.class);
        ImagePlantRoot imagePlant = createImagePlant();
        ImagePlantRoot imagePlant2 = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant2, templateOS);
        imagePlant.updateTemplate(template);
    }
    
    @Test
    public void testRemoveTemplate() {
        ImagePlantRoot imagePlant = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant, templateOS);
        Mockito.when(template.isRemovable()).thenReturn(true);
        imagePlant.removeTemplate(template);
        
        Mockito.verify(template).isRemovable();
        Mockito.verify(template).markAsVoid();
    }
    
    @Test
    public void testRemoveTemplate_Unremovable() {
        expectedException.expect(UnremovableTemplateException.class);
        ImagePlantRoot imagePlant = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant, templateOS);
        Mockito.when(template.isRemovable()).thenReturn(false);
        imagePlant.removeTemplate(template);
        
        Mockito.verify(template).isRemovable();
        Mockito.verify(template, Mockito.never()).markAsVoid();
    }
    
    @Test
    public void testRemoveTemplate_InvalidTemplate() {
        expectedException.expect(IllegalArgumentException.class);
        ImagePlantRoot imagePlant = createImagePlant();
        ImagePlantRoot imagePlant2 = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant2, templateOS);
        imagePlant.removeTemplate(template);
    }
    
    @Test
    public void testFetchTemplateById() {
        ImagePlantRoot imagePlant = createImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant, templateOS);
        Mockito.when(
                templateRepository.findTemplateByName(imagePlant, TEMPLATE_NAME)).thenReturn(template);
        Template oldTemplate = imagePlant.fetchTemplateByName(TEMPLATE_NAME);
        assertTrue(template.equals(oldTemplate));
        
        Mockito.verify(templateRepository).findTemplateByName(imagePlant, TEMPLATE_NAME);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testListAllTemplates() {
        ImagePlantRoot imagePlant = createImagePlant();
        PaginatedResult<List<Template>> result = Mockito.mock(PaginatedResult.class);
        Mockito.when(
                templateRepository.findAllTemplatesByImagePlant(imagePlant))
                .thenReturn(result);
        PaginatedResult<List<Template>> oldResult = imagePlant.listAllTemplates();
        assertTrue(result.equals(oldResult));
        
        Mockito.verify(templateRepository).findAllTemplatesByImagePlant(imagePlant);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testListActiveTemplates() {
        ImagePlantRoot imagePlant = createImagePlant();
        PaginatedResult<List<Template>> result = Mockito.mock(PaginatedResult.class);
        Mockito.when(
                templateRepository.findActiveTemplatesByImagePlant(imagePlant))
                .thenReturn(result);
        PaginatedResult<List<Template>> oldResult = imagePlant.listActiveTemplates();
        assertTrue(result.equals(oldResult));
        
        Mockito.verify(templateRepository).findActiveTemplatesByImagePlant(imagePlant);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testListArchivedTemplates() {
        ImagePlantRoot imagePlant = createImagePlant();
        PaginatedResult<List<Template>> result = Mockito.mock(PaginatedResult.class);
        Mockito.when(
                templateRepository.findArchivedTemplatesByImagePlant(imagePlant))
                .thenReturn(result);
        PaginatedResult<List<Template>> oldResult = imagePlant.listArchivedTemplates();
        assertTrue(result.equals(oldResult));
        
        Mockito.verify(templateRepository).findArchivedTemplatesByImagePlant(imagePlant);
    }
    
    @Test
    public void testCreateImage() {
        ImageEntity image = setupImageEntity();
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        Mockito.when(
                imageFactory.generateImage(
                        imagePlant, image.getContent(), imageRepository, 
                        templateRepository)).thenReturn(image);
        Image newImage = image.getImagePlant().createImage(image.getContent());
        assertTrue(newImage.equals(image));
        
        Mockito.verify(imageFactory).generateImage(
                imagePlant, image.getContent(), imageRepository, templateRepository);
    }
    
    private ImageEntity setupImageEntity() {
        ImagePlantRoot imagePlant = createImagePlant();
        Date dateTime = new Date();
        ImageDimension dimension = SetupHelper.setupImageDimension(100, 200);
        ImageMetadata metadata = SetupHelper.setupImageMetadata(
                dimension, ImageFormat.JPEG, 10000);
        ImageIdentity imageIdentity = SetupHelper.setupImageIdentity(
                imagePlant.getId(), "IMAGE_ID");
        ImageOS objectSegment = SetupHelper.setupImageOS(imageIdentity, dateTime, metadata);
        File imageContent = Mockito.mock(File.class);
        return SetupHelper.setupImageEntity(
                imagePlant, objectSegment, imageContent);
    }
  
    @Test
    public void testCreateImageWithTemplate() {
        ImageEntity originalImage = setupImageEntity();
        ImagePlantRoot imagePlant = (ImagePlantRoot) originalImage.getImagePlant();
        TemplateEntity template = SetupHelper.setupTemplateEntity(imagePlant, templateOS);
        ImageEntity image = setupImageEntity();
        Mockito.when(
                imageFactory.generateImage(
                        imagePlant, originalImage, template, imageRepository, 
                        templateRepository)).thenReturn(image);
        Image newImage = imagePlant.createImage(new Version(template, originalImage));
        assertTrue(newImage.equals(image));
        
        Mockito.verify(imageFactory).generateImage(
                imagePlant, originalImage, template, imageRepository, templateRepository);
    }
    
    
    
}
