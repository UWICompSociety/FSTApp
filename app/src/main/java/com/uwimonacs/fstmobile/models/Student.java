package com.uwimonacs.fstmobile.models;

/**
 * @author sultanofcardio
 * Models a UWI student accessing the Student Administrative
 * Services website
 */

import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Student")
public class Student extends Model {

    @Column(name = "id_number", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String idNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "login_page")
    private String loginPage;

    @Column(name = "terms_page")
    private String termsPage;

    @Column(name = "transcript_page")
    private String transcriptPage;

    @Column(name = "serialized_timetables")
    private String serializedTimetables;

    @Column(name = "serialized_timetable_pages")
    private String serializedTimetablePages;

    @Column(name = "serialized_transcript")
    private String serializedTranscript;

    private List<Pair<TimeTable, String>> timetables;
    private List<Pair<String, String>> timetablePages;

    private TimeTable timeTable;
    private Transcript transcript;

    private String password;

    public Student(){
        super();
    }

    public Student(String id){
        super();

        this.idNumber = id;
        this.password = "";
        this.timeTable = new TimeTable();
        this.transcript = new Transcript();
        timetables = new ArrayList<>();
        timetablePages = new ArrayList<>();

        termsPage = "";
        transcriptPage = "";
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

    public void addTimeTable(TimeTable timeTable, String term){
        boolean exists = false;
        int index = 0;
        Pair<TimeTable, String> newTimetable = new Pair<>(timeTable, term);
        for(Pair<TimeTable, String> timetable: timetables) {
            if (timetable.second.equals(newTimetable.second)) {
                exists = true;
                break;
            }
            index += 1;
        }

        if(exists){
            timetables.remove(index);
            timetables.add(index, newTimetable);
        } else {
            timetables.add(newTimetable);
        }
    }

    public void addTimeTablePage(String page, String term){
        timetablePages.add(new Pair<String, String>(page, term));
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    @Nullable
    public TimeTable getTimeTable(String term){
        TimeTable timeTable = null;
        for(Pair<TimeTable, String> pair: timetables){
            if(term.equals(pair.second)) {
                timeTable = pair.first;
                break;
            }
        }
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

    public String getTranscriptPage() {
        return transcriptPage;
    }

    public void setTranscriptPage(String transcriptPage) {
        this.transcriptPage = transcriptPage;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getTermsPage() {
        return termsPage;
    }

    public void setTermsPage(String termsPage) {
        this.termsPage = termsPage;
    }

    public List<Pair<TimeTable, String>> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<Pair<TimeTable, String>> timetables) {
        this.timetables = timetables;
    }

    public List<Pair<String, String>> getTimetablePages() {
        return timetablePages;
    }

    public void setTimetablePages(List<Pair<String, String>> timetablePages) {
        this.timetablePages = timetablePages;
    }

    public void serialize(){
        Gson gson = new Gson();
        this.serializedTimetablePages = gson.toJson(timetablePages);
        this.serializedTimetables = gson.toJson(timetables);
        this.serializedTranscript = gson.toJson(transcript);

    }

    public void unSerialize(){
        Gson gson = new Gson();
        timetablePages = gson.fromJson(this.serializedTimetablePages, new TypeToken<List<Pair<String, String>>>(){}.getType());
        timetables = gson.fromJson(this.serializedTimetables, new TypeToken<List<Pair<TimeTable, String>>>(){}.getType());
        transcript = gson.fromJson(this.serializedTranscript, Transcript.class);

        if(timetables == null){
            System.out.println("Timetables is null");
            timetables = new ArrayList<>();
        }
        if(timetablePages == null){
            System.out.println("Timetable pages is null");
            timetablePages = new ArrayList<>();
        }
        if(transcript == null){
            System.out.println("Transcript is null");
            transcript = new Transcript();
        }
    }
}
