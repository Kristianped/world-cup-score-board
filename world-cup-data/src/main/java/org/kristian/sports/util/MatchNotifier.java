package org.kristian.sports.util;

/**
 * A match notifier can be implemented by classes that wants to be notified of when matches are started, updated and ended
 */
public interface MatchNotifier {

    /**
     * A match is started
     */
    void matchStarted();

    /**
     * A match is updated
     */
    void matchUpdated();

    /**
     * A match is ended
     */
    void matchEnded();
}
