/*
 * Road
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


public class Road {
	
	String roadID;
	String intersect1;
	String intersect2;
	double distance;
	
	//constructor
	public Road(String road, String int1, String int2, double dist) {
		roadID = road;
		intersect1 = int1;
		intersect2 = int2;
		distance = dist;
	}

}
