package com.insogroup.utils.MailBoxes.interfaces;

import java.io.Serializable;

/**
 * MailBox which can get mails in T class
 * @param <T> Target mails class
 */
public interface MailBox<T> extends Serializable {

    /**
     * Using for receive mailBox with new JSONObject
     * @param mail Mail
     */
    void receive(T mail);

}
