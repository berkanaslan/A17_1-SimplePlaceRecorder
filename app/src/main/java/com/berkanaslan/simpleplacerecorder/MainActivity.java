package com.berkanaslan.simpleplacerecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<String> placesName;
    static ArrayList<LatLng> location;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesName = new ArrayList<String>();
        location = new ArrayList<LatLng>();

        listView = (ListView)findViewById(R.id.listView);

      try {
          MapsActivity.database = this.openOrCreateDatabase("PlaceRecorder", MODE_PRIVATE, null);

          Cursor cursor = MapsActivity.database.rawQuery("SELECT * FROM Places", null);

          int addressTitleIx = cursor.getColumnIndex("AddressTitle");
          int latitudeIx = cursor.getColumnIndex("Latitude");
          int longitudeIx = cursor.getColumnIndex("Longitude");

          while (cursor.moveToNext()) {

              String nameFromDatabase = cursor.getString(addressTitleIx);
              String latitudeFromDatabase = cursor.getString(latitudeIx);
              String longitudeFromDatabase = cursor.getString(longitudeIx);

              placesName.add(nameFromDatabase);

              Double l1 = Double.parseDouble(latitudeFromDatabase);
              Double l2 = Double.parseDouble(longitudeFromDatabase);

              LatLng locationFromDatabase = new LatLng(l1,l2);
              location.add(locationFromDatabase);
          }

          cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

      arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,placesName);
      listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_place) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("info", "new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
