package com.apd.tema2.intersections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CyclicBarrier;
import java.util.List;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

public class Crosswalk implements Intersection{
	// Variabila care retine daca e verde sau nu
	private boolean isGreen = true;
	// Lista de booleeni care retine daca o masina printat sau nu
	private List<Boolean> printed;
	// Getter si Setter
	public boolean isGreen() {
		return isGreen;
	}
	public void setGreen(boolean isGreen) {
		this.isGreen = isGreen;
	}
	//Initializez vectorul de printed
	public void setPrinted() {
		printed = Collections.synchronizedList(new ArrayList<>());
		for(int i=0;i<Main.carsNo;i++) {
			printed.add(false);
		}
	}
	// Inversez valorile din vectorul de printed
	public void invertPrinted() {
		for(int i=0;i<Main.carsNo;i++) {
			printed.set(i, !printed.get(i));
		}
	}
	public List<Boolean> getPrinted(){
		return printed;
	}
}
