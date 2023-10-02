package com.ua.foxminded.dmgolub.school.ui;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ConsoleMenuLevelTest {
    
    private ConsoleMenuLevel menuLevel = new ConsoleMenuLevel();
    
    @Test
    void add_shouldThrowIllegalArgumentException_whenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> menuLevel.add(null));
    }
    
    @Test
    void removeItem_should_when() {
        
    }
}