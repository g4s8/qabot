package wtf.g4s8.qabot.checks;

import java.io.IOException;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;

/**
 * QA review atomic check for ticket.
 *
 * @since 1.0
 */
public interface RvCheck {

    /**
     * Run check for ticket.
     * @param ticket Ticket to check
     * @param report QA report
     * @throws IOException On failure
     */
    void run(Ticket ticket, Report report) throws IOException;
}
