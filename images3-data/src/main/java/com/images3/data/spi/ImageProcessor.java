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

import java.io.File;

import com.images3.common.ImageMetadata;
import com.images3.common.ResizingConfig;

public interface ImageProcessor {

    public boolean isSupportedFormat(File imageFile);
    
    public ImageMetadata readImageMetadata(File imageFile);

    public File resizeImage(ImageMetadata metadata, File imageFile, ResizingConfig resizingConfig);
}
