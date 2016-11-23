package comgoldbin.vk.geonotes;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.location.LocationManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Marker marker;
    GoogleMap mMap;
    LocationManager locationManager;
    GoogleApiClient client;
    LatLng locationgeo;
    double latitude;
    double longitude;
    public long myidbd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        checkPermissions();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpscheck();

//Обьявили баттоны
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnList = (Button) findViewById(R.id.btnList);
//Переход на Список
        btnList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
//Переход на добавление
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this, AddActivity.class);
                intent.putExtra("geoMaps", locationgeo.toString());
                startActivity(intent);
            }
        });


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


//Определяем местоположение(для начала проверяем включено ли местоположение/если нет просим включить)

    }


    //Прорисовка карты
    //Будем брать все координаты хранящиеся в БД и прорисовывать их в на карте
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationgeo = new LatLng(latitude, longitude);

        RepoBDHelper db = new RepoBDHelper(this);
        ArrayList<DBData> datenotes = db.selectAll();

        for (int i = 0; i < datenotes.size(); i++) {
            final DBData dbd = datenotes.get(i);
            LatLng ll = new LatLng(dbd.getLat(), dbd.getLon());

            marker = mMap.addMarker(new MarkerOptions()
                    .position(ll)
                    .title(String.valueOf(dbd.getID()))
                    .snippet(dbd.getDate())
            );

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                public View popup;

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    RepoBDHelper dbb = new RepoBDHelper(MapsActivity.this);
                    DBData dBData =  dbb.select(Long.parseLong(marker.getTitle()));

                    popup = getLayoutInflater().inflate(R.layout.markerbro, null);
                    TextView tv = (TextView) popup.findViewById(R.id.title);
                    TextView tc = (TextView) popup.findViewById(R.id.snippet);
                    ImageView iv = (ImageView) popup.findViewById(R.id.img);

                    tv.setText(dBData.getText());
                    tc.setText(marker.getSnippet());
                    iv.setImageURI(Uri.parse(dBData.getPhoto().toString()));
                    myidbd = dBData.getID();
                    return popup;
                }
            }
            );


        }
            if (ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            //Слушатель для перехода на просмотр\редактирование.
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, AddActivity.class);
                    intent.putExtra("iddbd", myidbd);
                    startActivity(intent);

                }
            });
        }


    public  void gpscheck(){
        comgoldbin.vk.geonotes.GPSTracker gps = new comgoldbin.vk.geonotes.GPSTracker(MapsActivity.this);
        if(gps.canGetLocation())
        {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else
        {
            gps.showSettingsAlert();
        }
    }

    public  boolean checkPermissions() {

        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();


        if (loc2 != PackageManager.PERMISSION_GRANTED ) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}




