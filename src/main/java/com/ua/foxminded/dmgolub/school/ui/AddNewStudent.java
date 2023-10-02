package com.ua.foxminded.dmgolub.school.ui;

import java.util.Optional;

import com.ua.foxminded.dmgolub.school.dao.GroupDAO;
import com.ua.foxminded.dmgolub.school.dao.StudentDAO;
import com.ua.foxminded.dmgolub.school.domain.Group;
import com.ua.foxminded.dmgolub.school.domain.Student;

public class AddNewStudent extends ConsoleMenuItem {
    
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    
    public AddNewStudent(String displayName, GroupDAO groupDAO, StudentDAO studentDAO) {
        super(displayName);
        if (groupDAO == null) {
            throw new IllegalArgumentException("Group DAO can not be null");
        }
        if (studentDAO == null) {
            throw new IllegalArgumentException("Student DAO can not be null");
        }        
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
    }
    
    @Override
    public void execute() {
        System.out.print("Enter new student first name: ");
        String firstName = super.readNameFromConsole();
        System.out.print("Enter student last name: ");
        String lastName = super.readNameFromConsole();
        System.out.println("Avaliable groups: ");
        groupDAO.getAllGroups().forEach(
            g -> System.out.println(g.getId() + ". " + g.getName())
        );
        System.out.print("Enter group id: ");
        int groupId = readIdFromConsole();
        Optional<Group> group = groupDAO.getGroupById(groupId);
        if (group.isPresent()) {
            Student student = new Student(1, group.get(), firstName, lastName);
            int studentId = studentDAO.addNewStudent(student);
            if (studentId > 0) {
                System.out.println("Student with id = " + studentId + " added successfully");
                student.setId(studentId);
            } else {
                System.out.println("Can not add new student");
            }
        } else {
            System.out.println(String.format(
                "Can not find group with id = %d. Please try again", groupId));
        }
    }
}