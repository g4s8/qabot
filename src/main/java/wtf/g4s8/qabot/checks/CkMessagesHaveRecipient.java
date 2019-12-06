package wtf.g4s8.qabot.checks;

import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.json.JsonObject;
import org.cactoos.iterable.Filtered;
import org.cactoos.scalar.LengthOf;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;

/**
 *
 * @since
 */
public final class CkMessagesHaveRecipient implements RvCheck {

    /**
     * Username regex, see https://stackoverflow.com/a/30281147/1723695.
     */
    private static final Pattern PTN_MENTION =
        Pattern.compile("(.*\\s)?@([a-z0-9](?:-?[a-z0-9]){0,38}).*", Pattern.DOTALL);

    private static final String NAME = "all messages have recipient";

    private final Github ghb;

    public CkMessagesHaveRecipient(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final var issue = new Issue.Smart(new GhTicket(this.ghb, ticket));
        final int wrong = new LengthOf(
            new Filtered<>(
                cmt -> {
                    final JsonObject json = cmt.json();
                    final String body = json.getString("body");
                    final String author = json.getJsonObject("user").getString("login");
                    final boolean matches = CkMessagesHaveRecipient.PTN_MENTION.matcher(body).matches();
                    return !matches && !author.equals("0crat");
                },
                issue.comments().iterate(issue.createdAt())
            )
        ).intValue();
        final var smart = new Report.Smart(report);
        if (wrong == 0) {
            smart.good(NAME, "all messages have recipient");
        } else {
            smart.acceptable(
                NAME,
                String.format(
                    "there are %d messages without recipient", wrong
                )
            );
        }
    }
}
