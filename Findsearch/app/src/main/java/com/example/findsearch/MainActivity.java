package com.example.findsearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int NOTIFY_ID = 101;

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";

    private Button btn1, btnsave, btnbd;
    private RadioButton rb1,rb2;
    private EditText que;
    private TextView selection;
    private Spinner spinner;
    private ListView lvMain;
    private ListView lvMain1;
    private ListView lvMain2;
    private ListView lvMain3;
    private ListView lvMain4;

    //
    SQLiteDatabase db4;
    DatabaseHelper databaseHelper4;
    Cursor userCursor4;
    long userId4=0;
    //

    String item = " ";
    String[] shops = { "Farpost.ru", "Star-phone.ru", "Parts.dvsota.ru", "Electrashop.ru", "125gsm.ru", "Tggsm.ru"};
    ArrayList<String> ar = new ArrayList<String>();
    ArrayList<String> ar1 = new ArrayList<String>();
    ArrayList<String> ar2 = new ArrayList<String>();
    ArrayList<String> ar3 = new ArrayList<String>();
    ArrayList<String> ar4 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1= (Button) findViewById(R.id.btn1);
        btnsave= (Button) findViewById(R.id.btnsave);
        btnbd= (Button) findViewById(R.id.btnbd);
        que = (EditText) findViewById(R.id.que);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        selection = (TextView) findViewById(R.id.textView2);
        spinner = findViewById(R.id.spinner);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain1 = (ListView) findViewById(R.id.lvMain1);
        lvMain2 = (ListView) findViewById(R.id.lvMain2);
        lvMain3 = (ListView) findViewById(R.id.lvMain3);
        lvMain4 = (ListView) findViewById(R.id.lvMain4);

        btn1.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btnbd.setOnClickListener(this);

        databaseHelper4 = new DatabaseHelper(this);
        // создаем базу данных
       // databaseHelper4.create_db();
        db4 = databaseHelper4.open();
        Bundle extras4 = getIntent().getExtras();
        if (extras4 != null) {
            userId4 = extras4.getLong("id");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shops);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                item = (String)parent.getItemAtPosition(position);
                selection.setText(item); //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
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

    void writeFileSD1() {
        String querysd=que.getText().toString();
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
            notificationManager.notify(4, notification);
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btn1:
                MyTask mt = new MyTask();
                mt.execute();
                //
                ArrayAdapter newadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar);
                lvMain.setAdapter(newadapter);

                ArrayAdapter newadapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar1);
                lvMain1.setAdapter(newadapter1);

                ArrayAdapter newadapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar2);
                lvMain2.setAdapter(newadapter2);

                ArrayAdapter newadapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar3);
                lvMain3.setAdapter(newadapter3);

                ArrayAdapter newadapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar4);
                lvMain4.setAdapter(newadapter4);

                btnsave.setVisibility(View.VISIBLE);
                btnbd.setVisibility(View.VISIBLE);
                //
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Парсинг в процессе")
                                    .setContentText("Скоро данные будут распечатаны на экран");

                    Notification notification = builder.build();

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1, notification);
                //
                //
                break;
            case R.id.btnsave:
                writeFileSD1();
                break;
            case R.id.btnbd:
                //
                String q = que.getText().toString();
                //
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                //
                ContentValues cv = new ContentValues();
                //
                //

                for (int i = 0; i < ar.size(); i++)
                {
                    cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                    cv.put(DatabaseHelper.COLUMN_QUERY, q);
                    cv.put(DatabaseHelper.COLUMN_SHOP, item);
                    cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                    cv.put(DatabaseHelper.COLUMN_H0, ar.get(i));
                }

                for (int i1 = 0; i1 < ar1.size(); i1++)
                {
                    cv.put(DatabaseHelper.COLUMN_H1, ar1.get(i1));
                }

                for (int i2 = 0; i2 < ar2.size(); i2++)
                {
                    cv.put(DatabaseHelper.COLUMN_H2,  ar2.get(i2));
                }

                for (int i3 = 0; i3 < ar3.size(); i3++)
                {
                    cv.put(DatabaseHelper.COLUMN_H3,  ar3.get(i3));
                }

                for (int i4 = 0; i4 < ar4.size(); i4++)
                {
                    cv.put(DatabaseHelper.COLUMN_H4,  ar4.get(i4));
                }
                //
                //
                if (userId4 > 0) {
                    db4.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId4), null);
                } else {
                    db4.insert(DatabaseHelper.TABLE, null, cv);
                }
                // Закрываем подключение и курсор
                //
                db4.close();
                //userCursor4.close();
                break;
            default:
                break;
        }
        
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        String title;//Тут храним значение заголовка сайта
        Elements newsHeadlines;
        Elements newsHeadlines1;
        Elements newsHeadlines2;
        Elements newsHeadlines3;
        Elements newsHeadlines4;

        @Override
        protected Void doInBackground(Void... params) {

            //
            String q = que.getText().toString();

            switch (item) {
                case "Farpost.ru":
                {
                    if (rb1.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://www.farpost.ru/vladivostok/dir?query="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select(".bull-item-content__subject-container");
                            newsHeadlines1  = doc.select(".bull-item-content__price-info-container");
                            newsHeadlines2  = doc.select(".bull-item__annotation-row");
                            newsHeadlines3  = doc.getElementsByClass("ellipsis-text__left-side");
                            newsHeadlines4  = doc.getElementsByClass("bulletinLink bull-item__self-link auto-shy");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline3 : newsHeadlines3)
                            {
                                ar3.add(headline3.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }


                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        //selection.setText("Buy "+q+"all on Farpost");
                    }
                    if (rb2.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://www.farpost.ru/vladivostok/tech/communication/parts/?query="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select(".bull-item-content__subject-container");
                            newsHeadlines1  = doc.select(".bull-item-content__price-info-container");
                            newsHeadlines2  = doc.select(".bull-item__annotation-row");
                            newsHeadlines3  = doc.getElementsByClass("ellipsis-text__left-side");
                            newsHeadlines4  = doc.getElementsByClass("bulletinLink bull-item__self-link auto-shy");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline3 : newsHeadlines3)
                            {
                                ar3.add(headline3.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        //selection.setText("Buy "+q+"parts on Farpost");
                    }
                }
                break;
                case "Star-phone.ru":
                {
                    if (rb1.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://star-phone.ru/search/?search="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select("p.cat-model");
                            newsHeadlines1  = doc.select("div#res-products");
                            newsHeadlines2  = doc.select("p.price");
                            newsHeadlines4  = doc.select(".h4");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }

                            //
                            String id = null;
                            String a = null;
                            for (Element headline : newsHeadlines1)
                            {
                                id = headline.select("p.cat-model").text();
                                a = headline.select("a").text();

                                String[] arrayid = id.split(" ", -1);
                                String[] arraya = a.split("   ", -1);

                                for (int i=0;i<=arrayid.length;i++)
                                {
                                    ar1.add(headline.text());
                                }
                            }

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                       // selection.setText("Buy "+q+"all on Starphone");
                    }
                    if (rb2.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://star-phone.ru/search/?search="+query+"&sub_category=true&category_id=411").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select("p.cat-model");
                            newsHeadlines1  = doc.select("div#res-products");
                            newsHeadlines2  = doc.select("p.price");
                            newsHeadlines4  = doc.select(".h4");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }


                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }

                            //
                            String id = null;
                            String a = null;
                            for (Element headline : newsHeadlines1)
                            {
                                id = headline.select("p.cat-model").text();
                                a = headline.select("a").text();

                                String[] arrayid = id.split(" ", -1);
                                String[] arraya = a.split("   ", -1);

                                for (int i=0;i<=arrayid.length;i++)
                                {
                                    ar1.add(headline.text());
                                }
                            }
                            //

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        //selection.setText("Buy "+q+"parts on Starphone");
                    }
                }
                break;
                case "Parts.dvsota.ru":
                {
                    if (rb1.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://dvsota.ru/search?search="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select("a.product_item__text_wrapp");
                            newsHeadlines1  = doc.select("p.price__item");
                            newsHeadlines2  = doc.select("p.product_item__status");
                            newsHeadlines4  = doc.getElementsByClass("product_item__text_wrapp");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        //selection.setText("Buy all on DVsota");
                    }
                    if (rb2.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://parts.dvsota.ru/search?search="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select("a.product_item__text_wrapp");
                            newsHeadlines1  = doc.select("p.price__item");
                            newsHeadlines2  = doc.select("p.product_item__status");
                            newsHeadlines4  = doc.getElementsByClass("product_item__text_wrapp");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }



                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        // selection.setText("Buy parts on DVsota");
                    }
                }
                break;
                case "Electrashop.ru":
                {
                    if (rb1.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://electrashop.ru/search/?q="+query+"&send=Y&r=Y").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select("a.name");
                            newsHeadlines1  = doc.select(".price");
                            newsHeadlines2  = doc.select(".optional");
                            newsHeadlines4  = doc.getElementsByClass("name");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }


                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                       // selection.setText("Buy all on Electrashop");
                    }
                    if (rb2.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://electrashop.ru/search/?q="+query+"&send=Y&r=Y&set_filter=Y").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select("a.name");
                            newsHeadlines1  = doc.select(".price");
                            newsHeadlines2  = doc.select(".optional");
                            newsHeadlines4  = doc.getElementsByClass("name");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                       // selection.setText("Buy parts on Electrashop");
                    }
                }
                break;
                case "125gsm.ru":
                {
                    if (rb1.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://gsm.shop/catalog/search/?s="+query+"&sort=rat&sc=").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select(".product__code");
                            newsHeadlines1  = doc.select(".product__title");
                            newsHeadlines2  = doc.select(".product__price");
                            newsHeadlines3  = doc.getElementsByClass("product__instock");
                            newsHeadlines4  = doc.getElementsByClass("product__link");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }


                            for (Element headline3 : newsHeadlines3)
                            {
                                ar3.add(headline3.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }


                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        //selection.setText("Buy all on 125gsm");
                    }
                    if (rb2.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://gsm.shop/catalog/search/?s="+query+"&sort=rat&sc=2793").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select(".product__code");
                            newsHeadlines1  = doc.select(".product__title");
                            newsHeadlines2  = doc.select(".product__price");
                            newsHeadlines3  = doc.getElementsByClass("product__instock");
                            newsHeadlines4  = doc.getElementsByClass("product__link");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline3 : newsHeadlines3)
                            {
                                ar3.add(headline3.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }

                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
                break;
                case "Tggsm.ru":
                {
                    if (rb1.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://taggsm.ru/index.php?route=product/searcho&filter_name="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select(".cat_articul");
                            newsHeadlines1  = doc.select("h4");
                            newsHeadlines2  = doc.select(".category_price");
                            newsHeadlines3  = doc.select("b");
                            newsHeadlines4  = doc.select("h4");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline3 : newsHeadlines3)
                            {
                                ar3.add(headline3.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }


                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    if (rb2.isChecked()==true)
                    {
                        String query=q;
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("https://taggsm.ru/index.php?route=product/searcho&filter_name="+query+"&filter_category_id=900000").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

                            newsHeadlines  = doc.select(".cat_articul");
                            newsHeadlines1  = doc.select("h4");
                            newsHeadlines2  = doc.select(".category_price");
                            newsHeadlines4  = doc.select("h4");

                            for (Element headline : newsHeadlines)
                            {
                                ar.add(headline.text());
                            }

                            for (Element headline1 : newsHeadlines1)
                            {
                                ar1.add(headline1.text());
                            }

                            for (Element headline2 : newsHeadlines2)
                            {
                                ar2.add(headline2.text());
                            }

                            for (Element headline4 : newsHeadlines4)
                            {
                                ar4.add(String.valueOf(headline4));
                            }


                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                       // selection.setText("Buy parts on Tggsm");
                    }
                }
                break;

                default:
                    selection.setText("Error="+item);
                    break;
            }
            //

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            String[] first = new String[1000000];
//            String[] second = new String[1000000];
//            String[] thirty = new String[1000000];
//            String[] fourty = new String[1000000];
//            String[] fivety = new String[1000000];
//
//            for (Element headline : newsHeadlines)
//            {
//                int i=0;
//                first[i]=headline.text();
//                i++;
//            }
//            ArrayAdapter adapter = new ArrayAdapter((Context) MainActivity, android.R.layout.simple_list_item_1, first);
//            lvMain.setAdapter(adapter);
//
//            for (Element headline1 : newsHeadlines1)
//            {
//                int i1=0;
//                second[i1]=headline1.text();
//                i1++;
//            }
//            ArrayAdapter adapter1 = new ArrayAdapter((Context) MainActivity, android.R.layout.simple_list_item_1, second);
//            lvMain1.setAdapter(adapter1);
//
//            for (Element headline2 : newsHeadlines2)
//            {
//                int i2=0;
//                thirty[i2]=headline2.text();
//                i2++;
//            }
//            ArrayAdapter adapter2 = new ArrayAdapter((Context) MainActivity, android.R.layout.simple_list_item_1, thirty);
//            lvMain2.setAdapter(adapter2);
//
//            for (Element headline3 : newsHeadlines3)
//            {
//                int i3=0;
//                fourty[i3]=headline3.text();
//                i3++;
//            }
//            ArrayAdapter adapter3 = new ArrayAdapter((Context) MainActivity, android.R.layout.simple_list_item_1, thirty);
//            lvMain3.setAdapter(adapter3);
//
//            for (Element headline4 : newsHeadlines4)
//            {
//                int i4=0;
//                fivety[i4]=headline4.text();
//                i4++;
//            }
//            ArrayAdapter adapter4 = new ArrayAdapter((Context) MainActivity, android.R.layout.simple_list_item_1, fivety);
//            lvMain4.setAdapter(adapter4);
        }
    }
}