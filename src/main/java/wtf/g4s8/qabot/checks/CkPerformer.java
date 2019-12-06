package wtf.g4s8.qabot.checks;

import com.jcabi.github.Github;
import java.io.IOException;
import java.util.Objects;
import javax.json.JsonObject;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.misc.IoText;

/**
 * Check performer is not the same person as a reporter.
 * <p>
 * Problem reporter and problem solver are two different persons;
 * http://www.yegor256.com/2014/11/24/principles-of-bug-tracking.html#1.-keep-it-one-on-one
 * </p>
 * @since 1.0
 */
public final class CkPerformer implements RvCheck {

    private static final String NAME = "performer is not a solver";

    private final Github ghb;

    public CkPerformer(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final JsonObject json = new GhTicket(this.ghb, ticket).json();
        final String author = json.getJsonObject("user").getString("login");
        final String performer = new IoText(ticket.performer()).asString();
        final var smart = new Report.Smart(report);
        if (Objects.equals(performer, author)) {
            smart.acceptable(
                NAME, String.format("performer and solver is %s", author)
            );
        } else {
            smart.good(
                NAME, String.format("author: %s, performer: %s", author, performer)
            );
        }
    }
}
