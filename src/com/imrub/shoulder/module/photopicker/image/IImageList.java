/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imrub.shoulder.module.photopicker.image;

import java.util.HashMap;

import android.net.Uri;

//
// ImageList and Image classes have one-to-one correspondence.
// The class hierarchy (* = abstract class):
//
//    IImageList
//    - BaseImageList (*)
//      - VideoList
//      - ImageList
//        - DrmImageList
//    - SingleImageList (contains UriImage)
//    - ImageListUber
//
//    IImage
//    - BaseImage (*)
//      - VideoObject
//      - Image
//        - DrmImage
//    - UriImage
//

public interface IImageList {
	
    public HashMap<String, String> getBucketIds();

    public int getCount();
    public boolean isEmpty();

    public IImage getImageAt(int i);
    public IImage getImageForUri(Uri uri);
    
    public boolean removeImage(IImage image);
    public boolean removeImageAt(int i);

    public int getImageIndex(IImage image);

    public void close();
}
