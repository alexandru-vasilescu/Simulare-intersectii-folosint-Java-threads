package com.apd.tema2.entities;

import com.apd.tema2.Main;
import com.apd.tema2.intersections.Crosswalk;
import com.apd.tema2.utils.Constants;

import static java.lang.Thread.sleep;

/**
 * Clasa utilizata pentru gestionarea oamenilor care se strang la trecerea de pietoni.
 */
public class Pedestrians implements Runnable {
    private int pedestriansNo = 0;
    private int maxPedestriansNo;
    private boolean pass = false;
    private boolean finished = false;
    private int executeTime;
    private long startTime;

    public Pedestrians(int executeTime, int maxPedestriansNo) {
        this.startTime = System.currentTimeMillis();
        this.executeTime = executeTime;
        this.maxPedestriansNo = maxPedestriansNo;
    }

    @Override
    public void run() {
        while(System.currentTimeMillis() - startTime < executeTime) {
            try {
                pedestriansNo++;
                sleep(Constants.PEDESTRIAN_COUNTER_TIME);

                if(pedestriansNo == maxPedestriansNo) {
                    pedestriansNo = 0;
                    // Modific isGreen cand semaforul e rosu si inversez vectorul printed pentru a afisa mesajele
                    ((Crosswalk)Main.intersection).setGreen(false);
                    ((Crosswalk)Main.intersection).invertPrinted();
                    pass = true;
                    sleep(Constants.PEDESTRIAN_PASSING_TIME);
                    pass = false;
                    // Modific isGreen cand semaforul e verde si inversez vectorul printed pentru a afisa mesajele
                    ((Crosswalk)Main.intersection).setGreen(true);
                    ((Crosswalk)Main.intersection).invertPrinted();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        finished = true;
    }

    public boolean isPass() {
        return pass;
    }

    public boolean isFinished() {
        return finished;
    }
}
