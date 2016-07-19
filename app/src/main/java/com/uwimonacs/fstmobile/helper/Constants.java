package com.uwimonacs.fstmobile.helper;

import com.uwimonacs.fstmobile.models.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/8/2016.
 */
public  class Constants {

    public static String CONTACTS_URL = "https://infinite-citadel-20637.herokuapp.com/contacts/";
    public static String NEWS_URL  = "https://infinite-citadel-20637.herokuapp.com/news/";
    public static String FAQS_URL = "https://infinite-citadel-20637.herokuapp.com/faqs/";
    public static String SCHOL_URL = "https://infinite-citadel-20637.herokuapp.com/scholarship/";
    public static String PLACE_URL = "https://infinite-citadel-20637.herokuapp.com/places/";


    public static final String[] PLACE_CATEGORIES = {"Chemistry", "Life Sciences", "Physics", "Mathematics",
            "Computing", "Engineering"};
    public static final String[] SUB_CATEGORIES = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    public static final String[] chemistry = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    public static final String[] lifeSciences = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    public static final String[] physics = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    public static final String[] mathematics = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    public static final String[] computing = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    public static final String[] engineering = {"Room 1", "Room 2", "Room 3", "Room 4",
            "Room 5", "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"};
    private static String[][] categories = {chemistry, lifeSciences, physics, mathematics, computing, engineering};
    public static final List<Room> ROOMS = new ArrayList<>();

    public static void setUpRooms(){
        for(int i=0; i<PLACE_CATEGORIES.length; i++){
            for(int j=0; j<categories.length; j++){
                Room room = new Room();
                room.setCategory(PLACE_CATEGORIES[i]);
                room.setName(categories[i][j]);
                ROOMS.add(room);
            }
        }
    }
}
