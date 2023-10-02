package com.ua.foxminded.dmgolub.school.ui;

public class QuitProgram extends ConsoleMenuItem {

    public QuitProgram(String displayName) {
        super(displayName);
    }
    
    @Override
    public void execute() {
        System.exit(0);
    }
}