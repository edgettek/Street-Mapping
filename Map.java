/*
 * Map
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;


public class Map {
	
	public HashMap<String, LinkedList> graph;
	public static int numIntersections;
	
	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection> intersectionMap;
	public static PriorityQueue<Intersection> unknownIntersectionsHeap;
	public static PriorityQueue<Road> kruskalsRoads;
	public static HashMap<String, HashSet<String>> intersectionSets;
	public static ArrayList<Road> minWeightSpanTree;
	public static Intersection [] dijkstraPath;

	public static double minLat, maxLat, minLong, maxLong;
	
	//constructor for the Map
	public Map(int numVertices) {
		
		//the graph is represented by a HashMap of IntersectionIDs to LinkedLists
		//where the LinkedList has a head (containing the Intersection) which point to an Edge
		//where each Edge contains a road and a pointer to the next Edge
		graph = new HashMap<String, LinkedList>();

		numIntersections = numVertices;
		roads = new ArrayList<Road>();
		intersectionMap = new HashMap<String, Intersection>();

		//comparator used in the Heap of Intersections
		Comparator<Intersection> comparator = new Comparator<Intersection>() {

        	@Override
        	public int compare(Intersection i1, Intersection i2) {
            	
            	if(i1.distance < i2.distance) {
            		return -1;
            	}
            	else {
            		return 1;
            	}
            }
		};
		
		//heap of Unknown Intersections
		unknownIntersectionsHeap = new PriorityQueue<Intersection>(numVertices, comparator);
		
		//comparator used in Heap of Roads
		Comparator<Road> comparator2 = new Comparator<Road>() {

        	@Override
        	public int compare(Road r1, Road r2) {
            	
            	if(r1.distance < r2.distance) {
            		return -1;
            	}
            	else {
            		return 1;
            	}
            }
		};
		
		//heap of roads
		kruskalsRoads = new PriorityQueue<Road>(numVertices*3, comparator2);
		
		//set the minimum and maximum latitudes and longitudes to appropriate starting integer values
		minLat = minLong = Integer.MAX_VALUE;
		maxLat = maxLong = Integer.MIN_VALUE;

		
	}
	
	//method that returns the number of intersections in the graph
	public int size() {
		return graph.size();
	}
	
	// METHOD THAT DETERMINES THE PATH FOR DIJKSTRA'S ALGORITHM
	//adapted from Lab 20
	public static String formPath(String endID) {
		
		//get the intersection that has endID as its ID
		Intersection temp = intersectionMap.get(endID);
		
		//path will contain the order of the nodes from the end to the start vertex
		String [] path = new String[intersectionMap.size()];
		
		int counter = 0;
		
		
		//adds the Vertex's path to the path[] until we reach the start Vertex (which has no path)
		while(temp.path != null) {
			path[counter] = temp.IntersectionID;
			temp = temp.path;
			counter++;
		}
		
		//add the start vertex to the path[]
		path[counter] = temp.IntersectionID;
		
		int totalPath = 0;
		
		//calculate total length of path
		for(int i = 0; i < path.length; i++) {
			if(path[i] == null) {
				totalPath = i;
				break;
			}
		}
		
		//dijkstraPath is used to graph the directions
		dijkstraPath = new Intersection [totalPath];
		
		for(int i = 0; i < totalPath; i++) {
			dijkstraPath[i] = intersectionMap.get(path[i]);
		}
		
		String finalPath = "";
		
		//now working backwards from path[], create a string of the path from the start Vertex to the end Vertex
		for(int i = counter ; i > -1; i--) {
			finalPath = finalPath + path[i] + "\n";
		}
		
		return finalPath;
	}
	
	//method to determine the total distance required to travel between the intersections
	public static double dijkstraPathLength() {
		
		//converting form meters to miles
		return dijkstraPath[0].distance * 0.000621371;
	}
	
	//METHOD USED IN DIJKSTRA'S ALGORITHM TO GET THE SMALLEST UNKNOWN VERTEX
	public static Intersection smallestUnknownVertex() {
		
		//get the smallest intersection from the heap of intersections
		Intersection temp = unknownIntersectionsHeap.remove();
		
		//all of dijkstra's is done with the intersections from the intersectionMap
		return intersectionMap.get(temp.IntersectionID);
		
	}
	
	//METHOD TO CREATE THE SETS USED IN KRUSKAL'S ALGORITHM
	public void createSet() {
		
		//instantiate the HashMap that maps IntersectionID to HashSet of vertices connected to that intersection
		intersectionSets = new HashMap<String, HashSet<String>>();
		
		HashSet<String> intersections;
		
		//iterate over all the entries in the graph
		Iterator<Entry<String, LinkedList>> iterator = graph.entrySet().iterator();
		
		while (iterator.hasNext()) {
	        HashMap.Entry<String, LinkedList> pair = (HashMap.Entry<String, LinkedList>) iterator.next();
	        
	        
	        intersections = new HashSet<String>();
	        
	        //at first the set only contains the intersectionID for that intersection
	        
	        intersections.add(pair.getKey());
	        
	        intersectionSets.put(pair.getKey(), intersections);
	        
		}
		
		
	}
 	
	//METHOD THAT DETERMINES THE ROADS THAT MAKE UP THE MINIMUM WEIGHT SPANNING TREE
	//adapted from Weiss Figure 9.60 (Kruskal's Algorithm Pseudo-code)
	public void kruskals() {

		//make all the HashSets for the intersections
		createSet();
		
		//arraylist that will hold all the roads in the minimum weight spanning tree
		minWeightSpanTree = new ArrayList<Road>();
		
		Road currentRoad;
		
		//u and v are the intersections of the currentRoad
		HashSet<String> u;
		HashSet<String> v;
		
		//while there are still roads left in the Heap of Roads
		while(kruskalsRoads.size() > 0) {
			
			//get the road with the shortest distance from the heap of all roads
			currentRoad = kruskalsRoads.remove();
			
			u = intersectionSets.get(currentRoad.intersect1);
			v = intersectionSets.get(currentRoad.intersect2);
			
			//if the sets are not the same, accept the edge
			if(!u.equals(v)) {
				
				minWeightSpanTree.add(currentRoad);
				
				//union the two sets
				u.addAll(v);
				
				for(String intersectionID: u) {
					intersectionSets.put(intersectionID, u);
				}
			}
		}
	}
	
	//METHOD THAT DETERMINES THE PATHS FOLLOWED TO GET FROM START TO FINISH IN THE SHORTEST DISTANCE
	//adapted from Weiss Figure 9.31 (Dijkstra's Algorithm Pseudo-code)
	public void dijkstra(String intersectionID) {
		
		//get the starting intersection from the HashMap of intersections
		Intersection start = intersectionMap.get(intersectionID);
		
		//remove that intersection from the heap
		unknownIntersectionsHeap.remove(start);
		
		//change its distance to 0
		start.distance = 0;
		
		//add the intersection back into the heap
		unknownIntersectionsHeap.add(start);
		
		double cost;
		
		//for speed -> set the number of unknown vertices to the total number of vertices and decrement it when a vertex is set to known
		int numUnknownVertices = intersectionMap.size();
		
		while(numUnknownVertices > 0) {
			
			//smallestUnknownVertex() returns the unknown vertex with the smallest distance from the heap of intersections
			Intersection temp = smallestUnknownVertex();
			
			temp.known = true;
			numUnknownVertices--;
			
			//LinkedList has all the roads connected to the current smallest unknown vertex
			LinkedList currentVertex = graph.get(temp.IntersectionID);
			
			//get the first edge from the linked list
			Edge currentRoad = currentVertex.head.edge;
			Intersection currentIntersection;
			
			//while there are still edges in the linked list
			while(currentRoad != null) {
				
				//get the correct intersection in the edge
				//we want the intersection that is NOT the same as the one we are currently visiting
				if(currentRoad.road.intersect1.equals(temp.IntersectionID)) {
					currentIntersection = intersectionMap.get(currentRoad.road.intersect2);
				}
				else {
					currentIntersection = intersectionMap.get(currentRoad.road.intersect1);
				}
				
				//if the intersection is unknown
				if(currentIntersection.known == false) {
					
					//find the cost to get from the current vertex to its adjacent one
					cost = findCost(temp, currentIntersection);
					
					if(temp.distance + cost < currentIntersection.distance) {
						
						//update the intersection by removing it from the heap
						unknownIntersectionsHeap.remove(currentIntersection);
						
						//changing the values
						currentIntersection.distance = temp.distance + cost;
						currentIntersection.path = temp;
						
						//and adding it back into the heap
						unknownIntersectionsHeap.add(currentIntersection);
					}
				}
				//get to the next edge in the linked list
				currentRoad = currentRoad.next;
			}
		}
	}
	
	//METHOD TO FIND THE COST TO TRAVEL BETWEEN TWO CONNECTED INTERSECTIONS
	public double findCost(Intersection int1, Intersection int2) {
		
		//get the linked list for the first intersection
		LinkedList temp = graph.get(int1.IntersectionID);
		
		//call find cost on that linked list
		return temp.findCost(int2);
	}
	
	//METHOD THAT CHECKS TO SEE IF TWO INTERSECTIONS ARE CONNECTED
	public boolean connected(Intersection int1, Intersection int2) {
		
		//get the linked list for the first intersection
		LinkedList temp = graph.get(int1.IntersectionID);
		
		//call connected on the linked list
		return temp.connected(int2);
		
	}
	
	//METHOD THAT INSERTS AN INTERSECTION INTO THE GRAPH
	public void insert(Intersection i) {
		
		
		//continually finds and updates the minimum and maximum latitude and longitude
		if(i.latitude < minLat) {
			minLat = i.latitude;
		}
		
		if(i.latitude > maxLat) {
			maxLat = i.latitude;
		}
		
		if(i.longitude < minLong) {
			minLong = i.longitude;
		}
		
		if(i.longitude > maxLong) {
			maxLong = i.longitude;
		}
		
		//add the intersection into the HashMap of intersections
		intersectionMap.put(i.IntersectionID, i);
		
		//add the intersection into the heap of unknown intersections
		unknownIntersectionsHeap.add(i);
		
		//create a new linked list
		LinkedList temp = new LinkedList();
		
		//insert the intersection into the linked list
		temp.insert(i);
		
		//insert the intersectionID and the linked list into the main graph
		graph.put(i.IntersectionID, temp);
	}
	
	//METHOD THAT INSERTS A ROAD INTO THE GRAPH
	public void insert(Road e) {
		
		//gets the linked list for each intersection in the road
		LinkedList int1 = graph.get(e.intersect1);
		LinkedList int2 = graph.get(e.intersect2);
		
		//inserts the road into each linked list
		int1.insert(e);
		int2.insert(e);
		
		//adds the road to the heap of roads
		kruskalsRoads.add(e);
		
		//adds the road to the arrayList of all roads
		roads.add(e);
	}
	
	//METHOD THAT RETURNS THE INTERSECTION THAT CORRESPONDS TO THE INTERSECTION ID
	public static Intersection intersectLookup(String intersectID) {
		
		return intersectionMap.get(intersectID);
		
	}
	
	//method that calculates the distance between two intersection objects
	public static double roadDist(Intersection int1, Intersection int2) {
		
		return calcDist(int1.latitude, int1.longitude, int2.latitude, int2.longitude);
		
	}
	
	//method that calculates the distance between two pairs of longitude and latitude
	//adapted from the Haversine Formula found on http://www.movable-type.co.uk/scripts/latlong.html
	public static double calcDist(double lat1, double long1, double lat2, double long2) {
		
		int earthRadius = 6371000;
		
		lat1 = Math.toRadians(lat1);
		long1 = Math.toRadians(long1);
		lat2 = Math.toRadians(lat2);
		long2 = Math.toRadians(long2);
		
		double changeLat = lat2-lat1;
		double changeLong = long2-long1;
		
		double a = (Math.sin(changeLat/2) * Math.sin(changeLat/2)) + (Math.cos(lat1) * Math.cos(lat2) * Math.sin(changeLong/2) * Math.sin(changeLong/2));
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		return earthRadius * c;
		
	}

}
