package wtf.g4s8.qabot;

import com.jcabi.github.Github;
import java.io.IOException;
import org.cactoos.list.ListOf;
import wtf.g4s8.qabot.checks.CkBugHasSolution;
import wtf.g4s8.qabot.checks.CkMessagesHaveRecipient;
import wtf.g4s8.qabot.checks.CkPerformer;
import wtf.g4s8.qabot.checks.CkPrIssues;
import wtf.g4s8.qabot.checks.RvCheck;

/**
 *
 * @since
 */
public final class VerdictFor implements Verdict {

    /**
     * QA review checks.
     */
    private final Iterable<RvCheck> checks;

    private final Ticket tkt;

    public VerdictFor(final Ticket tkt, final Github ghb) {
        this.tkt = tkt;
        this.checks = new ListOf<>(
            new CkBugHasSolution(ghb),
            new CkPerformer(ghb),
            new CkMessagesHaveRecipient(ghb),
            new CkPrIssues(ghb)
        );
    }

    @Override
    public void say(final Report report) throws IOException {
        for (final RvCheck check : this.checks) {
            check.run(this.tkt, report);
        }
    }
}
