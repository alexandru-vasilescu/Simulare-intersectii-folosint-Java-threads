package com.apd.tema2.intersections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class Railroad implements Intersection{
	//Bariera initializata cu numarul total de masini
	public static CyclicBarrier barrier;
	//Lista de cozi cu sensurile de mers
	private List<ArrayBlockingQueue<Integer>> lanes;
	//Getter si Setter
	public void setLanes() {
		lanes = Collections.synchronizedList(new ArrayList<>());
	}
	public void addLanes() {
		lanes.add(new ArrayBlockingQueue<>(Main.carsNo));
	}
	public List<ArrayBlockingQueue<Integer>> getLanes() {
		return lanes;
	}
	//Adaugarea unei masini in coada respectiva
	public void addCar(int lane,int iD) {
		lanes.get(lane).add(iD);
	}
	//Afisare mesaj ca s-a trecut de intersectie si eliminarea masinii din coada
	public synchronized void getFirstCar(int lane) {
		System.out.println("Car " + lanes.get(lane).poll() + " from side number " + lane + " has started driving");
	}
}
