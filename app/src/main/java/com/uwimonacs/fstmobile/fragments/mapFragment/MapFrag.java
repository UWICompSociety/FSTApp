package com.uwimonacs.fstmobile.fragments.mapFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ImageShackAlbumAdapter;
import com.uwimonacs.fstmobile.models.ImageShackAlbum;
import com.uwimonacs.fstmobile.models.locations.Place;
import com.uwimonacs.fstmobile.models.locations.Vertex;
import com.uwimonacs.fstmobile.services.GoogleDriveAPI;
import com.uwimonacs.fstmobile.services.ImageShackAPIInterface;
import com.uwimonacs.fstmobile.services.ImageShackApiClient;
import com.uwimonacs.fstmobile.services.MapMarker;
import com.uwimonacs.fstmobile.services.MapPolylines;
import com.uwimonacs.fstmobile.util.MySettings;
import com.uwimonacs.fstmobile.util.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFrag extends Fragment implements MapFragMvPView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnCameraMoveListener, GoogleMap.OnMapClickListener{
    private static final String TAG = "com.android.comp3901";
    final LatLng sci_tech = new LatLng(18.005072, -76.749544);



    private MapPresenter presenter;
    private Animation fab_close,fab_open;



    //Map recyclerview variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageShackAlbumAdapter albumAdapter;
    private ImageShackAlbum imageShackAlbum;
    private ImageShackAPIInterface imageShackAPIInterface;


    //Map Clients and variables
    private UiSettings mUiSettings;
    public static MapMarker mapMarkers;
    private static MapPolylines mapPolylines;
    public static BottomSheetBehavior sheetBehavior;
    private int mapTheme;
    private GoogleDriveAPI driveServices;


    //Map Objects
    public static GoogleMap mGoogleMap;
    public static Path path;


    private static Activity instance;
    /**
     * Method used to get the application context
     * @return
     */
    public static Activity get() {
        return instance;
    }


    public MapFrag() {}

    /************************************************************
     *                            VIEWS
     ************************************************************/

    //Views
    @BindView(R.id.getSource)AutoCompleteTextView sourceEditTextView;
    @BindView(R.id.classSearch) AutoCompleteTextView destinationEditTextView;
    @BindView(R.id.bottom_sheet_layout)          NestedScrollView nestedView;
    @BindView(R.id.search_view_layout) View searchView;

    @BindView(R.id.fbPath)FloatingActionButton findPathBtn;
    @BindView(R.id.arrival_check_fab)FloatingActionButton arrivalFab;
    @BindView(R.id.arrival_cancel_fab)FloatingActionButton cancelArrivalFab;
    @BindView(R.id.path_buttons_layout)LinearLayout pathBtnLayout;


    private Unbinder unbinder = Unbinder.EMPTY;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_map_frag, container, false);
        ButterKnife.bind(MapFrag.this,view);
        instance = this.getActivity();



        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MySettings.googleServicesCheck(this.getActivity())) {
//            Toast.makeText(this.getActivity(), "Perfect!!", Toast.LENGTH_LONG).show();
            initMap();
        }

        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);

        //intialising recycler view
        recyclerView = (RecyclerView) getView().findViewById(R.id.bottomsheet_recyclerview);
        layoutManager = new LinearLayoutManager(instance,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        //callback to activity
    }

    /**
     * Initializes Map Fragment;
     */
    private void initMap() {
        MapFragment mapFragment = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        }
        mapFragment.getMapAsync(this);
    }



    /**
     *
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady:");
        presenter = new MapPresenter(this,getActivity(),googleMap);

        setTextViews();
        mGoogleMap = googleMap;

        //Map Objects Initialisations
//        path = Path.getInstance(dbHelper); //Initialises path object which creates graph
        mGoogleMap = googleMap;
        driveServices = new GoogleDriveAPI(instance);
        mapPolylines = MapPolylines.getInstance(mGoogleMap);
        mapMarkers = MapMarker.getInstance(mGoogleMap);
        mUiSettings = mGoogleMap.getUiSettings();


        // Map View Settings
        mGoogleMap.setInfoWindowAdapter(this);
        mUiSettings.setMapToolbarEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false);

        //Map Listeners
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // TODO hide all views but the map view
                Log.d(TAG, "onCameraIdle:");
            }
        });

        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnCameraMoveListener(this);

       if(mapPolylines != null){
            Log.d(TAG, "onMapReady: display graph");
            mapPolylines.createGraph();
            displayIcons(); // Display the node icons
        }
        goToLocation(sci_tech);
        setTheme(R.string.style_mapBox);


        mListener.onComplete();//Activity call back listener
    }

    /**
     *  This methods sets the text views...
     **/
    @NonNull
    private void setTextViews() {


        /**
         * Autocomplete Text Views
         */

        ArrayList<String> rooms =  presenter.getRoomsFromDb();

        rooms.addAll(presenter.getBuildingsFromDb());
        String[] locationSugg = rooms.toArray(new String[rooms.size()]);
        ArrayAdapter<String> roomsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, locationSugg);

        sourceEditTextView.setThreshold(1);
        sourceEditTextView.setAdapter(roomsArrayAdapter);
        sourceEditTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hideKeyboard();
                String selected = (String) parent.getItemAtPosition(position);
                presenter.setSource();
                Toast.makeText(getActivity(), "Selected :" + selected, Toast.LENGTH_SHORT);
            }
        });

        destinationEditTextView.setThreshold(1);
        destinationEditTextView.setAdapter(roomsArrayAdapter);
        destinationEditTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                String selected = (String) parent.getItemAtPosition(position);
                try {
                    presenter.geoLocate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "Selected :" + selected, Toast.LENGTH_SHORT);
            }
        });


        /*
         * Bottom Sheet View
         */
        sheetBehavior = BottomSheetBehavior.from(nestedView);
        sheetBehavior.setHideable(true);
        sheetBehavior.setPeekHeight(200);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    /***
     * Handles  the maps type switching
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleSearchView(){

        if(searchView.getVisibility() == View.VISIBLE){
            searchView.animate().translationY(10);
            searchView.setVisibility(View.INVISIBLE);
        }else{
            searchView.animate().translationY(0);
            searchView.setVisibility(View.VISIBLE);
        }

    }


    @OnClick(R.id.locationToggle)
    public void toggleClick(View v){

        boolean curState= ((ToggleButton)v).isChecked();
        boolean  newState= presenter.toggleLocation(curState);
        ((ToggleButton)v).setChecked(newState);
        //toggleLocations(v);

        //Toggle Source Edit Text View
        if(newState){//GPS Enabled
            sourceEditTextView.setHint("Location Enabled");
            sourceEditTextView.setFocusable(false);
        }else{
            sourceEditTextView.setHint(R.string.starting_point_editText_hint);
            sourceEditTextView.setFocusable(true);
        }


    }

    @OnClick(R.id.findBtn)
    public void findRoom(){
        searchRoom();
    }


    /**
     *
     * @return true if the a location was found withing the database, false if
     * none or more than one possible locations was found
     */
    public boolean searchRoom()  {
        try {
            return presenter.geoLocate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @OnClick(R.id.arrival_cancel_fab)
    public void  cancelPathFinding(){
        presenter.cancelPathFinding();
        togglePathBtn();
    }

    @OnClick(R.id.arrival_check_fab)
    public void arrival(){
        presenter.destinationArrived();
        togglePathBtn();
    }


    @OnClick(R.id.fbPath)
    public void fabOnClick(){
        boolean path = presenter.getPath();
        if(path){
            togglePathBtn();
        }
    }

    /**
     * method used to hide the find path button and show the
     * the arrival query buttons
     */
    private void togglePathBtn(){

        //check to see if the getpath button is available
        if(findPathBtn.isClickable()){
            //hide find path fab button
            findPathBtn.startAnimation(fab_close);
            findPathBtn.setVisibility(View.GONE);
            findPathBtn.setClickable(false);
            Log.d(TAG, "togglePathBtn: Hide find path fab");
            Log.d(TAG, "togglePathBtn visibility: "+findPathBtn.getVisibility()+ ":"+ View.GONE);

            //enable path button layout
            pathBtnLayout.setVisibility(View.VISIBLE);
            pathBtnLayout.startAnimation(fab_open);
            arrivalFab.setClickable(true);
            cancelArrivalFab.setClickable(true);
            }else{
            //hide path button layout
            pathBtnLayout.setVisibility(View.GONE);
            pathBtnLayout.startAnimation(fab_close);
            arrivalFab.setClickable(false);
            cancelArrivalFab.setClickable(false);

            //show fab button
            findPathBtn.startAnimation(fab_open);
            findPathBtn.setVisibility(View.VISIBLE);
            findPathBtn.setClickable(true);
            Log.d(TAG, "togglePathBtn: reveal find path fab");
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.d("Marker Snippet", marker.getSnippet() );

        switch (marker.getSnippet()){
            case "stairs":
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case "junction":
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            default:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                loadBottomSheet(marker);
                break;
        }
        return false;
    }

    //TODO Polish this method
    private void create_info_dialog(Vertex v) {
        AlertDialog.Builder info_dialog = new AlertDialog.Builder(getActivity(), AlertDialog.BUTTON_POSITIVE);
        View room_info_view = getActivity().getLayoutInflater().inflate(R.layout.dialog_place_info, null);
        TextView info_text = ButterKnife.findById(room_info_view,R.id.place_dialog_info);
        Switch known_switch =  ButterKnife.findById(room_info_view,R.id.place_info_known_swittch);
        Button dissmiss =  ButterKnife.findById(room_info_view,R.id.plac_info_dismiss);

        final Place place = ((Place) v );


        info_text.setText(presenter.getPlaceInfo(v.getId()));
        known_switch.setChecked(place.isKnown());
        known_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    place.setKnown(1);
                }else{
                    place.setKnown(0);
                }
                place.updateDB();
            }
        });
        info_dialog.setView(room_info_view);
        final AlertDialog dialog = info_dialog.create();
        dialog.show();
        dissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Method used to jump to location.
     * @param ll
     */
    @Override
    public void goToLocation(LatLng ll) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 19);
        mGoogleMap.moveCamera(update);
    }


    @Override
    public String getStartText() {
        return  sourceEditTextView.getText().toString();
    }

    @Override
    public String getDestText(){
        return destinationEditTextView.getText().toString();
    }

    @Override
    public void disableSourceText(boolean b) {
        if(b){
            EditText getSource = (EditText) getView().findViewById(R.id.getSource);
            getSource.setHint("YOUR LOCATION");
            getSource.getText().clear();
            getSource.setFocusable(false);
        }else{
            EditText getSource = (EditText) getView().findViewById(R.id.getSource);
            getSource.setHint("Choose a Starting point");
            getSource.setFocusableInTouchMode(true);
        }
    }

    @Override
    public void locationEnabled(boolean b) {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(b); //Enables google tracking.
    }

    @Override
    public void showLandmarks(boolean checked) {
        mapMarkers.showLandmarks(checked);
    }

    @Override
    public void removePath() {
        mapPolylines.deletePath();
        mapMarkers.removePOI();
        //TODO delete starter and destination marker and make is sourceset = flase
        mapMarkers.removeStart();
    }

    @Override
    public void setStartText(String startText) { sourceEditTextView.setText(startText); }

    @Override
    public void clearStartText() {
        sourceEditTextView.setText("");
    }

    @Override
    public void setDestinationText(String destination){ destinationEditTextView.setText(destination); }

    @Override
    public void clearDestinationText() {
        destinationEditTextView.setText("");
    }

    @Override
    public void setGroundFloor(boolean checked) {
        mapPolylines.setGroundFloor(checked);
    }

    @Override
    public void setFirstFloor(boolean checked) {
        mapPolylines.setFirstFloor(checked);
    }

    @Override
    public void setSecondFloor(boolean checked) {
        mapPolylines.setSecondFloor(checked);
    }

    @Override
    public void setThirdFloor(boolean checked) {

    }

    @Override
    public boolean groundFloorIsVisible() {
       return mapPolylines.groundFloorIsVisilble();
    }

    @Override
    public boolean secondFloorIsVisible() {
        return mapPolylines.secondFloorIsVisible();
    }

    @Override
    public boolean firstFloorIsVisible() {
        return mapPolylines.firstFloorIsVisible();
    }

    @Override
    public int getTheme() {
     return mapTheme;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick: ");
        toggleSearchView();
    }
    /*************************************************************
     *                              LOGIC
     *************************************************************/



    public void hideKeyboard(){
        /* hide keyboard */
        ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void displayIcons(){
        HashMap<String, Vertex> vertexHashMap = presenter.getNodes();
        for( Vertex node: vertexHashMap.values()){
            mapMarkers.addIcon(node);
         }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mapPolylines.destroy();
        mapMarkers.destroy();
    }

    /**
     * Loads the information for the current marker(room/building) selected;
     *
     * @param marker
     */
    private void loadBottomSheet(Marker marker) {
        final Marker myMarker = marker;
        Button image_button = ButterKnife.findById(getActivity(),R.id.bottom_sheet_find_route);
        TextView place_title = ButterKnife.findById(getActivity(),R.id.bottom_sheet_title);
        TextView place_info1 = ButterKnife.findById(getActivity(),R.id.place_info1);
        View moreInfo = ButterKnife.findById(getActivity(),R.id.more_info_layout);
//        driveServices.getFileIDs();

        ImageShackAPIInterface imageShackAPIInterface = ImageShackApiClient.getAPIClient().create(ImageShackAPIInterface.class);
        Call<ImageShackAlbum> apiCall = imageShackAPIInterface.getAlbum("J1Zl");
        apiCall.enqueue(new Callback<ImageShackAlbum>() {
            @Override
            public void onResponse(Call<ImageShackAlbum> call, Response<ImageShackAlbum> response) {
                imageShackAlbum = response.body();
                albumAdapter = new ImageShackAlbumAdapter(instance,imageShackAlbum);
                recyclerView.setAdapter(albumAdapter);

            }

            @Override
            public void onFailure(Call<ImageShackAlbum> call, Throwable t) {
                Log.d(TAG, "onFailure: API request failed");
                Log.d(TAG, "onFailure: "+ t.getMessage());


            }
        });


        final Place place = (Place) marker.getTag();

        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onInfoWindowLongClick: PRESSED");
                LatLng ll = myMarker.getPosition();
//                start = Distance.find_closest_marker(ll, path.getCNodes());
//                isSourceSet = true;
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                presenter.setSource(ll, place.getLevel() );

                if(presenter.getPath()){

                    togglePathBtn();
                };


            }
        });



