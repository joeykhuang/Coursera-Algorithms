/**
 * @author : Joey Huang
 * @since : 12/12/21, Sun
 **/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] rqueue;
    private int rqueue_size;
    private int rqueue_total_size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.rqueue_size = 0;
        this.rqueue_total_size = 10;
        this.rqueue = (Item[]) new Object[this.rqueue_total_size];
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rqueue = new RandomizedQueue<>();
        for (int i = 0; i < 50; i++) {
            rqueue.enqueue(i);
        }
        Iterator<Integer> itr = rqueue.iterator();
        StdOut.println(itr.next());
        StdOut.println(itr.next());
        StdOut.println(itr.next());
        StdOut.println(itr.hasNext());
        StdOut.println();

        for (int i = 0; i < 50; i++) {
            StdOut.println(rqueue.dequeue());
        }
        StdOut.println(rqueue.isEmpty());
    }

    private void resize_rqueue() {
        if (this.rqueue_size == this.rqueue_total_size - 1) {
            this.rqueue_total_size *= 2;
            Item[] temp_rqueue = (Item[]) new Object[this.rqueue_total_size];
            for (int i = 0; i < this.rqueue_size; i++) {
                temp_rqueue[i] = this.rqueue[i];
            }
            this.rqueue = temp_rqueue;
        }
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.rqueue_size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.rqueue_size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Parameter item is null;");
        }
        this.rqueue[this.rqueue_size++] = item;
        this.resize_rqueue();
    }

    // remove and return a random item
    public Item dequeue() {
        int random_index = StdRandom.uniform(this.rqueue_size);
        Item temp = this.rqueue[random_index];
        this.rqueue[random_index] = this.rqueue[--this.rqueue_size];
        return temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()){
            throw new NoSuchElementException();
        }
        int random_index = StdRandom.uniform(this.rqueue_size);
        return this.rqueue[random_index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        int[] randomOrder = new int[this.rqueue_size];
        for (int i = 0; i < this.rqueue_size; i++) {
            randomOrder[i] = i;
        }
        StdRandom.shuffle(randomOrder);
        return new Iterator<Item>() {
            int pointer = 0;
            @Override
            public boolean hasNext() {
                return pointer < rqueue_size;
            }

            @Override
            public Item next() {
                if (hasNext()) {
                    return rqueue[randomOrder[pointer++]];
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
