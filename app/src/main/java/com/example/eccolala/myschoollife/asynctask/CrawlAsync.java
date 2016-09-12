package com.example.eccolala.myschoollife.asynctask;

import android.os.AsyncTask;
import com.example.eccolala.myschoollife.model.Course;
import com.example.eccolala.myschoollife.model.User;
import io.realm.Realm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by eccolala on 16-9-11.
 */

public class CrawlAsync extends AsyncTask<User, Void, List<Course>> {
    public static final String LOGIN_URL = "http://202.195.206.35:8080/jsxsd/xk/LoginToXk";
    public static final String GRADE_URL = "http://202.195.206.35:8080/jsxsd/kscj/cjcx_list";
    public static final String TABLE_URL = "http://202.195.206.35:8080/jsxsd/xskb/xskb_list.do";

    private Map<String, String> loginData;
    private User user;
    private List<Course> courses;

    private Realm realm;

    public AsyncResponse delegate = null;


    @Override protected void onPostExecute(List<Course> courses) {
        super.onPostExecute(courses);
        delegate.processFinish(this.courses);
    }


    @Override
    protected List<Course> doInBackground(User... params) {

        user = params[0];

        loginData = new HashMap<String, String>();

        loginData.put("USERNAME", user.userName);

        loginData.put("PASSWORD", user.passWord);

        courses = new ArrayList<>();

        try {


            realm = Realm.getDefaultInstance();

            Connection conn = Jsoup.connect(LOGIN_URL);
            conn.data(loginData);
            conn.timeout(30000);
            conn.method(Connection.Method.POST);
            conn.userAgent("Mozilla");
            Connection.Response response = conn.execute();
            Map<String, String> cookies = response.cookies();
            Document doc = Jsoup.connect(TABLE_URL)
                .cookies(cookies)
                .timeout(30000)
                .get();

            Elements tables = doc.select("table[id=kbtable]");

            String string = doc.text();

            final Elements trs = tables.select("tr");


            realm.executeTransaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {

                    //删除数据库
                    realm.delete(Course.class);

                    //周数
                    for (int tr_index = 0; tr_index < trs.size(); tr_index++) {

                        Elements divs = trs.get(tr_index).select("div[class=kbcontent]");


                        for (int div_index = 0; div_index < divs.size(); div_index++) {

                            String html = divs.get(div_index).outerHtml().replaceAll("<br>", "#~#");
                            Document doc1 = Jsoup.parse(html.toString());
                            String newHtml = doc1.text();

                            //去除没有课程信息的课程
                            if (newHtml.length() < 2) {
                                continue;
                            }

                            String[] ary = newHtml.split("#~#");

                            //创建一个课程
                            Course course = realm.createObject(Course.class);

                            //遍历数组
                            for (int i = 0; i < ary.length; i++) {

                                //去除所有空格
                                ary[i] = ary[i].replaceAll("\\s+", "");

                                //课程坐标
                                course.posX = div_index;

                                course.posY = tr_index;

                                //一种课程选择情况
                                if (i % 5 == 0) {

                                    course.classNum = ary[i];

                                } else if (i % 5 == 1) {

                                    course.className = ary[i];

                                } else if (i % 5 == 2) {

                                    course.techName = ary[i];

                                } else if (i % 5 == 3) {

                                    course.classTime = ary[i];

                                } else if (i % 5 == 4) {
                                    course.classAddr = ary[i];
                                }

                            }

                            //把课程加入课表
                            courses.add(course);

                        }

                    }

                }
            });

            // Log.d("TAG", String.valueOf(courses.size()));
            //
            // for (Course s : realm.where(Course.class).findAll()) {
            //     // Log.d("TAG", s.className);
            //     String className = s.className;
            //     Log.d("TAG", String.valueOf(className));
            //
            // }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }

        return courses;
    }


    public interface AsyncResponse {
        void processFinish(List<Course> courses);

    }
}

