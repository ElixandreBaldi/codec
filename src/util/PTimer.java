/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author FREE
 */
public class PTimer {
    private String name = "nil";
    private long startTime = 0;
    private long endTime = 0;
    private boolean timerOk = true;
    
    public PTimer(String name){
        this.name = name;
    }
    
    public void startTimer(){
        timerOk = true;
        startTime = System.nanoTime();
    }
    
    public void endTimer(){
        timerOk = false;
        endTime = System.nanoTime();
    }
    
    public long getElapsed(){
        return(endTime-startTime);
    }
    
    public double getElapsedSegundos(){
        return((endTime-startTime+0.00)/(1E9+0.00));
    }
    
    @Override
    public String toString(){
        if (timerOk){
            return("Timer " + name + " ainda em andamento");
        }else{
            double elapsed = (endTime-startTime)/(1E9+0.00);
            return(name+" : "+String.format("%.4f s", elapsed));
        }
    }
}
