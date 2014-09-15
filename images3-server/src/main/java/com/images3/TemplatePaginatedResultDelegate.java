package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.AutoPaginatedResultDelegate;
import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.core.Template;

public class TemplatePaginatedResultDelegate extends AutoPaginatedResultDelegate<List<TemplateResponse>> {

    private AppObjectMapper objectMapper;
    
    public TemplatePaginatedResultDelegate(AppObjectMapper objectMapper) {
        super(0, "getActiveTempaltes", "getArchivedTemplates", "getAllTemplates");
        this.objectMapper = objectMapper;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TemplateResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getActiveTempaltes".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            List<Template> templates = result.getResult(pageCursor);
            return getTempaltes(templates);
        }
        if ("getArchivedTemplates".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            List<Template> templates = result.getResult(pageCursor);
            return getTempaltes(templates);
        }
        if ("getAllTemplates".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            List<Template> templates = result.getResult(pageCursor);
            return getTempaltes(templates);
        }
        throw new UnsupportedOperationException(tag);
    }
    
    @Override
    public boolean isFetchAllResultsSupported(String tag, Object[] arguments) {
        if (!"getActiveTempaltes".equals(tag)
                && !"getArchivedTemplates".equals(tag)
                && !"getAllTemplates".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[1];
        return osResult.isGetAllResultsSupported();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TemplateResponse> fetchAllResults(String tag, Object[] arguments) {
        if ("getActiveTempaltes".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            List<Template> templates = result.getAllResults();
            return getTempaltes(templates);
        }
        if ("getArchivedTemplates".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            List<Template> templates = result.getAllResults();
            return getTempaltes(templates);
        }
        if ("getAllTemplates".equals(tag)) {
            PaginatedResult<List<Template>> result = (PaginatedResult<List<Template>>) arguments[0];
            List<Template> templates = result.getAllResults();
            return getTempaltes(templates);
        }
        throw new UnsupportedOperationException(tag);
    }

    private List<TemplateResponse> getTempaltes(List<Template> templates) {
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
                && !"getArchivedTemplates".equals(tag)
                && !"getAllTemplates".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
        return osResult.getNextPageCursor();
    }

    @Override
    public Object getFirstPageCursor(String tag, Object[] arguments) {
        if (!"getActiveTempaltes".equals(tag)
                && !"getArchivedTemplates".equals(tag)
                && !"getAllTemplates".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
        return osResult.getFirstPageCursor();
    }

}
