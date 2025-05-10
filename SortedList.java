package com.vgb;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ListNode class provides structure for a specific node of the List with constructor, getter and setter methods
 */
public class SortedList<T> implements Iterable<T> {
    private Node<T> head;
    private int size;
    private Comparator<T> comparator;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
        this.size = 0;
    }

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    public class ComparableComparator<T extends Comparable<T>> implements Comparator<T> {
        @Override
        public int compare(final T a, final T b) {
            return a.compareTo(b);
        }
    }
    
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null || comparator.compare(element, head.data) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null && comparator.compare(element, current.next.data) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    public boolean remove(T element) {
        if (head == null) return false;
        if (head.data.equals(element)) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(element)) {
            current = current.next;
        }

        if (current.next == null) return false;

        current.next = current.next.next;
        size--;
        return true;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.data;
    }

    public int size() {
        return size;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}

