package com.insogroup.utils.MailBoxes.utils;

import com.insogroup.utils.MailBoxes.interfaces.MailBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MailBoxesHelper {
    public static final String MAILBOXES_FIELD = "mail_boxes";

    public static <T> List<MailBox<T>> getMailBoxesFromJSON(JSONObject from) {
        return getMailBoxesFromJSON(from, MAILBOXES_FIELD);
    }

    public static <T> void putMailBoxesInJSON(JSONObject target, List<MailBox<T>> mailBoxes) {
        putMailBoxesInJSON(target, MAILBOXES_FIELD, mailBoxes);
    }

    public static <T> List<MailBox<T>> getMailBoxesFromJSON(JSONObject from, String fromField) {
        List<MailBox<T>> mailBoxes = new ArrayList<>();

        try {
            JSONArray currentArray = from.getJSONArray(fromField);
            for (int i = 0; i < currentArray.length(); i++) {
                MailBox<T> mailBox = (MailBox<T>) currentArray.get(i);
                mailBoxes.add(mailBox);
            }
        } catch (JSONException | ClassCastException e) {
        }
        return mailBoxes;
    }

    public static <T> void putMailBoxesInJSON(JSONObject target, String targetField, List<MailBox<T>> mailBoxes) {
        JSONArray mailBoxesJSONArray = new JSONArray();
        List<MailBox<T>> newMailBoxes = getMailBoxesFromJSON(target, targetField);
        newMailBoxes.addAll(mailBoxes);
        for (MailBox<T> mailBox : newMailBoxes) {
            mailBoxesJSONArray.put(mailBox);
        }
        target.put(targetField, mailBoxesJSONArray);
    }

    public static <T> void deliver(Collection<MailBox<T>> boxes, T... objects) {
        for (MailBox<T> mailBox : boxes) {
            for (T object : objects) {
                mailBox.receive(object);
            }
        }
    }
}
