package com.images3.infrastructure.data.spi;

import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.infrastructure.data.ImagePlantOS;
import com.images3.infrastructure.data.TemplateOS;

public interface TemplateAccess {

    public String generateTemplateId(ImagePlantOS imagePlant);
    
    public boolean isDuplicatedTemplateName(String imagePlantId, String name);
    
    public void insertTemplate(TemplateOS template);
    
    public void updateTemplate(TemplateOS template);
    
    public void deleteTemplate(TemplateOS template);
    
    public void deleteTemplatesByImagePlantId(String imagePlantId);
    
    public TemplateOS selectTemplateById(String imagePlantId, String id);
    
    public PaginatedResult<List<TemplateOS>> selectTemplatesByImagePlantId(String imagePlant);
    
    public PaginatedResult<List<TemplateOS>> selectTemplatesByImagePlantId(String imagePlant, boolean isArchived);
    
}
