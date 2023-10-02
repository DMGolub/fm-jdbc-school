package com.ua.foxminded.dmgolub.school.domain;

public class Course {
    
    private final int id;
    private final String name;
    private final String description;
    
    public Course(int id, String name, String description) {
        if (id < 1) {
            throw new IllegalArgumentException("Course id can not be less than 1");
        }
        if (name == null) {
            throw new IllegalArgumentException("Course name can not be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Course name can not be empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("Course description can not be null");
        }
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Course description can not be empty");
        }
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return "Course [id = " + id + ", name = " + name + ", description = "
            + description + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((description == null) ? 0 : description.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Course other = (Course) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}