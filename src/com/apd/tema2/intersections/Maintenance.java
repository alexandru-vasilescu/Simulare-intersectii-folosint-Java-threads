package com.apd.tema2.intersections;


import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import com.apd.tema2.entities.Intersection;

public class Maintenance implements Intersection{
	//Variabila care retine cate masini pot trece o data
	private int x;
	// Semafor care indica daca masinile de pe sensul 1 pot merge
	private Semaphore semaphoreForOne = new Semaphore(0);
	// Semafor care indica daca masinile de pe sensul 0 pot merge
	private Semaphore semaphoreForZero;
	// Bariera initializata cu numarul de masini care pot trece o data
	public static CyclicBarrier barrier;
	// Getteri si Setteri
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
		semaphoreForZero = new Semaphore(x);
	}
	public Semaphore getSemaphoreForZero() {
		return semaphoreForZero;
	}
	public Semaphore getSemaphoreForOne() {
		return semaphoreForOne;
	}
	
	
}
