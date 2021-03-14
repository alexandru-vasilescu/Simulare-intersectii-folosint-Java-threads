package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import static java.lang.Thread.sleep;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	try {
                		// Afiseaza cand ajunge in intersectie
                		System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                		// Asteapta waitingTime
						sleep(car.getWaitingTime());
						// Afiseaza ca a parasit intersectia
						System.out.println("Car "+ car.getId() + " has waited enough, now driving...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	// Retin intersectia intr-o variabila locala pentru usurinta
                	SimpleIntersection si = (SimpleIntersection)Main.intersection;
                	// Afisare mesaj cand o masina ajunge la intersectie
                	System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                	try {
                		// Verificare daca se poate intra in intersectie
						si.getSemaphore().acquire();
						// Afisare mesaj ca se intra in intersectie
						System.out.println("Car " + car.getId() + " has entered the roundabout");
						// Asteptare pe waitingTime al intersectiei
						sleep(si.getWaitingTime());
						// Afisare mesaj ca masina iese din intersectie
						System.out.println("Car " + car.getId() + " has exited the roundabout after " 
											+ si.getWaitingTime()/1000 + " seconds");
						// Instiintare semafor ca masina a parasit intersectia
						si.getSemaphore().release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	// Intersectie de tip CarRoundabout care retine intersectia din main
                	CarRoundabout cr = (CarRoundabout)Main.intersection;
                	// Afisare mesaj ca s-a ajuns la intersectie
                	System.out.println("Car " + car.getId() + " has reached the roundabout");
                	// Adaugare masina la sensul de mers corespunzator
                	cr.addCar(car.getId(), car.getStartDirection());
                	// Verificare daca toate sensurile de mers au cel putin o masina
                	for(int i=0;i<cr.getNumberOfLanes();i++) {
                		while(cr.getLanes().get(i).isEmpty());
                	}
                	try {
                		// Verificare daca se poate intra in intersectie
						cr.getSemaphores().get(car.getStartDirection()).acquire();
						// Afisare mesaj ca s-a intrat in intersectie
						System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
													+ car.getStartDirection());
						// Eliminare masina de pe sensul ei initial de mers
						cr.removeCar(car.getId(), car.getStartDirection());
						// Asteptare timp necesar pentru a parasi intersectia
						sleep(cr.getWaitingTime());
						// Afisare mesaj ca s-a parasit intersectia
						System.out.println("Car " + car.getId() + " has exited the roundabout after " 
													+ cr.getWaitingTime()/1000 + " seconds");
						// Anuntare semafor ca o masina a parasit intersectia
						cr.getSemaphores().get(car.getStartDirection()).release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	// Intersectie de tip CarRoundabout initializata cu intersectia din Main
                	CarRoundabout cr = (CarRoundabout)Main.intersection;
                	// Initializare innerBarrier cu numarul de masini care pot fii la un moment dat intr-o intersectie
                	CarRoundabout.innerBarrier = new CyclicBarrier(cr.getNumberOfCars()*cr.getNumberOfLanes());
                	// Afisare mesaj masinile au ajuns la intersectie
                	System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                	// Bariera de asteptarea a tuturor masinilor sa ajunga la intersectie
                	try {
						CarRoundabout.barrier.await();
					} catch (InterruptedException | BrokenBarrierException e1) {
						e1.printStackTrace();
					}
                	try {
                		// Verificare daca se poate intra in intersectie
						cr.getSemaphores().get(car.getStartDirection()).acquire();
						// Afisare mesaj ca se poate intra in intersectie
						System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane "
													+ car.getStartDirection());
						// Adaugare masina in lista sensului de mers corespunzator
						cr.addCar(car.getId(), car.getStartDirection());
						// Verificare si asteptare ca pe toate sensurile sa intru numarul maxim de masini
						for(int i=0;i<cr.getNumberOfLanes();i++) {
	                		while(cr.getLanes().get(i).size()<cr.getNumberOfCars());
	                	}
						// Afisare mesaj ca s-a intrat in intersectie
						System.out.println("Car " + car.getId() + " has entered the roundabout from lane " 
													+ car.getStartDirection());
						// Asteptare timp necesar iesirii din intersectie
						sleep(cr.getWaitingTime());
						// Afisare mesaj ca s-a iesit din intersectie
						System.out.println("Car " + car.getId() + " has exited the roundabout after " 
													+ cr.getWaitingTime()/1000 + " seconds");
						// Eliminare masina din lista sensului de mers corespunzator
						cr.removeCar(car.getId(), car.getStartDirection());
						// InnerBarrier care asteapta ca toate masinile sa iasa din intersectie
						CarRoundabout.innerBarrier.await();
						// Se anunta semaforul ca alte masini pot intra in intersectie
						cr.getSemaphores().get(car.getStartDirection()).release();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                	CarRoundabout cr = (CarRoundabout)Main.intersection;
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici
                    // Afisare mesaj ca s-a ajuns la intersectie
                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane "
                    							+ car.getStartDirection());
                    // Adaugare masina in lista sensului de mers corespunzator
                	cr.addCar(car.getId(), car.getStartDirection());
                	try {
                		// Se incearca intrarea in intersectie
						cr.getSemaphores().get(car.getStartDirection()).acquire();
						// Afisare mesaj cand se intra in intersectie
						System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
													+ car.getStartDirection());
						// Eliminare masina din lista sensului de mers
						cr.removeCar(car.getId(), car.getStartDirection());
						// Asteptare timp necesar parasirii intersectiei
						sleep(cr.getWaitingTime());
						// Afisare mesaj cand se iese din intersectie
						System.out.println("Car " + car.getId() + " has exited the roundabout after " 
													+ cr.getWaitingTime()/1000 + " seconds");
						// Anuntare semafor ca alte masini pot intra in intersectie
						cr.getSemaphores().get(car.getStartDirection()).release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                	PriorityIntersection pi = (PriorityIntersection)Main.intersection;
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici
                    //Verific prioritatea masinii
                    if(car.getPriority() == 1) {
                    	//Pentru masinile fara prioritate
                    	//Afisez mesaj cand ajunge la intersectie
                    	System.out.println("Car " + car.getId() 
                    								+ " with low priority is trying to enter the intersection...");
                    	//Adaug masina in coada
                    	pi.addCar(car.getId());
                    	//Verific si astept sa nu mai fie nici o masina in intersectie
                    	while(pi.getCarsInIntersection().get()!=0);
                    	//Afisez mesaj de intrare in intersectie si elimin masina din coada
                    	pi.enterIntersection(car.getId());
                    }else {
                    	//Incrementez numarul masinilor din intersectie pentru a face masinile fara prioritate sa astepte
                    	pi.getCarsInIntersection().addAndGet(1);
                    	//Afisez mesaj de intrare in intersectie
                    	System.out.println("Car " + car.getId() + " with high priority has entered the intersection");
                    	//Astept iesirea din intersectie(2 secunde)
                    	try {
							sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    	//Afisez mesaj de iesire din intersectie
                    	System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
                    	//Decrementez numarul masinilor din intersectie
                    	pi.getCarsInIntersection().addAndGet(-1);
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	//Initializez o variabila crosswalk cu intersectia din main
                	Crosswalk cw = (Crosswalk)Main.intersection;
                	// Execut cat timp pedestrians nu s-a terminat
                    while(!Main.pedestrians.isFinished()) {
                    	//Verific daca s-a printat deja
                    	if(!cw.getPrinted().get(car.getId())) {
                    		// Verific daca se printeaza green sau red
                    		if(cw.isGreen())
                    			System.out.println("Car " + car.getId() + " has now green light");
                    		else
                    			System.out.println("Car " + car.getId() + " has now red light");
                    		cw.getPrinted().set(car.getId(), true);
                    	}
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	//Intersectie de tip Maintenance initializata cu intersectia din Main
                	Maintenance m = (Maintenance)Main.intersection;
                	//Afisare mesaj daca s-a ajuns la bottleneck
                    System.out.println("Car " + car.getId() + " from side number " 
                    							+ car.getStartDirection() + " has reached the bottleneck");
                    //Verificare pe ce sens de mers vine masina
                    if(car.getStartDirection() == 0) {
                    	try {
                    		//Verificare daca se poate intra in bottleneck
							m.getSemaphoreForZero().acquire();
							//Afisare ca s-a trecut de bottleneck
							System.out.println("Car " + car.getId() + " from side number " 
														+ car.getStartDirection() + " has passed the bottleneck");
							//Asteptare ca toate masinile care pot sa treaca sa treaca
							Maintenance.barrier.await();
							//Instiintare celalalt sens ca poate trece
							m.getSemaphoreForOne().release();
						} catch (InterruptedException | BrokenBarrierException e) {
							e.printStackTrace();
						}
                    }else {
                    	try {
                    		//Verificare daca se poate intra in bottleneck
							m.getSemaphoreForOne().acquire();
							//Afisare ca s-a trecut de bottleneck
							System.out.println("Car " + car.getId() + " from side number " 
														+ car.getStartDirection() + " has passed the bottleneck");
							//Asteptare ca toate masinile care pot trece sa treaca
							Maintenance.barrier.await();
							//Instiintare celalalt sens ca poate trece
							m.getSemaphoreForZero().release();
						} catch (InterruptedException | BrokenBarrierException e) {
							e.printStackTrace();
						}
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                	//Intersectie de tip Railroad initializata cu intersectia din Main
                    Railroad r = (Railroad)Main.intersection;
                    //Adaug sensuri de mers la lista de sensuri
                	while(car.getStartDirection() >= r.getLanes().size()) {
                    	r.addLanes();
                	}
                	synchronized (r) {
                		//Afisez mesajul ca a ajuns masina in intersectie
                		System.out.println("Car " + car.getId() + " from side number " 
                								+ car.getStartDirection() + " has stopped by the railroad");
                        //Adaug masina in coada sensului de mers corespunzator
                		r.addCar(car.getStartDirection(), car.getId());
					}
                	//Astept sa ajunga toate masinile de intersectie
                    try {
						r.barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                    //Threadul 0 afiseaza ca a trecut trenul
                    if(car.getId()==0)
                    	System.out.println("The train has passed, cars can now proceed");
                    //Astept sa afiseze trecerea trenului
                    try {
						r.barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
                    //Afisez toate masinile care pleaca din intersectie
                    r.getFirstCar(car.getStartDirection());
                }
            };
            default -> null;
        };
    }
}
