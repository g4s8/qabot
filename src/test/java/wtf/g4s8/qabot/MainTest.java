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
 */
public final class MainTest extends TestCase.Wrap {

    private MainTest() throws Exception {
        super(
            new SequentialTests(
                MainTest.tkNewTicketsTest(),
                new QualityTest()
            )
        );
    }

    public static void test() throws Exception {
        try (FailingReport report = new FailingReport(new ConsoleReport())) {
            new MainTest().run(report);
        }
    }

    private static TestCase tkNewTicketsTest() throws IOException {
        final var store = new LinkedList<Ticket>();
        return new TkTest(
            new TkNewTicket(new Tickets.Simple(store), new Reviews()),
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
