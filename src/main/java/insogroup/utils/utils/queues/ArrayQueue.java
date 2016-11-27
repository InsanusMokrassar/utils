package insogroup.utils.utils.queues;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;

public class ArrayQueue<E> extends AbstractQueue<E> {

    protected LinkedList<E> queue = new LinkedList<>();

    /**
     * Return queue iterator
     * @return Queue iterator
     */
    @Override
    public Iterator iterator() {
        return queue.iterator();
    }

    /**
     * Return sum of elements in queue
     * @return Sum of elements in queue
     */
    @Override
    public int size() {
        return queue.size();
    }

    /**
     * Put object to the last position
     * @param object New object
     * @return True if putting was correct
     */
    @Override
    public boolean offer(E object) {
        return queue.add(object);
    }

    /**
     * Return the first element and remove it from his list
     * @return The first element
     */
    @Override
    public E poll() {
        E object = queue.get(0);
        queue.remove(0);
        return object;
    }

    /**
     * Return the first element
     * @return The first element
     */
    @Override
    public E peek() {
        return queue.get(0);
    }

    @Override
    public String toString() {
        return "ArrayQueue{" +
                "queue=" + queue +
                '}';
    }
}
