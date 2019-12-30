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

import com.jcabi.github.Issue;
import com.jcabi.github.Repo;
import com.jcabi.github.Repos;
import com.jcabi.github.mock.MkGithub;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import wtf.g4s8.oot.TestCase;
import wtf.g4s8.oot.TestReport;
import wtf.g4s8.qabot.checks.CkMessagesHaveRecipient;

/**
 * Recipient messages check test.
 * @since 1.0
 */
public final class CkMessagesHaveRecipientTest implements TestCase {

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void run(final TestReport report) throws IOException {
        final var user = "test";
        final var github = new MkGithub(user);
        final var coord = "test/test";
        final Repo repo = github.repos().create(new Repos.RepoCreate(coord, false));
        final Issue issue = repo.issues().create("test issue", "@user none");
        issue.comments().post("@AsdfZxcv123 hello");
        final List<String[]> reports = new LinkedList<>();
        final var ticket = new Ticket.Fake(
            String.format("%s#%d", repo.coordinates().toString(), issue.number()), user
        );
        new CkMessagesHaveRecipient(github)
            .run(ticket, new Report.Fake(reports));
        if (reports.size() != 1) {
            report.failure(this, "empty reports");
        }
        if (reports.get(0)[1].equals("good")) {
            report.success(this);
        } else {
            report.failure(
                this,
                String.format(
                    "expected good quality but report was %s",
                    String.join("|", reports.get(0))
                )
            );
        }
    }
}
