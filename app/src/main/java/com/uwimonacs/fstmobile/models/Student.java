package com.uwimonacs.fstmobile.models;

/**
 * @author sultanofcardio
 * Models a UWI student accessing the Student Administrative
 * Services website
 */
public class Student {
    private String idNumber, password, name;
    private TimeTable timeTable;
    private Transcript transcript;

    public Student(String id){
        this.idNumber = id;
        this.password = "";
        this.timeTable = new TimeTable();
        this.transcript = new Transcript();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }
}
