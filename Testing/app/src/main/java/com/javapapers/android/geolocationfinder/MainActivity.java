/*package com.example.testing;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}*/
package com.javapapers.android.geolocationfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.MarginLayoutParams;

import android.util.Log;

import androidx.annotation.RequiresApi;

public class MainActivity extends Activity {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Location curr_loc;
    TextView txtLat, up1, up2, up3, up4;
    Button button;
    protected String reason;

    protected Context context;
    ImageView icon, map, coordBG,logBG;
    int viewHeight, viewWidth;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = findViewById(R.id.coord);
        up1 = findViewById(R.id.updater1);
        up2 = findViewById(R.id.updater2);
        up3 = findViewById(R.id.updater3);
        up4 = findViewById(R.id.updater4);
        icon = findViewById(R.id.icon);
        logBG = findViewById(R.id.logBG);
        coordBG = findViewById(R.id.coordBG);
        map = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        viewWidth = size.x;
        viewHeight = size.y;

        //map.setImageDrawable(scaleImage(map.getDrawable(), 1));
        //ImageView iv = (ImageView) findViewById(R.id.imageView);
        //Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.york_hill_map);
        //Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 2152, 1668, true);
        //iv.setImageBitmap(bMapScaled);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
// getting GPS status
        gps_enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
// getting network status
        network_enabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        /*if (gps_enabled) {
            txtLat.setText(locationManager.getAllProviders().get(0));
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0,
                    0, this);
        } else if (network_enabled) {
            txtLat.setText("net");
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0,
                    0, this);
        }*/

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (logBG.getVisibility() == View.VISIBLE)
                {
                    logBG.setVisibility(View.INVISIBLE);
                    up1.setVisibility(View.INVISIBLE);
                    up2.setVisibility(View.INVISIBLE);
                    up3.setVisibility(View.INVISIBLE);
                    up4.setVisibility(View.INVISIBLE);
                    coordBG.setVisibility(View.INVISIBLE);
                    txtLat.setVisibility(View.INVISIBLE);
                } else {
                    logBG.setVisibility(View.VISIBLE);
                    up1.setVisibility(View.VISIBLE);
                    up2.setVisibility(View.VISIBLE);
                    up3.setVisibility(View.VISIBLE);
                    up4.setVisibility(View.VISIBLE);
                    coordBG.setVisibility(View.VISIBLE);
                    txtLat.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void makeUseOfNewLocation(Location loc)
    {
        up4.setText(up3.getText());
        up3.setText(up2.getText());
        up2.setText(up1.getText());
        if (isBetterLocation(loc, curr_loc))
        {
            curr_loc = loc;

            txtLat.setText("Longitude:" + loc.getLongitude() + ", Latitude:" + loc.getLatitude());

            double posModifierX = -1.84047826, posModifierY = -1.17059355;

            int y = (int) ((41.41802426 - (loc.getLatitude() + posModifierY)) / 0.000002595);
            y = (viewHeight / 2) - y;

            int x = (int) (-(-72.913862347 - (loc.getLongitude() + posModifierX)) / 0.000002753125);
            x = (viewWidth / 2) - x;
            //up1.setText(String.valueOf(x));

            MarginLayoutParams marginParams = new MarginLayoutParams(map.getLayoutParams());
            marginParams.setMargins(x, y, 0, 0);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginParams);
            map.setLayoutParams(layoutParams);
            //41.41633619 -72.91138041   41.41516245 (3,140,345.7819 pixels from 0) -72.91272546 (4,553,487.9288 pixels from 0)
            //-0.91272546 (57,000.8093 pixels off) 0.41516245 (31,480.1046 pixels)
            // 84 pixels = 0.00134505 and  89 pixels = 0.00117374  10 x pixels = 0.000160125, 10 y pixels = 0.00013188089
            //top of map image is at 41.41802426, left of image is at -72.913862347
            //start at half of screen height and subtract the right amount of pixels

            //my living room is -71.0722472 42.585756
            up1.setText("New loc used because: " + reason);
        }
        else
        {
            up1.setText("New loc denied");
        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            reason = "past was null";
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 120000;
        boolean isSignificantlyOlder = timeDelta < -120000;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            reason = "much newer";
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            reason = "much older";
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            reason = "more accurate";
            return true;
        } else if (isNewer && !isLessAccurate) {
            reason = "newer";
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            reason = "newer";
            return true;
        }
        reason = "no reason";
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public Drawable scaleImage (Drawable image, float scaleFactor) {

        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable)image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        image = new BitmapDrawable(getResources(), bitmapResized);

        return image;
    }
}
