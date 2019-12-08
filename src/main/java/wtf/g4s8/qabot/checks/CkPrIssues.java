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
import com.jcabi.github.Issue;
import com.jcabi.github.Pull;
import com.jcabi.github.PullComment;
import java.io.IOException;
import java.util.Collections;
import org.cactoos.iterable.Filtered;
import org.cactoos.scalar.LengthOf;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.misc.IoText;

/**
 * Check PR has at least 3 comments from assignee.
 *
 * @since 1.0
 */
public final class CkPrIssues implements RvCheck {

    /**
     * Check name.
     */
    private static final String NAME = "PR has 3 review comments";

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Ctor.
     * @param ghb Github
     */
    public CkPrIssues(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final var issue = new Issue.Smart(new GhTicket(this.ghb, ticket));
        if (!issue.isPull()) {
            return;
        }
        final var pull = new Pull.Smart(issue.pull());
        final var smart = new Report.Smart(report);
        final String performer = new IoText(ticket.performer()).asString();
        final int comments = new LengthOf(
            new Filtered<>(
                cmt -> new PullComment.Smart(cmt).author().equals(performer),
                pull.comments().iterate(Collections.emptyMap())
            )
        ).intValue();
        if (comments >= 3) {
            smart.good(NAME, String.format("performer found %d issues in code review", comments));
        } else if (comments > 0) {
            smart.acceptable(
                NAME, String.format("performer found only %d issues in code review", comments)
            );
        } else {
            smart.bad(NAME, "performer didn't find any issue in code review");
        }
    }
}
