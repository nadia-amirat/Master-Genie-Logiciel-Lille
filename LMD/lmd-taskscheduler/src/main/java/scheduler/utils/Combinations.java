package scheduler.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Combinations {

    public static ArrayList<ArrayList<Integer>> print(Vector<Integer>[] arr) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();

        // Number of arrays
        int n = arr.length;

        // To keep track of next element in
        // each of the n arrays
        int[] indices = new int[n];

        // Initialize with first element's index

        for (int i = 0; i < n; i++)
            indices[i] = 0;

        while (true) {
            ArrayList<Integer> tmp = new ArrayList<>();

            // Print current combination
            for (int i = 0; i < n; i++) {
                tmp.add(arr[i].get(indices[i]));
            }
            res.add(tmp);

            // Find the rightmost array that has more
            // elements left after the current element
            // in that array
            int next = n - 1;
            while (next >= 0 &&
                    (indices[next] + 1 >=
                            arr[next].size()))
                next--;

            // No such array is found so no more
            // combinations left
            if (next < 0)
                return res;

            // If found move to next element in that
            // array
            indices[next]++;

            // For all arrays to the right of this
            // array current index again points to
            // first element
            for (int i = next + 1; i < n; i++)
                indices[i] = 0;
        }
    }

}
