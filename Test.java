/*
 * Test
 * 
 * Version 1.0
 * 
 * Copyright Kyle Edgette
 * 
 * Course : CSC 172 SPRING 2015
 * 
 * Assignment : Project 04
 * 
 * Author : Kyle Edgette
 * 
 * Lab Session : Monday/Wednesday 2pm-3:15pm
 * 
 * Lab TA : TJ Stein
 * 
 * Last Revised : May 3, 2015
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;


public class Test {
	
	public static void main(String [] args) throws FileNotFoundException {
		
		//start the timer!
		long startTime = System.currentTimeMillis();
		
		//get the name of the file
		File mapData = new File(args[0]);
		
		//if the file is of the University of Rochester graph the map using thicker lines
		if(args[0].equals("ur_campus.txt")) {
			MapGUI.thickLines = true;
		}
		
		
		//first scanner finds the number of intersections in the entire graph
		Scanner scan = new Scanner(mapData);
		
		int numIntersects = 0;
		
		while(scan.nextLine().startsWith("i")) {
			numIntersects++;
		}

		scan.close();
		
		String intersectionID;
		double lat, longitude;
		Intersection v;
		
		//scan2 scans through all the data
		Scanner scan2 = new Scanner(mapData);
		
		//create the Map
		Map map = new Map(numIntersects);
		
		String currentLine = scan2.nextLine();
		
		String [] info;

		//INSERTING INTERSECTIONS
		while(currentLine.startsWith("i")) {
			
			info = currentLine.split("\t");
			
			intersectionID = info[1];
			lat = Double.parseDouble(info[2]);
			longitude = Double.parseDouble(info[3]);
			
			//create the new Intersection
			v = new Intersection();
			v.distance = Integer.MAX_VALUE;
			v.IntersectionID = intersectionID;
			v.latitude = lat;
			v.longitude = longitude;
			v.known = false;
			
			currentLine = scan2.nextLine();
			
			//add the new intersection into the map
			map.insert(v);
		}
		
		String roadID, int1, int2;
		
		Intersection w, x;
		
		double distance;
		
		//INSERTING ROADS
		while(currentLine.startsWith("r")) {
			
			info = currentLine.split("\t");
			
			roadID = info[1];
			
			int1 = info[2];
			int2 = info[3];
			
			w = Map.intersectLookup(int1);
			x = Map.intersectLookup(int2);
			
			distance = Map.roadDist(w, x);
			
			//create and add the new road
			map.insert(new Road(roadID, int1, int2, distance));
			
			if(scan2.hasNextLine() == false) {
				break;
			}
			
			currentLine = scan2.nextLine();
			
		}
		

		String fileName;
		
		//used for title of JFrame
		if(args[0].equals("ur_campus.txt")) {
			fileName = "U of R Campus";
		}
		else {
			if(args[0].equals("monroe.txt")) {
				fileName = "Monroe County";
			}
			else {
				if(args[0].equals("nys.txt")) {
					fileName = "New York State";
				}
				else {
					fileName = "Map";
				}
			}
		}
		
		//booleans for whether dijkstra's or kruskal's needs to be called and if the map needs to be displayed
		boolean showMap = false;
		boolean dijkstras = false;
		boolean mwst = false;
		
		//default directions
		String directionsStart = "i0";
		String directionsEnd = "i1";
		
		//checks the command line arguments
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-show")) {
				showMap = true;
			}
			
			if(args[i].equals("-directions")) {
				dijkstras = true;
				
				directionsStart = args[i+1];
				directionsEnd = args[i+2];
			}
			
			if(args[i].equals("-meridianmap")) {
				mwst = true;
			}
			
		}
		
		//if directions are needed
		if(dijkstras == true) {
			
			map.dijkstra(directionsStart);
			
			System.out.println("\nThe shortest path from " + directionsStart + " to " + directionsEnd + " is: ");
			System.out.println(Map.formPath(directionsEnd));
			
			System.out.println("Length of the path from " + directionsStart + " to " + directionsEnd + " is: " + Map.dijkstraPathLength() + " miles.");
		}
		
		//if the meridian map is needed
		if(mwst == true) {
			
			map.kruskals();
			
			System.out.println("\nRoads Taken to Create Minimum Weight Spanning Tree for " + fileName + ":\n");
			
			for(Road r : Map.minWeightSpanTree) {
				System.out.println(r.roadID);
			}
			
		}
		
		//if the GUI is needed
		if(showMap == true) {
		
			JFrame frame = new JFrame("Map");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.getContentPane().add(new MapGUI(Map.roads, Map.intersectionMap, Map.minLat, Map.maxLat, Map.minLong, Map.maxLong));
			frame.pack();
			frame.setVisible(true);
		}
		
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime-startTime;
		
		System.out.println("\n\nTime required to do everything: " + elapsedTime/1000 + " seconds.");
		
		
		scan2.close();
	}

}
