package com.uwimonacs.fstmobile.activities.mapActivity_MVP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.fragments.mapFragment.MapFrag;
import com.uwimonacs.fstmobile.fragments.mapFragment.MapFragMvPView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements MapActivityMvpView, MapFrag.OnCompleteListener,RadioGroup.OnCheckedChangeListener, NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener ,CompoundButton.OnCheckedChangeListener{

    private static final String TAG = "com.android.comp3901";
    private FragmentManager fragmentManager = getFragmentManager();
    private MapFrag mapFrag;
    private MapActivityPresenter presenter;
    public static final int REQUEST_CODE = 0;


    //menu
    Menu menu;
    MenuItem menuItem;

    //place
    private String location;
    private String department;
    private String shortname;
    private String fullname;


    //views
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //inner views
    private SwitchCompat landmark_switch;
    private SwitchCompat satellite_switch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //getting intent data
        final Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            location = extras.getString("location");
            department = extras.getString("department");
            shortname = extras.getString("shortname");
            fullname = extras.getString("fullname");
        }
        presenter = new MapActivityPresenter(this);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MapFrag(), "mapFrag" ).commit();



        //initializing navigation bar and listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_landmark);
        View action_view = MenuItemCompat.getActionView(menuItem);

        landmark_switch = ButterKnife.findById(action_view,R.id.drawer_landmark_switch);
        landmark_switch.setOnCheckedChangeListener(this);

        menuItem = menu.findItem(R.id.nav_sat_view);
        View sat_view = MenuItemCompat.getActionView(menuItem);

        satellite_switch = ButterKnife.findById(sat_view, R.id.drawer_satellite_switch);
        satellite_switch.setOnCheckedChangeListener(this);

   }

    @Override
    public void onBackPressed() {
        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mapFrag.sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN ){
            mapFrag.sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            presenter.bottomSheet();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
//       getMenuInflater().inflate(R.menu.main, menu);
//       getMenuInflater().inflate(R.menu.map_menu, menu);
//       getMenuInflater().inflate(R.menu.theme_pop_menu,menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        MapFrag mapFragFrag = (MapFrag) getFragmentManager().findFragmentByTag("mapFrag");
        //TODO Needs fixing
      if(mapFragFrag != null){
             switch (item.getItemId()) {
               case R.id.mapTypeNormal:
                    mapFragFrag.setStyle("normal");
                    break;
                case R.id.mapTypeSatellite:
                    mapFragFrag.setStyle("satellite");
                    break;
                case R.id.style1:
                    mapFragFrag.setTheme(R.string.style_icyBlue);
                    break;
                case R.id.style2:
                    mapFragFrag.setTheme(R.string.style_cobalt);
                    break;
                case R.id.style3:
                    mapFragFrag.setTheme(R.string.style_chilled);
                    break;
                case R.id.style4:
                    mapFragFrag.setTheme(R.string.style_mapBox);
                    break;
                case R.id.style5:
                    mapFragFrag.setTheme(R.string.style_Rainforest_Fringe);
                    break;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_theme) {
            Log.d("theme", "onNavigationItemSelected: ");
            createThemeSelectionDialog();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_paths) {
            createLevelSelectionDialog();
        } else if (id == R.id.nav_landmark) {
            landmark_switch.setChecked(!(landmark_switch.isChecked()));
        } else if (id == R.id.nav_sat_view){
            satellite_switch.setChecked(!(satellite_switch.isChecked()));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView temp = (TextView) view;

        Toast.makeText(this, temp.getText(), Toast.LENGTH_SHORT);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resumed");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.drawer_landmark_switch:
                //TODO switch static object to public method
                mapFrag.showLandmarks(landmark_switch.isChecked());
                break;
            case R.id.drawer_satellite_switch:
                if(isChecked){
                    mapFrag.setStyle("satellite");
                }else{
                    mapFrag.setStyle("normal");
                }
                break;
            case R.id.ground_switch:
                presenter.groundFloorState(buttonView.isChecked());
                break;
            case R.id.first_floor_switch:
                presenter.firstFloorState(buttonView.isChecked());
                break;
            case R.id.second_floor_switch:
                presenter.secondFloorState(buttonView.isChecked());
                break;
            case R.id.third_floor_switch:
                presenter.thirdFloorState(buttonView.isChecked());
                break;
        }

    }


    public void createLevelSelectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_map_floor_layers,null);

        builder.setView(view);

        final Dialog floor_dialog = builder.create();
        if(!this.isFinishing()){//show dialog
            floor_dialog.show();
        }

        Switch GroundFloor = ButterKnife.findById(view, R.id.ground_switch);
        Switch FirstFloor = ButterKnife.findById(view, R.id.first_floor_switch);
        Switch SecondFloor = ButterKnife.findById(view, R.id.second_floor_switch);
        Switch ThirdFloor = ButterKnife.findById(view, R.id.third_floor_switch);

        //Initialising Check Widgets
        GroundFloor.setChecked(presenter.groundFloorState());
        GroundFloor.setOnCheckedChangeListener(this);

        FirstFloor.setChecked(presenter.firstFloorState());
        FirstFloor.setOnCheckedChangeListener(this);

        SecondFloor.setChecked(presenter.secondFloorState());
        SecondFloor.setOnCheckedChangeListener(this);

        ThirdFloor.setChecked(presenter.thirdFloorState());
        SecondFloor.setOnCheckedChangeListener(this);
    }


    public void createThemeSelectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_theme_selection,null);

        builder.setView(view);

        final Dialog theme_selection_dialog = builder.create();
        if(!this.isFinishing()){//show dialog
            theme_selection_dialog.show();
        }

        RadioGroup themeRadioGroup = ButterKnife.findById(view, R.id.themeRadioGroup);
        RadioButton mapBoxTheme = ButterKnife.findById(view, R.id.MapBox);
        RadioButton icyBlue = ButterKnife.findById(view, R.id.icyBlue);
        RadioButton cobalt = ButterKnife.findById(view, R.id.colbalt);
        RadioButton rainForest = ButterKnife.findById(view, R.id.rainForest);

        themeRadioGroup.setOnCheckedChangeListener(this);
        switch (presenter.getTheme())
        {
            case R.string.style_mapBox:
                themeRadioGroup.check(R.id.MapBox);
                break;
            case R.string.style_icyBlue:
                themeRadioGroup.check(R.id.icyBlue);
                break;
            case R.string.style_cobalt:
                themeRadioGroup.check(R.id.colbalt);
                break;
            case R.string.style_Rainforest_Fringe:
                themeRadioGroup.check(R.id.rainForest);
                break;
            default:
                break;
        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId)
        {
            case R.id.MapBox:
                mapFrag.setTheme(R.string.style_mapBox);
                break;
            case R.id.icyBlue:
                mapFrag.setTheme(R.string.style_icyBlue);
                break;
            case R.id.colbalt:
                mapFrag.setTheme(R.string.style_cobalt);
                break;
            case R.id.rainForest:
                mapFrag.setTheme(R.string.style_Rainforest_Fringe);
                break;
            default:
                break;
        }
  }


    /**
     * fragment call back interface.
     * this method gets called when the fragment has been
     * attached. Used to initial the mapFrag a class a the right
     * moment.
     */
    @Override
    public void onComplete() {
        //getting reference to the fragment added
        mapFrag = (MapFrag) getFragmentManager().findFragmentByTag("mapFrag");
        presenter.setMapFragView((MapFragMvPView) mapFrag);

        if(location!=null) {
            //getting location data
            final String[] locals = location.split(",");
            final String snip = department + " - " + shortname;

            double lat = Double.parseDouble(locals[0]);
            double lon = Double.parseDouble(locals[1]);


            mapFrag.goToLocation(new LatLng(lat, lon));


            //creating marker for the location
            mapFrag.setDestinationText(fullname);


            boolean isFound = mapFrag.searchRoom();
            if (!isFound) {
                //Create a dynamic vertex to represent the location
                mapFrag.setMarker(shortname, fullname, new LatLng(lat, lon));
            }
        }

    }


}

