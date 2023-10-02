package com.ua.foxminded.dmgolub.school.domain;

public class Student {
    
    private int id;
    private Group group;
    private final String firstName;
    private final String lastName;
    
    public Student(int id, Group group, String firstName, String lastName) {
        if (id < 1) {
            throw new IllegalArgumentException("Student's id can not be less than 1");
        }
        if (firstName == null) {
            throw new IllegalArgumentException("Student's first name can not be null");
        }
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("Student's first name can not be empty");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("Student's last name can not be null");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Student's last name can not be empty");
        }
        this.id = id;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Student's id can not be less than 1");
        }
        this.id = id;
    }
    
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Student [id = " + id + ", group = " + group + ", firstName = "
            + firstName + ", lastName = " + lastName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + id;
        result = prime * result
                + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        return true;
    }
}