package com.jmj.edu.databaseinsert;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyDBOpenHelper dbHelper;
    SQLiteDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDBOpenHelper(this, "awe.db", null, 1);
        mdb = dbHelper.getWritableDatabase();

        ((Button) findViewById(R.id.buttonInsert)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonReadData)).setOnClickListener(this);

        ((EditText) findViewById(R.id.editTextCountry)).getText().toString();
        ((EditText) findViewById(R.id.editTextCity)).getText().toString();
    }

    @Override
    public void onClick(View v) {

        String country = ((EditText) findViewById(R.id.editTextCountry)).getText().toString();
        String city = ((EditText) findViewById(R.id.editTextCity)).getText().toString();

        switch (v.getId()) {
            case R.id.buttonInsert:
                mdb.execSQL("INSERT INTO awe_country VALUES(null, '" + country + "','" + city + "');");
                break;

            case R.id.buttonReadData:
                TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                String query = "SELECT * FROM awe_country ORDER BY _ID DESC ";
                Cursor cursor = mdb.rawQuery(query, null);
                String str = "";

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str += (id + " : " + country + " - " + city + "\n");
                }
                if (str.length() > 0) {
                    textViewResult.setText(str);
                } else {
                    Toast.makeText(getApplicationContext(), "Warning Empty DB", Toast.LENGTH_SHORT).show();
                    textViewResult.setText("");
                }
                break;

            case R.id.buttonDelete:
                query = "DELETE FROM awe_country WHERE country='country'";
                cursor = mdb.rawQuery(query, null);

                break;

            case R.id.buttonUpdate:
                query = "UPDATE awe_country SET city='city' WHERE country='country'";
                cursor = mdb.rawQuery(query, null);
                break;
        }
    }
}