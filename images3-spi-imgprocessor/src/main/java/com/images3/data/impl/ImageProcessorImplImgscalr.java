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

import com.images3.common.*;
import com.images3.data.spi.ImageProcessor;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ImageProcessorImplImgscalr implements ImageProcessor {
    
    private final String tempDir;
    
    public ImageProcessorImplImgscalr(String tempDir) {
        checkForDirExistence(tempDir);
        this.tempDir = tempDir;
    }
    
    private void checkForDirExistence(String path) {
        File folder = new File(path);
        if (!folder.exists() 
                || !folder.isDirectory()) {
            throw new IllegalArgumentException("Directory doesn't exists " + path);
        }
    }

    @Override
    public boolean isSupportedFormat(File imageFile) {
        return (null != getImageFormat(imageFile));
    }

    public ImageMetadata readImageMetadata(File imageFile) {
        ImageMetadata metadata = null;
        ImageInputStream imageInputStream = null;
        ImageReader imageReader = null;
        try {
            imageInputStream = ImageIO.createImageInputStream(imageFile);
            imageReader = getImageReader(imageFile, imageInputStream);
            metadata = createImageMetadata(imageFile, imageReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (null != imageReader) {
                imageReader.dispose();
            }
        }
        return metadata;
    }
    
    private ImageReader getImageReader(File imageFile, ImageInputStream imageInputStream) {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
        if (!imageReaders.hasNext()) {
            throw new IllegalArgumentException(imageFile.getAbsolutePath());
        }
        ImageReader imageReader = imageReaders.next();
        imageReader.setInput(imageInputStream);
        return imageReader;
    }
    
    private ImageMetadata createImageMetadata(File imageFile, ImageReader imageReader) throws IOException {
        ImageDimension dimension = new ImageDimension(imageReader.getWidth(0), imageReader.getHeight(0));
        ImageFormat format = ImageFormat.valueOf(imageReader.getFormatName().toUpperCase());
        return new ImageMetadata(dimension, format, imageFile.length());
    }

    public File resizeImage(ImageMetadata metadata, File imageFile, ResizingConfig resizingConfig) {
        try {
            ImageFormat format = getImageFormat(imageFile);
            String fileName = UUID.randomUUID().toString();
            File resizedImageFile = prepareImageFile(tempDir + File.separator + fileName);
            resizingConfig = getResizingConfig(metadata, resizingConfig);
            if (format == ImageFormat.GIF) {
                resizeGifImage(metadata, imageFile, resizedImageFile, resizingConfig);
            } else {
                resizeNonGifImage(metadata, imageFile, resizedImageFile, resizingConfig);
            }
            return resizedImageFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resizeGifImage(ImageMetadata metadata, File imageFile,
                                File resizedImageFile, ResizingConfig resizingConfig) throws IOException {
        GifFrameReader reader = new GifFrameReader(imageFile);
        List<ImageFrame> frames = reader.read();
        reader.close();
        BufferedImage[] resizedImages = new BufferedImage[frames.size()];
        for( int i = 0; i < frames.size(); i++ ){
            BufferedImage image = resizeImage(frames.get(i).getImage(), resizingConfig);
            resizedImages[i] = image;
        }
        GifFrameWriter writer =
                new GifFrameWriter(
                        resizedImageFile,
                        frames.get(0).getImage().getType(),
                        frames.get(0).getDelay() * 10,
                        true,
                        frames.get(0).getDisposal());
        writer.writeToSequence(resizedImages[0]);
        for ( int i = 1; i < resizedImages.length; i++ ) {
            BufferedImage nextImage = resizedImages[i];
            writer.writeToSequence(nextImage);
        }
        writer.close();
    }

    private void resizeNonGifImage(ImageMetadata metadata, File imageFile,
                                   File resizedImageFile, ResizingConfig resizingConfig) throws IOException {
        BufferedImage originalImage = ImageIO.read(
                new BufferedInputStream(Files.newInputStream(imageFile.toPath())));
        BufferedImage resizedImage = resizeImage(originalImage, resizingConfig);
        ImageIO.write(
                resizedImage,
                getImageFormat(imageFile).toString(),
                new BufferedOutputStream(Files.newOutputStream(resizedImageFile.toPath())));
    }

    private ResizingConfig getResizingConfig(ImageMetadata metadata, ResizingConfig resizingConfig) {
        if (resizingConfig.getUnit() == ResizingUnit.PERCENT) {
            float width = (float) metadata.getDimension().getWidth() * ((float) resizingConfig.getWidth() / 100.0f);
            float height = (float) metadata.getDimension().getHeight() * ((float) resizingConfig.getHeight() / 100.0f);
            return new ResizingConfig(
                    ResizingUnit.PIXEL, 
                    (int) width, 
                    (int) height,
                    resizingConfig.isKeepProportions());
        }
        return resizingConfig;
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, ResizingConfig resizingConfig) {
        BufferedImage resizedImage = null;
        if (resizingConfig.isKeepProportions()) {
            resizedImage = Scalr.resize(
                    originalImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, 
                    resizingConfig.getWidth(), resizingConfig.getHeight());
        } else {
            resizedImage = Scalr.resize(
                    originalImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, 
                    resizingConfig.getWidth(), resizingConfig.getHeight());
        }
        return resizedImage;
    }
    
    private File prepareImageFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            throw new IllegalArgumentException("Image file, " + file.getName() + " has already exist!");
        } else {
            file.createNewFile();
        }
        return file;
    }
    
    public ImageFormat getImageFormat(File imageFile) {
        ImageInputStream imageInputStream = null;
        ImageFormat format = null;
        try {
            imageInputStream = ImageIO.createImageInputStream(imageFile);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
            if (iter.hasNext()) {
                ImageReader reader = iter.next();
                if (reader.getFormatName().equalsIgnoreCase("JPEG")) {
                    format = ImageFormat.JPEG;
                } else if (reader.getFormatName().equalsIgnoreCase("png")) {
                    format = ImageFormat.PNG;
                } else if (reader.getFormatName().equalsIgnoreCase("bmp")) {
                    format = ImageFormat.BMP;
                } else if (reader.getFormatName().equalsIgnoreCase("gif")) {
                    format = ImageFormat.GIF;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return format;
    }
    
}
