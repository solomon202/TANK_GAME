package com.tank.utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
//загружает картинки
public class ResourceLoader {
//путь к папке 
	public static final String	PATH	= "res/";
//сохранение 
	public static BufferedImage loadImage(String fileName) {

		BufferedImage image = null;

		try {
//загрузить картинку с комп путь принимает файл
			image = ImageIO.read(new File(PATH + fileName));

		} catch (IOException e) {
			e.printStackTrace();
		}
//и возвращает
		return image;

	}

}
