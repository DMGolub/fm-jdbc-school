package com.ua.foxminded.dmgolub.school.ui;

import java.util.List;
import java.util.Optional;

import com.ua.foxminded.dmgolub.school.dao.CourseDAO;
import com.ua.foxminded.dmgolub.school.dao.StudentDAO;
import com.ua.foxminded.dmgolub.school.domain.Course;
import com.ua.foxminded.dmgolub.school.domain.Student;

public class FindStudentsByCourse extends ConsoleMenuItem {
    
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    
    public FindStudentsByCourse(String displayName, CourseDAO courseDAO, StudentDAO studentDAO) {
        super(displayName);
        if (courseDAO == null) {
            throw new IllegalArgumentException("Course DAO can not be null");
        }
        if (studentDAO == null) {
            throw new IllegalArgumentException("Student DAO can not be null");
        }
        this.courseDAO = courseDAO;
        this.studentDAO = studentDAO;
    }
    
    @Override
    public void execute() {
        System.out.println("Avaliable courses: ");
        courseDAO.getAllCourses().forEach(
            c -> System.out.println(c.getId() + " " + c.getName())
        );
        System.out.print("> Enter course name: ");
        String courseName = readNameFromConsole();   

        Optional<Course> course = courseDAO.getCourseByName(courseName);
        if (course.isPresent()) {
            List<Student> students = studentDAO.findStudentsByCourse(course.get());
            if (students.isEmpty()) {
                System.out.println("No students found");
            } else {
                students.forEach(
                    s -> System.out.println(s.getId() + " " + s.getFirstName() + " " + s.getLastName())
                );
            }
        } else {
            System.out.println("Can not find course by name " + courseName);
        }
    }
}