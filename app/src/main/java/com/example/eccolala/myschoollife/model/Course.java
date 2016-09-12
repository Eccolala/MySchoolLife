package com.example.eccolala.myschoollife.model;

import io.realm.RealmObject;

/**
 * Created by eccolala on 16-9-11.
 */

public class Course extends RealmObject{
    public String classNum;
    public String className;
    public String techName;
    //上课周次
    public String classTime;
    public String classAddr;
    //课程坐标
    public int posX;
    public int posY;
}
