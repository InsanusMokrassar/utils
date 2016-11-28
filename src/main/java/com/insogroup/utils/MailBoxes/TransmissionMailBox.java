package com.insogroup.utils.MailBoxes;

import com.insogroup.utils.MailBoxes.interfaces.MailBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransmissionMailBox<T> extends MinimalMailBox<T> {

    protected List<MailBox<T>> receivers = new ArrayList<>();

    /**
     * Receive other mailboxes the input mail
     * @param mail JSON with mail
     */
    @Override
    public void receive(T mail) {
        super.receive(mail);
        for (MailBox<T> currentReceiver : receivers) {
            currentReceiver.receive(mail);
        }
    }

    /**
     * Subscribe the mailbox to receivers
     * @param receiver MailBoxes
     */
    public void subscribe(MailBox<T>... receiver) {
        receivers.addAll(Arrays.asList(receiver));
    }
    /**
     * Unsubscribe the mailbox to receivers
     * @param receiver MailBoxes
     */
    public void unsubscribe (MailBox<T>... receiver) {
        receivers.removeAll(Arrays.asList(receiver));
    }

}
