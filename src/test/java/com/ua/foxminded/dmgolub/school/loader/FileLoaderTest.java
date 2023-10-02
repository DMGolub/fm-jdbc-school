package com.ua.foxminded.dmgolub.school.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class FileLoaderTest {
    
    @Test
    void fileLoader_shouldThrowIllegalArgumentException_whenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FileLoader(null));
    }
    
    @Test
    void fileLoader_shouldThrowIllegalArgumentException_whenAddressIsAnEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> new FileLoader(""));
    }

    @Test
    void fileLoader_shouldReturnAnEmptyList_whenTheGivenFileIsEmpty() {
        List<String> expected = new ArrayList<>();
    
        String path = ClassLoader.getSystemResource("emptyTestFile.txt").getPath();
        Loader<List<String>> loader = new FileLoader(path);

        assertEquals(expected, loader.load());
    }
    
    @Test
    void fileLoader_shouldReturnAListWithOneString_whenFileContainsOneLine() {
        List<String> expected = new ArrayList<>();
        expected.add("some random string");

        String path = ClassLoader.getSystemResource("oneLineTestFile.txt").getPath();
        Loader<List<String>> loader = new FileLoader(path);
        
        assertEquals(expected, loader.load());
    }
    
    @Test
    void fileLoader_shouldReturnAListWithMultipleElements_whenMultilineFileIsGiven() {
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

        String path = ClassLoader.getSystemResource("courses.txt").getPath();
        Loader<List<String>> loader = new FileLoader(path);
        
        assertEquals(expected, loader.load());
    }
}