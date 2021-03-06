/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.images3.data.spi;

import java.util.List;

import com.images3.common.TemplateIdentity;
import com.images3.data.TemplateOS;

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
