package com.example.android.wallpaper.Model;

/**
 * Created by Ahmed ElHendawy on 2018/03/26.
 */

public class CategoryItem {
	
	public String name;
	public String imageLink;
	
	public CategoryItem() {
	}
	
	public CategoryItem(String name, String imageLink) {
		this.name = name;
		this.imageLink = imageLink;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImageLink() {
		return imageLink;
	}
	
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
}
