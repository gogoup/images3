package com.images3.core.infrastructure.spi;

import java.util.List;

import com.images3.ImageIdentity;
import com.images3.VersionIdentity;
import com.images3.core.infrastructure.VersionOS;
import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public interface VersionAccess extends PaginatedResultDelegate<List<VersionOS>> {

    public void insertVersion(VersionOS version);
    
    public void deleteVersionsByImageId(ImageIdentity imageId);
    
    public VersionOS selectVersionById(VersionIdentity id);
    
    public PaginatedResult<List<VersionOS>> selectVersionsByImageId(ImageIdentity imageId);
    
}
