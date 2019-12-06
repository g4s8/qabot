package wtf.g4s8.qabot.tk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsWithStatus;
import wtf.g4s8.qabot.GithubFrom;
import wtf.g4s8.qabot.Reviews;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.Tickets;
import wtf.g4s8.qabot.VerdictFor;
import wtf.g4s8.qabot.misc.JsonText;

/**
 * Take to accept new tickets for review.
 * @since 1.0
 */
public final class TkNewTicket implements Take {

    private static final Executor EXEC =
        Executors.newCachedThreadPool();

    /**
     * Tickets.
     */
    private final Tickets tkts;

    private final Reviews reviews;

    /**
     * Ctor.
     * @param tkts Tickets
     * @param reviews
     */
    public TkNewTicket(final Tickets tkts, final Reviews reviews) {
        this.tkts = tkts;
        this.reviews = reviews;
    }

    @Override
    public Response act(final Request req) throws Exception {
        final JsonTicket ticket = new JsonTicket(new RqJson(req).value().asJsonObject());
        this.tkts.put(ticket);
        return new RsWithStatus(HttpURLConnection.HTTP_ACCEPTED);
    }

    /**
     * JSON {@link Ticket} implementation.
     * @since 1.0
     */
    private static final class JsonTicket implements Ticket {

        /**
         * Ticket coordinates.
         */
        private final Text coord;

        /**
         * Performer.
         */
        private final Text perf;

        /**
         * Ctor.
         * @param json JSON
         */
        public JsonTicket(final JsonObject json) {
            this(new JsonText(json, "id"), new JsonText(json, "performer"));
        }

        /**
         * Primary ctor.
         * @param coord Coordinates.
         * @param perf Performer
         */
        public JsonTicket(final Text coord, final Text perf) {
            this.coord = coord;
            this.perf = perf;
        }

        @Override
        public Text coordinates() {
            return this.coord;
        }

        @Override
        public Text performer() {
            return this.perf;
        }
    }
}
