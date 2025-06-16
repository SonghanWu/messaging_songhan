package com.songhanwu.messaging;

public class Main {

    public static void main(String[] args) {
        yell(new Duck());
        yell(new Dog());
    }

    public static void yell(Audible audible) {
        audible.yell();
    }
}
