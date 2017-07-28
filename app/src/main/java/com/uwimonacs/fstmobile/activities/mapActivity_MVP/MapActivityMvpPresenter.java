package com.uwimonacs.fstmobile.activities.mapActivity_MVP;


import com.uwimonacs.fstmobile.fragments.mapFragment.MapFragMvPView;

/**
 * Created by Kyzer on 5/23/2017.
 */

public interface MapActivityMvpPresenter {
    void createLevelSelectionDialog();

    boolean groundFloorState();

    boolean thirdFloorState();

    boolean secondFloorState();

    boolean firstFloorState();

    void groundFloorState(boolean checked);

    void firstFloorState(boolean checked);

    void secondFloorState(boolean checked);

    void thirdFloorState(boolean checked);

    void setMapFragView(MapFragMvPView mapFrag);

    int getTheme();
}
