package com.images3.core.infrastructure.data.spi;

import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.core.infrastructure.data.VersionOS;

public interface VersionAccess {

    public void insertVersion(VersionOS version);
    
    public void deleteVersionsByImageId(String imageId);
    
    public VersionOS selectVersionById(String imageId, String templateId);
    
    public PaginatedResult<List<VersionOS>> selectVersionsByImageId(String id);
    
}
