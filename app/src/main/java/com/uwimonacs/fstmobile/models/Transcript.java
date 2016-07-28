package com.uwimonacs.fstmobile.models;

import java.util.List;

/**
 * Created by sultanofcardio on 7/28/16.
 */
public class Transcript {
    String degree, program, faculty, major, minor;
    List<Term> terms;

    public String getDegree() {
        return degree;
    }

    public Transcript setDegree(String degree) {
        this.degree = degree;
        return this;
    }

    public String getProgram() {
        return program;
    }

    public Transcript setProgram(String program) {
        this.program = program;
        return this;
    }

    public String getFaculty() {
        return faculty;
    }

    public Transcript setFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public String getMajor() {
        return major;
    }

    public Transcript setMajor(String major) {
        this.major = major;
        return this;
    }

    public String getMinor() {
        return minor;
    }

    public Transcript setMinor(String minor) {
        this.minor = minor;
        return this;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public static class Term{
        String name;
        List<Course> courses;

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class Course{
            String subject, code, title, score, grade, creditHours;

            public String getSubject() {
                return subject;
            }

            public Course setSubject(String subject) {
                this.subject = subject;
                return this;
            }

            public String getCode() {
                return code;
            }

            public Course setCode(String code) {
                this.code = code;
                return this;
            }

            public String getTitle() {
                return title;
            }

            public Course setTitle(String title) {
                this.title = title;
                return this;
            }

            public String getScore() {
                return score;
            }

            public Course setScore(String score) {
                this.score = score;
                return this;
            }

            public String getGrade() {
                return grade;
            }

            public Course setGrade(String grade) {
                this.grade = grade;
                return this;
            }

            public String getCreditHours() {
                return creditHours;
            }

            public Course setCreditHours(String creditHours) {
                this.creditHours = creditHours;
                return this;
            }
        }
    }
}
