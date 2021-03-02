package com.tank.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JFrame;

import com.tank.IO.Input;

//класс полотно
public class Display {
	
	private static boolean created = false;
	private static JFrame window;
	private static Canvas content;
//	класс который содержит изображение
	private static BufferedImage buffer;
//	буфер данных изображения
	private static int[] bufferData;
//	ккласс для работы с графикой
	private static Graphics bufferGraphics;
//	цвет для отчистки картинки
	private static int clearColor;
	private static BufferStrategy bufferStrategy;
	

	
	
   // метод получает размеры и имя
	
	 public static void create(int width, int height, String title,int _clearColor,int numBuffers) {
		 
   //	если поле есть то не создаём если нет создаём
		 
		if (created)
			return;
		
  //      рамка название обьект рамка
		
		window = new JFrame(title);
		
 //		 что сделать с окном свернуть рамку при закрытии
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
  //		содержания листа  обьект лист
		
		content = new Canvas();
		
		
 //		обьек тразмер
		
		Dimension size = new Dimension(width, height);
		
 //		вставляем ссылку на обьект размер в метод содержания листа
		
		content.setPreferredSize(size);
  //	методы рамки не изменяемый размер
		
		window.setResizable(false);
		

		//		рамка получит содержимое получить содержимое и настройки
		
		window.getContentPane().add(content);
//		отвечает за размер
 	    window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
			

//	хранит картинку высота ширина и способ хранения виде списка int которые в себе храят
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//		вытаскиваем Arrer int из буфераизображения
		bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
//	получаем обект типа график	
		bufferGraphics = buffer.getGraphics();
		//функция сглаживание
		 ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		сохранить цвет на тот которыйбыл инцылизирован
		clearColor =  _clearColor;

//создание буферов 3 шт
		 content.createBufferStrategy(numBuffers);
		 //буферная стратегия вставляем буферы
		 bufferStrategy = content.getBufferStrategy();

//       поле есть 
			created = true;
		}
//	 очищяет буфер на тот цвет который установлен
	 public static void clear() {
//		 зопалняет одинаковыми значениями (буфер данных и цвет)
//		 стереть и залить цветом
			Arrays.fill(bufferData, clearColor);
		}
// меняет на новую сцену на холсте
		public static void swapBuffers() {
//			вытаскиваем графику и втавляем следующию по очереди
			Graphics g = bufferStrategy.getDrawGraphics();
//		и	мы хотим нарисовать наш буферкартинки
			g.drawImage(buffer, 0, 0, null);
			//показать буфер
			bufferStrategy.show();
	
	}
//расширяет класс график
	public static Graphics2D getGraphics() {
		return (Graphics2D) bufferGraphics;
	}
//стереть окно
	public static void destroy() {
	 	//проверка не создано значит нечего стирать

		if (!created)
			return;
//установить окно
		window.dispose();

	}
//меняет имя окна
	public static void setTitle(String title) {

		window.setTitle(title);

	}

	public static void addInputListener(Input inputListener) {
		window.add(inputListener);

}
}

