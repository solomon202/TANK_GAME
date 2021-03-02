package com.tank.IO;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
//получить доступ к методам, component
public class Input extends JComponent {
//ключи связываем кнопку с тем что надо сделать 
	private boolean[]	map;

	public Input() {
// создаем     список знаков 
		map = new boolean[256];
//пробигаемся по всем значениям 
		for (int i = 0; i < map.length; i++) {

			final int KEY_CODE = i;
//        приравниваем кнопки какоето значение реагирует на поле фокусе
//			put какую кнопку i мы хотим мониторить нажата отпущена 
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i, 0, false), i * 2);
//			что должно происходить при нажатии звать функцию
			getActionMap().put(i * 2, new AbstractAction() {
				@Override
//				индекс нажатой кнопки в списке привратится в true
				public void actionPerformed(ActionEvent arg0) {
					map[KEY_CODE] = true;
				}
			});
//   когда кнопка отпускается 
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i, 0, true), i * 2 + 1);
			getActionMap().put(i * 2 + 1, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					map[KEY_CODE] = false;
				}
			});

		}

	}
//карта какие кнопки сейчас нажаты 
	public boolean[] getMap() {
		return Arrays.copyOf(map, map.length);
	}
//нажата кнопкаили не нажата 
	public boolean getKey(int keyCode) {
//		да или нет 
		return map[keyCode];
	}

}

