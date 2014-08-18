package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

import com.images3.core.Image;
import com.images3.core.Version;

public class ImagePaginatedResultDelegate implements
        PaginatedResultDelegate<List<ImageResponse>> {

    private AppObjectMapper objectMapper;
    
    public ImagePaginatedResultDelegate(AppObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ImageResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getImages".equals(tag)) {
            PaginatedResult<List<Image>> result = (PaginatedResult<List<Image>>) arguments[0];
            List<String> templateIds = (List<String>) arguments[1];
            return getImages(result, templateIds, pageCursor);
        }
        if ("getVersioningImages".equals(tag)) {
            PaginatedResult<List<Version>> result = (PaginatedResult<List<Version>>) arguments[0];
            List<String> templateIds = (List<String>) arguments[1];
            return getVersioningImages(result, templateIds, pageCursor);
        }
        throw new UnsupportedOperationException(tag);
    }
    
    private List<ImageResponse> getImages(
            PaginatedResult<List<Image>> result, List<String> templateIds, Object pageCursor) {
        List<Image> images = result.getResult(pageCursor);
        List<ImageResponse> responses = new ArrayList<ImageResponse>(images.size());
        for (Image image: images) {
            responses.add(objectMapper.mapToResponse(image, templateIds));
        }
        return responses;
    }
    
    private List<ImageResponse> getVersioningImages(
            PaginatedResult<List<Version>> result, List<String> templateIds, Object pageCursor) {
        List<Version> versions = result.getResult(pageCursor);
        List<ImageResponse> responses = new ArrayList<ImageResponse>(versions.size());
        for (Version version: versions) {
            Image image = version.getOriginalImage();
            responses.add(objectMapper.mapToResponse(image, templateIds));
        }
        return responses;
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<ImageResponse> result) {
        if (!"getImages".equals(tag)
                && !"getVersioningImages".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
        return osResult.getNextPageCursor();
    }

}
