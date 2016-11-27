package insogroup.utils.utils.queues;

public class CycleQueue<T> extends ArrayQueue<T> {

    protected T currentObject = null;

    public T remove() {
        return super.poll();
    }

    @Override
    public T poll() {
        if (currentObject != null) {
            this.offer(currentObject);
        }
        currentObject = super.poll();
        return currentObject;
    }
}
