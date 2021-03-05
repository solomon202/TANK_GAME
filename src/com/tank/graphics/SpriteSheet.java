package com.tank.graphics;
import java.awt.image.BufferedImage;

//отвечает за маленький кусок анимации
public class SpriteSheet {

	private BufferedImage	sheet;
	//хранение количество спрайтов спрайтов
	private int				spriteCount;
	//размер сспрайта
	private int				scale;
	//количество спрайтов в ширину
	private int				spritesInWidth;
    //лист спрайта
	public SpriteSheet(BufferedImage sheet, int spriteCount, int scale) {
		this.sheet = sheet;
		this.spriteCount = spriteCount;
		this.scale = scale;

		this.spritesInWidth = sheet.getWidth() / scale;

	}
   //номируем танк фигуру вырезаного танка
	public BufferedImage getSprite(int index) {
  //количество загружаемых спрайтов
		index = index % spriteCount;
//вырезать танк
		int x = index % spritesInWidth * scale;
		int y = index / spritesInWidth * scale;
  //возвратить вырезаную картинку высота и ширена
		return sheet.getSubimage(x, y, scale, scale);

	}

}