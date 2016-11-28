package com.insogroup.utils.IOC;

import com.insogroup.utils.ClassExtractor.exceptions.ClassExtractException;
import com.insogroup.utils.ClassExtractor.ClassExtractor;
import com.insogroup.utils.IOC.exceptions.ResolveStrategyException;
import com.insogroup.utils.IOC.interfaces.IOCStrategy;
import com.insogroup.utils.MailBoxes.TransmissionMailBox;
import com.insogroup.utils.MailBoxes.interfaces.MailBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IOC container using for resolving some dependencies
 */
public class IOC {

    protected static final Map<String, IOCStrategy> strategies = new HashMap<>();
    protected static final TransmissionMailBox<String> targetMailBox = new TransmissionMailBox<>();

    /**
     * Register dependency in system
     * @param key Using for ident strategy in the future
     * @param strategy Some strategy
     */
    public static void register(String key, IOCStrategy strategy) {
        synchronized (strategies) {
            strategies.put(key, strategy);
        }
        targetMailBox.receive("Strategy for key \"" + key + "\" with strategy \"" + strategy.toString() + "\" registered.\n");
    }

    public static void subscribe(MailBox<String>... mailBoxes) {
        targetMailBox.subscribe(mailBoxes);
    }

    public static void unsubscribe(MailBox<String>... mailBoxes) {
        targetMailBox.unsubscribe(mailBoxes);
    }

    public static void subscribe(List<MailBox<String>> mailBoxes) {
        for (MailBox<String> mailBox : mailBoxes) {
            targetMailBox.subscribe(mailBox);
        }
    }

    public static void unsubscribe(List<MailBox<String>> mailBoxes) {
        for (MailBox<String> mailBox : mailBoxes) {
            targetMailBox.unsubscribe(mailBox);
        }
    }

    /**
     * Call for get dependency
     * @param key Key of dependency
     * @param args Args for dependency
     * @param <T> Type of target dependency
     * @return Resolved dependency
     * @throws ResolveStrategyException Throw when strategy is not registered
     */
    public static <T> T resolve(String key, Object... args) throws ResolveStrategyException{
        try {
            IOCStrategy strategy = strategies.get(key);
            return (T) strategy.getInstance(args);
        } catch (NullPointerException e) {
            try {
                return ClassExtractor.extract(key, args);
            } catch (ClassExtractException e1) {
                throw new ResolveStrategyException("Can't find strategy by key or extract class: " + key, e);
            }
        }
    }
}
