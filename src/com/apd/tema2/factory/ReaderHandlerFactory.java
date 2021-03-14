package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                	// Am initializat intersectia ca un simpleIntersection desi nu am folosit-o la rezolvare
                	 Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // To parse input line use:
                    String[] line = br.readLine().split(" ");
                    // Intersectie de tip simpleIntersection
                    Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");
                    // Setarea variabilelor intersectiei
                    ((SimpleIntersection)Main.intersection).setSemaphore(new Semaphore(Integer.parseInt(line[0])));
                    ((SimpleIntersection)Main.intersection).setMaxNumberOfCars(Integer.parseInt(line[0]));
                    ((SimpleIntersection)Main.intersection).setWaitingTime(Integer.parseInt(line[1]));
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                	// Intersectie de tip CarRoundabout
                    Main.intersection = IntersectionFactory.getIntersection("carRoundabout");
                    // Setare varibilele intersectiei
                    ((CarRoundabout)Main.intersection).setNumberOfLanes(Integer.parseInt(line[0]));
                    ((CarRoundabout)Main.intersection).setWaitingTime(Integer.parseInt(line[1]));
                    ((CarRoundabout)Main.intersection).setNumberOfCars(1);
                    ((CarRoundabout)Main.intersection).setSemaphores();
                    ((CarRoundabout)Main.intersection).setLanes();
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                	// Intersectie de tip CarRoundabout
                    Main.intersection = IntersectionFactory.getIntersection("carRoundabout");
                    // Setare variabilele intersectiei 
                    ((CarRoundabout)Main.intersection).setNumberOfLanes(Integer.parseInt(line[0]));
                    ((CarRoundabout)Main.intersection).setWaitingTime(Integer.parseInt(line[1]));
                    ((CarRoundabout)Main.intersection).setNumberOfCars(Integer.parseInt(line[2]));
                    ((CarRoundabout)Main.intersection).setSemaphores();
                    ((CarRoundabout)Main.intersection).setLanes();
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                	// Intersectie de tip CarRoundabout
                    Main.intersection = IntersectionFactory.getIntersection("carRoundabout");
                    // Setare variabilele intersectiei 
                    ((CarRoundabout)Main.intersection).setNumberOfLanes(Integer.parseInt(line[0]));
                    ((CarRoundabout)Main.intersection).setWaitingTime(Integer.parseInt(line[1]));
                    ((CarRoundabout)Main.intersection).setNumberOfCars(Integer.parseInt(line[2]));
                    ((CarRoundabout)Main.intersection).setSemaphores();
                    ((CarRoundabout)Main.intersection).setLanes();
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                	//Intersectie de tip PriorityIntersection
                    Main.intersection = IntersectionFactory.getIntersection("priority");
                    // Setare variabilele intersectiei
                    ((PriorityIntersection)Main.intersection).setNumberOfCarsWithPriority(Integer.parseInt(line[0]));
                    ((PriorityIntersection)Main.intersection).setNumberOfCarsWithoutPriority(Integer.parseInt(line[1]));
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String[] line = br.readLine().split(" ");
                	//Se instantiaza pedestrians din Main cu parametrii primiti la input
                	int executeTime = Integer.parseInt(line[0]);
                	int maxPedestriansNo = Integer.parseInt(line[1]);
                	Main.pedestrians = new Pedestrians(executeTime, maxPedestriansNo);
                	// Intersectie de tip Crosswalk
                	Main.intersection = IntersectionFactory.getIntersection("crosswalk");
                	// Se seteaza vectorul de printed
                	((Crosswalk)Main.intersection).setPrinted();
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	String line = br.readLine();
                	//Intersectie de tip Maintenance
                	Main.intersection = IntersectionFactory.getIntersection("maintenance");
                	// Se seteaza variabilele intersesctiei
                	((Maintenance)Main.intersection).setX(Integer.parseInt(line));
                	Maintenance.barrier = new CyclicBarrier(Integer.parseInt(line));
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                	//Intersectie de tip Railroad
                    Main.intersection = IntersectionFactory.getIntersection("railroad");
                    //Se seteaza variabilele intersectiei
                    Railroad.barrier = new CyclicBarrier(Main.carsNo);
                    ((Railroad)Main.intersection).setLanes();
                }
            };
            default -> null;
        };
    }

}
