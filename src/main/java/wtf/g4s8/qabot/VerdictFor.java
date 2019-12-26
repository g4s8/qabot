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

import com.jcabi.github.Github;
import java.io.IOException;
import org.cactoos.list.ListOf;
import wtf.g4s8.qabot.checks.CkBugHasSolution;
import wtf.g4s8.qabot.checks.CkImpedimentHasReason;
import wtf.g4s8.qabot.checks.CkMessagesHaveRecipient;
import wtf.g4s8.qabot.checks.CkPerformer;
import wtf.g4s8.qabot.checks.CkPrIssues;
import wtf.g4s8.qabot.checks.RvCheck;

/**
 * Verdict for ticket.
 * @since 1.0
 */
public final class VerdictFor implements Verdict {

    /**
     * QA review checks.
     */
    private final Iterable<RvCheck> checks;

    /**
     * Tickets.
     */
    private final Ticket tkt;

    /**
     * Verdict for Github ticket.
     * @param tkt Ticket
     * @param ghb Github
     */
    public VerdictFor(final Ticket tkt, final Github ghb) {
        this.tkt = tkt;
        this.checks = new ListOf<>(
            new CkBugHasSolution(ghb),
            new CkPerformer(ghb),
            new CkMessagesHaveRecipient(ghb),
            new CkPrIssues(ghb),
            new CkImpedimentHasReason(ghb)
        );
    }

    @Override
    public void say(final Report report) throws IOException {
        for (final RvCheck check : this.checks) {
            check.run(this.tkt, report);
        }
    }
}
