package com.uwimonacs.fstmobile.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sultanofcardio on 7/28/16.
 */
public class Transcript {
    private String degree, program, faculty, major, minor;
    private List<Term> terms;
    private double GPA, degreeGPA;

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

    public double getGPA() {
        calculateGPA();
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    private void calculateGPA(){
        double totalQualityPoints = 0;
        double totalCreditHours = 0;
        for(Term term: terms){
            totalCreditHours += term.getTotalCreditHours();
            totalQualityPoints += term.getTotalQualityPoints();
        }
        this.GPA = totalQualityPoints/totalCreditHours;
    }

    public double getDegreeGPA() {
        calculateDegreeGPA();
        return degreeGPA;
    }

    public void setDegreeGPA(double degreeGPA) {
        this.degreeGPA = degreeGPA;
    }

    private void calculateDegreeGPA(){
        double totalDegreeQualityPoints = 0;
        double totalDegreeCreditHours = 0;
        for(Term term: terms){
            totalDegreeCreditHours += term.getTotalDegreeCreditHours();
            totalDegreeQualityPoints += term.getTotalDegreeQualityPoints();
        }
        this.degreeGPA = totalDegreeQualityPoints/totalDegreeCreditHours;
    }

    public static class Term{
        private String name;
        private List<Course> courses;
        private double GPA;
        private Map<String, Double> gradePoints = new HashMap<>();
        private double totalCreditHours, totalQualityPoints;
        private double totalDegreeCreditHours, totalDegreeQualityPoints;

        public Term(){
            initPoints();
        }

        private void initPoints(){
            gradePoints.put("A+", 4.3);
            gradePoints.put("A", 4.0);
            gradePoints.put("A-", 3.7);
            gradePoints.put("B+", 3.3);
            gradePoints.put("B", 3.0);
            gradePoints.put("B-", 2.7);
            gradePoints.put("C+", 2.3);
            gradePoints.put("C", 2.0);
            gradePoints.put("C-", 1.7);
            gradePoints.put("F1", 0.0);
            gradePoints.put("F2", 0.0);
            gradePoints.put("F3", 0.0);
            gradePoints.put("FE", 0.0);
            gradePoints.put("FC", 0.0);
        }

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

        public double getGPA() {
            calculateGPA();
            return GPA;
        }

        public void setGPA(double GPA) {
            this.GPA = GPA;
        }

        private void calculateGPA(){
            double qualityPoints = getTotalQualityPoints();
            double creditHours = getTotalCreditHours();
            GPA = qualityPoints/creditHours;
        }

        public double getTotalQualityPoints() {
            double qualityPoints = 0;
            for(Term.Course course: courses){
                double points = this.gradePoints.get(course.getGrade());
                double hours = Double.valueOf(course.getCreditHours());
                qualityPoints += points * hours;
            }
            this.totalQualityPoints = qualityPoints;
            return totalQualityPoints;
        }

        public void setTotalQualityPoints(double totalQualityPoints) {
            this.totalQualityPoints = totalQualityPoints;
        }

        public double getTotalCreditHours() {
            double creditHours = 0;
            for(Term.Course course: courses){
                creditHours += Double.valueOf(course.getCreditHours());
            }
            this.totalCreditHours = creditHours;
            return totalCreditHours;
        }

        public void setTotalCreditHours(double totalCreditHours) {
            this.totalCreditHours = totalCreditHours;
        }

        public double getTotalDegreeQualityPoints() {
            double degreeQualityPoints = 0;
            for(Term.Course course: courses){
                if(!course.getCode().startsWith("1")) {
                    double degreePoints = this.gradePoints.get(course.getGrade());
                    double degreeHours = Double.valueOf(course.getCreditHours());
                    degreeQualityPoints += degreePoints * degreeHours;
                }
            }
            this.totalDegreeQualityPoints = degreeQualityPoints;
            return totalDegreeQualityPoints;
        }

        public void setTotalDegreeQualityPoints(double totalDegreeQualityPoints) {
            this.totalDegreeQualityPoints = totalDegreeQualityPoints;
        }

        public double getTotalDegreeCreditHours() {
            double degreeCreditHours = 0;
            for(Term.Course course: courses){
                if(!course.getCode().startsWith("1"))
                    degreeCreditHours += Double.valueOf(course.getCreditHours());
            }
            this.totalDegreeCreditHours = degreeCreditHours;
            return totalDegreeCreditHours;
        }

        public void setTotalDegreeCreditHours(double totalDegreeCreditHours) {
            this.totalDegreeCreditHours = totalDegreeCreditHours;
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
