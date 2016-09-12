package com.example.eccolala.myschoollife.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CrawlAsync.AsyncResponse {

    private TitleBuilder tbBuilder;

    private LinearLayout bgLayout;

    //当前
    private int mNowWeek;

    private Intent intent;

    private User user;

    private CrawlAsync async;

    private Realm realm;

    private LinearLayout[] linearLayouts = new LinearLayout[7];

    private ArrayList<Integer> myImageList;


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

        user = (User) intent.getSerializableExtra("user");

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

        myImageList = new ArrayList<>();
        myImageList.add(R.drawable.ic_course_bg_bohelv);
        myImageList.add(R.drawable.ic_course_bg_cyan);
        myImageList.add(R.drawable.ic_course_bg_cheng);
        myImageList.add(R.drawable.ic_course_bg_huang);
        myImageList.add(R.drawable.ic_course_bg_kafei);
        myImageList.add(R.drawable.ic_course_bg_lan);
        myImageList.add(R.drawable.ic_course_bg_lv);
        myImageList.add(R.drawable.ic_course_bg_molan);
        myImageList.add(R.drawable.ic_course_bg_qing);
        myImageList.add(R.drawable.ic_course_bg_tao);
        myImageList.add(R.drawable.ic_course_bg_zi);

    }


    /**
     * 选择星期
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
                tbBuilder.setTitleText("第" + mNowWeek + "周");

                // addCourses("呵呵打");
            }
        });

    }


    /**
     * 设置界面
     */
    public void onClickSetting(View v) {
        // for (int i=0;i<linearLayouts.length;i++){
        //     linearLayouts[i].removeAllViews();
        // }

    }


    private void createCourses(int[] counts, int j, int k, String msg) {
        Random ran = new Random();
        int x = ran.nextInt(11);

        TextView
            textView = new TextView(getApplicationContext());
        textView.setText(msg);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(
            myImageList.get(x));
        textView.setTextSize(12);

        LinearLayout.LayoutParams params
            = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 280);
        params.setMargins(0, (k - counts[j]) * 280, 0,
            0); //left,top,right, bottom
        textView.setLayoutParams(params);
        linearLayouts[j].addView(textView);
        counts[j] = k + 1;
    }


    @Override public void processFinish(final List<Course> courses) {

        realm.executeTransaction(
            new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    int[] counts = new int[7];

                    // for (int i = 0; i < linearLayouts.length; i++) {
                    //     for (Course course : realm.where(Course.class).findAll()){
                    //         if (course.posX == i){
                    //
                    //             String msg = course.className + "\n" + "\n" + course.classAddr;
                    //
                    //             int posX = course.posX;
                    //
                    //             int posY = course.posY - 1;
                    //
                    //
                    //             addCourses(msg,posX,posY);
                    //         }
                    //     }
                    //
                    // }

                    for (Course course : realm.where(Course.class).findAll()) {
                        for (int j = 0; j < 7; j++) {
                            if (course.posX == j) {
                                for (int k = 0; k < 5; k++) {
                                    if (course.posY - 1 == k) {
                                        String msg = course.className + "\n" + "\n" +
                                            course.classAddr;

                                        createCourses(counts, j, k, msg);

                                    }
                                }
                            }
                        }
                    }

                }

            }

        );
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
