package com.example.piyush.parse;

import android.app.Activity;
import android.location.Location;


import android.os.Bundle;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener
{

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    ImageView myImage;
    Animation myRotation;
    TextView t1,t2;
    Button b1;
    LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    LocationListener l1;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    ParseUser currentUser;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Toast.makeText(this,"On Create", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        //b1=  (Button) findViewById(R.id.b2);

        myImage = (ImageView)findViewById(R.id.img);

        buildGoogleApiClient();

        currentUser = ParseUser.getCurrentUser();


    }


    public void update_query()
    {
        Toast.makeText(this,"update query", Toast.LENGTH_SHORT).show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LocationObject");
        query.whereEqualTo("user", currentUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e)
            {
                if(e==null)
                {
                    for (ParseObject con:list)
                    {
                        con.put("latitude",mLastLocation.getLatitude());
                        con.put("longitude",mLastLocation.getLongitude());
                        con.saveInBackground();
                    }


                }
            }
        });
    }



    protected synchronized void buildGoogleApiClient()
    {
        Toast.makeText(this,"build Google Api Client", Toast.LENGTH_SHORT).show();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    protected void createLocationRequest()
    {
        Toast.makeText(this,"createlocation request",Toast.LENGTH_SHORT).show();
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onStart()
    {
        Toast.makeText(this,"On Start", Toast.LENGTH_LONG).show();
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume()
    {
        Toast.makeText(this,"On resume", Toast.LENGTH_LONG).show();
        super.onResume();

        if (mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause()
    {
        Toast.makeText(this,"On Pause", Toast.LENGTH_LONG).show();
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected())
        {
            stopLocationUpdates();
        }
    }





    @Override
    public void onConnected(Bundle bundle)
    {
        Toast.makeText(this,"On Connected", Toast.LENGTH_LONG).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null)
        {
            updateui();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Toast.makeText(this,"updating", Toast.LENGTH_LONG).show();
            update_query();

        }

        else
        {
            Toast.makeText(this,"sorry", Toast.LENGTH_LONG).show();
        }
    }

    private void updateui()
    {
        Toast.makeText(this,"update the ui", Toast.LENGTH_LONG).show();

        if(mLastLocation!= null)
        {
            t1.setText(String.valueOf(mLastLocation.getLatitude()));
            t2.setText(String.valueOf(mLastLocation.getLongitude()));

        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Toast.makeText(this,"On Connection Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Toast.makeText(this,"On Connection Failed", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onLocationChanged(Location location)
    {

        Toast.makeText(this,"On Location Changed", Toast.LENGTH_LONG).show();
        rotateimg();
        mLastLocation = location;
        updateui();;
        update_query();
    }

    public void rotateimg()
    {
        myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotator);
        myImage.startAnimation(myRotation);
    }


    protected void startLocationUpdates()
    {
        Toast.makeText(this,"start Location updates", Toast.LENGTH_LONG).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates()
    {
        Toast.makeText(this,"stop Location updates", Toast.LENGTH_LONG).show();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


}
