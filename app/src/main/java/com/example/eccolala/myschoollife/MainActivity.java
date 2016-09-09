package com.example.eccolala.myschoollife;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.example.eccolala.myschoollife.util.TitleBuilder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TitleBuilder tbBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        tbBuilder = new TitleBuilder(this);

        tbBuilder.setTitleText("首页")
            .setTitleText("第1周");

    }


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
                // mNowWeek = positon + 1;
                pop.dismiss();
                // drawNowWeek();
                // drawAllCourse();
            }
        });

    }
}
