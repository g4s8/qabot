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

import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.wire.VerboseWire;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import javax.json.Json;
import wtf.g4s8.oot.ConsoleReport;
import wtf.g4s8.oot.FailingReport;
import wtf.g4s8.oot.SequentialTests;
import wtf.g4s8.oot.TestCase;
import wtf.g4s8.qabot.tk.TkNewTicket;

/**
 * Tests entry point.
 * @since 1.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings({"PMD.TestClassWithoutTestCases", "PMD.JUnit4TestShouldUseTestAnnotation"})
public final class MainTest extends TestCase.Wrap {

    /**
     * Ctor.
     * @throws Exception On failure
     */
    private MainTest() throws Exception {
        super(
            new SequentialTests(
                MainTest.tkNewTicketsTest(),
                new QualityTest(),
                new CkMessagesHaveRecipientTest()
            )
        );
    }

    /**
     * Tests entry point.
     * @throws Exception On failure
     */
    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    public static void test() throws Exception {
        try (FailingReport report = new FailingReport(new ConsoleReport())) {
            new MainTest().run(report);
        }
    }

    /**
     * New tickets take test.
     * @return Test case
     * @throws IOException On failure
     */
    private static TestCase tkNewTicketsTest() throws IOException {
        final var store = new LinkedList<Ticket>();
        return new TkTest(
            new TkNewTicket(new Tickets.Simple(store)),
            home -> new JdkRequest(home)
                .method(Request.PUT)
                .header("Content-type", "application/json; charset=UTF-8")
                .body()
                .set(
                    Json.createObjectBuilder()
                        .add("id", "gh:test/test#1")
                        .build()
                ).back()
                .through(VerboseWire.class)
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_ACCEPTED)
        );
    }
}
