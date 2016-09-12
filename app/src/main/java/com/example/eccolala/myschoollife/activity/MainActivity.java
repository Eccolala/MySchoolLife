package com.example.eccolala.myschoollife.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.eccolala.myschoollife.R;
import com.example.eccolala.myschoollife.asynctask.CrawlAsync;
import com.example.eccolala.myschoollife.model.Course;
import com.example.eccolala.myschoollife.model.User;
import com.example.eccolala.myschoollife.util.TitleBuilder;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CrawlAsync.AsyncResponse{

    private TitleBuilder tbBuilder;

    private LinearLayout bgLayout;

    //当前
    private int mNowWeek;

    private Intent intent;

    private User user;

    private CrawlAsync async;

    private RealmConfiguration realmConfig;

    private Realm realm;

    private LinearLayout[] linearLayouts = new LinearLayout[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        bgLayout = (LinearLayout) findViewById(R.id.bg_ll);

        // Bitmap d = new BitmapDrawable(ctx.getResources(), R.drawable.lolita).getBitmap();
        //
        // int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
        //
        // Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);


        realm = Realm.getDefaultInstance();

        init();

        //模糊效果
        bgLayout.setBackgroundResource(R.drawable.lolita_pure);

        tbBuilder = new TitleBuilder(this);

        tbBuilder.setTitleText("第1周").setLeftText("成绩").setRightImage(R.drawable.selecter_settings);

        intent = this.getIntent();

        user = (User)intent.getSerializableExtra("user");




        async = new CrawlAsync();

        async.delegate = this;

        async.execute(user);



    }





    private void init() {
        linearLayouts[0] = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayouts[1] = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayouts[2] = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayouts[3] = (LinearLayout) findViewById(R.id.linearLayout4);
        linearLayouts[4] = (LinearLayout) findViewById(R.id.linearLayout5);
        linearLayouts[5] = (LinearLayout) findViewById(R.id.linearLayout6);
        linearLayouts[6] = (LinearLayout) findViewById(R.id.linearLayout7);

    }


    /**
     * 选择星期
     * @param v
     */
    public void weekSelect(View v) {
        View view = View.inflate(this, R.layout.changweek_layout, null);

        ListView weekList = (ListView) view.findViewById(R.id.weekList);

        ArrayList<String> strList = new ArrayList<String>();

        for (int i = 1; i < 21; i++) {

            strList.add("第" + i + "周");

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            R.layout.item, strList);

        weekList.setAdapter(adapter);

        view.measure(0, 0);

        final PopupWindow pop = new PopupWindow(view, 400, 700, true);

        pop.setBackgroundDrawable(new ColorDrawable(0x00000000));

        int xOffSet = -(pop.getWidth() - v.getWidth()) / 2;

        pop.showAsDropDown(v, xOffSet, 0);

        weekList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int positon, long id) {
                mNowWeek = positon + 1;
                pop.dismiss();
                // drawNowWeek();
                // drawAllCourse();
                tbBuilder.setTitleText("第"+mNowWeek+"周");

                addCourses("呵呵打");
            }
        });

    }


    private void addCourses(String s) {
        for (int i=0;i<linearLayouts.length;i++){
            TextView textView = new TextView(getApplicationContext());
            textView.setText(s);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.ic_course_bg_cyan);
            textView.setTextSize(16);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                280));

            linearLayouts[i].addView(textView);
        }

    }


    /**
     * 设置界面
     * @param v
     */
    public void onClickSetting(View v) {
        // for (int i=0;i<linearLayouts.length;i++){
        //     linearLayouts[i].removeAllViews();
        // }

    }






    @Override public void processFinish(List<Course> courses) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {

                for (Course s : realm.where(Course.class).findAll()) {
                    String className = s.className;
                    Log.d("TAG", String.valueOf(className));

                    addCourses(className);
                }
            }
        });
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
