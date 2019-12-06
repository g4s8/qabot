package wtf.g4s8.qabot;

import com.jcabi.aspects.Tv;
import com.jcabi.github.Github;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.cactoos.list.ListOf;

/**
 *
 * @since
 */
public final class ReviewJob implements Runnable {

    private final Tickets tks;

    public ReviewJob(final Tickets tickets) {
        this.tks = tickets;
    }

    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        while (!thread.isInterrupted()) {
            try {
                this.job();
            } catch (IOException e) {
                final Logger logger = Logger.getLogger(Thread.currentThread().getName());
                logger.warning(e.getMessage());
                e.printStackTrace();
            }
            try {
                Thread.sleep(Tv.HUNDRED);
            } catch (InterruptedException iex) {
                thread.interrupt();
            }
        }
    }

    private void job() throws IOException {
        final Logger logger = Logger.getLogger(Thread.currentThread().getName());
        final Collection<Ticket> tickets = new ListOf<>(this.tks.take());
        if (tickets.size() == 0) {
            return;
        }
        logger.info(String.format("received %d tickets", tickets.size()));
        final Github github = new GithubFrom();
        for (final Ticket ticket : tickets) {
            try (var report = new GhReport(github, ticket)) {
                new VerdictFor(ticket, github).say(report);
            }
        }
    }
}
