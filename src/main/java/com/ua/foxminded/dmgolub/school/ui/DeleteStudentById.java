package com.ua.foxminded.dmgolub.school.ui;

import java.util.Optional;

import com.ua.foxminded.dmgolub.school.dao.StudentDAO;
import com.ua.foxminded.dmgolub.school.domain.Student;

public class DeleteStudentById extends ConsoleMenuItem {

    private StudentDAO studentDAO;
    
    public DeleteStudentById(String displayName, StudentDAO studentDAO) {
        super(displayName);
        if (studentDAO == null) {
            throw new IllegalArgumentException("Student DAO can not be null");
        }
        this.studentDAO = studentDAO;
    }
    
    @Override
    public void execute() {
        System.out.print("> Enter student id: ");
        int studentId = readIdFromConsole();
        Optional<Student> student = studentDAO.getStudentById(studentId);
        if (student.isPresent()) {
            if (studentDAO.deleteStudentById(studentId)) {
                System.out.println("Student with id = " + studentId + " deleted");
            } else {
                System.out.println("Can not delete student by id = " + studentId);
            }
        } else {
            System.out.println("Can not find student by id = " + studentId);
        }
    }
}