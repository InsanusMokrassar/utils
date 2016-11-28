package com.insogroup.utils.MailBoxes;

import com.insogroup.utils.MailBoxes.interfaces.MailBox;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by aleksey on 23.04.16.
 */
public class OutputStreamMailBox implements MailBox<String> {

    protected OutputStream os;

    protected void init(OutputStream os) {
        this.os = os;
    }

    public OutputStreamMailBox(OutputStream os) {
        init(os);
    }

    public OutputStreamMailBox() {
        init(System.out);
    }

    @Override
    public void receive(String mail) {
        try {
            os.write(mail.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
