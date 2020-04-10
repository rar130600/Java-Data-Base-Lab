package ui.controller;

import database.ProductDAO;
import ui.MainApp;

public class DataBaseWindowController {
    private MainApp mainApp;
    private ProductDAO dao;

    public void provideApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDao(ProductDAO dao) {
        this.dao = dao;
    }
}
