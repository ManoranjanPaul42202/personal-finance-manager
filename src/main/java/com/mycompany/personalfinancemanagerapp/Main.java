package com.mycompany.personalfinancemanagerapp;

import view.LoginView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView login = new LoginView();
                login.setVisible(true);
            }
        });
    }
}
