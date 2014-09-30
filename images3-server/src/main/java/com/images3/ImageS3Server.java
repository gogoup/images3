package com.images3;

import java.io.File;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.ImageIdentity;
import com.images3.common.TemplateIdentity;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.ImagePlantRepository;
import com.images3.core.ImageReport;
import com.images3.core.ImageReporter;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageContentAccess;
import com.images3.core.infrastructure.spi.ImageMetricsService;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.core.infrastructure.spi.ImageProcessor;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.core.models.imageplant.ImageFactoryService;
import com.images3.core.models.imageplant.ImagePlantFactoryService;
import com.images3.core.models.imageplant.ImagePlantRepositoryService;
import com.images3.core.models.imageplant.ImageReporterFactoryService;
import com.images3.core.models.imageplant.ImageRepositoryService;
import com.images3.core.models.imageplant.TemplateFactoryService;
import com.images3.core.models.imageplant.TemplateRepositoryService;

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
    public ImagePlantResponse addImagePlant(ImagePlantAddRequest request) {
        ImagePlant imagePlant = imagePlantFactory.generateImagePlant(
                request.getName(), request.getBucket(), request.getResizingConfig());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(imagePlant);
    }

    @Override
    public ImagePlantResponse updateImagePlant(ImagePlantUpdateRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(request.getId());
        imagePlant.updateName(request.getName());
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
                imagePlantDelegate, "getAllImagePlants", new Object[]{result});
    }
    
    @Override
    public TemplateResponse addTemplate(TemplateAddRequest request) {
        ImagePlant imagePlant = 
                imagePlantRepository.findImagePlantById(request.getId().getImagePlantId());
        Template template = imagePlant.createTemplate(
                request.getId().getTemplateName(), request.getResizingConfig());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(template);
    }

    @Override
    public TemplateResponse archiveTemplate(TemplateIdentity id, boolean isArchived) {
        ImagePlant imagePlant = 
                imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = 
                imagePlant.fetchTemplate(id.getTemplateName());
        template.setArchived(isArchived);
        imagePlant.updateTemplate(template);
        imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(template);
    }

    @Override
    public void deleteTemplate(TemplateIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = imagePlant.fetchTemplate(id.getTemplateName());
        imagePlant.removeTemplate(template);
        imagePlantRepository.storeImagePlant(imagePlant);
    }

    @Override
    public TemplateResponse getTemplate(TemplateIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = imagePlant.fetchTemplate(id.getTemplateName());
        return objectMapper.mapToResponse(template);
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getAllTemplates(
            String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Template>> result = imagePlant.listAllTemplates();
        return new PaginatedResult<List<TemplateResponse>>(
                templateDelegate, "getAllTemplates", new Object[]{result});
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getActiveTempaltes(
            String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Template>> result = imagePlant.listActiveTemplates();
        return new PaginatedResult<List<TemplateResponse>>(
                templateDelegate, "getActiveTempaltes", new Object[]{result});
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getArchivedTemplates(
            String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Template>> result = imagePlant.listArchivedTemplates();
        return new PaginatedResult<List<TemplateResponse>>(
                templateDelegate, "getArchivedTemplates", new Object[]{result});
    }

    @Override
    public ImageResponse addImage(ImageAddRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(request.getImagePlantId());
        Image image = imagePlant.createImage(request.getContent());
        imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        return objectMapper.mapToResponse(image);
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
        return objectMapper.mapToResponse(image);
    }

    @Override
    public ImageResponse getImage(ImageIdentity id, String templateName) {
        ImagePlant imagePlant = 
                imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image originalImage = imagePlant.fetchImageById(id.getImageId());
        Template template = imagePlant.fetchTemplate(templateName);
        Image image = getGurenteedVersioningImage(
                imagePlant, new Version(template, originalImage));
        return objectMapper.mapToResponse(image);
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getImages(String imagePlantId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(imagePlantId);
        PaginatedResult<List<Image>> result = imagePlant.listAllImages();
        return new PaginatedResult<List<ImageResponse>>(
                imageDelegate, "getImages", new Object[]{result});
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getVersioningImages(
            ImageIdentity originalImageId) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(originalImageId.getImagePlantId());
        Image originalImage = imagePlant.fetchImageById(originalImageId.getImageId());
        PaginatedResult<List<Image>> result = imagePlant.fetchVersioningImages(originalImage);
        return new PaginatedResult<List<ImageResponse>>(
                imageDelegate, "getImages", new Object[]{result});
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getImages(TemplateIdentity id) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Template template = imagePlant.fetchTemplate(id.getTemplateName());
        PaginatedResult<List<Image>> result = imagePlant.fetchImagesByTemplate(template);
        return new PaginatedResult<List<ImageResponse>>(
                imageDelegate, "getImages", new Object[]{result});
    }

    @Override
    public File getImageContent(ImageIdentity id, String templateName) {
        ImagePlant imagePlant = 
                imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image originalImage = imagePlant.fetchImageById(id.getImageId());
        Template template = imagePlant.fetchTemplate(templateName);
        Image versioningImage = getGurenteedVersioningImage(
                imagePlant, new Version(template, originalImage));
        return versioningImage.getContent();
    }

    private Image getGurenteedVersioningImage(ImagePlant imagePlant, Version version) {
        Image image = null;
        if (imagePlant.hasVersiongImage(version)) {
            image = imagePlant.fetchImageByVersion(version);
        } else {
            image = imagePlant.createImage(version);
            imagePlant = imagePlantRepository.storeImagePlant(imagePlant);
        }
        return image;
    }

    @Override
    public File getImageContent(ImageIdentity id) {
        ImagePlant imagePlant = 
                imagePlantRepository.findImagePlantById(id.getImagePlantId());
        Image image = imagePlant.fetchImageById(id.getImageId());
        return image.getContent();
    }

    @Override
    public ImageReportResponse getImageReport(ImageReportQueryRequest request) {
        ImageReporter reporter = getImageReporter(request);
        ImageReport report = reporter.fetchReport(request.getTypes(), request.getInterval());
        return new ImageReportResponse(
                report.getImagePlant().getId(), 
                (report.getTemplate() == null ? null : report.getTemplate().getName()),
                report.getTimes(), 
                report.getValues(), 
                report.getScale());
    }
    
    private ImageReporter getImageReporter(ImageReportQueryRequest request) {
        ImagePlant imagePlant = imagePlantRepository.findImagePlantById(request.getImagePlantId());
        ImageReporter reporter = null;
        if (null == request.getTemplateName()) {
            reporter = imagePlant.generateImageReporter();
        } else {
            Template template = imagePlant.fetchTemplate(request.getTemplateName());
            reporter = imagePlant.generateImageReporter(template);
        }
        return reporter;
    }

    public static class Builder {
        
        private ImagePlantAccess imagePlantAccess;
        private ImageAccess imageAccess;
        private ImageContentAccess imageContentAccess;
        private ImageProcessor imageProcessor;
        private TemplateAccess templateAccess;
        private ImageMetricsService imageMetricsServcie;
        
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
        
        public Builder setImageMetricsService(ImageMetricsService imageStatServcie) {
            this.imageMetricsServcie = imageStatServcie;
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
        
        protected ImageMetricsService getImageMetricsService() {
            if (null == imageMetricsServcie) {
                throw new NullPointerException("ImageMetricsService");
            }
            return imageMetricsServcie;
        }
        
        private void checkForNecessaryParameters() {
            getImagePlantAccess();
            getImageAccess();
            getImageContentAccess();
            getImageProcessor();
            getTemplateAccess();
            getImageMetricsService();
        }

        public ImageS3 build() {
            checkForNecessaryParameters();
            TemplateFactoryService templateFactory = new TemplateFactoryService(templateAccess);
            TemplateRepositoryService templateRepository = new TemplateRepositoryService(
                    templateAccess,
                    templateFactory);
            ImageFactoryService imageFactory = new ImageFactoryService(
                    imageAccess, 
                    imageProcessor);
            ImageRepositoryService imageRepository = new ImageRepositoryService(
                    imageAccess, 
                    imageContentAccess,
                    imageFactory, 
                    templateRepository,
                    imageMetricsServcie);
            ImageReporterFactoryService imageReporterFactory = 
                    new ImageReporterFactoryService(imageMetricsServcie);
            ImagePlantFactoryService imagePlantFactory = new ImagePlantFactoryService(
                    imagePlantAccess,
                    templateFactory,
                    templateRepository,
                    imageFactory,
                    imageRepository,
                    imageReporterFactory);
            ImagePlantRepositoryService imagePlantRepository = new ImagePlantRepositoryService(
                    imagePlantAccess,
                    imagePlantFactory,
                    imageRepository,
                    templateRepository);
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
