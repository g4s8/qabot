package wtf.g4s8.qabot;

import com.jcabi.github.Github;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.cactoos.map.MapEntry;
import org.cactoos.text.UncheckedText;
import wtf.g4s8.qabot.checks.GhTicket;

/**
 *
 * @since
 */
public final class GhReport implements Report, Closeable {

    private final Github ghb;

    private final Ticket tkt;

    private final Map<String, MapEntry<String, String>> checks;

    private final Quality qstate;

    public GhReport(final Github ghb, final Ticket tkt) {
        this.ghb = ghb;
        this.tkt = tkt;
        this.checks = new HashMap<>();
        this.qstate = new Quality();
    }

    @Override
    public void review(final String name, final String quality, final String details) throws IOException {
        Logger.getLogger(this.getClass().getSimpleName())
            .info(String.format("review by '%s': %s (%s)", name, quality, details));
        this.checks.put(name, new MapEntry<>(quality, details));
        this.qstate.push(quality);
    }

    @Override
    public void close() throws IOException {
        final StringBuilder out = new StringBuilder();
        out.append(String.format("@%s it's automated quality report.\n", new UncheckedText(this.tkt.performer()).asString()))
            .append("Quality is ").append(this.qstate.toString()).append('\n')
            .append("Please see QA checks below:\n");
        for (final Map.Entry<String, MapEntry<String, String>> entry : this.checks.entrySet()) {
            out.append(" - ").append(String.format("`%s`: ", entry.getKey()))
                .append(entry.getValue().getKey())
                .append(" (").append(entry.getValue().getValue()).append(")\n");
        }
        out.append("\n\nIf you agree with this review click `agree` link,")
            .append("if you don't agree follow `dispute` link.\n")
            .append("\n(bot is in experimental mode)\n\n//cc @g4s8");
        this.checks.clear();
        new GhTicket(this.ghb, this.tkt).comments().post(out.toString());
    }
}
