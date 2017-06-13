package com.uwimonacs.fstmobile.fragments.mapFragment;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kyzer on 5/24/2017.
 */

public interface MapFragMvPView {

    void makeToast(String msg);
    void goToLocation(LatLng ll);

    String getStartText();
    String getDestText();

    void disableSourceText(boolean b);

    void locationEnabled(boolean b);

    void showLandmarks(boolean checked);

    void removePath();

    void clearStartText();

    void clearDestinationText();

    void setGroundFloor(boolean checked);

    void setFirstFloor(boolean checked);

    void setSecondFloor(boolean checked);

    void setThirdFloor(boolean checked);

    boolean groundFloorIsVisible();

    boolean secondFloorIsVisible();

    boolean firstFloorIsVisible();

    int getTheme();
}
