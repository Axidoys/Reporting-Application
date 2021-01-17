package com.company;

import java.util.Arrays;

public class DataFilter {

    //FT
    int fTime;
    boolean isEnable_fTime(){
        return fTime != Integer.MAX_VALUE;
    }
    //FM
    String[] fMachine;
    boolean isEnable_fMachine(){
        return fMachine !=null;
    }
    //FpM
    boolean fPerMachine;
    //FE
    String[] fError;
    boolean isEnable_fError(){
        return fError !=null;
    }
    //FP
    int fNEntries;
    boolean isEnable_fNEntries(){
        return fNEntries!= Integer.MAX_VALUE;
    }


    DataFilter(String ft, String fm, String fpm, String fe, String fp){
        //assume args are formatted and positive

        if(ft.equals("*")){
            fTime = Integer.MAX_VALUE;
        }else{
            fTime = Integer.parseInt(ft);
        }

        if(fm.equals("*")){
            fMachine = null; //flag for "all machines"
        }else{
            fMachine = fm.split(",");
        }

        fPerMachine = fpm.equals("true");

        if(fe.equals("*")){
            fError = null; //flag for "all machines"
        }else{
            fError = fe.split(",");
        }

        if(fp.equals("*")){
            fNEntries = Integer.MAX_VALUE; //flag for "all machines"
        }else{
            fNEntries = Integer.parseInt(fp);
        }

    }

    //okdate used in parsing, so it is a bit diff

    public boolean okMachine(EventMachine em){
        if(!isEnable_fMachine())
            return true;
        return Arrays.asList(fMachine).contains(em.machineID);
    }

    public boolean okError(EventMachine em){
        if(!isEnable_fError())
            return true;
        return Arrays.asList(fError).contains(em.errorType);
    }

    public String toString(){
        String ret = "";

        if(isEnable_fTime()){
            ret += "FT enable, set to : " + fTime + "\n";
        }else{
            ret += "FT disable\n";
        }

        if(isEnable_fMachine()){
            ret += "FM enable, machines considered : " + Arrays.toString(fMachine) + "\n";
        }else{
            ret += "FM disable\n";
        }

        if(fPerMachine){
            ret += "FpM enable\n";
        }else{
            ret += "FpM disable\n";
        }

        if(isEnable_fError()){
            ret += "FE enable, set to : " + Arrays.toString(fError) + "\n";
        }else{
            ret += "FE disable\n";
        }

        if(isEnable_fNEntries()){
            if(fNEntries>1){
                ret += "FP enable, set to show " + fNEntries + " entries\n";
            }else{
                ret += "FP enable, set to show " + fNEntries + " entry\n";
            }
        }else{
            ret += "FP disable\n";
        }

        return ret;
    }

}
