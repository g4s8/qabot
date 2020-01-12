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

import com.jcabi.github.Comment;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;
import org.cactoos.iterable.Filtered;
import org.cactoos.scalar.LengthOf;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;
import wtf.g4s8.qabot.misc.IoText;

/**
 * Check impediments requests.
 * <p>
 * All impediments registered in the ticket have explanations of the
 * reasons and links to related tickets, if necessary.
 * </p>
 * @since 1.0
 */
public final class CkImpedimentHasReason implements RvCheck {

    /**
     * Check name.
     */
    private static final String NAME = "all impediments have a reason";

    /**
     * Bad quality impediment request.
     */
    private static final Pattern BAD_WAIT =
        Pattern.compile("^@0crat wait(ing)?(\\s+#\\d+[.,;\\s]?)?$");

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Ctor.
     * @param ghb Github
     */
    public CkImpedimentHasReason(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final Issue.Smart issue = new Issue.Smart(new GhTicket(this.ghb, ticket));
        final String performer = new IoText(ticket.performer()).asString();
        final int mistakes = new LengthOf(
            new Filtered<>(
                cmt -> {
                    final Comment.Smart smart = new Comment.Smart(cmt);
                    return Objects.equals(smart.author().login(), performer)
                        && CkImpedimentHasReason.BAD_WAIT.matcher(smart.body()).matches();
                },
                issue.comments().iterate(issue.createdAt())
            )
        ).intValue();
        final var smart = new Report.Smart(report);
        if (mistakes == 0) {
            smart.good(CkImpedimentHasReason.NAME, "no impediments without a reason");
        } else {
            smart.acceptable(
                CkImpedimentHasReason.NAME,
                String.format(
                    "there are %d impediments without a reason", mistakes
                )
            );
        }
    }
}
