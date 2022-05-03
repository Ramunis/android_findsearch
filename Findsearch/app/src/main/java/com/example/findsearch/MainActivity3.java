package com.example.findsearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";

    private Button button11, buttonsave;
    private RadioButton rb5,rb6;
    private EditText que2;
    private EditText que3;
    private TextView selection2;
    private Spinner spinner1;
    private ListView mvMain;
    private ListView mvMain1;
    private ListView mvMain2;
    private ListView mvMain3;
    private ListView mvMain4;

    String item1 = " ";
    String[] shops1 = { "Farpost.ru", "Star-phone.ru", "Parts.dvsota.ru", "Electrashop.ru", "125gsm.ru", "Tggsm.ru"};

    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> ar1 = new ArrayList<String>();
    ArrayList<String> ar2 = new ArrayList<String>();
    ArrayList<String> ar3 = new ArrayList<String>();
    ArrayList<String> ar4 = new ArrayList<String>();
    //
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor1;
    SimpleCursorAdapter userAdapter;
    SimpleCursorAdapter userAdapter1;
    SimpleCursorAdapter userAdapter2;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        button11= (Button) findViewById(R.id.button11);
        buttonsave= (Button) findViewById(R.id.buttonsave);
        que2 = (EditText) findViewById(R.id.que2);
        que3 = (EditText) findViewById(R.id.que3);
        rb5 = findViewById(R.id.rb5);
        rb6 = findViewById(R.id.rb6);
        selection2 = (TextView) findViewById(R.id.textView10);
        spinner1 = findViewById(R.id.spinner1);
        mvMain = (ListView) findViewById(R.id.mvMain);
        mvMain1 = (ListView) findViewById(R.id.mvMain1);
        mvMain2 = (ListView) findViewById(R.id.mvMain2);
        mvMain3 = (ListView) findViewById(R.id.mvMain3);
        mvMain4 = (ListView) findViewById(R.id.mvMain4);

        button11.setOnClickListener(this);
        buttonsave.setOnClickListener(this);

        //
        sqlHelper = new DatabaseHelper(getApplicationContext());
       // sqlHelper.create_db();


        ArrayAdapter<String> aadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shops1);
        aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(aadapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                item1 = (String)parent.getItemAtPosition(position);
                selection2.setText(item1); //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner1.setOnItemSelectedListener(itemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        TextView infoTextView = findViewById(R.id.textView);

        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_cat1:
                // infoTextView.setText("Вы выбрали кота!");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_cat2:
                // infoTextView.setText("Вы выбрали кошку!");
                Intent intent2 = new Intent(this, MainActivity2.class);
                startActivity(intent2);
                return true;
            case R.id.action_cat3:
                // infoTextView.setText("Вы выбрали котёнка!");
                Intent intent3 = new Intent(this, MainActivity3.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void writeFileSD() {
        String querysd=que2.getText().toString();
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(querysd);
            for(String el : ar){
                bw.write(el);
            }
            bw.write("_____________________");
            for(String el1 : ar1){
                bw.write(el1);
            }
            bw.write("_____________________");
            for(String el2 : ar2){
                bw.write(el2);
            }
            bw.write("_____________________");
            for(String el3 : ar3){
                bw.write(el3);
            }
            bw.write("_____________________");
            for(String el4 : ar4){
                bw.write(el4);
            }
            bw.write("_____________________");
            // закрываем поток
            bw.close();
          //  Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Запись на SD карту")
                            .setContentText("Данные сохранены на /sdcard/MyFiles/");

            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(3, notification);
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button11:

                buttonsave.setVisibility(View.VISIBLE);
                String query=que2.getText().toString();
                String queryprice=que3.getText().toString();

                db = sqlHelper.open();

                if (rb5.isChecked()==true) {

                    userCursor1 = db.rawQuery("select * from " + DatabaseHelper.TABLE+" WHERE" +
    " typ='All' AND shop='"+item1+"' AND qu Like'"+query+"%'"+" AND (h1 Like'"+queryprice+"%'"+" OR h2 Like'"+queryprice+"%'"+")", null);

                userCursor1.moveToFirst();
                while (!userCursor1.isAfterLast())
                {
                    ar.add( userCursor1.getString(5));
                    ar1.add( userCursor1.getString(6));
                    ar2.add( userCursor1.getString(7));
                    ar3.add( userCursor1.getString(8));
                    ar4.add( userCursor1.getString(9));
                    userCursor1.moveToNext();
                }

                    ArrayAdapter mewadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar);
                    mvMain.setAdapter(mewadapter);

                    ArrayAdapter mewadapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar1);
                    mvMain1.setAdapter(mewadapter1);

                    ArrayAdapter mewadapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar2);
                    mvMain2.setAdapter(mewadapter2);

                    ArrayAdapter mewadapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar3);
                    mvMain3.setAdapter(mewadapter3);

                    ArrayAdapter mewadapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar4);
                    mvMain4.setAdapter(mewadapter4);


                selection2.setText(item1);
                    db.close();
                    userCursor1.close();
                }
                if (rb6.isChecked()==true) {


                    userCursor1 = db.rawQuery("select * from " + DatabaseHelper.TABLE+" WHERE" +
   " typ='Parts' AND shop='"+item1+"' AND qu Like'"+query+"%'"+" AND (h1 Like'"+queryprice+"%'"+" OR h2 Like'"+queryprice+"%'"+")", null);


                    userCursor1.moveToFirst();
                    while (!userCursor1.isAfterLast())
                    {
                        ar.add( userCursor1.getString(5));
                        ar1.add( userCursor1.getString(6));
                        ar2.add( userCursor1.getString(7));
                        ar3.add( userCursor1.getString(8));
                        ar4.add( userCursor1.getString(9));
                        userCursor1.moveToNext();
                    }

                    ArrayAdapter mewadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar);
                    mvMain.setAdapter(mewadapter);

                    ArrayAdapter mewadapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar1);
                    mvMain1.setAdapter(mewadapter1);

                    ArrayAdapter mewadapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar2);
                    mvMain2.setAdapter(mewadapter2);

                    ArrayAdapter mewadapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar3);
                    mvMain3.setAdapter(mewadapter3);

                    ArrayAdapter mewadapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar4);
                    mvMain4.setAdapter(mewadapter4);

                    selection2.setText(item1);
                    db.close();
                    userCursor1.close();
                }
                //
                break;
            case R.id.buttonsave:
                writeFileSD();
                break;
            default:
                break;
        }
    }


}