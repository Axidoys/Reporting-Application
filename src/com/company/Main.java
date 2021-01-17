package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        if(args.length==7) {

            // Verify existence of workspace
            Path wsPath;
            wsPath = Paths.get(args[1]); //TODO check syntax try and catch

            if (!Files.isDirectory(wsPath)) {
                System.out.println("Workspace directory does not exist");
                java.lang.System.exit(1);
            }

            //Format of FT
            if(!args[2].equals("*")){
                //noinspection ResultOfMethodCallIgnored
                try {
                    Integer.parseInt(args[2]);
                }
                catch (NumberFormatException e){
                    System.out.println("Error in argument FT");
                    java.lang.System.exit(1);
                }
            }
            if(args[2].startsWith("-")){
                System.out.println("FT MUST be positive or null");
                java.lang.System.exit(1);
            }

            //note : FM cannot be misformatted

            //Format of Fpm
            if(!args[4].equals("true") && !args[4].equals("false")){
                System.out.println("Error in argument FpM");
                java.lang.System.exit(1);
            }

            //note : FE cannot be misformatted

            //Format of FP
            if(!args[6].equals("*")){
                //noinspection ResultOfMethodCallIgnored
                try {
                    Integer.parseInt(args[6]);
                }
                catch (NumberFormatException e){
                    System.out.println("Error in argument FT");
                    java.lang.System.exit(1);
                }
            }
            if(args[6].startsWith("-")){
                System.out.println("FP MUST be positive");
                java.lang.System.exit(1);
            }

            //Instantiate DataFilter
            DataFilter filter = new DataFilter(args[2], args[3], args[4], args[5], args[6]);

            //Instantiate indexing
            DataProcessing index = new DataProcessing(args[1]);

            // processing
            try {
                index.GenerateReport(filter);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // show report CLI
            ArrayList<PairEntry> report = index.getReport(filter, false);
            System.out.format("%30s    %s\n", "Machine/Error", "Count");
            System.out.println("--------------------------------------------------");
            for(int i = 0; i<report.size();i++){
                System.out.format("%30s    %d\n", report.get(i).getL(), report.get(i).getR());
            }
        }
        else {
            System.out.println("data <Workspace path> <FT> <FM> <FpM> <FE> <FP>");
            System.out.println("\tFT : Time filter, days to take in account, >=0, <90, '*' for max");
            System.out.println("\tFM : Machine filter, list of machines separate with comma, '*' if no filter");
            System.out.println("\tFpM : Result per machine, true or false");
            System.out.println("\tFE : Error filter, list of error separate with comma, '*' if no filter");
            System.out.println("\tFP : Number of table entry, >0, '*' if no limit");
        }

    }

}
