package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.AutoPaginatedResultDelegate;
import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.core.Image;

public class ImagePaginatedResultDelegate extends AutoPaginatedResultDelegate<List<ImageResponse>> {

    private AppObjectMapper objectMapper;
    
    public ImagePaginatedResultDelegate(AppObjectMapper objectMapper) {
        super(0, "getImages");
        this.objectMapper = objectMapper;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<ImageResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getImages".equals(tag)) {
            PaginatedResult<List<Image>> result = (PaginatedResult<List<Image>>) arguments[0];
            List<Image> images = result.getResult(pageCursor);
            return getImages(images);
        }
        throw new UnsupportedOperationException(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ImageResponse> fetchAllResults(String tag,
            Object[] arguments) {
        if ("getImages".equals(tag)) {
            PaginatedResult<List<Image>> result = (PaginatedResult<List<Image>>) arguments[0];
            List<Image> images = result.getAllResults();
            return getImages(images);
        }
        throw new UnsupportedOperationException(tag);
    }

    private List<ImageResponse> getImages(List<Image> images) {
        List<ImageResponse> responses = new ArrayList<ImageResponse>(images.size());
        for (Image image: images) {
            responses.add(objectMapper.mapToResponse(image));
        }
        return responses;
    }

}
