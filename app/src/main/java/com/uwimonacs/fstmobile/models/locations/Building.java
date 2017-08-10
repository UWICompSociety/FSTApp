package com.uwimonacs.fstmobile.models.locations;

import java.util.HashMap;

/**
 * Created by Kyzer on 5/6/2017.
 */




public class Building extends Place {

    private String rooms;
    private final int floors;





    public Building(String id, String name, double lat, double lng, String rooms, int floors, int known, double fam, int landmark, String cat) {
        super(id, name, lat, lng, "Building",known,fam, landmark,0, cat);
        this.rooms=rooms;
        this.floors = floors;
    }

    public boolean isKnown() {
        return known>0;
    }

    public String getRooms() {
        return rooms;
    }

    public int getFloors() {
       return this.floors;
    }

    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = super.getInfo();
                info.put("Floors", String.valueOf(getFloors()));
                info.put("Rooms", getRooms());
        return info;
    }

}
