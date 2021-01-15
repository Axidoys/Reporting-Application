package com.company;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventMachine {

    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd");

    String machineID;
    String machineType;
    Calendar date;
    String errorType;

    EventMachine(String s){


        JSONParser parser = new JSONParser();

        try{
            JSONObject obj = (JSONObject) parser.parse(s);

            machineID = (String) obj.get("machineID");
            machineType = (String) obj.get("machineType");
            errorType = (String) obj.get("errorType");

            JSONObject dateTime = (JSONObject) obj.get("dateTime");
            date = Calendar.getInstance();
            date.set(((Long)dateTime.get("year")).intValue(),
                    ((Long)((Long)dateTime.get("monthValue")-1)).intValue(),
                    ((Long)dateTime.get("dayOfMonth")).intValue()
            );


        }catch(ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
    }

    public String toString(){
        return "machineID{" + machineID + "} machineType{" + machineType + "} errorType{" + errorType
                + "} date{" + ISO8601.format(date.getTime()) + "}";
    }

    public String serialize(){ //return Json
        return "{"
                +"\"machineID\":\"" + machineID + "\""
                + "},\"machineType\":\"" + machineType + "\""
                + "},\"errorType\":\"" + errorType + "\""
                + "},\"date\":\"" + ISO8601.format(date.getTime()) + "\""
                + "}";
    }

}
