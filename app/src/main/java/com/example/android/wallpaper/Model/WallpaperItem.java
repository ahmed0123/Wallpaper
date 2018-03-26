package com.example.android.wallpaper.Model;

/**
 * Created by Ahmed El Hendawy on 2018/03/26.
 */

public class WallpaperItem {
	public String imageLink;
	public String categoryId;
	
	public WallpaperItem() {
	}
	
	public WallpaperItem(String imageLink, String categoryId) {
		this.imageLink = imageLink;
		this.categoryId = categoryId;
	}
	
	public String getImageLink() {
		return imageLink;
	}
	
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
