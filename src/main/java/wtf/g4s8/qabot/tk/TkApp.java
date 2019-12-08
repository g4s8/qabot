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

import java.util.regex.Pattern;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkTypes;
import org.takes.facets.fork.TkFork;
import org.takes.rq.RqMethod;
import org.takes.tk.TkWrap;
import wtf.g4s8.qabot.Tickets;

/**
 * Web app.
 * @since 1.0
 */
public final class TkApp extends TkWrap {

    /**
     * Ctor.
     * @param tickets Tickets
     */
    public TkApp(final Tickets tickets) {
        super(
            new TkFork(
                new FkRegex(
                    Pattern.compile("/tickets"),
                    new TkFork(
                        new FkMethods(
                            RqMethod.PUT,
                            new TkNewTicket(tickets)
                        ),
                        new FkMethods(
                            RqMethod.GET,
                            new TkFork(
                                new FkTypes(
                                    "application/json",
                                    new TkTicketsJson(tickets)
                                )
                            )
                        )
                    )
                )
            )
        );
    }
}
