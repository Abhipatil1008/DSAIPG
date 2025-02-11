/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.Helper;
import com.phasmidsoftware.dsaipg.sort.Sort;
import com.phasmidsoftware.dsaipg.sort.SortWithHelper;
import com.phasmidsoftware.dsaipg.util.Config;
import com.phasmidsoftware.dsaipg.util.Config_Benchmark;

import java.io.IOException;
import java.util.Comparator;

import static com.phasmidsoftware.dsaipg.sort.InstrumentedComparatorHelper.getRunsConfig;
import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

/**
 * A class for performing insertion sort using a comparator, extending functionality from SortWithHelper.
 * This includes methods for initialization and invocation of insertion sort,
 * along with specific utilities like counting inversions.
 *
 * @param <X> the type of elements to be sorted, which can be compared using a provided comparator.
 */
public class InsertionSortComparator<X> extends SortWithHelper<X> {
    /**
     * Constructor for InsertionSortComparator, which initializes the comparator with the provided helper.
     *
     * @param helper the Helper object to be used for managing the sorting process.
     */
    public InsertionSortComparator(Helper<X> helper) {
        super(helper);
    }

    /**
     * Constructor for any subclasses to use.
     *
     * @param description the description.
     * @param comparator  the comparator to use.
     * @param N           the number of elements expected.
     * @param nRuns       the number of runs to be expected (this is only significant when instrumenting).
     * @param config      the configuration.
     */
    protected InsertionSortComparator(String description, Comparator<X> comparator, int N, int nRuns, Config config) {
        super(description, comparator, N, nRuns, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param nRuns  the number of runs to be expected (this is only significant when instrumenting).
     * @param config the configuration.
     */
    public InsertionSortComparator(Comparator<X> comparator, int N, int nRuns, Config config) {
        this(DESCRIPTION, comparator, N, nRuns, config);
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        
        // TO BE IMPLEMENTED 
        //throw new RuntimeException("implementation missing");
    for (int i = from + 1; i < to; i++) {
        for (int j = i; j > from; j--) {
            
            if (!helper.swapStableConditional(xs, j)) {
                break; // Stopping when the correct position is found
            }
        }
    }
    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Sorts the given array in-place using the provided insertion sort comparator.
     *
     * @param <T> the generic type parameter that extends Comparable.
     * @param ts  the array of elements to be sorted, where elements must implement {@code Comparable}.
     *            The method modifies this array directly to produce the sorted order.
     * @throws RuntimeException if an IOException occurs during the sorting process.
     */
    public static <T extends Comparable<T>> void sort(T[] ts) {
        try (InsertionSortComparator<T> sort = new InsertionSortComparator<>(DESCRIPTION, Comparable::compareTo, ts.length, 1, Config.load(InsertionSortComparator.class))) {
            sort.mutatingSort(ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a case-insensitive string sorter using an insertion sort comparator.
     *
     * @param n      the expected number of elements to be sorted.
     * @param config the configuration object containing necessary settings.
     * @return a {@code SortWithHelper<String>} instance configured for case-insensitive string sorting.
     */
    public static Sort<String> stringSorterCaseInsensitive(int n, Config config) {
        return new InsertionSortComparator<>(DESCRIPTION, String.CASE_INSENSITIVE_ORDER, n, getRunsConfig(config), config);
    }

    /**
     * This method is designed to count inversions in quadratic time, using insertion sort.
     *
     * @param ts  an array of comparable T elements.
     * @param <T> the underlying type of the elements.
     * @return the number of inversions in ts, which remains unchanged.
     */
    public static <T> long countInversions(T[] ts, Comparator<T> comparator) {
        final Config config = Config_Benchmark.setupConfigFixes();
        try (InsertionSortComparator<T> sorter = new InsertionSortComparator<>(comparator, ts.length, getRunsConfig(config), config)) {
            Helper<T> helper = sorter.getHelper();
            sorter.sort(ts, true);
            return helper.getFixes();
        }
    }
    
    public static void main(String[] args) {

        String[] orderTypes = {"Random", "Ordered", "Partially-Ordered", "Reverse-Ordered"};
        int[] sizes = {1000, 2000, 4000, 8000, 16000};


        Comparator<Integer> comparator = Integer::compareTo;


        Config config =null;
        try{
             config = Config.load(InsertionSortComparator.class);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
        
        for (String orderType : orderTypes) {
            System.out.println("\nBenchmarking InsertionSortComparator for: " + orderType);

            for (int n : sizes) {
                // Generating an array based on ordering type
                Integer[] array = generateArray(n, orderType);

                //System.out.println("Array :" + Arrays.toString(array));

                InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(comparator, n, 1, config);


                Benchmark_Timer<Integer[]> benchmarkTimer = new Benchmark_Timer<>(
                        "InsertionSortComparator " + orderType,
                        arr -> sorter.sort(arr, 0, arr.length)
                );


                Supplier<Integer[]> arraySupplier = () -> Arrays.copyOf(array, array.length);

                // Running the benchmark with supplier function
                double timeTaken = benchmarkTimer.runFromSupplier(arraySupplier, 10);


                System.out.printf("Array Size: %d, Time Taken: %.6f ms%n", n, timeTaken);
            }
        }
    }

    private static Integer[] generateArray(int n, String orderType) {
        Integer[] array = new Integer[n];
        Random random = new Random();

        switch (orderType) {
            case "Random":
                for (int i = 0; i < n; i++) array[i] = random.nextInt(10000);
                break;
            case "Ordered":
                for (int i = 0; i < n; i++) array[i] = i;
                break;
            case "Partially-Ordered":
                for (int i = 0; i < n; i++) array[i] = i;
                for (int i = 0; i < n / 10; i++) array[random.nextInt(n)] = random.nextInt(10000);
                break;
            case "Reverse-Ordered":
                for (int i = 0; i < n; i++) array[i] = n - i;
                break;
        }
        return array;
    }

}