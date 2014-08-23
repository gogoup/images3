package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

import com.images3.core.Image;

public class ImagePaginatedResultDelegate implements
        PaginatedResultDelegate<List<SimpleImageResponse>> {

    private AppObjectMapper objectMapper;
    
    public ImagePaginatedResultDelegate(AppObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<SimpleImageResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getImages".equals(tag)) {
            PaginatedResult<List<Image>> result = (PaginatedResult<List<Image>>) arguments[0];
            return getImages(result, pageCursor);
        }
        throw new UnsupportedOperationException(tag);
    }
    
    private List<SimpleImageResponse> getImages(
            PaginatedResult<List<Image>> result, Object pageCursor) {
        List<Image> images = result.getResult(pageCursor);
        List<SimpleImageResponse> responses = new ArrayList<SimpleImageResponse>(images.size());
        for (Image image: images) {
            responses.add(objectMapper.mapToResponse(image));
        }
        return responses;
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<SimpleImageResponse> result) {
        if (!"getImages".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
        return osResult.getNextPageCursor();
    }

}
