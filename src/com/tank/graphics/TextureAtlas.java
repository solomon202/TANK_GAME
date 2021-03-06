package com.tank.graphics;
//класс отвечает за большое полотно изображения из которого мы можем вырезать маленькие куски
import java.awt.image.BufferedImage;

import com.tank.utils.ResourceLoader;

public class TextureAtlas {

	BufferedImage	image;

	public TextureAtlas(String imageName) {
//		загружаем нашу картинку 
		image = ResourceLoader.loadImage(imageName);
	}
//вырезаем скартинки куски 
	public BufferedImage cut(int x, int y, int w, int h) {
		return image.getSubimage(x, y, w, h);
	}

}