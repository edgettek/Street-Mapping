/*
 * MapGUI
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

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapGUI extends JPanel{
	
	
	//private static MapGUI temp;
	
	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection> intersectionMap;
	public static boolean thickLines = false;
	
	public static double minLat, minLong, maxLat, maxLong;
	public static double xScale, yScale;
	
	public MapGUI(ArrayList<Road> roads, HashMap<String, Intersection> intersectMap, double minimumLat, double maximumLat, double minimumLong, double maximumLong) {
		
		MapGUI.roads = roads;
		MapGUI.intersectionMap = intersectMap;
		
		minLat = minimumLat;
		maxLat = maximumLat;
		minLong = minimumLong;
		maxLong = maximumLong;
		
		setPreferredSize(new Dimension(800, 800));
		
	}
	
	//paint component
	public void paintComponent(Graphics page) {
		
		//use 2D graphics to display lines with double values for coordinates
		Graphics2D page2 = (Graphics2D) page;
		super.paintComponent(page2);
		
		page2.setColor(Color.BLACK);
		
		//increase the thickness of the lines if the map is of the University of Rochester
		if(thickLines) {
			page2.setStroke(new BasicStroke(3));
		}
		
		//set the scales
		//adapted from Lab TAs
		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);
		
		Intersection int1, int2;
		
		double x1, y1, x2, y2;
		
		//GRAPHING ALL THE ROADS
		for(Road r : roads) {
			
			scale();
			
			int1 = intersectionMap.get(r.intersect1);
			int2 = intersectionMap.get(r.intersect2);
			
			x1 = int1.longitude;
			y1 = int1.latitude;
			x2 = int2.longitude;
			y2 = int2.latitude;
		
			page2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
					(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
			
		}
		
		//GRAPHING THE DIRECTIONS USING DIJKSTRA'S ALGORITHM
		if(Map.dijkstraPath != null) {
			
			page2.setColor(Color.RED);
			
			for(int i = 0; i < Map.dijkstraPath.length - 1; i++) {
				
				x1 = Map.dijkstraPath[i].longitude;
				y1 = Map.dijkstraPath[i].latitude;
				x2 = Map.dijkstraPath[i+1].longitude;
				y2 = Map.dijkstraPath[i+1].latitude;
				
				page2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
						(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));

			}
			
			
		}
		
		//GRAPHING THE MERIDIAN MAP
		if(Map.minWeightSpanTree != null) {
			for(Road r : Map.minWeightSpanTree) {
				
				page2.setColor(Color.BLUE);
				
				int1 = intersectionMap.get(r.intersect1);
				int2 = intersectionMap.get(r.intersect2);
				
				x1 = int1.longitude;
				y1 = int1.latitude;
				x2 = int2.longitude;
				y2 = int2.latitude;
			
				page2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
						(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
				
			}
		}
	
	}
	
	//METHOD USED TO RESCALE THE PANEL
	public void scale() {
		
		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);
		
	}
	
	

}
