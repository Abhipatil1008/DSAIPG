/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.adt.threesum;

import com.phasmidsoftware.dsaipg.util.Stopwatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Implementation of ThreeSum which follows the brute-force approach of
 * testing every candidate in the solution-space.
 * The array provided in the constructor may be randomly ordered.
 * <p>
 * This algorithm runs in O(N^3) time.
 */
class ThreeSumCubic implements ThreeSum {
    /**
     * Construct a ThreeSumCubic on a.
     *
     * @param a an array.
     */
    public ThreeSumCubic(int[] a) {
        this.a = a;
        length = a.length;
    }

    /**
     * Retrieves all unique triples of integers from the input array whose sum is zero.
     * The triples are sorted in lexicographical order based on their elements.
     *
     * @return an array of unique {@code Triple} objects, each representing a set of three integers whose sum is zero.
     */
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    if (a[i] + a[j] + a[k] == 0)
                        triples.add(new Triple(a[i], a[j], a[k]));
                }
            }
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    private final int[] a;
    private final int length;
    
    /* Adding main method to test custom arrays of different sizes N. I am also, sorting the arrays before passing
    it to the ThreeSumCubic constructor.  
    */
    public static void main(String[] args) {
        int[] N = {250, 500, 1000, 2000, 4000}; 
        int m = 1000; 

        System.out.println("N, ThreeSumCubic (ms)");

        for (int n : N) {
            int[] input = generateRandomArray(n, m);
            long timeCubic = timeThreeSumCubic(input);
            System.out.printf("%d, %d%n", n, timeCubic);
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
    
    private static long timeThreeSumCubic(int[] input) {
        try (Stopwatch stopwatch = new Stopwatch()) {
            new ThreeSumCubic(input).getTriples(); 
            return stopwatch.lap(); 
        }
    }
    
}