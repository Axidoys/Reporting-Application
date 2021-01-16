package com.company;

import java.io.IOException;
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
            else if (args[0].equals("listWS")) {
                listWS(args);
            }else if (args[0].equals("test")) {
                test(args);
            }else if (args[0].equals("data")) {
                data(args);
            }else {
                System.out.println("Wrong argument, use ...TODO LIST ARGUMENT");
            }
        }else{
            System.out.println("No argument");
        }

    }

    public static void indexing(String[] args) {
        //args should contain : Workspace path ; Raw log file (which can be realtime input)
        if(args.length==3) {

            // Verify existence of workspace
            Path wsPath;
            wsPath = Paths.get(args[1]); //TODO check syntax try and catch

            if (!Files.isDirectory(wsPath)) {
                System.out.println("Workspace directory does not exist");
                java.lang.System.exit(1);
            }

            // Verify existence of log file?
            Path logPath;
            logPath = Paths.get(args[2]);
            if (!Files.exists(logPath) && !args[2].equals("STDIN")) {
                System.out.println("log file " + args[2] + " does not exist");
                java.lang.System.exit(1);
            }

            //Instantiate indexing
            Archive archive = new Archive(args[1]);

            //Index it
            if(args[2].equals("STDIN")){
                archive.logStdRead();
            }
            else{
                archive.logFileRead(args[2]);
            }
        }
        else {
            System.out.println("index <Workspace path> <Log file>");
        }

    }

    public static void listWS(String[] args) {
        if(args.length==2) {

            // Verify existence of workspace
            Path wsPath;
            wsPath = Paths.get(args[1]); //TODO check syntax try and catch

            if (!Files.isDirectory(wsPath)) {
                System.out.println("Workspace directory does not exist");
                java.lang.System.exit(1);
            }

            //Instantiate indexing
            Index index = new Index(args[1]);

            System.out.println("Listing directory...");
            index.listArchivedFiles();
        }
        else {
            System.out.println("testWS <Workspace path>");
        }

    }

    public static void test(String[] args) { //test over DataProcessing class
        if(args.length==2) {

            // Verify existence of workspace
            Path wsPath;
            wsPath = Paths.get(args[1]); //TODO check syntax try and catch

            if (!Files.isDirectory(wsPath)) {
                System.out.println("Workspace directory does not exist");
                java.lang.System.exit(1);
            }

            //Instantiate indexing
            DataProcessing index = new DataProcessing(args[1]);
            try {
                index.listInit();

                System.out.println("-- Listing machines");
                System.out.println(index.getListMachine());

                System.out.println("-- Listing errors");
                System.out.println(index.getListError());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            System.out.println("test <Workspace path>");
        }

    }

    public static void data(String[] args) {
        if(args.length==2) {

            // Verify existence of workspace
            Path wsPath;
            wsPath = Paths.get(args[1]); //TODO check syntax try and catch

            if (!Files.isDirectory(wsPath)) {
                System.out.println("Workspace directory does not exist");
                java.lang.System.exit(1);
            }

            //Instantiate indexing
            DataProcessing index = new DataProcessing(args[1]);

            //DO THINGS
        }
        else {
            System.out.println("testWS <Workspace path>");
        }

    }

}
