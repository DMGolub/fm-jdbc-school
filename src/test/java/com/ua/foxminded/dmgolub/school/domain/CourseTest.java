package com.ua.foxminded.dmgolub.school.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CourseTest {
    
    @Test
    void constructor_shouldThrowIllegalArgumentException_whenCourseIdIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> new Course(-1, "Test course", "Description"));
    }

    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenCourseNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Course(1, null, "Description"));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenCourseNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Course(1, "", "Description"));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenCourseDescriptionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Course(1, "Test course", null));
    }
    
    @Test
    void constructor_shouldThrowIllegalAgrumentException_whenCourseDescriptionIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Course(1, "Test course", ""));
    }
}