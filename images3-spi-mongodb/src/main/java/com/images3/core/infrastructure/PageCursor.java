package com.images3.core.infrastructure;

import java.util.Date;

public class PageCursor {

    private String id;
    private String previousPageCursorId;
    private Page page;
    private Date creationTime;
    
    public PageCursor(String id, String previousPageCursorId, Page page,
            Date creationTime) {
        this.id = id;
        this.previousPageCursorId = previousPageCursorId;
        this.page = page;
        this.creationTime = creationTime;
    }
    
    public String getId() {
        return id;
    }
    public String getPreviousPageCursorId() {
        return previousPageCursorId;
    }
    public Page getPage() {
        return page;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    
}
