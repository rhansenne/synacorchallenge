import java.util.*;

/**
 * Determines the correct order the coins should be inputted to unlock the door.
 */
public class CoinEquationSolver {

	static final Map<Integer, String> coins = new HashMap<Integer, String>();
	static {
		coins.put(2, "red");
		coins.put(3, "corroded");
		coins.put(5, "shiny");
		coins.put(7, "concave");
		coins.put(9, "blue");
	};
	
	public static void main(String[] args) {
		permute(new ArrayList<Integer>(coins.keySet()),0);		
	}

    private static void permute(List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1)
            if (arr.get(0) + arr.get(1) * Math.pow(arr.get(2),2) + Math.pow(arr.get(3),3) - arr.get(4) == 399)
            	for (Integer i: arr) System.out.println(coins.get(i));
    }
}
