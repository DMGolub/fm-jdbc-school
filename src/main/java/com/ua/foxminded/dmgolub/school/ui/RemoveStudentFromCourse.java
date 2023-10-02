package com.ua.foxminded.dmgolub.school.ui;

import java.util.List;
import java.util.Optional;

import com.ua.foxminded.dmgolub.school.dao.CourseDAO;
import com.ua.foxminded.dmgolub.school.dao.StudentDAO;
import com.ua.foxminded.dmgolub.school.domain.Course;
import com.ua.foxminded.dmgolub.school.domain.Student;

public class RemoveStudentFromCourse extends ConsoleMenuItem {

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    
    public RemoveStudentFromCourse(String displayName, CourseDAO courseDAO, StudentDAO studentDAO) {
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
        System.out.print("> Enter student id: ");
        int studentId = readIdFromConsole();
        Optional<Student> student = studentDAO.getStudentById(studentId);
        if (student.isPresent()) {
            List<Course> courses = studentDAO.getCoursesForStudent(studentId);
            if (!courses.isEmpty()) {
                System.out.println("Student's course list: ");
                courses.forEach(
                    c -> System.out.println(c.getId() + ". " + c.getName())
                );
                System.out.print("> Enter course id: ");
                int courseId = readIdFromConsole();
                Optional<Course> course = courseDAO.getCourseById(courseId);
                if (course.isPresent()) {
                    if (studentDAO.removeStudentFromCourse(student.get(), course.get())) {
                        System.out.println(String.format(
                            "Student %d removed from course %d", studentId, courseId));
                    } else {
                        System.out.println(String.format(
                            "Can not remove student %d from course %d", studentId, courseId));
                    }
                } else {
                    System.out.println(
                        String.format("Can not find course by id = %d", courseId));
                }
            } else {
                System.out.println("Student has no courses yet");
            }
        } else {
            System.out.println(String.format("Can not find student by id = %d", studentId));
        }
    }
}