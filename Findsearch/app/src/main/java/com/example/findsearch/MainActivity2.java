package com.example.findsearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private Button button7;
    private RadioButton rb3,rb4;
    private EditText que1;
    private TextView selection1;

    String item = " ";
    String[] shops = { "Farpost.ru", "Star-phone.ru", "Parts.dvsota.ru", "Electrashop.ru", "125gsm.ru", "Tggsm.ru"};

    //
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    Cursor userCursor;
    long userId=0;
    //
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button7= (Button) findViewById(R.id.button7);
        que1 = (EditText) findViewById(R.id.que1);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        selection1 = (TextView) findViewById(R.id.textView6);
        //
        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();
        //
        button7.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button7:
                //
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Парсинг в процессе")
                                .setContentText("Данные скачиваются из всех сайтов в базу данных");

                Notification notification = builder.build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(2, notification);
                //

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    userId = extras.getLong("id");
                }
                //
                MainActivity2.MyTask mt = new MainActivity2.MyTask();
                mt.execute();

                break;

            default:
                break;
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        String title="Please Wait";//Тут храним значение заголовка сайта
        Semaphore sem = new Semaphore(1);

        @Override
        protected Void doInBackground(Void... params) {

            if (rb3.isChecked()==true) {

                Thread afarp = new Thread(new Runnable()
                {
                    public void run() //Этот метод будет выполняться в побочном потоке
                    {

                        try {
                            sem.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        title="10%";
                        farpostall();
                        sem.release();

                    }
                });
                afarp.start();
                try {
                    afarp.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (afarp.isAlive()==false)
                {
                    Thread astar = new Thread(new Runnable()
                    {
                        public void run() //Этот метод будет выполняться в побочном потоке
                        {
                            try {
                                sem.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            title="20%";
                            starphoneall();
                            sem.release();

                        }
                    });
                    astar.start();
                    try {
                        astar.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //
                    if (astar.isAlive()==false)
                    {
                        Thread asota = new Thread(new Runnable()
                        {
                            public void run() //Этот метод будет выполняться в побочном потоке
                            {
                                try {
                                    sem.acquire();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                title="40%";
                                dvsotall();
                                sem.release();

                            }
                        });
                        asota.start();
                        try {
                            asota.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //
                        if (asota.isAlive()==false)
                        {
                            Thread alect = new Thread(new Runnable()
                            {
                                public void run() //Этот метод будет выполняться в побочном потоке
                                {
                                    try {
                                        sem.acquire();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    title="60%";
                                    electrashopall();
                                    sem.release();
                                }
                            });
                            alect.start();
                            try {
                                alect.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //
                            if (alect.isAlive()==false)
                            {
                                Thread amail = new Thread(new Runnable()
                                {
                                    public void run() //Этот метод будет выполняться в побочном потоке
                                    {
                                        try {
                                            sem.acquire();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        title="80%";
                                        l25gsmall();
                                        sem.release();
                                    }
                                });
                                amail.start();
                                try {
                                    amail.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //
                                if (amail.isAlive()==false)
                                {
                                    Thread atgg = new Thread(new Runnable()
                                    {
                                        public void run() //Этот метод будет выполняться в побочном потоке
                                        {
                                            try {
                                                sem.acquire();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            title="90%";
                                            tggsmall();
                                            sem.release();
                                        }

                                    });
                                    atgg.start();
                                    try {
                                        atgg.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //
                                }
                                //
                            }
                            //
                        }
                        //
                    }
                    //
                }

            title="90%";
            title="100%";
            title="Success!";
            }

            if (rb4.isChecked()==true) {

                Thread pfarp = new Thread(new Runnable()
                {
                    public void run() //Этот метод будет выполняться в побочном потоке
                    {
                        try {
                            sem.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        title="10%";
                        farpostparts();
                        sem.release();

                    }
                });
                pfarp.start();
                try {
                    pfarp.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (pfarp.isAlive()==false)
                {
                    Thread pstar = new Thread(new Runnable()
                    {
                        public void run() //Этот метод будет выполняться в побочном потоке
                        {
                            try {
                                sem.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            title="20%";
                            starphoneparts();
                            sem.release();

                        }
                    });
                    pstar.start();
                    try {
                        pstar.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //
                    if (pstar.isAlive()==false)
                    {
                        Thread psota = new Thread(new Runnable()
                        {
                            public void run() //Этот метод будет выполняться в побочном потоке
                            {
                                try {
                                    sem.acquire();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                title="40%";
                                dvsotaparts();
                                sem.release();
                            }
                        });
                        psota.start();
                        try {
                            psota.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //
                        if (psota.isAlive()==false)
                        {
                            Thread plect = new Thread(new Runnable()
                            {
                                public void run() //Этот метод будет выполняться в побочном потоке
                                {
                                    try {
                                        sem.acquire();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    title="60%";
                                    electrashopparts();
                                    sem.release();
                                }
                            });
                            plect.start();
                            try {
                                plect.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //
                            if (plect.isAlive()==false)
                            {
                                Thread pmail = new Thread(new Runnable()
                                {
                                    public void run() //Этот метод будет выполняться в побочном потоке
                                    {
                                        try {
                                            sem.acquire();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        title="80%";
                                        l25gsmparts();
                                        sem.release();
                                    }
                                });
                                pmail.start();
                                try {
                                    pmail.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //
                                if (pmail.isAlive()==false)
                                {
                                    Thread ptgg = new Thread(new Runnable()
                                    {
                                        public void run() //Этот метод будет выполняться в побочном потоке
                                        {
                                            try {
                                                sem.acquire();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            title="90%";
                                            tggsmparts();
                                            sem.release();
                                        }

                                    });
                                    ptgg.start();
                                    try {
                                        ptgg.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //
                                }
                                //
                            }
                            //
                        }
                        //
                    }
                    //
                }


                title="90%";
                title="100%";
                title="Success!";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            selection1.setText(title);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    public void farpostall()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://www.farpost.ru/vladivostok/dir?query="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select(".bull-item-content__subject-container");
            Elements newsHeadlines1  = doc.select(".bull-item-content__price-info-container");
            Elements newsHeadlines2  = doc.select(".bull-item__annotation-row");
            Elements newsHeadlines3  = doc.getElementsByClass("ellipsis-text__left-side");
            Elements newsHeadlines4  = doc.getElementsByClass("bulletinLink bull-item__self-link auto-shy");

            //
            ContentValues cv = new ContentValues();


            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Farpost.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline3 : newsHeadlines3)
            {
                cv.put(DatabaseHelper.COLUMN_H3, headline3.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
           if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
          } else {
               db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void starphoneall()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://star-phone.ru/search/?search="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select("p.cat-model");
            Elements newsHeadlines1  = doc.select("div#res-products");
            Elements newsHeadlines2  = doc.select("p.price");
            Elements newsHeadlines4  = doc.select(".h4");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Star-phone.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
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
                    cv.put(DatabaseHelper.COLUMN_H1, headline.text());
                }
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void dvsotall()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://dvsota.ru/search?search="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select("a.product_item__text_wrapp");
            Elements newsHeadlines1  = doc.select("p.price__item");
            Elements newsHeadlines2  = doc.select("p.product_item__status");
            Elements newsHeadlines4  = doc.getElementsByClass("product_item__text_wrapp");

            //
            ContentValues cv = new ContentValues();


            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Parts.dvsota.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void electrashopall()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://electrashop.ru/search/?q="+query+"&send=Y&r=Y").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select("a.name");
            Elements newsHeadlines1  = doc.select(".price");
            Elements newsHeadlines2  = doc.select(".optional");
            Elements newsHeadlines4  = doc.getElementsByClass("name");

            //
            ContentValues cv = new ContentValues();


            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Electrashop.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void l25gsmall()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://gsm.shop/catalog/search/?s="+query+"&sort=rat&sc=").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select(".product__code");
            Elements newsHeadlines1  = doc.select(".product__title");
            Elements newsHeadlines2  = doc.select(".product__price");
            Elements newsHeadlines3  = doc.getElementsByClass("product__instock");
            Elements newsHeadlines4  = doc.getElementsByClass("product__link");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "125gsm.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }


            for (Element headline3 : newsHeadlines3)
            {
                cv.put(DatabaseHelper.COLUMN_H3, headline3.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void tggsmall()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://taggsm.ru/index.php?route=product/searcho&filter_name="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select(".cat_articul");
            Elements newsHeadlines1  = doc.select("h4");
            Elements newsHeadlines2  = doc.select(".category_price");
            Elements newsHeadlines3  = doc.select("b");
            Elements newsHeadlines4  = doc.select("h4");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Tggsm.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "All");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline3 : newsHeadlines3)
            {
                cv.put(DatabaseHelper.COLUMN_H3, headline3.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void farpostparts()
    {
        String query=que1.getText().toString();
        Document doc = null;

        //
        ContentValues cv = new ContentValues();

        //

        try {
            doc = Jsoup.connect("https://www.farpost.ru/vladivostok/tech/communication/parts/?query="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select(".bull-item-content__subject-container");
            Elements newsHeadlines1  = doc.select(".bull-item-content__price-info-container");
            Elements newsHeadlines2  = doc.select(".bull-item__annotation-row");
            Elements newsHeadlines3  = doc.getElementsByClass("ellipsis-text__left-side");
            Elements newsHeadlines4  = doc.getElementsByClass("bulletinLink bull-item__self-link auto-shy");

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Farpost.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "Parts");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline3 : newsHeadlines3)
            {
                cv.put(DatabaseHelper.COLUMN_H3, headline3.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
              if (userId > 0) {
                 db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
               } else {
                  db.insert(DatabaseHelper.TABLE, null, cv);
               }
            //

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void starphoneparts()
    {
        String query=que1.getText().toString();
        Document doc = null;

        //
        ContentValues cv = new ContentValues();

        //

        try {
            doc = Jsoup.connect("https://star-phone.ru/search/?search="+query+"&sub_category=true&category_id=411").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select("p.cat-model");
            Elements newsHeadlines1  = doc.select("div#res-products");
            Elements newsHeadlines2  = doc.select("p.price");
            Elements newsHeadlines4  = doc.select(".h4");

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Star-phone.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "Parts");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }


            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
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
                    cv.put(DatabaseHelper.COLUMN_H1, headline.text());
                }
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void dvsotaparts()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://parts.dvsota.ru/search?search="+query).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select("a.product_item__text_wrapp");
            Elements newsHeadlines1  = doc.select("p.price__item");
            Elements newsHeadlines2  = doc.select("p.product_item__status");
            Elements newsHeadlines4  = doc.getElementsByClass("product_item__text_wrapp");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Parts.dvsota.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "Parts");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void electrashopparts()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://electrashop.ru/search/?q="+query+"&send=Y&r=Y&set_filter=Y").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select("a.name");
            Elements newsHeadlines1  = doc.select(".price");
            Elements newsHeadlines2  = doc.select(".optional");
            Elements newsHeadlines4  = doc.getElementsByClass("name");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Electrashop.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "Parts");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void l25gsmparts()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://gsm.shop/catalog/search/?s="+query+"&sort=rat&sc=2793").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select(".product__code");
            Elements newsHeadlines1  = doc.select(".product__title");
            Elements newsHeadlines2  = doc.select(".product__price");
            Elements newsHeadlines3  = doc.getElementsByClass("product__instock");
            Elements newsHeadlines4  = doc.getElementsByClass("product__link");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "125gsm.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "Parts");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }


            for (Element headline3 : newsHeadlines3)
            {
                cv.put(DatabaseHelper.COLUMN_H3, headline3.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void tggsmparts()
    {
        String query=que1.getText().toString();
        Document doc = null;

        try {
            doc = Jsoup.connect("https://taggsm.ru/index.php?route=product/searcho&filter_name="+query+"&filter_category_id=900000").userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

            Elements newsHeadlines  = doc.select(".cat_articul");
            Elements newsHeadlines1  = doc.select("h4");
            Elements newsHeadlines2  = doc.select(".category_price");
            Elements newsHeadlines4  = doc.select("h4");

            //
            ContentValues cv = new ContentValues();

            //

            for (Element headline : newsHeadlines)
            {
                cv.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(date).toString());
                cv.put(DatabaseHelper.COLUMN_QUERY, query);
                cv.put(DatabaseHelper.COLUMN_SHOP, "Tggsm.ru");
                cv.put(DatabaseHelper.COLUMN_TYPE, "Parts");
                cv.put(DatabaseHelper.COLUMN_H0, headline.text());
            }

            for (Element headline1 : newsHeadlines1)
            {
                cv.put(DatabaseHelper.COLUMN_H1, headline1.text());
            }

            for (Element headline2 : newsHeadlines2)
            {
                cv.put(DatabaseHelper.COLUMN_H2, headline2.text());
            }

            for (Element headline4 : newsHeadlines4)
            {
                cv.put(DatabaseHelper.COLUMN_H4, String.valueOf(headline4));
            }

            //
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            //


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}