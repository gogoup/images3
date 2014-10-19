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
package com.images3;

import java.util.Date;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ImageVersion;

public class ImageResponse {

    private ImageIdentity id;
    private Date dateTime;
    private ImageVersion version;
    private ImageMetadata metadata;
    
    public ImageResponse(ImageIdentity id, Date dateTime, ImageVersion version,
            ImageMetadata metadata) {
        this.id = id;
        this.dateTime = dateTime;
        this.version = version;
        this.metadata = metadata;
    }

    public ImageIdentity getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public ImageVersion getVersion() {
        return version;
    }

    public ImageMetadata getMetadata() {
        return metadata;
    }
    
}
