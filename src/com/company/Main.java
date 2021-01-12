package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {


        if(args.length>0) {

            //Index raw log files
            if (args[0].equals("index")) {
                indexing(args);
            }
        }else{
            System.out.println("No argument");
        }

    }

    public static void indexing(String[] args){
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
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.toUpperCase().endsWith(".TXT");//TODO check isFile() (does not work)
                }
            };
            File[] files = wsFile.listFiles(filter);

            for(File file : files){
                System.out.println(file.getName() + file.isFile());
            }

           //String[] daylyFiles;//TODO Array for the 90 last days
            //TODO Remove old days


            // Read the content from file
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]))) {
                String line = bufferedReader.readLine();
                while(line != null) {
                    System.out.println(line);
                    line = bufferedReader.readLine();
                }
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
