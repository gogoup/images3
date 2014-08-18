package com.images3.core.infrastructure.spi;

import java.util.List;

import com.images3.TemplateIdentity;
import com.images3.core.infrastructure.TemplateOS;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public interface TemplateAccess extends PaginatedResultDelegate<List<TemplateOS>> {
    
    public boolean isDuplicatedTemplateName(String imagePlantId, String name);
    
    public void insertTemplate(TemplateOS template);
    
    public void updateTemplate(TemplateOS template);
    
    public void deleteTemplate(TemplateOS template);
    
    public void deleteTemplatesByImagePlantId(String imagePlantId);
    
    public TemplateOS selectTemplateById(TemplateIdentity id);
    
    public PaginatedResult<List<TemplateOS>> selectTemplatesByImagePlantId(
            String imagePlantId, Boolean isArchived);
    
}
