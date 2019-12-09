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

import com.jcabi.aspects.Tv;
import com.jcabi.github.Github;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import org.cactoos.list.ListOf;

/**
 * Review daemon.
 * @since 1.0
 */
public final class ReviewJob implements Runnable {

    /**
     * Tickets.
     */
    private final Tickets tks;

    /**
     * Ctor.
     * @param tickets Tickets
     */
    public ReviewJob(final Tickets tickets) {
        this.tks = tickets;
    }

    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        while (!thread.isInterrupted()) {
            try {
                this.job();
            } catch (final IOException err) {
                final Logger logger = Logger.getLogger(Thread.currentThread().getName());
                logger.warning(err.getMessage());
            }
            try {
                Thread.sleep(Tv.HUNDRED);
            } catch (final InterruptedException iex) {
                thread.interrupt();
            }
        }
    }

    /**
     * Job action.
     * @throws IOException On failure
     */
    private void job() throws IOException {
        final Collection<Ticket> tickets = new ListOf<>(this.tks.take());
        if (tickets.isEmpty()) {
            return;
        }
        final Github github = new GithubFrom();
        for (final Ticket ticket : tickets) {
            try (var report = new GhReport(github, ticket)) {
                new VerdictFor(ticket, github).say(report);
            }
        }
    }
}
