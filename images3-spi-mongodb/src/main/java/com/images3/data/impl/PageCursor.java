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
package com.images3.data.impl;

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
