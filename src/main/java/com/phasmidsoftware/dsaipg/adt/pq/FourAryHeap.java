package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;

public class FourAryHeap<K> extends PriorityQueue<K> {
    private final boolean maxHeap;
    private final Comparator<? super K> comparator;
    private final List<K> heap;

    public FourAryHeap(int capacity, Comparator<? super K> comparator, boolean maxHeap, boolean useFloydsTrick) {
        super(capacity, (Comparator<K>) comparator);
        this.maxHeap = maxHeap;
        this.comparator = comparator;
        this.heap = new ArrayList<>(capacity);
        if (useFloydsTrick) {
            buildHeap();
        }
    }

    private void buildHeap() {
        // Start from the last non-leaf node
        int startIndex = (heap.size() / 4) - 1;
        for (int i = startIndex; i >= 0; i--) {
            sink(i);
        }
    }


    public boolean offer(K key) {
        heap.add(key);
        swim(heap.size() - 1);
        return true;
    }


    public K poll() {
        if (heap.isEmpty()) return null;
        K root = heap.get(0);
        K last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            sink(0);
        }
        return root;
    }


    public K peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    @Override
    public int size() {
        return heap.size();
    }

    private void swim(int index) {
        while (index > 0) {
            int parentIndex = getParent(index);
            if (compare(heap.get(index), heap.get(parentIndex)) >= 0) break;
            Collections.swap(heap, index, parentIndex);
            index = parentIndex;
        }
    }

    @Override
    protected void sink(int index) {
        while (true) {
            int smallest = index;
            for (int i = 1; i <= 4; i++) {
                int childIndex = getChild(index, i);
                if (childIndex < heap.size() && compare(heap.get(childIndex), heap.get(smallest)) < 0) {
                    smallest = childIndex;
                }
            }
            if (smallest == index) break;
            Collections.swap(heap, index, smallest);
            index = smallest;
        }
    }

    private int compare(K a, K b) {
        return maxHeap ? comparator.compare(b, a) : comparator.compare(a, b);
    }

    private int getParent(int index) {
        return (index - 1) / 4;
    }

    private int getChild(int index, int childNumber) {
        return 4 * index + childNumber;
    }
}
