package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

import com.images3.core.Template;

public class TemplatePaginatedResultDelegate implements
        PaginatedResultDelegate<List<TemplateResponse>> {

    private AppObjectMapper objectMapper;
    
    public TemplatePaginatedResultDelegate(AppObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TemplateResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getActiveTempaltes".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            return getTempaltes(result, pageCursor);
        }
        if ("getArchivedTemplates".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            return getTempaltes(result, pageCursor);
        }
        throw new UnsupportedOperationException(tag);
    }
    
    private List<TemplateResponse> getTempaltes(
            PaginatedResult<List<Template>> result, Object pageCursor) {
        List<Template> templates = result.getResult(pageCursor);
        List<TemplateResponse> responses = new ArrayList<TemplateResponse>(templates.size());
        for (Template template: templates) {
            responses.add(objectMapper.mapToResponse(template));
        }
        return responses;
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<TemplateResponse> result) {
        if (!"getActiveTempaltes".equals(tag)
                && !"getArchivedTemplates".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
        return osResult.getNextPageCursor();
    }

}
