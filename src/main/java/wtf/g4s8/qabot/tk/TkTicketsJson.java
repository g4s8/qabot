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

import javax.json.Json;
import org.cactoos.scalar.Folded;
import org.cactoos.scalar.IoChecked;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;
import wtf.g4s8.qabot.Tickets;

/**
 * All tickets as JSON.
 * @since 1.0
 */
public final class TkTicketsJson implements Take {

    /**
     * Tickets.
     */
    private final Tickets tkts;

    /**
     * Ctor.
     * @param tickets Tickets
     */
    public TkTicketsJson(final Tickets tickets) {
        this.tkts = tickets;
    }

    @Override
    public Response act(final Request req) throws Exception {
        return new RsJson(
            new IoChecked<>(
                new Folded<>(
                    Json.createArrayBuilder(),
                    (acc, tkt) -> acc.add(
                        Json.createObjectBuilder()
                            .add("id", tkt.coordinates().asString())
                            .build()
                    ),
                    this.tkts
                )
            ).value().build()
        );
    }
}
