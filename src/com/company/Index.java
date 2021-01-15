package com.company;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Index {

    static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd");
    static final Calendar today = Calendar.getInstance();

    String wsPath;

    File[] dailyFiles;

    Index(String wsPath){
        this.wsPath = wsPath;

        // List files, clean old files (don't touch to alien-files), store 90 last days file paths
        java.io.File wsFile = new File(wsPath);
        File[] files = wsFile.listFiles();
        dailyFiles = new File[90]; //size of 90, direct access to name of day => index=0 is today

        if (files != null) {
            for(File file : files){
                String name = file.getName();
                //SEARCH FOR ALL the files that are yyyy-mm-dd.txt
                if(!file.isDirectory()) {
                    if (name.length() != 6) { // "yyyy-mm-dd.txt" is 14-length
                        if (name.toLowerCase().endsWith(".txt")) {
                            Calendar date = Calendar.getInstance();
                            Date d;
                            try {
                                d = ISO8601.parse(name);

                                if (d != null) {
                                    date.setTime(d);
                                    int daysBetween = (int) ChronoUnit.DAYS.between(date.toInstant(), today.toInstant());
                                    if (daysBetween < 90) {
                                        //save it to the array daily
                                        dailyFiles[daysBetween] = file;
                                    } else {
                                        //if it is too old, remove it
                                        if (!file.delete()) {
                                            System.out.println("Warning, file " + name + " cannot be delete");
                                        }
                                    }
                                }

                            } catch (ParseException e) {
                                //not a date
                            }
                        }
                    }
                }
            }
        }


    }

    public void listArchivedFiles(){
        for(int i = 0; i<dailyFiles.length ; i++){
            if(dailyFiles[i]!=null){
                System.out.println("File n'" + i + " is present (" + dailyFiles[i].getName() + ")");
            }
        }
    }

}
