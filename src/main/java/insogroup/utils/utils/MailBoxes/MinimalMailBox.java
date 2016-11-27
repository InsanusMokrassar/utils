package insogroup.utils.utils.MailBoxes;

import insogroup.utils.utils.MailBoxes.interfaces.MailBox;
import insogroup.utils.utils.queues.ArrayQueue;

/**
 * Created by aleksey on 23.04.16.
 */
public class MinimalMailBox<T> implements MailBox<T> {
    protected ArrayQueue<T> mails = new ArrayQueue<>();

    /**
     * Put mail
     * @param mail T object with mail
     */
    @Override
    public void receive(T mail) {
        mails.offer(mail);
    }

    /**
     * Clean list of mails
     */
    public void clean() {
        mails = new ArrayQueue<>();
    }

    /**
     * Return true if has some elements
     * @return Boolean
     */
    public Boolean isReceived() {
        return !mails.isEmpty();
    }

    /**
     * Get first mail
     * @return Inputted T object
     */
    public T getMail() {
        return mails.poll();
    }
}
