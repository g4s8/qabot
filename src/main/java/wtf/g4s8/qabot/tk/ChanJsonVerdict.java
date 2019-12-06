package wtf.g4s8.qabot.tk;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import wtf.g4s8.qabot.Report;

/**
 * Json channel for QA verdict.
 * @since 1.0
 */
public final class ChanJsonVerdict implements Report {

    /**
     * Json output.
     */
    private final JsonArrayBuilder json;

    private final AtomicInteger quality;

    /**
     * Ctor.
     * @param json Json output
     */
    ChanJsonVerdict(final JsonArrayBuilder json) {
        this.json = json;
        this.quality = new AtomicInteger(2);
    }

    @Override
    public void review(final String name, final String report, final String details)
        throws IOException {
        this.json.add(
            Json.createObjectBuilder()
                .add("check", name)
                .add("quality", report)
                .add("details", details)
        );
        final int qua;
        switch (report) {
            case "good":
                qua = 2;
                break;
            case "acceptable":
                qua = 1;
                break;
            case "bad":
                qua = 0;
                break;
            default:
                throw new IOException(String.format("unsupported quality %s", report));
        }
        if (qua < this.quality.get()) {
            this.quality.set(qua);
        }
    }

    public String report() {
        final int qua = this.quality.get();
        if (qua == 2) {
            return "good";
        }
        if (qua == 1) {
            return "acceptable";
        }
        if (qua == 0) {
            return "bad";
        }
        throw new RuntimeException("should not be reached");
    }
}
