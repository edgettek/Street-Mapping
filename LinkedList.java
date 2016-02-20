/*
 * LinkedList
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


public class LinkedList {
	
	//all LinkedLists have a size and a head
	public int size;
	public Node head;
	
	//constructor
	public LinkedList() {
		head = new Node();
		size = 0;
	}
	
	//method that returns the size of the linked list
	public int size() {
		return size;
	}
	
	//METHOD THAT FINDS THE COST BETWEEN TWO INTERSECTIONS, THE CURRENT INTERSECTION OF THE LINKED LIST AND THE INTERSECTIONS ADJACENT TO IT
	public double findCost(Intersection int2) {

		Edge temp2 = head.edge;
		
		//travel down the linked list
		while(temp2 != null) {
			
			if(temp2.road.intersect1.equals(int2.IntersectionID) || temp2.road.intersect2.equals(int2.IntersectionID)) {
				return temp2.road.distance;
			}
			
			temp2 = temp2.next;
		}
		
		return -1;
		
	}
	
	//METHOD THAT INSERTS AN INTERSECTION INTO THE LINKED LIST
	//only called when the linked list has nothing in it
	public void insert(Intersection intersect) {
		
		if(head.intersection == null) {
			head.intersection = intersect;
		}
		
		size++;
	}
	
	//METHOD THAT CHECKS IF THE INTERSECTIONS ARE CONNECTED, THE CURRENT INTERSECTION OF THE LINKED LIST AND THE INTERSECTIONS ADJACENT TO IT
	public boolean connected(Intersection int2) {
		
		Edge temp2 = head.edge;
		
		//travel down the linked list
		while(temp2 != null) {
			
			if(temp2.road.intersect1.equals(int2.IntersectionID) || temp2.road.intersect2.equals(int2.IntersectionID)) {
				return true;
			}
			
			temp2 = temp2.next;
		}
		
		return false;
		
	}
	
	//METHOD THAT CHECKS IF TWO INTERSECTIONS ARE CONNECTED
	public boolean contains(Intersection i) {
		
		Node temp = head;
		
		while(temp != null) {
			
			if(temp.intersection.equals(i)) {
				return true;
			}
			
			temp = temp.next;
		}
		
		return false;
		
	}
	
	//METHOD THAT INSERTS A ROAD INTO THE LINKED LIST
	public void insert(Road road) {
		
		Edge tempEdge = new Edge();
		tempEdge.road = road;
		
		//isert at the front of the list (after the head)
		tempEdge.next = head.edge;
		head.edge = tempEdge;
		

	}

}
