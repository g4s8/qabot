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
import com.jcabi.github.RepoCommit;
import java.io.IOException;
import org.cactoos.iterable.Filtered;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.scalar.LengthOf;
import wtf.g4s8.qabot.Report;
import wtf.g4s8.qabot.Ticket;

/**
 * Check that a bug has solution provided.
 *
 * @since 1.0
 */
public final class CkBugHasSolution implements RvCheck {

    /**
     * Check name.
     */
    private static final String NAME = "bug has solution";

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Ctor.
     * @param ghb Github
     */
    public CkBugHasSolution(final Github ghb) {
        this.ghb = ghb;
    }

    @Override
    public void run(final Ticket ticket, final Report report) throws IOException {
        final Issue.Smart issue = new Issue.Smart(new GhTicket(this.ghb, ticket));
        if (issue.isPull()) {
            return;
        }
        final int commits = new LengthOf(
            new Filtered<>(
                cmt -> new RepoCommit.Smart(cmt).message()
                    .contains(String.format("#%d", issue.number())),
                issue.repo().commits().iterate(
                    new MapOf<>(
                        new MapEntry<>("author", issue.assignee().login()),
                        new MapEntry<>("since", issue.createdAt().toInstant().toString())
                    )
                )
            )
        ).intValue();
        final var smart = new Report.Smart(report);
        if (commits > 0) {
            smart.good(CkBugHasSolution.NAME, String.format("%d commits were found", commits));
        } else {
            smart.bad(CkBugHasSolution.NAME, "no referenced commits were found for this ticket");
        }
    }
}
