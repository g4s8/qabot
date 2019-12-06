package wtf.g4s8.qabot;

import java.io.IOException;

/**
 * Quality verdict.
 * @since 1.0
 */
public interface Verdict {

    /**
     * Say verdict to QA communication channel.
     * @param report Review report
     * @throws IOException On failure
     */
    void say(Report report) throws IOException;
}
