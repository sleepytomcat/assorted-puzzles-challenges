import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;
//
// Input: height map in form of integer array;
// imagine heavy rain starts, filling all pools on the given landscape.
// Goal: calculate volume of the water in all the pools.
//
// Example input data: {1,3,5,2,8,5,4,4,4,9,2}
// 
//          #
//     #    #
//     #    #
//     #    #
//   # ##   #
//   # ######
//  ## ######
//  ##########
// ###########
//
// Pools:
//          #
//     #----#
//     #----#
//     #----#
//   #-##---#
//   #-######
//  ##-######
//  ##########
// ###########
//
// Water volume: 14
//
class IntegerPools {
    public static void main(String[] args) {
	Landscape landscape[] = new Landscape[6];
	landscape[0] = new Landscape(Arrays.asList(1,3,5,2,8,5,4,4,4,9,2));
	landscape[1] = new Landscape(Arrays.asList(5,0,0,0,0,0,0,0,0,0,5));
	landscape[2] = new Landscape(Arrays.asList(3,3,3,3,3,3,3,3,3,3,3));
	landscape[3] = new Landscape(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0));
	landscape[4] = new Landscape(Arrays.asList(5,4,3,2,1,12,10,2,3,7,9));
	landscape[5] = new Landscape(Arrays.asList(0,1,2,3,4,5,4,3,2,1,0));
	for (int i = 0; i < landscape.length; i++) {
	    System.out.println("--------------------------------------");
	    System.out.println("Landscape with pools:");
	    landscape[i].printPools();
	    System.out.println("Water volume: " + landscape[i].getWaterVolume());
	}
    }
}

class Landscape {
    Landscape(List<Integer> heights) {
	landscape = new ArrayList<Integer>(heights);
	waterVolume = calculatePoolsVolume();
    }

    public int getWaterVolume() {
	return waterVolume;
    }

    private int calculatePoolsVolume() {
	List<Pool> pools = findPools(landscape);
	int waterVolume = 0;
	waterColumns = new ArrayList<Integer>(Collections.nCopies(landscape.size(), 0));
	for (Pool pool: pools) {
	    int leftCliffHeight = landscape.get(pool.leftCliff);
	    int rightCliffHeight = landscape.get(pool.rightCliff);
	    int waterLevel = Math.min(leftCliffHeight, rightCliffHeight);
	    for (int column = pool.leftCliff + 1; column < pool.rightCliff; ++column) {
		int waterColumn = waterLevel - landscape.get(column); 
		if (waterColumn > 0) {
		    waterVolume += waterColumn;
		    waterColumns.set(column, waterColumn);
		}
	    }
	}

	return waterVolume;
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

    private static List<Pool> findPools(List<Integer> landscape) {
	List<Pool> pools = new LinkedList<Pool>();
	Goal currentGoal = Goal.LEFT_CLIFF;
	int column = 0;
	Pool pool = null;
	int lastColumn = landscape.size() - 1;
	while (column <= lastColumn) {
	    if (landscape.get(column) == 0) {
		currentGoal = Goal.LEFT_CLIFF;
		pool = null;
		column++;
	    }
	    else if (currentGoal == Goal.LEFT_CLIFF) {
		if ((column < lastColumn) && (landscape.get(column) > landscape.get(column+1))) {
		    pool = new Pool();
		    pool.leftCliff = column;
		    currentGoal = Goal.TRENCH;
		}
		column++;
	    }
	    else if (currentGoal == Goal.TRENCH) {
		if (column < lastColumn && landscape.get(column) < landscape.get(column+1)) {
		    currentGoal = Goal.RIGHT_CLIFF;
		}
		column++;
	    }
	    else if (currentGoal == Goal.RIGHT_CLIFF) {
		if ((column < lastColumn && landscape.get(column) > landscape.get(column+1))
		    || (column == lastColumn)) {
		    currentGoal = Goal.LEFT_CLIFF;
		    pool.rightCliff = column;
		    pools.add(pool);
		    pool = null;
		}
		else
		    column++;
	    }
	}

	return pools;
    }

    private static class Pool {
	public int leftCliff;
	public int rightCliff;
    }

    private enum Goal {LEFT_CLIFF, TRENCH, RIGHT_CLIFF};
    private List<Integer> landscape;
    private List<Integer> waterColumns;
    private int waterVolume;
}
