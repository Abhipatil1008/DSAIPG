/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.adt.threesum;

import com.phasmidsoftware.dsaipg.util.Stopwatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Implementation of ThreeSum which follows the approach of dividing the solution-space into
 * N sub-spaces where each sub-space corresponds to a fixed value for the middle index of the three values.
 * Each sub-space is then solved by expanding the scope of the other two indices outwards from the starting point.
 * Since each sub-space can be solved in O(N) time, the overall complexity is O(N^2).
 * <p>
 * NOTE: The array provided in the constructor MUST be ordered.
 */
public class ThreeSumQuadratic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadratic on a.
     *
     * @param a a sorted array.
     */
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }

    /**
     * Retrieves an array of unique Triples. Each Triple represents a unique combination of three integers from
     * the source array that sum to zero.
     *
     * @return an array of distinct Triples, sorted in natural order, where each Triple satisfies the condition that
     * the sum of its three integers is zero.
     */
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a list of Triples such that the middle index is the given value j.
     *
     * @param j the index of the middle value.
     * @return a Triple such that
     */
     List<Triple> getTriples(int j) {
         List<Triple> triples = new ArrayList<>();
        // TO BE IMPLEMENTED  : for each candidate, test if a[i] + a[j] + a[k] = 0.
        
        int left = 0, right = length - 1;
        while (left < j && right > j) {
            int sum = a[left] + a[j] + a[right];
            if (sum == 0) {
                triples.add(new Triple(a[left], a[j], a[right]));
                left++;
                right--;
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }
        return triples;
        //throw new RuntimeException("implementation missing");
    }

    private final int[] a;
    private final int length;
    
    /* Adding main method to test custom arrays of different sizes N. I am also, sorting the arrays before passing
    it to the ThreeSumQuadratic constructor.  
    */
    public static void main(String[] args) {
        int[] N = {250, 500, 1000, 2000, 4000}; // Array sizes to test
        int m = 1000; 

        System.out.println("N, ThreeSumQuadratic (ms)");

        for (int n : N) {
            int[] input = generateRandomArray(n, m); 
            Arrays.sort(input); 
            long timeQuadrithmic = timeThreeSumQuadratic(input);
            System.out.printf("%d, %d%n", n, timeQuadrithmic);
        }
    }
    
    private static int[] generateRandomArray(int n, int m) {
        Random random = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(2 * m) - m; 
        }
        return array;
    }
    
    private static long timeThreeSumQuadratic(int[] input) {
        try (Stopwatch stopwatch = new Stopwatch()) {
            new ThreeSumQuadratic(input).getTriples(); 
            return stopwatch.lap(); 
        }
    }
}