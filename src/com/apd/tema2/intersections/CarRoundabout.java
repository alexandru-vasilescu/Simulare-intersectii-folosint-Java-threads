package com.apd.tema2.intersections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;


public class CarRoundabout implements Intersection{
	// Numarul de sensuri de mers
	private int numberOfLanes;
	// Timpul de asteptare in intersectie
	private int waitingTime;
	// Numarul maxim de masini de pe ficare sens de mers
	private int numberOfCars;
	// Lista de semafoare pentru fiecare sens de mers
	private List<Semaphore> semaphores;
	// Lista pentru fiecare sens de mers cu liste de masini 
	private List<List<Integer>> lanes;
	// Bariera pentru toate masinile care intra in intersectie
	public static CyclicBarrier barrier = new CyclicBarrier(Main.carsNo);
	// Bariera pentru masinile care sunt in intersectie la un moment de timp
	public static CyclicBarrier innerBarrier;
	
	//Getteri si Setteri pentru fiecare variabila
	// Setare lista de semafoare si initializare semafoare
	public void setSemaphores() {
		semaphores = Collections.synchronizedList(new ArrayList<>());
		for(int i=0;i<numberOfLanes;i++) {
			semaphores.add(new Semaphore(numberOfCars));
		}
	}
	
	public List<Semaphore> getSemaphores() {
		return semaphores;
	}
	public int getNumberOfCars() {
		return numberOfCars;
	}
	public void setNumberOfCars(int numberOfCars) {
		this.numberOfCars = numberOfCars;
	}
	// Initializare a listelor pentru sensurile de mers.
	public void setLanes() {
		lanes = Collections.synchronizedList(new ArrayList<>());
		for(int i=0;i<numberOfLanes;i++) {
			lanes.add(Collections.synchronizedList(new ArrayList<>()));
		}
	}
	public List<List<Integer>> getLanes() {
		return lanes;
	}
	public int getNumberOfLanes() {
		return numberOfLanes;
	}
	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}
	public int getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	//Metoda folosita pentru a adaugta o masina la un sens de mers
	public void addCar(int iD, int lane) {
		lanes.get(lane).add(iD);
	}
	//Metoda folosita pentru a scoate o masina de pe un sens de mers
	public void removeCar(int iD,int lane) {
		for(int i=0; i<lanes.get(lane).size();i++)
			if(lanes.get(lane).get(i) == iD)
				lanes.get(lane).remove(i);
	}
}
