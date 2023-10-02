package com.ua.foxminded.dmgolub.school.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GroupTest {
    
    @Test
    void constructor_shouldThrowIllegalArgumentException_whenGroupIdIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Group(-1, "Test group"));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenGroupNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Group(1, null));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenGroupNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Group(1, ""));
    }
}