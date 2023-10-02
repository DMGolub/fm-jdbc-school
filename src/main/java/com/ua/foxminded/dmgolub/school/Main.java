package com.ua.foxminded.dmgolub.school;

import com.ua.foxminded.dmgolub.school.dao.*;
import com.ua.foxminded.dmgolub.school.ui.*;

public class Main {
    
    private static final String PROPERTIES_FILE_NAME = "connection.properties";

    public static void main(String[] args) {
        ConnectionProvider connectionProvider = new ProdConnectionProvider(PROPERTIES_FILE_NAME);
        
        InitialDataGenerator generator = new InitialDataGenerator(10, 10, 200, 3, connectionProvider);
        generator.generate();

        CourseDAO courseDAO = new CourseDAO(connectionProvider);
        GroupDAO groupDAO = new GroupDAO(connectionProvider);
        StudentDAO studentDAO = new StudentDAO(connectionProvider);

        ConsoleMenuLevel mainMenu = new ConsoleMenuLevel();
        mainMenu.add(new FindGroupsByStudentCount(
            "Find all groups with less or equal student count", groupDAO));
        mainMenu.add(new FindStudentsByCourse(
            "Find all students related to course with given name", courseDAO, studentDAO));
        mainMenu.add(new AddNewStudent(
            "Add new student", groupDAO, studentDAO));
        mainMenu.add(new DeleteStudentById(
            "Delete student by STUDENT_ID", studentDAO));
        mainMenu.add(new AddStudentToCourse(
            "Add a student to the course (from a list)", courseDAO, studentDAO));
        mainMenu.add(new RemoveStudentFromCourse(
            "Remove the student from one of his or her courses", courseDAO, studentDAO));
        mainMenu.add(new QuitProgram("Quit program"));
        
        while (true) {
            mainMenu.execute();
        }
    }
}