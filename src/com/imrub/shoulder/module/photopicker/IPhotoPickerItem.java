package com.imrub.shoulder.module.photopicker;

import java.util.List;

import com.imrub.shoulder.module.photopicker.image.IImage;

public interface IPhotoPickerItem {
	public void onAddNewItem(PhotoPickerItem item);
	public void onAllImages(List<IImage> allImages);
}
