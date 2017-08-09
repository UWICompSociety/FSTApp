package com.uwimonacs.fstmobile.activities.mapActivity_MVP;


import com.uwimonacs.fstmobile.fragments.mapFragment.MapFragMvPView;

/**
 * Created by Kyzer on 5/23/2017.
 */

public class MapActivityPresenter implements MapActivityMvpPresenter {

    MapActivityMvpView view;
    MapFragMvPView mapView;

    MapActivityPresenter(MapActivityMvpView view){
        this.view = view;
    }


    public void bottomSheet() {
    }

    public int getBottomSheetState() {
        return 1; //bottomSheetState;
    }


    @Override
    public void createLevelSelectionDialog() {
        
    }

    @Override
    public boolean groundFloorState() {
        return mapView.groundFloorIsVisible();
    }

    @Override
    public boolean thirdFloorState() {
        return false;
    }

    @Override
    public boolean secondFloorState() {
        return mapView.secondFloorIsVisible();
    }

    @Override
    public boolean firstFloorState() {
        return mapView.firstFloorIsVisible();
    }

    @Override
    public void groundFloorState(boolean checked) {
        mapView.setGroundFloor(checked);
    }

    @Override
    public void firstFloorState(boolean checked) {
        mapView.setFirstFloor(checked);
    }

    @Override
    public void secondFloorState(boolean checked) {
        mapView.setSecondFloor(checked);
    }

    @Override
    public void thirdFloorState(boolean checked) {
        mapView.setThirdFloor(checked);
    }

    @Override
    public void setMapFragView(MapFragMvPView mapFrag) {
        this.mapView = mapFrag;
    }

    @Override
    public int getTheme() {
        return this.mapView.getTheme();
    }


}
