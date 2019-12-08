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
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.cactoos.map.MapEntry;
import org.cactoos.text.UncheckedText;
import wtf.g4s8.qabot.checks.GhTicket;

/**
 * Report to Github ticket.
 * @since 1.0
 */
public final class GhReport implements Report, Closeable {

    /**
     * Github.
     */
    private final Github ghb;

    /**
     * Ticket.
     */
    private final Ticket tkt;

    /**
     * Checks output.
     */
    private final Map<String, MapEntry<String, String>> checks;

    /**
     * Quality state machine.
     */
    private final Quality qstate;

    /**
     * Ctor.
     * @param ghb Github
     * @param tkt Ticket
     */
    public GhReport(final Github ghb, final Ticket tkt) {
        this.ghb = ghb;
        this.tkt = tkt;
        this.checks = new HashMap<>();
        this.qstate = new Quality();
    }

    @Override
    public void review(final String name, final String quality, final String details) {
        Logger.getLogger(this.getClass().getSimpleName())
            .info(String.format("review by '%s': %s (%s)", name, quality, details));
        this.checks.put(name, new MapEntry<>(quality, details));
        this.qstate.push(quality);
    }

    @Override
    public void close() throws IOException {
        final StringBuilder out = new StringBuilder();
        out.append(
            String.format(
                "@%s it's automated quality report.\n",
                new UncheckedText(this.tkt.performer()).asString()
            )
        )
            .append("Quality is ").append(this.qstate.toString()).append('\n')
            .append("Please see QA checks below:\n");
        for (final Map.Entry<String, MapEntry<String, String>> entry : this.checks.entrySet()) {
            out.append(" - ")
                .append(String.format("`[%s]`: ", entry.getValue().getKey()))
                .append(entry.getKey())
                .append(" (").append(entry.getValue().getValue()).append(")\n");
        }
        // @todo #1:30min Links to agree or dispute are not implemented.
        //  Create these links and automatically submit review on "agree"
        //  or save the text on "dispute".
        out.append("\n\nIf you agree with this review click `agree` link,")
            .append("if you don't agree follow `dispute` link.\n")
            .append("\n(bot is in experimental mode)\n\n//cc @g4s8");
        this.checks.clear();
        new GhTicket(this.ghb, this.tkt).comments().post(out.toString());
    }
}
