package comgoldbin.vk.geonotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comgoldbin.vk.geonotes.R;

public class AddActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText mTextEdit;
    private ImageView img;
    static final int GALLERY_REQUEST = 1;
    private long MyDataID;
    String selectedImg;
    GoogleMap mMap;
    LatLng locationgeo;
    double latitude;
    double longitude;
    long pol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final RepoBDHelper db = new RepoBDHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addMap);
        mapFragment.getMapAsync(this);

        Button btnADDSql = (Button) findViewById(R.id.btnADDSql);
        Button btnDEL = (Button) findViewById(R.id.btndel);
        Button btnREF = (Button) findViewById(R.id.btnref);

        mTextEdit = (EditText) findViewById(R.id.editText);
        img = (ImageView) findViewById(R.id.imageView);


        img.setImageURI(Uri.parse("android.resource://comgoldbin.vk.geonotes/drawable/v4"));
        selectedImg = "android.resource://comgoldbin.vk.geonotes/drawable/v4";

        gpscheck();

        final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Intent intent = getIntent();
        final long myidbd = intent.getLongExtra("iddbd",pol);
        selectBD(myidbd);


        btnADDSql.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBData md = new DBData(MyDataID,date,mTextEdit.getText().toString(),selectedImg,latitude,longitude);
                db.insert(md);
                finish();
                showMessagaADD(v);
            }
        });

        btnDEL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.delete(myidbd);
                finish();
                showMessagaDEL(v);
            }
        });

        btnREF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBData md = new DBData(myidbd,date,mTextEdit.getText().toString(),selectedImg,latitude,longitude);
                db.update(md);
                finish();
                showMessagaREF(v);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showImageAlert();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationgeo = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions()
                .position(locationgeo)
        );
    }

    public void selectBD(long i)
    {
        if (i != 0)
        {
            View del = findViewById(R.id.btndel);
            View ref = findViewById(R.id.btnref);
            View btn = findViewById(R.id.btnADDSql);
            del.setVisibility(View.VISIBLE);
            ref.setVisibility(View.VISIBLE);
            btn.setVisibility(View.INVISIBLE);

            RepoBDHelper dbb = new RepoBDHelper(this);
            DBData dBData =  dbb.select(i);
            mTextEdit.setText(dBData.getText());
            img.setImageURI(Uri.parse(dBData.getPhoto().toString()));
            latitude = dBData.getLat();
            longitude = dBData.getLon();
        }
        else
        return;
    }


    public void showMessagaADD(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Запись добавлена!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessagaREF(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Запись обновлена!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMessagaDEL(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Запись удалена!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImage = imageReturnedIntent.getData();
        img.setImageURI(selectedImage);
        selectedImg = selectedImage.toString();
    }

    public  void gpscheck(){
        comgoldbin.vk.geonotes.GPSTracker gps = new comgoldbin.vk.geonotes.GPSTracker(this);
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


    //Диалоговое окно
    public void showImageAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddActivity.this);
        alertDialog.setTitle("Выбор изображения.");
        alertDialog.setMessage("Сделайте новое фото или прейдите в галлерею для выбора:");

        alertDialog.setPositiveButton("Камера", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 0);
            }
        });

        alertDialog.setNegativeButton("Галлерея", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        alertDialog.show();
    }
}



