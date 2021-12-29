/**
 * @author : Joey Huang
 * @since : 12/12/21, Sun
 **/

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Item[] deque;
    private int deque_size;
    private int deque_total_size;
    private int forward_pointer;
    private int backward_pointer;

    public Deque() {
        this.deque_size = 0;
        this.deque_total_size = 4;
        this.forward_pointer = 2;
        this.backward_pointer = 1;
        this.deque = (Item[]) new Object[this.deque_total_size];
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println(deque.isEmpty());
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(3);
        deque.addLast(4);
        deque.addFirst(0);
        Iterator<Integer> itr = deque.iterator();
        StdOut.println(itr.next());
        StdOut.println(itr.next());
        StdOut.println(itr.next());
        StdOut.println(itr.hasNext());
        StdOut.println();
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeFirst());
        StdOut.println("Size is: " + deque.size());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
    }

    private void resize_deque() {
        if (this.forward_pointer == 0 || this.backward_pointer == this.deque_total_size - 1) {
            this.deque_total_size *= 2;
            Item[] temp_deque = (Item[]) new Object[this.deque_total_size];
            int new_front_pointer = this.deque_total_size / 2 - 1;
            int new_back_pointer = new_front_pointer;
            for (int i = forward_pointer; i <= this.backward_pointer; i++) {
                temp_deque[new_back_pointer] = this.deque[i];
                new_back_pointer++;
            }
            new_back_pointer--;
            this.deque = temp_deque;
            this.forward_pointer = new_front_pointer;
            this.backward_pointer = new_back_pointer;
        }
    }

    public boolean isEmpty() {
        return this.deque_size == 0;
    }

    public int size() {
        return this.deque_size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Parameter item is null;");
        }
        this.forward_pointer--;
        this.deque[this.forward_pointer] = item;
        this.deque_size++;
        this.resize_deque();
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Parameter item is null;");
        }
        this.backward_pointer++;
        this.deque[this.backward_pointer] = item;
        this.deque_size++;
        this.resize_deque();
    }

    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        Item returnedItem = this.deque[this.forward_pointer];
        this.deque_size--;
        this.forward_pointer++;
        resize_deque();
        return returnedItem;
    }

    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        Item returnedItem = this.deque[this.backward_pointer];
        this.deque_size--;
        this.backward_pointer--;
        resize_deque();
        return returnedItem;
    }

    public Iterator<Item> iterator() {
        return new Iterator<>() {
            int pointer_location = forward_pointer;

            @Override
            public boolean hasNext() {
                return pointer_location < backward_pointer;
            }

            @Override
            public Item next() {
                if (hasNext()) {
                    return deque[pointer_location++];
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove(){
                throw new UnsupportedOperationException();
            }
        };
    }
}