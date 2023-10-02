package com.ua.foxminded.dmgolub.school.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StudentTest {
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenIdIsLessThanOne() {
        assertThrows(IllegalArgumentException.class,
            () -> new Student(-1, null, "firstName", "lastName"));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenFirstNameIsNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Student(1, null, null, "lastName"));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenFirstNameIsEmpty() {
        assertThrows(IllegalArgumentException.class,
            () -> new Student(1, null, "", "lastName"));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenLastNameIsNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Student(1, null, "firstName", null));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenLastNameIsEmpty() {
        assertThrows(IllegalArgumentException.class,
            () -> new Student(1, null, "firstName", null));
    }
}