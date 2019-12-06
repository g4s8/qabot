package wtf.g4s8.qabot.tk;

import java.util.regex.Pattern;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkParams;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkTypes;
import org.takes.facets.fork.TkFork;
import org.takes.facets.fork.TkMethods;
import org.takes.rq.RqMethod;
import org.takes.tk.TkWrap;
import wtf.g4s8.qabot.Reviews;
import wtf.g4s8.qabot.Tickets;

/**
 *
 * @since
 */
public final class TkApp extends TkWrap {

    public TkApp(final Tickets tickets, final Reviews reviews) {
        super(
            new TkFork(
                new FkRegex(
                    Pattern.compile("/tickets"),
                    new TkFork(
                        new FkMethods(
                            RqMethod.PUT,
                            new TkNewTicket(tickets, reviews)
                        ),
                        new FkMethods(
                            RqMethod.GET,
                            new TkFork(
                                new FkTypes(
                                    "application/json",
                                    new TkTicketsJson(tickets)
                                )
                            )
                        )
                    )
                ),
                new FkRegex(
                    Pattern.compile("/reviews"),
                    new TkFork(
                        new FkParams(
                            "ticket", Pattern.compile("gh:[a-zA-Z0-9-_]+/[a-zA-Z0-9-_]+#\\d+"),
                            new TkMethods(
                                new TkFork(
                                    new FkTypes(
                                        "application/json",
                                        new TkReviewJson(tickets, reviews)
                                    )
                                ),
                                RqMethod.GET
                            )
                        )
                    )
                )
            )
        );
    }
}
