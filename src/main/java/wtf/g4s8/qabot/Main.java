package wtf.g4s8.qabot;

import com.jcabi.log.Logger;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import org.cactoos.list.ListOf;
import org.takes.http.Exit;
import org.takes.http.FtCli;
import wtf.g4s8.qabot.tk.TkApp;

/**
 *
 * @since
 */
public final class Main implements Runnable {

    private final List<String> args;
    private final Tickets tks;


    private Main(final List<String> args, final Tickets tickets) {
        this.args = Collections.unmodifiableList(args);
        this.tks = tickets;
    }

    @Override
    public void run() {
        try {
            new FtCli(
                new TkApp(new TksValid(this.tks), new Reviews()),
                this.args
            ).start(Exit.NEVER);
        } catch (final IOException err) {
            throw new UncheckedIOException(err);
        }
    }

    public static void main(String[] args) {
        final Tickets.Simple tickets = new Tickets.Simple();

        final var thread = new Thread(new Main(new ListOf<>(args), tickets));
        thread.setDaemon(false);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName(Main.class.getSimpleName());
        thread.start();

        final var notifications = new Thread(new NotificationsJob(tickets));
        notifications.setName("notifications[d]");
        notifications.setDaemon(true);
        notifications.setPriority(Thread.NORM_PRIORITY);
//        notifications.start();

        final var review = new Thread(new ReviewJob(tickets));
        review.setName("review");
        review.setDaemon(false);
        review.setPriority(Thread.NORM_PRIORITY);
        review.start();

        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> {
                thread.interrupt();
                review.interrupt();
                notifications.interrupt();
            })
        );

        try {
            thread.join();
            review.join();
        } catch (final InterruptedException err) {
            Logger.info(Main.class, "main interrupted %[exception]s", err);
            Thread.currentThread().interrupt();
        }
    }

}
