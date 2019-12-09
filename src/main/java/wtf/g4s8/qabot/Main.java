/*
 * MIT License
 *
 * Copyright (c) 2019 g4s8
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights * to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package wtf.g4s8.qabot;

import com.jcabi.log.Logger;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.cactoos.list.ListOf;
import org.takes.http.Exit;
import org.takes.http.FtCli;
import wtf.g4s8.qabot.tk.TkApp;

/**
 * Entry point.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MethodBodyCommentsCheck (500 lines)
 * @checkstyle ExecutableStatementCountCheck (500 lines)
 */
public final class Main implements Runnable {

    /**
     * CLI arguments.
     */
    private final List<String> args;

    /**
     * Tickets.
     */
    private final Tickets tks;

    /**
     * Ctor.
     * @param args Arguments
     * @param tickets Tickets
     */
    private Main(final List<String> args, final Tickets tickets) {
        this.args = Collections.unmodifiableList(args);
        this.tks = tickets;
    }

    @Override
    public void run() {
        try {
            new FtCli(
                new TkApp(new TksValid(this.tks)),
                this.args
            ).start(Exit.NEVER);
        } catch (final IOException err) {
            throw new UncheckedIOException(err);
        }
    }

    /**
     * Main func.
     * @param args Arguments
     */
    public static void main(final String... args) {
        final Tickets.Simple tickets = new Tickets.Simple();
        final var thread = new Thread(new Main(new ListOf<>(args), tickets));
        thread.setDaemon(false);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName(Main.class.getSimpleName());
        thread.start();
        // @todo #1:30min Implement notifications daemon
        //  it should fetch all notifications for qabot
        //  fitter only from 0crat with the request for QA
        //  review. Pares performer username from message
        //  and submit a ticket for processing.
        final var review = new Thread(new ReviewJob(tickets));
        review.setName("review");
        review.setDaemon(false);
        review.setPriority(Thread.NORM_PRIORITY);
        review.start();
        new Shutdown(thread, review).register(Runtime.getRuntime());
        try {
            thread.join();
            review.join();
        } catch (final InterruptedException err) {
            Logger.info(Main.class, "main interrupted %[exception]s", err);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Shutdown.
     * @since 1.0
     */
    private static final class Shutdown implements Runnable {

        /**
         * Threads.
         */
        private final Collection<Thread> tds;

        /**
         * Ctor.
         * @param threads Threads to stop
         */
        Shutdown(final Thread... threads) {
            this(Arrays.asList(threads));
        }

        /**
         * Ctor.
         * @param threads Threads to terminat
         */
        Shutdown(final Collection<Thread> threads) {
            this.tds = Collections.unmodifiableCollection(threads);
        }

        @Override
        public void run() {
            for (final Thread thread : this.tds) {
                thread.interrupt();
            }
        }

        /**
         * Register itself as shutdown hook.
         * @param runtime Runtime to register in
         */
        public void register(final Runtime runtime) {
            runtime.addShutdownHook(new Thread(this));
        }
    }
}
