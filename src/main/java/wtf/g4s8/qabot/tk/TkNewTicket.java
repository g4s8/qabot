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

package wtf.g4s8.qabot.tk;

import java.net.HttpURLConnection;
import javax.json.JsonObject;
import org.cactoos.Text;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsWithStatus;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.Tickets;
import wtf.g4s8.qabot.misc.JsonText;

/**
 * Take to accept new tickets for review.
 * @since 1.0
 */
public final class TkNewTicket implements Take {

    /**
     * Tickets.
     */
    private final Tickets tkts;

    /**
     * Ctor.
     * @param tkts Tickets
     */
    public TkNewTicket(final Tickets tkts) {
        this.tkts = tkts;
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
        JsonTicket(final JsonObject json) {
            this(new JsonText(json, "id"), new JsonText(json, "performer"));
        }

        /**
         * Primary ctor.
         * @param coord Coordinates.
         * @param perf Performer
         */
        JsonTicket(final Text coord, final Text perf) {
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
