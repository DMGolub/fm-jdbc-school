package com.ua.foxminded.dmgolub.school.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class ConsoleMenuItem implements MenuComponent {
    
    private String displayName;
    
    protected ConsoleMenuItem(String displayName) {
        if (displayName == null) {
            throw new IllegalArgumentException("menu item display name can not be null");
        }
        if (displayName.isEmpty()) {
            throw new IllegalArgumentException("menu item display name can not be an empty string");
        }
        this.displayName = displayName;
    }
    
    @Override
    public String getDisplayName() {
        return displayName;
    }
    
    protected int readIdFromConsole() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = 0;
        do {
            try {
                id = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.print("Wrong input format. ");
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            if (id < 1) {
                System.out.print("Id must be a positive number. Please try again: ");
            }
        } while (id < 1);
        return id;
    }
    
    protected String readNameFromConsole() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = null;
        try {
            name = reader.readLine();
            while (name.isEmpty()) {
                System.out.print("Name can not be an empty string. Please try again: ");
                name = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}