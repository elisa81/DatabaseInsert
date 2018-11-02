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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyDBOpenHelper dbHelper;
    SQLiteDatabase mdb;
    String query;
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDBOpenHelper(this, "awe.db", null, 1);
        mdb = dbHelper.getWritableDatabase();

        ((Button) findViewById(R.id.buttonInsert)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonReadData)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonUpdate)).setOnClickListener(this);
        ((Button) findViewById(R.id.buttonDelete)).setOnClickListener(this);

        ((EditText) findViewById(R.id.editTextCountry)).getText().toString();
        ((EditText) findViewById(R.id.editTextCity)).getText().toString();
    }

    @Override
    public void onClick(View v) {

        String country = ((EditText) findViewById(R.id.editTextCountry)).getText().toString();
        String city = ((EditText) findViewById(R.id.editTextCity)).getText().toString();
        TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
        TextView textViewPKID = (TextView) findViewById(R.id.textViewPKID);
        TextView textViewVisited = (TextView) findViewById(R.id.textViewPKID);

        switch (v.getId()) {
            case R.id.buttonInsert:
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String datetime = format.format(new Date());

                mdb.execSQL("INSERT INTO awe_country VALUES(' " + datetime + "', '" + country + "','" + city + "');");
                break;


            case R.id.buttonReadData:
                query = "SELECT * FROM awe_country ORDER BY pkid DESC ";
                Cursor cursor = mdb.rawQuery(query, null);
                String str = "";

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
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
                query = "DELETE FROM awe_country WHERE country='"+country+"';";
                mdb.execSQL(query);

                query = "SELECT * FROM awe_country ORDER BY pkid DESC ";
                cursor = mdb.rawQuery(query, null);
                str = "";

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
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


            case R.id.buttonUpdate:
                query = "UPDATE awe_country SET city='"+city+"' WHERE country='"+country+"';";
                mdb.execSQL(query);

                query = "SELECT * FROM awe_country ORDER BY pkid DESC ";
                cursor = mdb.rawQuery(query, null);
                str = "";

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
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


            case R.id.buttonVisited:
                String strPKID = textViewPKID.getText().toString();
                query = "INSERT INTO awe_country_visitedcount VALUES('" + strPKID + "')";
                mdb.execSQL(query);
                break;


            case R.id.buttonSearch:
                query = "SELECT pkid, country, city, count(fkid) visitedTotal " +
                        "FROM awe_country INNER JOIN awe_country_visitedcount " +
                        "ON pkid = fkid AND pkid =  ' " + country +  " ' ";
                cursor = mdb.rawQuery(query, null);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    int visited = cursor.getInt(cursor.getColumnIndex("visitedTotal"));
                    textViewVisited.setText(String.valueOf(visited));
                }
        }
    }
}