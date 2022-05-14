package main;

import main.domain.viewUtils.Application;

import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        TestData t = new TestData();
        Application app = new Application();
        app.run();
    }
}