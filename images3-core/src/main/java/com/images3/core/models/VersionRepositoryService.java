package com.images3.core.models;

import java.util.ArrayList;
import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.common.PaginatedResultDelegate;
import com.images3.core.Version;
import com.images3.core.infrastructure.data.VersionOS;
import com.images3.core.infrastructure.data.spi.VersionAccess;

public class VersionRepositoryService implements PaginatedResultDelegate<List<Version>> {
    
    private VersionAccess versionAccess;
    private VersionFactoryService versionFactory;
    private TemplateRepositoryService templateRepository;
    private ImageRepositoryService imageRepository;
    
    public VersionRepositoryService(VersionAccess versionAccess, 
            VersionFactoryService versionFactory, TemplateRepositoryService templateRepository,
            ImageRepositoryService imageRepository) {
        this.versionAccess = versionAccess;
        this.versionFactory = versionFactory;
        this.templateRepository = templateRepository;
        this.imageRepository = imageRepository;
    }

    public VersionEntity storeVersion(VersionEntity version) {
        checkIfVoid(version);
        VersionOS objectSegment = version.getObjectSegment();
        VersionEntity entity = null;
        if (version.isNew()) {
            versionAccess.insertVersion(objectSegment);
            entity = versionFactory.reconstituteVersion(version.getImage(),
                    objectSegment, version.getTemplate(), version.getVesioningImage(),
                    templateRepository, imageRepository);
        } else {
            entity = versionFactory.reconstituteVersion(version.getImage(),
                    objectSegment, null, null, templateRepository, imageRepository);
        }
        version.cleanMarks();
        return entity;
    }
    
    public void removeVersionsByImage(ImageEntity image) {
        versionAccess.deleteVersionsByImageId(image.getId());
    }
    
    public Version findVersionById(ImageEntity image, TemplateEntity template) {
        VersionOS objectSegment = versionAccess.selectVersionById(
                image.getId(), template.getId());
        return versionFactory.reconstituteVersion(image, objectSegment, template, null, 
                templateRepository, imageRepository);
    }
    
    public PaginatedResult<List<Version>> findVersionsByImage(ImageEntity image) {
        PaginatedResult<List<VersionOS>> osResult =
                versionAccess.selectVersionsByImageId(image.getId());
        return new PaginatedResult<List<Version>>(
                this, "getVersionsByImage", new Object[]{image, osResult}) {};
    }
    
    private List<Version> getVersionsByImage(ImageEntity image, 
            PaginatedResult<List<VersionOS>> osResult, Object pageCursor) {
        List<VersionOS> objectSegments = osResult.getResult(pageCursor);
        List<Version> versions = new ArrayList<Version>(objectSegments.size());
        for (VersionOS os: objectSegments) {
            versions.add(versionFactory.reconstituteVersion(
                    image, os, null, null, templateRepository, imageRepository));
        }
        return versions;
    }
    
    private void checkIfVoid(VersionEntity version) {
        if (version.isVoid()) {
            throw new IllegalStateException(version.toString());
        }
    }
    
    @Override
    public Object getNextPageCursor(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getVersionsByImage".equals(methodName)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[1];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(methodName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Version> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getVersionsByImage".equals(methodName)) {
            ImageEntity image = (ImageEntity) arguments[0];
            PaginatedResult<List<VersionOS>> osResult = (PaginatedResult<List<VersionOS>>) arguments[1];
            return getVersionsByImage(image, osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }
    
}
