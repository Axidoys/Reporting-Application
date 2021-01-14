package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Main {

    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar today = Calendar.getInstance();


    public static void main(String[] args) {

        //EventMachine em = new EventMachine("line");
        //System.out.println(em);


        if(args.length>0) {

            //Index raw log files
            if (args[0].equals("index")) {
                indexing(args);
            }
        }else{
            System.out.println("No argument");
        }

    }

    public static void indexing(String[] args) {
        //args should contain : Workspace path ; Raw log file (which can be realtime input) //TODO realtime input
        if(args.length==3){

            // Verify existence of workspace
            Path wsPath;
            wsPath = Paths.get(args[1]); //TODO check syntax try and catch

            if ( ! Files.isDirectory(wsPath) ) {
                System.out.println("Workspace directory does not exist");
                java.lang.System.exit(1);
            }
            // Verify existence of log file?
            Path logPath ;
            logPath = Paths.get(args[2]);

            if ( ! Files.exists(logPath) ) {
                System.out.println("Log file does not exist");
                java.lang.System.exit(1);
            }
            // List files, clean old files (don't touch to alien-files), store 90 last days file paths
            java.io.File wsFile = new File(args[1]);
            File[] files = wsFile.listFiles();
            File[] dailyFiles = new File[90]; //size of 90, direct access to name of day => index=0 is today

            if (files != null) {
                for(File file : files){
                    String name = file.getName();
                    System.out.println(file.getName() + file.isFile());
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


            // Read the content from file
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(args[2]))) {
                String line = "";
                do {
                    if(line.startsWith("INFO: send message:")){//it is a message
                        line = line.substring(19);

                        EventMachine em = new EventMachine(line);

                        System.out.println(em);

                    }

                    line = bufferedReader.readLine(); //for next read if not null
                } while(line != null);
            } catch (IOException e) {
                System.out.println("Error during the read of the log file");
                java.lang.System.exit(1);
            }

        }
        else{
            System.out.println("index <Workspace path> <Log file>");
        }
    }

}
