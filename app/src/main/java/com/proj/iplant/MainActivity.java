package com.proj.iplant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.proj.iplant.ble.BlunoLibraryListActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BlunoLibraryListActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment currFrag;

    ImageView imgStatus;

    ScanDevicesFragment scanDevices;
    PhFragment phFragment;
    WebViewFragment webViewFragment;
    Toolbar toolbar;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


/*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/


        onCreateProcess();
        serialBegin(115200);
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if( ContextCompat.checkSelfPermission( this,
                    Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission( this,
                    Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( this,
                        new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1 );
            }
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutParams.height = 200;
        View v = inflator.inflate(R.layout.actionbar, null);
        toolbar.addView(v);
        toolbar.setLayoutParams(layoutParams);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.short_hamburg);
        //toolbar.setElevation(0f);
        toolbar.requestLayout();

        imgStatus = (ImageView) findViewById(R.id.img_status);


        FragmentManager fragmentManager =
                this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState == null) {
            scanDevices = new ScanDevicesFragment();
            currFrag = scanDevices;
            fragmentTransaction.add(R.id.frame_content, currFrag);
            fragmentTransaction.commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createCSVFile();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            if(currFrag.equals(phFragment)){
                goToScan();
            }else if(currFrag.equals(webViewFragment)) {
                goToPhPage();
            }else
            {
                scanDevices.resetPage();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theconnectionStateEnum) {
        switch( theconnectionStateEnum ) {                                            //Four connection state
            case isConnected:
                imgStatus.setImageResource(android.R.drawable.presence_online);
                Log.d("iPlant", "Connected");
                break;
            case isConnecting:
                imgStatus.setImageResource(android.R.drawable.presence_invisible);
                Log.d( "iPlant", "Connecting" );
                break;
            case isToScan:
                imgStatus.setImageResource(android.R.drawable.presence_invisible);
                Log.d( "iPlant", "Scan" );
                break;
            case isScanning:
                imgStatus.setImageResource(android.R.drawable.presence_invisible);
                Log.d( "iPlant", "Scanning" );
                break;
            case isDisconnecting:
                imgStatus.setImageResource(android.R.drawable.presence_invisible);
                Log.d( "iPlant", "isDisconnecting" );
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived( String bleData ) {
        bleData = bleData.trim().toUpperCase();
        Log.e("bluno", "data:"+ bleData);

        if(phFragment != null && !bleData.isEmpty()) {
            phFragment.setpHLevel(bleData);
        }
    }

    public void goToWebView(String url){
        FragmentManager fragmentManager =
                this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currFrag);
        if(webViewFragment != null)
            fragmentTransaction.remove(webViewFragment);

        webViewFragment = new WebViewFragment();
        fragmentTransaction.add(R.id.frame_content, webViewFragment);

        webViewFragment.changeUrl(url);
        currFrag = webViewFragment;
        fragmentTransaction.show(currFrag);
        fragmentTransaction.commit();
        //toolbar.setVisibility(View.VISIBLE);

    }

    public void goToPhPage(){
        FragmentManager fragmentManager =
                this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currFrag);
        if(phFragment == null) {
            phFragment = new PhFragment();
            fragmentTransaction.add(R.id.frame_content, phFragment);
        }
        currFrag = phFragment;
        fragmentTransaction.show(currFrag);
        fragmentTransaction.commit();
        toolbar.setVisibility(View.GONE);

    }
    public void goToScan(){
        FragmentManager fragmentManager =
                this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currFrag);
        scanDevices.refreshPHPage();
        currFrag = scanDevices;
        fragmentTransaction.show(currFrag);
        fragmentTransaction.commit();
        toolbar.setVisibility(View.VISIBLE);


    }





    protected void onResume() {
        super.onResume();
        onResumeProcess();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();                                                        //onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
    }

    @Override
    public void onRequestPermissionsResult( int requestCode,
                                            String permissions[], int[] grantResults ) {
        switch( requestCode ) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if( grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED ) {
                    Log.d( "iPlant", "coarse location permission granted" );
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder( this );
                    builder.setTitle( "Functionality limited" );
                    builder.setMessage( "Since location access has not been granted, this app will not be able to discover beacons when in the background." );
                    builder.setPositiveButton( android.R.string.ok, null );
                    builder.setOnDismissListener( new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss( DialogInterface dialog ) {
                        }

                    } );
                    builder.show();
                }
                return;
            }
        }
    }



    private File mDir;
    private File mFile;
    private String mTimeStamp;

    private static final String DATA_STORAGE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "iplant" + File.separator;

    private StringBuffer stringBufferFile;

    private void createCSVFile() {
        mDir = new File( DATA_STORAGE_PATH );
        if( !mDir.exists() ) {
            mDir.mkdir();
        }

        stringBufferFile = new StringBuffer();
        mFile = new File( mDir, "iplant.csv" );
        try {
            if( mFile.createNewFile() ) {
                Log.e( "Bluno", "success" );
                stringBufferFile.append("Time, pH Level, Location \n");

            }
        } catch( IOException e ) {
            e.printStackTrace();
        }

    }



    private void writeToFile( String data ) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( mFile, true ) );
            outputStreamWriter.append( data );
            outputStreamWriter.close();
            stringBufferFile = new StringBuffer();
        } catch( IOException e ) {
            Log.e( "Exception", "File write failed: " + e.toString() );
        }
    }

    public void addLogToCSV(float phLevel, String location){
        mTimeStamp = new SimpleDateFormat( "yyyy-MM-dd-HH-mm-ss" ).format( new Date() );
        stringBufferFile
                .append(String.valueOf(mTimeStamp)).append(",")
                .append(String.valueOf(phLevel)).append(",")
                .append(String.valueOf(location)).append("\r\n");

        writeToFile(stringBufferFile.toString());

    }


}
