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
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.json.JsonObject;
import org.cactoos.iterable.Filtered;
import org.cactoos.scalar.LengthOf;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;

/**
 * Check all messages have recipients.
 * @since 1.0
 */
public final class CkMessagesHaveRecipient implements RvCheck {

    /**
     * Users to ignore.
     */
    private static final Set<String> IGNORE = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList("0crat", "0pdd"))
    );

    /**
     * Username regex, see https://stackoverflow.com/a/30281147/1723695.
     */
    private static final Pattern PTN_MENTION =
        Pattern.compile("(.*\\s)?@([a-z0-9](?:-?[a-z0-9]){0,38}).*", Pattern.DOTALL);

    /**
     * Check name.
     */
    private static final String NAME = "all messages have recipient";

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Ctor.
     * @param ghb Github
     */
    public CkMessagesHaveRecipient(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final var issue = new Issue.Smart(new GhTicket(this.ghb, ticket));
        final int wrong = new LengthOf(
            new Filtered<>(
                cmt -> {
                    final JsonObject json = cmt.json();
                    final String body = json.getString("body");
                    final String author = json.getJsonObject("user").getString("login");
                    final boolean matches =
                        CkMessagesHaveRecipient.PTN_MENTION.matcher(body).matches();
                    return !matches && !CkMessagesHaveRecipient.IGNORE.contains(author);
                },
                issue.comments().iterate(issue.createdAt())
            )
        ).intValue();
        final var smart = new Report.Smart(report);
        if (wrong == 0) {
            smart.good(CkMessagesHaveRecipient.NAME, "no messages without recipient");
        } else {
            smart.acceptable(
                CkMessagesHaveRecipient.NAME,
                String.format(
                    "there are %d messages without recipient", wrong
                )
            );
        }
    }
}
