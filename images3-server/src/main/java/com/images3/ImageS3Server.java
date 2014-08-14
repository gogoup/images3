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
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageContentAccess;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.core.infrastructure.spi.ImageProcessor;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.core.infrastructure.spi.VersionAccess;
import com.images3.core.models.imageplant.ImageFactoryService;
import com.images3.core.models.imageplant.ImagePlantFactoryService;
import com.images3.core.models.imageplant.ImagePlantRepositoryService;
import com.images3.core.models.imageplant.ImageRepositoryService;
import com.images3.core.models.imageplant.TemplateFactoryService;
import com.images3.core.models.imageplant.TemplateRepositoryService;
import com.images3.core.models.imageplant.VersionFactoryService;
import com.images3.core.models.imageplant.VersionRepositoryService;

public class ImageS3Server implements ImageS3 {
    
    private ImagePlantFactory imagePlantFactory;
    private ImagePlantRepository imagePlantRepository;
    private AppObjectMapper objectMapper;
    private ImagePlantPaginatedResultDelegate imagePlantDelegate;
    private TemplatePaginatedResultDelegate templateDelegate;
    private ImagePaginatedResultDelegate imageDelegate;
    
    private ImageS3Server(ImagePlantFactory imagePlantFactory, 
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

    public static class Builder {
        
        private ImagePlantAccess imagePlantAccess;
        private ImageAccess imageAccess;
        private ImageContentAccess imageContentAccess;
        private ImageProcessor imageProcessor;
        private TemplateAccess templateAccess;
        private VersionAccess versionAccess;
        
        public Builder() {
            
        }
        
        public Builder setImagePlantAccess(ImagePlantAccess imagePlantAccess) {
            this.imagePlantAccess = imagePlantAccess;
            return this;
        }

        public Builder setImageAccess(ImageAccess imageAccess) {
            this.imageAccess = imageAccess;
            return this;
        }

        public Builder setImageContentAccess(ImageContentAccess imageContentAccess) {
            this.imageContentAccess = imageContentAccess;
            return this;
        }

        public Builder setImageProcessor(ImageProcessor imageProcessor) {
            this.imageProcessor = imageProcessor;
            return this;
        }

        public Builder setTempalteAccess(TemplateAccess templateAccess) {
            this.templateAccess = templateAccess;
            return this;
        }

        public Builder setVersionAccess(VersionAccess versionAccess) {
            this.versionAccess = versionAccess;
            return this;
        }
        
        protected ImagePlantAccess getImagePlantAccess() {
            if (null == imagePlantAccess) {
                throw new NullPointerException("ImagePlantAccess");
            }
            return imagePlantAccess;
        }

        protected ImageAccess getImageAccess() {
            if (null == imageAccess) {
                throw new NullPointerException("ImageAccess");
            }
            return imageAccess;
        }

        protected ImageContentAccess getImageContentAccess() {
            if (null == imageContentAccess) {
                throw new NullPointerException("ImageContentAccess");
            }
            return imageContentAccess;
        }

        protected ImageProcessor getImageProcessor() {
            if (null == imageProcessor) {
                throw new NullPointerException("ImageProcessor");
            }
            return imageProcessor;
        }

        protected TemplateAccess getTemplateAccess() {
            if (null == templateAccess) {
                throw new NullPointerException("TemplateAccess");
            }
            return templateAccess;
        }

        protected VersionAccess getVersionAccess() {
            if (null == versionAccess) {
                throw new NullPointerException("VersionAccess");
            }
            return versionAccess;
        }
        
        private void checkForNecessaryParameters() {
            getImagePlantAccess();
            getImageAccess();
            getImageContentAccess();
            getImageProcessor();
            getTemplateAccess();
            getVersionAccess();
        }

        public ImageS3 build() {
            checkForNecessaryParameters();
            TemplateFactoryService templateFactory = new TemplateFactoryService(templateAccess);
            VersionFactoryService versionFactory = new VersionFactoryService();
            ImageFactoryService imageFactory = new ImageFactoryService(
                    imageAccess, 
                    imageProcessor,
                    versionFactory);
            ImagePlantFactoryService imagePlantFactory = new ImagePlantFactoryService(
                    imagePlantAccess,
                    templateFactory,
                    imageFactory);
            TemplateRepositoryService templateRepository = new TemplateRepositoryService(
                    templateAccess,
                    templateFactory);
            VersionRepositoryService versionRepository = new VersionRepositoryService(
                    versionAccess, 
                    versionFactory, 
                    templateRepository);
            ImageRepositoryService imageRepository = new ImageRepositoryService(
                    imageAccess, 
                    imageContentAccess,
                    imageFactory, 
                    versionRepository, 
                    templateRepository);
            ImagePlantRepositoryService imagePlantRepository = new ImagePlantRepositoryService(
                    imagePlantAccess,
                    imagePlantFactory,
                    imageRepository,
                    templateRepository,
                    versionRepository);
            AppObjectMapper objectMapper = new AppObjectMapper();
            ImagePlantPaginatedResultDelegate imagePlantDelegate =
                    new ImagePlantPaginatedResultDelegate(objectMapper);
            TemplatePaginatedResultDelegate templateDelegate = 
                    new TemplatePaginatedResultDelegate(objectMapper);
            ImagePaginatedResultDelegate versionDelegate =
                    new ImagePaginatedResultDelegate(objectMapper);
            return new ImageS3Server(
                    imagePlantFactory, 
                    imagePlantRepository, 
                    objectMapper,
                    imagePlantDelegate,
                    templateDelegate,
                    versionDelegate);
        }
    }
}
