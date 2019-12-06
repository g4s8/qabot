package wtf.g4s8.qabot.tk;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.NoSuchElementException;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.Solid;
import org.takes.HttpException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;
import org.takes.rs.RsJson;
import wtf.g4s8.qabot.Reviews;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.Tickets;

/**
 * Take for {@link wtf.g4s8.qabot.Ticket} review.
 * @since 1.0
 */
public final class TkReviewJson implements Take {

    /**
     * Tickets.
     */
    private final Tickets tkts;

    private final Reviews reviews;

    /**
     * Ctor.
     * @param tickets Tickets
     * @param reviews
     */
    public TkReviewJson(final Tickets tickets, final Reviews reviews) {
        this.tkts = tickets;
        this.reviews = reviews;
    }

    @Override
    public Response act(final Request req) throws Exception {
        try {
            return new RsJson(this.reviews.review(new RqTicket(this.tkts, req)));
        } catch (final NoSuchElementException nex) {
            throw new HttpException(HttpURLConnection.HTTP_NOT_FOUND);
        }
    }

    /**
     * Ticket from request.
     * @since 1.0
     */
    private static final class RqTicket implements Ticket {

        /**
         * Tiket source.
         */
        private final Scalar<Ticket> scalar;

        /**
         * Ctor.
         * @param tkts Tickets
         * @param req Request
         */
        RqTicket(final Tickets tkts, final Request req) {
            this.scalar = new Solid<>(
                () -> tkts.find(new RqHref.Smart(req).single("ticket"))
            );
        }

        @Override
        public Text coordinates() throws IOException {
            return new IoChecked<>(this.scalar).value().coordinates();
        }

        @Override
        public Text performer() throws IOException {
            return new IoChecked<>(this.scalar).value().performer();
        }
    }
}