//        Log.d("Marker Object", " Room Instance: ");
        //TODO ADD BUILDING TO SECOND INFO VIEW
        place_title.setText(place.getName());
//        place_info1.setText(place.getInfo().toString());
        String info = presenter.getPlaceInfo(place.getId());
        place_info1.setText(info);
        //TODO Start activity that shows the room info;
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("more info", "onClick: ");
                create_info_dialog(place);
            }


        });

    }

    public void setTheme(int style){
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapTheme = style;
        boolean success = mGoogleMap.setMapStyle(new MapStyleOptions(getResources().getString(style)) );
        if (!success) {
            Toast.makeText(this.getActivity(), "Style parsing failed.", Toast.LENGTH_LONG).show();
        }
    }

    public void setStyle(String style){
         switch (style){
            case "normal":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "satellite":
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Map Frag Resumed" );

//        mapPolylines.createGraph();
//        displayIcons(); // Display the node icons

        //Todo reload graph lines and icons!!
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * Customized info window
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        if(marker.getSnippet().equals("stairs")  ){
            return null;
        }
        Place location = (Place) marker.getTag();
        View v = getActivity().getLayoutInflater().inflate(R.layout.layout_info_window,null);
        TextView infoTitle = (TextView) v.findViewById(R.id.info_window_title);
        infoTitle.setText(((Place)marker.getTag()).getName());
        ImageView infoImage = (ImageView) v.findViewById(R.id.info_window_image_view);

        String filepath = Environment.getExternalStorageDirectory()+ File.separator+ location.getId();
        File f = new File(getActivity().getApplicationContext().getFilesDir(), location.getId());
        int img = getActivity().getResources().getIdentifier(location.getId().toLowerCase(),"mipmap",getActivity().getPackageName() );
        if(f.exists()){
            presenter.setPic(infoImage,filepath);
        }else if(img >0){
            infoImage.setImageResource(img);
        }
        else{
            infoImage.setImageResource(R.mipmap.photos_coming_soon);
        }
        return v;
    }

    @Override
    public void makeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMove() {
        Log.d(TAG, ""+ mGoogleMap.getCameraPosition().zoom);

        double level = mGoogleMap.getCameraPosition().zoom;
        if (level < 20.0) {
            mapMarkers.showStairs(false);
        } else {
            mapMarkers.showStairs(true);
        }

        if (level < 18.58317) {
            mapMarkers.showBuildings(false);
        } else {
            mapMarkers.showBuildings(true);
        }

        if (level < 19.0) {
            mapMarkers.showJunctions(false);
        } else {
            mapMarkers.showJunctions(true);
        }
    }

    public void setMarker(String shortname, String fullname, LatLng latLng) {
        Place dynamicLocation = new Place(shortname,fullname,latLng.latitude,latLng.longitude, Vertex.PLACE, Vertex.UNKNOWN, 0.0, 0, 0);
        presenter.setVertex(dynamicLocation, 3);
    }


    /**
     * Fragment call back interface that is call when it the fragment is ready.
     */
    public interface OnCompleteListener{
        void onComplete();
    }


    private  OnCompleteListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnCompleteListener)activity;
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mListener = (OnCompleteListener)context;
            this.mListener = (OnCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }



}//End of FINDFME





