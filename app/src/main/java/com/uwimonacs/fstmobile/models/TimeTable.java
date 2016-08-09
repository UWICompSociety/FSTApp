package com.uwimonacs.fstmobile.models;

import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    List<Course> courses;

    public TimeTable(){
        this.courses = new ArrayList<>();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public boolean addCourse(Course course){
        boolean there = false;
//        for(int i=0; i<courses.size(); i++)
//            if(course.getCourseCode().equals(courses.get(i).getCourseCode()))
                //there = true;
        if(!there) {
            courses.add(course);
            return true;
        } else
            return false;
    }

    public boolean removeCourse(String code){
        boolean there = false;
        int i;
        for(i=0; i<courses.size(); i++)
            if(code.equals(courses.get(i).getCourseCode())) {
                there = true;
                break;
            }
        if(there) {
            courses.remove(i);
            return true;
        } else
            return false;
    }
}
