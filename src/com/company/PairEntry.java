package com.company;

public class PairEntry implements Comparable<PairEntry>{
    private String l;
    private Integer r;
    public PairEntry(String l, Integer r){
        this.l = l;
        this.r = r;
    }
    public String getL(){ return l; }
    public Integer getR(){ return r; }
    public void setL(String l){ this.l = l; }
    public void setR(Integer r){ this.r = r; }

    @Override
    public int compareTo(PairEntry p){
        if (this.getR() > p.getR())
            return 1;
        else if (this.getR() == p.getR())
            return 0;
        else
            return -1;
    }
}