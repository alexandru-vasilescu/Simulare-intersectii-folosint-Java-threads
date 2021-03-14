package com.apd.tema2.intersections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class PriorityIntersection implements Intersection{
	// Variabila care retine numarul masinilor cu prioritate
	private int numberOfCarsWithPriority;
	// Variabila care retine numarul masinilor fara prioritate
	private int numberOfCarsWithoutPriority;
	// Variabila sincronizata care retine cate masini sunt intersectie
	private AtomicInteger carsInIntersection = new AtomicInteger(0);
	// Coada masinilor care asteapta sa intre in intersectie
	private ArrayBlockingQueue<Integer> carsWaiting = new ArrayBlockingQueue<>(Main.carsNo);
	// Getter si Setter pentru variabile
	public ArrayBlockingQueue<Integer> getCarsWaiting() {
		return carsWaiting;
	}
	public AtomicInteger getCarsInIntersection() {
		return carsInIntersection;
	}
	public void setCarsInIntersection(AtomicInteger carsInIntersection) {
		this.carsInIntersection = carsInIntersection;
	}
	public int getNumberOfCarsWithPriority() {
		return numberOfCarsWithPriority;
	}
	public void setNumberOfCarsWithPriority(int numberOfCarsWithPriority) {
		this.numberOfCarsWithPriority = numberOfCarsWithPriority;
	}
	public int getNumberOfCarsWithoutPriority() {
		return numberOfCarsWithoutPriority;
	}
	public void setNumberOfCarsWithoutPriority(int numberOfCarsWithoutPriority) {
		this.numberOfCarsWithoutPriority = numberOfCarsWithoutPriority;
	}
	// Metoda care adauga o masina in coada
	public void addCar(int iD) {
		carsWaiting.add(iD);
	}
	// Metoda sincronizata care afiseaza prima masina din coada si o elimina din coada
	public synchronized void enterIntersection(int iD) {
    	System.out.println("Car " + carsWaiting.poll() + " with low priority has entered the intersection");
	}
}
