package com.ua.foxminded.dmgolub.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ua.foxminded.dmgolub.school.domain.Group;

public class GroupDAO {
    
    private ConnectionProvider connectionProvider;
    
    public GroupDAO(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException("Connection supplier can not be null");
        }
        this.connectionProvider = connectionProvider;
    }
    
    public List<Group> getAllGroups() {
        final String query = "SELECT group_id, group_name FROM groups";
        List<Group> groups = new ArrayList<>();
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                groups.add(new Group(
                    resultSet.getInt("group_id"), 
                    resultSet.getString("group_name")
                ));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return groups;
    }
    
    public Optional<Group> getGroupById(int id) {
        final String query = "SELECT group_id, group_name FROM groups WHERE group_id = ?";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Group(
                    resultSet.getInt("group_id"), 
                    resultSet.getString("group_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    public List<Group> findGroupsByStudentCount(int studentNumber) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Student count can not be negative");
        }
        final String query = 
            "SELECT g.group_id, group_name, COUNT(student_id) AS student_count "
            + "FROM students AS s INNER JOIN groups AS g ON s.group_id = g.group_id "
            + "GROUP BY g.group_id HAVING (COUNT(student_id) <= ?) ORDER BY g.group_id";
        List<Group> groups = new ArrayList<>();
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, studentNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                groups.add(new Group(
                    resultSet.getInt("group_id"), 
                    resultSet.getString("group_name")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return groups;
    }
}