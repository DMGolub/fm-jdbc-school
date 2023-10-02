package com.ua.foxminded.dmgolub.school.domain;

public class Group {
    
    private final int id;
    private String name;
    
    public Group(int id, String name) {
        if (id < 1) {
            throw new IllegalArgumentException("Group id can not be less than 1");
        }
        if (name == null) {
            throw new IllegalArgumentException("Group name can not be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Group name can not be an empty string");
        }
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Group [id = " + id + ", name = " + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        Group other = (Group) obj;
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