package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Index {

    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar today = Calendar.getInstance();

    private File[] dailyFiles;

    Index(String wsPath){
        // List files, clean old files (don't touch to alien-files), store 90 last days file paths
        java.io.File wsFile = new File(wsPath);
        File[] files = wsFile.listFiles();
        dailyFiles = new File[90]; //size of 90, direct access to name of day => index=0 is today

        if (files != null) {
            for(File file : files){
                String name = file.getName();
                //SEARCH FOR ALL the files that are yyyy-mm-dd.txt
                if(name.length()!=6) { // "yyyy-mm-dd.txt" is 14-length
                    if(name.toLowerCase().endsWith(".txt")) {
                        Calendar date = Calendar.getInstance();
                        Date d;
                        try {
                            d = ISO8601.parse(name);

                            if(d!=null) {
                                date.setTime(d);
                                int daysBetween = (int) ChronoUnit.DAYS.between(date.toInstant(), today.toInstant());
                                if (daysBetween < 90) {
                                    //save it to the array daily
                                    dailyFiles[daysBetween] = file;
                                }else{
                                    //if it is too old, remove it
                                    if(!file.delete()){
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


        //TODO open dailyFiles as writable
    }


    public void logFileRead(String logPath){
        // Read the content from file
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(logPath))) {
            String line = "";
            do {

                processLine(line);

                line = bufferedReader.readLine(); //for next read if not null
            } while(line != null);

            //remove file
            //try and catch HERE !!

        } catch (IOException e) {
            System.out.println("Error during the read of the log file");
            java.lang.System.exit(1);
        }
    }


    @SuppressWarnings("InfiniteLoopStatement")
    public void logStdRead(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String myString = scanner.nextLine();

            processLine(myString);
        }
        //scanner.close();
    }


    private void processLine(String line){
        if(line.startsWith("INFO: send message:")){//it is a message
            line = line.substring(19);

            EventMachine em = new EventMachine(line);

            //index.addEvent(em);

            System.out.println(em);
        }else{
            //TODO maybe catch errors from server communication ?
        }
    }


}
