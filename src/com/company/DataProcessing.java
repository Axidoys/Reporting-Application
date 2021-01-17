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

    public enum DataType {MACHINE, ERROR, MACHINE_ERROR}
    DataType focus;

    DataProcessing(String wsPath) {
        super(wsPath);
    }

    ArrayList<PairEntry> report = new ArrayList<>();

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

    public void GenerateReport(DataFilter fe) throws IOException {
        /*Parse();*/

        //Set focus
        //Consider machineCount (one error filtered)
        if(fe.isEnable_fError() && fe.fError.length == 1){
            focus = DataType.MACHINE;
        }
        //Consider errorCount (one machine && fpm disable)
        else if(fe.isEnable_fMachine() && fe.fMachine.length == 1 && !fe.fPerMachine){
            focus = DataType.ERROR;
        }
        //Consider machineErrorCount (>1 machines && >1 errors && fpm enable)
        else{
            focus = DataType.MACHINE_ERROR;
        }

        for(int i = 0; i<dailyFiles.length && i<fe.fTime ; i++){
            if(dailyFiles[i]!=null) {

                //Read it
                FileReader fileReader = new FileReader(dailyFiles[i]);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                do {
                    //process each line

                    try {

                        EventMachine em = new EventMachine(line, i);//force date

                        //USE FILTEREVENT TO FILL DATA : machineCount, errorCount or machineErrorCount
                        addEvent(em, fe);

                    } catch (ParseException ignored) {

                    }

                    line = bufferedReader.readLine(); //for next read if not null
                } while(line != null);


            }
        }

        //Sort value in memory and output it in report linkedlist
        //TODO VERY SLOW AS IT
        if(focus == DataType.MACHINE) {
            ArrayList<String> keys = new ArrayList<String>(machineCount.keySet());
            for(String key : keys){
                report.add(new PairEntry(key, machineCount.get(key)));
            }
        }
        else if(focus == DataType.ERROR) {
            ArrayList<String> keys = new ArrayList<String>(errorCount.keySet());
            for(String key : keys){
                report.add(new PairEntry(key, errorCount.get(key)));
            }
        }
        else /* focus == DataType.MACHINE_ERROR */{
            ArrayList<String> keys = new ArrayList<String>(machineErrorCount.keySet());
            for(String key : keys){
                report.add(new PairEntry(key, machineErrorCount.get(key)));
            }
        }
        //sort low from high values
        Collections.sort(report);

    }

    private void addEvent(EventMachine em, DataFilter fe){ //initialize if not, and increment
        //apply filter
        if(fe.okMachine(em) && fe.okError(em)) {
            //Sort of record depend on focus
            if (focus == DataType.MACHINE) {
                machineCount.merge(em.machineID, 1, Integer::sum);
            } else if (focus == DataType.ERROR) {
                errorCount.merge(em.errorType, 1, Integer::sum);
            } else if (focus == DataType.MACHINE_ERROR) {
                machineErrorCount.merge(em.machineID + ";" + em.errorType, 1, Integer::sum);
            } else {
                System.out.println("focus is not set unexpectedly");
            }
        }
    }

    public ArrayList<PairEntry> getReport(DataFilter fe, boolean ascOrder){
        ArrayList<PairEntry> ret = new ArrayList<>();

        for(int i = 0; i<fe.fNEntries && i<report.size(); i++){
            if(ascOrder) {
                ret.add(report.get(i));
            }else{
                ret.add(report.get(report.size()-i-1));
            }
        }

        return ret;
    }

}
