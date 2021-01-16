package com.company;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataProcessing extends Index{



    HashMap<String, Integer> machineCount = new HashMap<String, Integer>();
    HashMap<String, Integer> errorCount = new HashMap<String, Integer>();
    HashMap<String, Integer> machineErrorCount = new HashMap<String, Integer>();
    //Order

    DataProcessing(String wsPath) {
        super(wsPath);
    }

    /*public void Parse(Function readLine()){

    }*/

    public void listInit() throws IOException {
        /*Parse();*/
        for(int i = 0; i<dailyFiles.length ; i++){
            if(dailyFiles[i]!=null) {

                //Read it
                FileReader fileReader = new FileReader(dailyFiles[i]);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                do {
                    //process each line

                    try {

                        EventMachine em = new EventMachine(line, i);//force date

                        //si machine connnu, rien ; sinon on l'ajoute (hashmap)
                        machineCount.putIfAbsent(em.machineID, 0);
                        //idem error
                        errorCount.putIfAbsent(em.errorType, 0);

                    } catch (ParseException ignored) {

                    }

                    line = bufferedReader.readLine(); //for next read if not null
                } while(line != null);


            }
        }
    }

    public LinkedList<String> getListMachine(){ //automatically sorted
        //COULD use optimized sort with iterable

        //get List
        LinkedList<String> ret = new LinkedList<>();
        Set keys = machineCount.keySet();
        for (Object key : keys) {
            ret.add((String)key);
        }

        //sort it
        Collections.sort(ret);

        return ret;
    }

    public List<String> getListError(){ //automatically sorted
        //COULD use optimized sort with iterable

        //get List
        LinkedList<String> ret = new LinkedList<>();
        Set keys = errorCount.keySet();
        for (Object key : keys) {
            ret.add((String)key);
        }

        //sort it
        Collections.sort(ret);

        return ret;
    }

    /*public void GenerateReport(FilterEvent fe){

    }*/

}
