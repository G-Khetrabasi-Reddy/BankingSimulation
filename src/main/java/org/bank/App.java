package org.bank;

import org.bank.config.DBSetup;

public class App {
    public static void main(String[] args) {
        DBSetup.createTables();
    }
}
