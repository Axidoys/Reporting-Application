package com.company;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Index {

    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar today = Calendar.getInstance();

    private String wsPath;

    private File[] dailyFiles;
    private FileWriter[] dailyFilesW;
    private BufferedWriter[] dailyFilesBW; //here because cannot be opened after if has been closed

    Index(String wsPath){
        this.wsPath = wsPath;

        // List files, clean old files (don't touch to alien-files), store 90 last days file paths
        java.io.File wsFile = new File(wsPath);
        File[] files = wsFile.listFiles();
        dailyFiles = new File[90]; //size of 90, direct access to name of day => index=0 is today
        dailyFilesW = new FileWriter[90]; //opened only if write is needed
        dailyFilesBW = new BufferedWriter[90];

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


    }

    public void close(){
        // always close the file
        //FILES closes
        try {
            for(int i = 0; i<dailyFilesBW.length ; i++){
                if(dailyFilesBW[i]!=null) {
                    dailyFilesBW[i].close();
                }
                dailyFilesBW[i]=null; //cannot use foreach because of this statement
            }
            for(int i = 0; i<dailyFilesW.length ; i++){
                if(dailyFilesW[i]!=null) {
                    dailyFilesW[i].close();
                }
                dailyFilesW[i]=null;
            }
        } catch (IOException ignored) {}
    }

    public void logFileRead(String logPath){
        // Read the content from file
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(logPath))) {
            String line = "";
            do {

                processLine(line);

                line = bufferedReader.readLine(); //for next read if not null
            } while(line != null);

            close();
            //TODO remove log file

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

            close(); //don't keep handle on files and flush buffer
        }
        //scanner.close();
    }

    private void processLine(String line){
        if(line.startsWith("INFO: send message:")){//it is a message
            line = line.substring(19);

            EventMachine em = new EventMachine(line);

            try {
                recordEvent(em);
            } catch (Exception e) {
                System.out.println("Error while processing " + em);
                System.out.println("Program shut down");
                e.printStackTrace();
                java.lang.System.exit(1);
            }

            System.out.println(em);
        }else{
            //TODO maybe catch errors from server communication ?
        }
    }


    private void recordEvent(EventMachine em) throws Exception {
        System.out.println("Record");
        //verify that all the fields of em are filled
        //get em index (by 90 days)
        int index = (int) ChronoUnit.DAYS.between(em.date.toInstant(), today.toInstant());
        if(index>=90){
            return; // too old event
        }
        index ++; //TODO should not be here !!
        System.out.println(index);
        if(dailyFiles[index]==null){
            dailyFiles[index] = new File(wsPath + "\\" + ISO8601.format(em.date.getTime()) + ".txt");
            if(!dailyFiles[index].createNewFile())
                throw new Exception("Cannot create index file");
        }

        if(dailyFilesW[index]==null){
            dailyFilesW[index] = new FileWriter(dailyFiles[index], true);
        }

        if(dailyFilesBW[index]==null){ //should never be different of dailyFilesW
            dailyFilesBW[index] = new BufferedWriter(dailyFilesW[index]);
        }


        dailyFilesBW[index].newLine();
        dailyFilesBW[index].write(em.serialize());


    }


}
