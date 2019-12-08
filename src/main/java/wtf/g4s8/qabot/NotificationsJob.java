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
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.cactoos.iterable.Mapped;

/**
 * Notifications daemon.
 * @since 1.0
 */
public final class NotificationsJob implements Runnable {

    /**
     * Tickets.
     */
    private final Tickets tickets;

    /**
     * Ctor.
     * @param tickets Tickets
     */
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

    /**
     * Job.
     * @throws IOException On failure
     */
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
