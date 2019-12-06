package wtf.g4s8.qabot.checks;

import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.RepoCommit;
import java.io.IOException;
import org.cactoos.iterable.Filtered;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.scalar.LengthOf;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;

/**
 * Check that a bug has solution provided.
 *
 * @since 1.0
 */
public final class CkBugHasSolution implements RvCheck {

    private static final String NAME = "bug has solution";

    private final Github ghb;

    public CkBugHasSolution(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final Issue.Smart issue = new Issue.Smart(new GhTicket(this.ghb, ticket));
        if (issue.isPull()) {
            return;
        }
        final int commits = new LengthOf(
            new Filtered<>(
                cmt -> new RepoCommit.Smart(cmt).message()
                    .contains(String.format("#%d", issue.number())),
                issue.repo().commits().iterate(
                    new MapOf<>(
                        new MapEntry<>("author", issue.assignee().login()),
                        new MapEntry<>("since", issue.createdAt().toInstant().toString())
                    )
                )
            )
        ).intValue();
        final var smart = new Report.Smart(report);
        if (commits > 0) {
            smart.good(NAME, String.format("%d commits were found", commits));
        } else {
            smart.bad(NAME, "no referenced commits were found for this ticket");
        }
    }
}
