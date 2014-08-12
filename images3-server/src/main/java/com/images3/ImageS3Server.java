package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.ImagePlantRepository;
import com.images3.core.Template;
import com.images3.core.Version;

public class ImageS3Server implements ImageS3 {
    
    private ImagePlantFactory imagePlantFactory;
    private ImagePlantRepository imagePlantRepository;
    private AppObjectMapper objectMapper;
    private ImagePlantPaginatedResultDelegate imagePlantDelegate;
    private TemplatePaginatedResultDelegate templateDelegate;
    private ImagePaginatedResultDelegate imageDelegate;
    
    public ImageS3Server(ImagePlantFactory imagePlantFactory, 
            ImagePlantRepository imagePlantRepository, AppObjectMapper objectMapper,
            ImagePlantPaginatedResultDelegate imagePlantDelegate,
            TemplatePaginatedResultDelegate templateDelegate,
            ImagePaginatedResultDelegate versionDelegate) {
        this.imagePlantFactory = imagePlantFactory;
        this.imagePlantRepository = imagePlantRepository;
        this.objectMapper = objectMapper;
        this.imagePlantDelegate = imagePlantDelegate;
        this.templateDelegate = templateDelegate;
        this.imageDelegate = versionDelegate;
    }

    @Override
    public ImagePlantResponse addImagePlant(ImagePlantRequest request) {
        ImagePlant imagePlant = imagePlantFactory.generateImagePlant(
                request.getName(), request.getBucket());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(imagePlant);
    }

    @Override
    public ImagePlantResponse updateImagePlant(String id, ImagePlantRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id);
        imagePlant.setName(request.getName());
        imagePlant.setAmazonS3Bucket(request.getBucket());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(imagePlant);
    }

    @Override
    public void deleteImagePlant(String id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id);
        imagePlantRepository.removeImagePlant(imagePlant);
    }

    @Override
    public ImagePlantResponse getImagePlant(String id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id);
        return objectMapper.mapToResponse(imagePlant);
    }

    @Override
    public PaginatedResult<List<ImagePlantResponse>> getAllImagePlants() {
        PaginatedResult<List<ImagePlant>> result = imagePlantRepository.findAllImagePlants();
        return new PaginatedResult<List<ImagePlantResponse>>(
                imagePlantDelegate, "getAllImagePlants", new Object[]{result}) {};
    }
    
    @Override
    public TemplateResponse addTemplate(TemplateRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(request.getImagePlantId());
        Template template = imagePlant.createTemplate(request.getName(), request.getResizingConfig());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(template);
    }

    @Override
    public TemplateResponse updateTemplate(TemplateIdentity id, TemplateRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = imagePlant.fetchTemplateById(id.getTemplateId());
        template.setName(request.getName());
        template.setArchived(request.isArchived());
        imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(template);
    }

    @Override
    public void deleteTemplate(TemplateIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = imagePlant.fetchTemplateById(id.getTemplateId());
        imagePlant.removeTemplate(template);
        imagePlantRepository.storeImagePlant(imagePlant);
    }

    @Override
    public TemplateResponse getTemplate(TemplateIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = imagePlant.fetchTemplateById(id.getTemplateId());
        return objectMapper.mapToResponse(template);
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getActiveTempaltes(
            String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Template>> result = imagePlant.listActiveTemplates();
        return new PaginatedResult<List<TemplateResponse>>(
                templateDelegate, "getActiveTempaltes", new Object[]{result}) {};
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getArchivedTemplates(
            String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Template>> result = imagePlant.listArchivedTemplates();
        return new PaginatedResult<List<TemplateResponse>>(
                templateDelegate, "getArchivedTemplates", new Object[]{result}) {};
    }

    @Override
    public ImageResponse addImage(ImageRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(request.getImagePlantId());
        Image image = imagePlant.createImage(request.getContent());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(
                image, 
                getTemplateIds(imagePlant));
    }
    
    private List<String> getTemplateIds(ImagePlant imagePlant) {
        PaginatedResult<List<Template>> result = imagePlant.listActiveTemplates();
        List<Template> templates = result.getAllResults();
        List<String> templateIds = new ArrayList<String>(templates.size());
        for (Template template: templates) {
            templateIds.add(template.getId());
        }
        return templateIds;
    }

    @Override
    public void deleteImage(ImageIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image image = imagePlant.fetchImageById(id.getImageId());
        imagePlant.removeImage(image);
        imagePlantRepository.storeImagePlant(imagePlant);
    }

    @Override
    public void deleteImageAndVersions(ImageIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image image = imagePlant.fetchImageById(id.getImageId());
        imagePlant.removeImageAndVerions(image);
        imagePlantRepository.storeImagePlant(imagePlant);
    }

    @Override
    public ImageResponse getImage(ImageIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image image = imagePlant.fetchImageById(id.getImageId());
        return objectMapper.mapToResponse(
                image, 
                getTemplateIds(imagePlant));
    }

    @Override
    public ImageResponse getVersioningImage(VersionIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImageId().getImagePlantId());
        Image image = imagePlant.fetchImageById(id.getImageId().getImageId());
        Template template = imagePlant.fetchTemplateById(id.getTemplateId());
        Version version = getGurenteedVersion(imagePlant, image, template);
        return objectMapper.mapToResponse(
                version.getImage(), 
                getTemplateIds(imagePlant));
    }
    
    private Version getGurenteedVersion(ImagePlant imagePlant, Image image, Template template) {
        Version version = image.fetchVersion(template);
        if (null == version) {
            version = image.createVersion(template);
            imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        }
        return version;
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getImages(String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Image>> result = imagePlant.listAllImages();
        List<String> templateIds = getTemplateIds(imagePlant);
        return new PaginatedResult<List<ImageResponse>>(
                imageDelegate, "getImages", new Object[]{result, templateIds}) {};
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getVersioningImages(
            ImageIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image image = imagePlant.fetchImageById(id.getImageId());
        PaginatedResult<List<Version>> result = image.fetchAllVersions();
        List<String> templateIds = getTemplateIds(imagePlant);
        return new PaginatedResult<List<ImageResponse>>(
                imageDelegate, "getVersioningImages", new Object[]{result, templateIds}) {};
    }

}
