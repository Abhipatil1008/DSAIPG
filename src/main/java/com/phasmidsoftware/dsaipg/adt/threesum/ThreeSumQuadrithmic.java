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
 * Implementation of ThreeSum which follows the simple optimization of
 * requiring a sorted array, then using binary search to find an element x where
 * -x the sum of a pair of elements.
 * <p>
 * The array provided in the constructor MUST be ordered.
 * <p>
 * This algorithm runs in O(N^2 log N) time.
 */
class ThreeSumQuadrithmic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadrithmic on a.
     *
     * @param a a sorted array.
     */
    public ThreeSumQuadrithmic(int[] a) {
        this.a = a;
        length = a.length;
    }

    /**
     * Retrieves an array of unique, sorted triples that represent combinations
     * of three integers in the underlying sorted array whose sum equals zero.
     * <p>
     * This method iterates over all pairs of elements in the array, uses a helper
     * method to find the third element that satisfies the condition, and returns
     * all such combinations as distinct {@code Triple} objects sorted in natural order.
     *
     * @return an array of unique {@code Triple} objects representing all valid combinations
     * of three integers in the array that sum to zero.
     */
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < length; j++) {
                Triple triple = getTriple(i, j);
                if (triple != null) triples.add(triple);
            }
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Finds a "triple" consisting of three integers from the sorted array such that their sum equals zero,
     * given two indices representing the first two elements of the triple.
     *
     * @param i the index of the first element in the triple.
     * @param j the index of the second element in the triple. Must satisfy j > i.
     * @return a {@code Triple} object representing the three integers if such a combination exists,
     * or {@code null} if no such triple can be found.
     */
    Triple getTriple(int i, int j) {
        // TO BE IMPLEMENTED  : use binary search to find the third element
        // END SOLUTION
        int sum = a[i] + a[j];
        int k = Arrays.binarySearch(a, j + 1, length, -sum);
        if (k > j) {
            return new Triple(a[i], a[j], a[k]);
        }
        
        return null;
        
    }

    private final int[] a;
    private final int length;
    
    /* Adding main method to test custom arrays of different sizes N. I am also, sorting the arrays before passing
    it to the ThreeSumQuadrithmic constructor.  
    */
    public static void main(String[] args) {
        int[] N = {250, 500, 1000, 2000, 4000}; // Array sizes to test
        int m = 1000; 

        System.out.println("N, ThreeSumQuadrithmic (ms)");

        for (int n : N) {
            int[] input = generateRandomArray(n, m); 
            Arrays.sort(input); 
            long timeQuadrithmic = timeThreeSumQuadrithmic(input);
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
    
    private static long timeThreeSumQuadrithmic(int[] input) {
        try (Stopwatch stopwatch = new Stopwatch()) {
            new ThreeSumQuadrithmic(input).getTriples(); 
            return stopwatch.lap(); 
        }
    }
    
}