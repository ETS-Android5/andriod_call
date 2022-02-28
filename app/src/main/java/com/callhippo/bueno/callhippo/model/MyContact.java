package com.callhippo.bueno.callhippo.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP on 11-09-2020.
 */

public class MyContact {

    public String id;
    public  String name;
    public ArrayList<String> numbers;
   public HashMap<String ,String> rawContactIdMap;

   public String num;

    public MyContact(String num) {
        this.num = num;
    }

    public MyContact(){

    }

    public MyContact(String name, String num) {
        this.name = name;
        this.num = num;
    }

    public MyContact(String id, String name,  String num) {
        this.id = id;
        this.name = name;
        this.num = num;
    }

    public MyContact(String id, String name, HashMap<String, String> rawContactIdMap){
        this.id = id;
        this.name = name;
        this.rawContactIdMap = rawContactIdMap;
    }
    public MyContact(String id, String name, ArrayList<String> numbers, HashMap<String, String> rawContactIdMap) {
        this.id = id;
        this.name = name;
        this.numbers = numbers;
        this.rawContactIdMap = rawContactIdMap;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public HashMap<String, String> getRawContactIdMap() {
        return rawContactIdMap;
    }

    public void setRawContactIdMap(HashMap<String, String> rawContactIdMap) {
        this.rawContactIdMap = rawContactIdMap;
    }
}
