package com.pasey.peculiardevices.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CircularList<T> {
    private final List<T> list = new ArrayList<>();

    @SafeVarargs
    public CircularList(T... elements) {
        list.addAll(Arrays.asList(elements));
    }

    public void add(T element) {
        list.add(element);
    }

    public T next(T current) {
        int index = list.indexOf(current);
        if (index == -1) {
            throw new IllegalArgumentException("Element not found: " + current);
        }
        int nextIndex = (index + 1) % list.size();
        return list.get(nextIndex);
    }

    // Optional: getter for size
    public int size() {
        return list.size();
    }
}