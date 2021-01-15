package com.company;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Archive extends Index {

    private FileWriter[] dailyFilesW;
    private BufferedWriter[] dailyFilesBW; //here because cannot be opened after if has been closed

    Archive(String wsPath) {
        super(wsPath);

        dailyFilesW = new FileWriter[90]; //opened only if write is needed
        dailyFilesBW = new BufferedWriter[90];
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

            bufferedReader.close();
            close();
            new File(logPath).delete();

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

        }else{
            //TODO maybe catch errors from server communication ?
        }
    }


    private void recordEvent(EventMachine em) throws Exception {
        //verify that all the fields of em are filled
        //get em index (by 90 days)
        int index = (int) ChronoUnit.DAYS.between(em.date.toInstant(), today.toInstant()) + 1;
        if(index>=90){
            return; // too old event
        }

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
