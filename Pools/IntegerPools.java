import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;

class IntegerPools {
    public static void main(String[] args) {
	Landscape landscape1 = new Landscape(Arrays.asList(1,3,5,2,8,5,4,4,4,9,2));
	System.out.println("volume " + landscape1.calculatePoolsVolume());
	landscape1.printPools();

	Landscape landscape2 = new Landscape(Arrays.asList(5,0,0,0,0,0,0,0,0,0,5));
	System.out.println("volume " + landscape2.calculatePoolsVolume());
	landscape2.printPools();
	
	Landscape landscape3 = new Landscape(Arrays.asList(3,3,3,3,3,3,3,3,3,3,3));
	System.out.println("volume " + landscape3.calculatePoolsVolume());
	landscape3.printPools();
	
	Landscape landscape4 = new Landscape(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0));
	System.out.println("volume " + landscape4.calculatePoolsVolume());
	landscape4.printPools();
	
	Landscape landscape5 = new Landscape(Arrays.asList(5,4,3,2,1,12,10,2,3,7,9));
	System.out.println("volume " + landscape5.calculatePoolsVolume());
	landscape5.printPools();
    }
}

class Landscape {
    Landscape(List<Integer> heights) {
	landscape = new ArrayList<Integer>(heights);
    }

    public void printPools() {
	int tallestCliff = Collections.max(landscape);
	for (int height = tallestCliff; height >= 0; height--) {
	    for (int i = 0; i < landscape.size(); i++)
		if (landscape.get(i) > height)
		    System.out.print("#");
		else if (waterColumns != null && (landscape.get(i) + waterColumns.get(i) > height))
		    System.out.print("-");
		else
		    System.out.print(" ");
	    System.out.println();
	}
    }

    public int calculatePoolsVolume() {
	Direction direction = Direction.ASCENDING;

	List<Integer> localMaximums = new LinkedList<Integer>();
	for (int i=0; i < landscape.size(); ++i){
	    switch (direction) {
		case ASCENDING:
		    if (i > 0 && landscape.get(i-1) > landscape.get(i)) {
			direction = Direction.DESCENDING;
			localMaximums.add(i-1);
		    }
		    break;
		case DESCENDING:
		    if (i > 0 && landscape.get(i-1) < landscape.get(i)) {
			direction = Direction.ASCENDING;
		    }
		    break;
	    }
    
	    if (i == (landscape.size()-1) && direction == Direction.ASCENDING)
		localMaximums.add(i); // last column is local maximum if direction is 'ASCENDING' 
	    System.out.println(i + ", " +  direction); 
	}

	for (Integer i: localMaximums)
	    System.out.println(i + " ");
	
	int waterVolume = 0;
	waterColumns = new ArrayList<Integer>(Collections.nCopies(landscape.size(), 0));
	for (int m = 0; m < localMaximums.size()-1; ++m) {
	    int leftCliff = landscape.get(localMaximums.get(m));
	    int rightCliff = landscape.get(localMaximums.get(m+1));
	    int waterLevel = Math.min(leftCliff, rightCliff);
	    System.out.println(localMaximums.get(m) + "-" + localMaximums.get(m+1) + " water level is " + waterLevel);
	    for (int column = localMaximums.get(m) + 1; column < localMaximums.get(m+1); ++column) {
		System.out.println("column: " + column + ", depth " + (waterLevel - landscape.get(column)));
		int waterColumn = waterLevel - landscape.get(column); 
		if (waterColumn > 0) {
		    waterVolume += waterColumn;
		    waterColumns.set(column, waterColumn);
		}
	    }
	}

	return waterVolume;
    }

    private enum Direction {ASCENDING, DESCENDING};
    private List<Integer> landscape;
    private List<Integer> waterColumns;
}
