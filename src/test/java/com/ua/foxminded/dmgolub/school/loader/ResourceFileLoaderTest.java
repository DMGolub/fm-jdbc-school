package com.ua.foxminded.dmgolub.school.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class ResourceFileLoaderTest {
    
    @Test
    void constructor_shouldThrowIllegalArgumentException_whenNoFileFound() {
        assertThrows(IllegalArgumentException.class,
            () -> new ResourceFileLoader("fileNotFound.txt"));
    }

    @Test
    void resourceFileLoader_shouldLoadDataFromFile_whenAFileNameWithoutPathIsGiven() {        
        List<String> expected = new ArrayList<>(Arrays.asList(
            "Architecture", 
            "Biology", 
            "Chemistry", 
            "Computer science", 
            "Economics", 
            "History", 
            "Literature", 
            "Mathematics", 
            "Physics", 
            "Political science"
        ));
        
        Loader<List<String>> loader = new ResourceFileLoader("courses.txt");
        
        assertEquals(expected, loader.load());
    }
}