package com.tank.utils;


    public class Time {
//предстовление времени все манипуляции со временем поле
        public static final long	SECOND	= 1000000000l;
//возвращает текущие время
        public static long get() {

            return System.nanoTime();
        }

    }

