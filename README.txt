README


Contact Information:

Name: Kyle Edgette
Class: CSC 172 SPRING 2015
Lab Session: Monday/Wednesday 2pm
TA Name: TJ Stein
Assignment Number: Project 04


Description:

A JAVA program that reads formatted data on the intersections and roads that make up a map, creates 
a graph using that data, displays the graph using Java Graphics, and calculates the shortest path 
between two intersections and the minimum weight spanning tree if prompted to do so. The final graph 
representation used is a HashMap of Strings to LinkedLists, where the Strings are IntersectionIDs and 
the LinkedList have a node Head, that stores the Intersection object, and a pointer to an Edge, which 
stores a Road that the intersection is a part of. All Edges have a pointer to the next Edge, thus forming 
the linked list. In order to display the entire Graph, every Road is added to an ArrayList of Roads and 
a 2D line is painted with the same endpoints as the road.

Dijkstra's algorithm is used for computing the shortest path between two intersections. A second HashMap 
that maps IntersectionIDs to the Intersections themselves, is used to hold the intersections for Dijkstra's. 
The HashMap allows for a very fast lookup of intersections. In order to quickly find the shortest unknown intersection, 
all intersections are inserted into a PriorityQueue when they are inserted into the Graph. Other than those modifications, 
the method that carries out Dijkstra's Algorithm is identical to the method I wrote in Lab 20, which was adapted from 
Weiss Figure 9.31. Then the formPath method finds the desired ending Intersection from the HashMap and uses the intersections' 
.path pointer to get back to the starting Intersection. This path is stored in an Array of Intersections, and the array is 
traversed two at a time (because two intersections make up a road) to print the path.

Kruskal's algorithm is used for computing the minimum weight spanning tree for the graph. The implementation is adapted from 
the pseudo-code in Weiss Figure 9.60. First, a HashMap is generated that maps IntersectionIDs to HashSets of all the 
intersectionIDs that intersection is connected to. At first the HashSet only contains that intersection's ID. A PriorityQueue 
of Roads is used to find the shortest Road in the algorithm, which allows for a fast "lookup" of the smallest road. 
Then, following Kruskal's algorithm, if the current Road is accepted, it is added to another ArrayList of Roads, which is graphed 
in the same manner as all of the Roads in the Graph.


Notable Obstacles:

At first, I tried using an adjacency matrix to represent the Graph, similar to the implementation we used in the Labs. I found 
that the program ran out of memory when using an adjacency matrix for Monroe County, so I implemented an Adjacency List for the 
Graph. Although the Adjacency List did not run out of memory, traversing it was too slow for New York State. Then, I decided on 
my final implementation, a HashMap of IntersectionIDs to LinkedLists as described above. The last major obstacle was the runtime 
for Dijkstra's and Kruskal's Algorithm. Instead of iterating through all the remaining unknown intersections to find the smallest 
one like I did in the labs, I decided on a PriorityQueue which would be much faster.


Runtime Analysis:

For Displaying the Graph: Because all the roads are added to an ArrayList of Roads, which is then iterated through to graph 
every road, I believe the runtime for displaying the regular graph is O(E) where is E is the number of roads in the 
graph. All other calculations (i.e. finding the minimum and maximum longitude and latitude, which are updated when a new Road 
is inserted) have constant time and can therefor be ignored.

For Calculating the Shortest Path between Two Intersections: Finding the smallest unknown vertex is a constant time operation, 
thanks to the Priority Queue of Intersections. I use a counter for the number of unknown vertices instead of scanning through 
all the vertices, so that check is a constant time operation. I believe the limiting factor is the findCost() method, which gets
the appropriate LinkedList from the HashMap (a constant time operation) and then traverses the list until it finds the desired 
Road. Thusly, the worst case run time for the method would be if the Road is the very last in the list, so the runtime of 
the method is O(E) where E is the number of Edges in the linked list. All other calculations in the method are constant time 
operations. Since, findCost() is called on each intersection in the Graph, I propose that the total runtime for Dijkstra's 
algorithm is O(V*|E|), where V is the number of intersections in the Graph and |E| is the average number of Edges in each 
Linked List.

For Calculating the Minimum Weight Spanning Tree: Finding the shortest road is a constant time operation, thanks to the 
Priority Queue of Roads. Since checking if the two HashSets corresponding to the end of the Road is a constant time operation, 
the runtime for Kruskal's algorithm is O(E) where E is the number of Roads in the graph, because every Road is checked in my 
implementation of the algorithm.

Displaying The Shortest Path: The runtime for displaying the directions between two intersections is O(V) where V is the number 
of intersections that make up the path. 

Displaying the Minimum Weight Spanning Tree: The runtime for displaying the Meridian map is O(E) where E is all the roads in the 
tree. All the edges have already been added to a separate ArrayList, so every Road in the List is displayed.

I expect that the runtime for the entire program will grow linearly as the data size increases, because (as stated above) none of 
calculations have a quadratic or slower runtime.


Files Handed In:

1. Road.java contains the constructor for a Road. Road objects have a RoadID, and the IDs for the intersections if connects.
2. Intersection.java Intersection objects have an IntersectionID, a Longitude and Latitude, are either known or unknown, and a 
distance and a path (for Dijkstra's).
3. Node.java is used for the head of all the LinkedLists. Nodes have an Intersection and a pointer to an Edge.
4. Edge.java is used for everything but the head of all LinkedLists. Edges have a Road and a pointer to the next Edge.
5. LinkedList.java has a constructor and helper methods for inserting a new Intersection into the List (which is only done 
on new Linked Lists, i.e. Lists whose heads don't hold an Intersection), for inserting an Edge into the List, and for calculating 
the cost between the Intersection in the head of the list and an intersection it connects to.
6. Map.java contains a constructor for the Map, and methods to insert Intersections and Edges, as well as methods that calculate 
the minimum weight spanning tree and the shortest path between two intersections. 
7. MapGUI.java contains a constructor and a paint method, that paints all the roads in the Graph, as well as the shortest path 
between two intersections and the minimum weight spanning tree if specified by the user.
8. Test.java contains the main method that reads from file the data on the intersections and roads that make up the graph and 
creates the graph object. Then, the command line arguments are interpreted and the desired specifications are made.
9. ur_campus.txt contains the formatted data for the University of Rochester campus as provided with the assignment.
10. monroe.txt contains the formatted data for Monroe county as provided with the assignment.
11. nys.txt contains the formatted data for New York State as provided with the assignment.
12. README.txt
13. OUTPUT.txt contains contact information, instructions for running the program, and sample output of the program.


Instructions for Running the Program:

In the command line, change directories until you are in the correct directory. Please note that the program takes several 
command line arguments, combinations of which are specified below.

Compile:

javac *.java

Run:

java Test map.txt
java Test map.txt -show
java Test map.txt -directions i1 i2
java Test map.txt -meridianmap
java Test map.txt -show -directions i1 i2
java Test map.txt -show -meridianmap

Where map.txt contains the formatted data of intersections and roads in the map, and i1 and i2 are the IntersectionIDs 
of the starting and ending intersections, respectively.
