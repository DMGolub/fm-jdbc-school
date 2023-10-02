package com.ua.foxminded.dmgolub.school.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ConsoleMenuLevel implements MenuComponent {
    
    private ArrayList<MenuComponent> menuComponents;
    
    private static final char MENU_FIRST_INDEX = 'a';
    
    public ConsoleMenuLevel() {
        this.menuComponents = new ArrayList<>();
    }

    @Override
    public void execute() {
        System.out.println(this.getDisplayName());
        System.out.print("> Enter menu item index (character): ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            char lineIndex = (char)reader.read();
            reader.readLine();
            char lastIndex = (char)(MENU_FIRST_INDEX + menuComponents.size());
            while (lineIndex < MENU_FIRST_INDEX || lineIndex > lastIndex) {
                System.out.print(
                    String.format("Index should be a character from \'%c\' to \'%c\'. "
                    + "Please try again: ", MENU_FIRST_INDEX, (char)(lastIndex - 1))
                );
                lineIndex = (char)reader.read();
                reader.readLine();
            }
            menuComponents.get(lineIndex - MENU_FIRST_INDEX).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getDisplayName() {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        result.add("-----------------------Main menu-----------------------");
        char index = MENU_FIRST_INDEX;
        for (MenuComponent menuComponent : menuComponents) {
            StringBuilder menuLine = new StringBuilder();
            menuLine.append(index++)
                .append(". ")
                .append(menuComponent.getDisplayName());
            result.add(menuLine.toString());
        }
        result.add("-------------------------------------------------------");
        return result.toString();
    }
    
    public void add(MenuComponent menuComponent) {
        if (menuComponent == null) {
            throw new IllegalArgumentException("Menu component can not be null");
        }
        menuComponents.add(menuComponent);
    }
    
    public void remove(int index) {
        if (index < 0 || index > menuComponents.size() - 1) {
            throw new IllegalArgumentException("Menu index must be positive "
                + "and less than" + menuComponents.size());
        }
        menuComponents.remove(index);
    }
}