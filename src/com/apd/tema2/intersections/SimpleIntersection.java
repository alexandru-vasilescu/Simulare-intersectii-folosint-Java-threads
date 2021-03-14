package com.apd.tema2.intersections;

import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class SimpleIntersection implements Intersection {
    // Define your variables here.
	// Variabila care retine numarul maxim de masini din intersectie la un moment de timp
	private int maxNumberOfCars;
	// Variabila care retine timpul pe care o masina trebuie sa il astepte inainte de a iesi din intersectie
	private int waitingTime;
	// Variabila care retine un semafor pentru a nu lasa alte masini sa intre in intersectie daca sunt prea multe
	private Semaphore semaphore;
	//Getteri si Setteri pentru toate varibilele
	public Semaphore getSemaphore() {
		return semaphore;
	}
	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}
	public int getMaxNumberOfCars() {
		return maxNumberOfCars;
	}
	public void setMaxNumberOfCars(int maxNumberOfCars) {
		this.maxNumberOfCars = maxNumberOfCars;
	}
	public int getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
}
