package com.tank.main;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Timer;
//доступ к пакету дисплей и к его обьектам
import com.tank.display.Display;




public class Main {
	
	public static void main (String []args) {
//		создаём обьект и передаём аргументы в егометод
//		если поле статично к нему можно обращатся через поле класа аргументы глобальные неизменяемы 
		Display.create(800,600,"Tank", 0xff00ff00);
		
		Timer t = new Timer(1000 / 60, new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
//				стереть буфер изображения
				Display.clear();
				Display.render();
//				и вставить новое изабражение
				Display.swapBuffers();
			}

		});
		
		t.setRepeats(true);
		t.start();

	} 
	} 
	
