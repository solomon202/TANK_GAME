package com.tank.game;



import java.awt.Graphics2D;

import com.tank.IO.Input;
import com.tank.display.Display;
import com.tank.graphics.TextureAtlas;
import com.tank.utils.Time;
//поток
public class Game implements Runnable {

    public static final int		WIDTH			= 800;
    public static final int		HEIGHT			= 600;
    public static final String	TITLE			= "Tanks";
    public static final int		CLEAR_COLOR		= 0xff000000;
    public static final int		NUM_BUFFERS		= 3;
//обновляем 60 раз в секунду расчеты и физика
    public static final float	UPDATE_RATE		= 60.0f;
    public static final float	UPDATE_INTERVAL	= Time.SECOND / UPDATE_RATE;
    //остановка потока на время чтобы другие потоки могли что делать
    public static final long	IDLE_TIME		= 1;

    public static final String	ATLAS_FILE_NAME	= "texture_atlas.png";
//идет игра или не идёт
    private boolean				running;
    //поток
    private Thread				gameThread;
    private Graphics2D			graphics;
    private Input				input;
    private TextureAtlas		atlas;
    private Player				player;

    public Game() {
        //пока не запущена
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        //вытащить графику и рисовать изменения с помощью неё
        graphics = Display.getGraphics();
        input = new Input();
//        засунули компонет в нутирь нашего дисплея 
        Display.addInputListener(input);
//        атлас вырезаем маленькие картинки 
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
//        игрок
        player = new Player(300, 300, 2, 3, atlas);
    }
    //старт игры сихронизированый запускается по очереди
    public synchronized void start() {
 //создать да нет
        if (running)
            return;

        running = true;
        //в поток вставляем наш класс
        gameThread = new Thread(this);
        //и стартуем и запускаем ран
        gameThread.start();

    }
//остановить поток
    public synchronized void stop() {

        if (!running)
            return;

        running = false;
//как добежит поток до конца после остановить ждет пока он закончит свою работу
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//после остановки отчистить
        cleanUp();

    }
//обработка физики математике все расчёты дижения
    private void update() {

        player.update(input);
    }
//выше с пощитоной иформацией уже на этой основе ресуем
    private void render() {
        //ОЧИЩЯЕТ БУФЕР
        Display.clear();
        player.render(graphics);
        //мы закончили рисовать нашу сцену и теперь хотим её нарисовать
        Display.swapBuffers();
    }
//потк вызывает в нутри себя методы
    public void run() {
//кадров в секунду
        int fps = 0;
        //обновление
        int upd = 0;
        //догоняем обновления
        int updl = 0;
//печать по времени
        long count = 0;
//количество времени с прошлого потока
        float delta = 0;
//содержит время
        long lastTime = Time.get();
        //пока поток бежит пока поток не изменится
        while (running) {
            //текущие время
            long now = Time.get();
            //сколько время прошло
            long elapsedTime = now - lastTime;
            //ответ стокото времени
            lastTime = now;

            count += elapsedTime;

            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta > 1) {
                update();
                upd++;
                delta--;
                //были изменения да нет
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }
//если произошли какието изменения на карте значит рисовать
            if (render) {
                render();
                fps++;
                //если ни каких изменений не произошло не чего не делать
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//считае и выводит сколько обновлений и фпс
            //КАЖДУЮ СЕКУНДУ ОБНАВЛЯЕ И СБРАСЫВАЕМ В НУЛИ
            if (count >= Time.SECOND) {
                //вставляем в наше окно
                Display.setTitle(TITLE + " || Fps: " + fps + " | Upd: " + upd + " | Updl: " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }

        }

    }
//закрывание функций уничтожить окно при закрытии окна
    private void cleanUp() {

        Display.destroy();
    }

}