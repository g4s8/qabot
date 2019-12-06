package wtf.g4s8.qabot;

import com.jcabi.aspects.Tv;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.cactoos.iterable.Mapped;

/**
 *
 * @since
 */
public final class NotificationsJob implements Runnable {

    private final Tickets tickets;

    public NotificationsJob(final Tickets tickets) {
        this.tickets = tickets;
    }

    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        final Logger logger = Logger.getLogger(thread.getName());
        while (!thread.isInterrupted()) {
            try {
                this.job();
            } catch (final IOException err) {
                logger.warning(String.format("job failed: %s", err.getMessage()));
            }
            try {
                Thread.sleep(Tv.THOUSAND * 5);
            } catch (InterruptedException err) {
                logger.info(String.format("thread stopped: %s", err.getMessage()));
                thread.interrupt();
            }
        }
    }

    private void job() throws IOException {
        final var ghb = new GithubFrom();
        final Logger logger = Logger.getLogger(Thread.currentThread().getName());
        final Iterable<JsonObject> threads = new Mapped<>(
            JsonValue::asJsonObject,
            ghb.entry()
                .uri().path("/notifications").back()
                .fetch().as(JsonResponse.class)
                .json().readArray()
        );
        for (final JsonObject thread : threads) {
            logger.info(
                String.format("got notification: %s %s", thread.getString("id"), thread.getJsonObject("subject"))
            );
        }
    }
}
