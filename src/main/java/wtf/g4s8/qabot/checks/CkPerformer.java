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

package wtf.g4s8.qabot.checks;

import com.jcabi.github.Github;
import java.io.IOException;
import java.util.Objects;
import javax.json.JsonObject;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.misc.IoText;

/**
 * Check performer is not the same person as a reporter.
 * <p>
 * Problem reporter and problem solver are two different persons;
 * http://www.yegor256.com/2014/11/24/principles-of-bug-tracking.html#1.-keep-it-one-on-one
 * </p>
 * @since 1.0
 */
public final class CkPerformer implements RvCheck {

    /**
     * Check name.
     */
    private static final String NAME = "performer is not a solver";

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Ctor.
     * @param ghb Github
     */
    public CkPerformer(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final JsonObject json = new GhTicket(this.ghb, ticket).json();
        final String author = json.getJsonObject("user").getString("login");
        final String performer = new IoText(ticket.performer()).asString();
        final var smart = new Report.Smart(report);
        if (Objects.equals(performer, author)) {
            smart.acceptable(
                NAME, String.format("performer and solver is %s", author)
            );
        } else {
            smart.good(
                NAME, String.format("author: %s, performer: %s", author, performer)
            );
        }
    }
}
