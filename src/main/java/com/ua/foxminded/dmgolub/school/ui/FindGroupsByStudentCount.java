package com.ua.foxminded.dmgolub.school.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.ua.foxminded.dmgolub.school.dao.GroupDAO;
import com.ua.foxminded.dmgolub.school.domain.Group;

public class FindGroupsByStudentCount extends ConsoleMenuItem {

    private GroupDAO groupDAO;
    
    public FindGroupsByStudentCount(String displayName, GroupDAO groupDAO) {
        super(displayName);
        if (groupDAO == null) {
            throw new IllegalArgumentException("Group DAO can not be null");
        }
        this.groupDAO = groupDAO;
    }
    
    @Override
    public void execute() {
        System.out.print("> Enter student count: ");
        int studentCount = -1;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            try {
                studentCount = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.print("Wrong input format. ");
            }
            while (studentCount < 0) {
                System.out.print("Student count must be a number greater than "
                    + "or equal to zero. Please try again: ");
                try {
                    studentCount = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.print("Wrong input format. ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Group> groups = groupDAO.findGroupsByStudentCount(studentCount);
        if (!groups.isEmpty()) {
            groups.forEach(
                g -> System.out.println(g.getId() + " " + g.getName())
            );
        } else {
            System.out.println("No groups found");
        }
    }
}