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

import com.jcabi.github.Comments;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Event;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.IssueLabels;
import com.jcabi.github.Repo;
import com.jcabi.log.Logger;
import java.io.IOException;
import javax.json.JsonObject;
import org.cactoos.BiFunc;
import org.cactoos.Scalar;
import org.cactoos.func.SolidBiFunc;
import org.cactoos.scalar.Unchecked;
import wtf.g4s8.qabot.Ticket;

/**
 * Ticket as {@link Issue}.
 * @since 1.0
 */
public final class GhTicket implements Issue {

    /**
     * Fetch func.
     */
    private static final BiFunc<Github, Ticket, Issue> fetch = new SolidBiFunc<>(
        (ghb, tkt) -> {
            final String[] parts = tkt.coordinates().asString().split("#");
            Logger.info(GhTicket.class, "resolving ticket %s", tkt.coordinates().asString());
            return ghb.repos().get(
                new Coordinates.Simple(parts[0].replace("gh:", ""))
            ).issues().get(Integer.parseInt(parts[1]));
        }
    );

    /**
     * Origin issue.
     */
    private final Scalar<Issue> origin;

    /**
     * Ctor.
     * @param ghb Github
     * @param tkt Ticket
     */
    public GhTicket(final Github ghb, final Ticket tkt) {
        this.origin = () -> fetch.apply(ghb, tkt);
    }

    @Override
    public Repo repo() {
        return new Unchecked<>(this.origin).value().repo();
    }

    @Override
    public int number() {
        return new Unchecked<>(this.origin).value().number();
    }

    @Override
    public Comments comments() {
        return new Unchecked<>(this.origin).value().comments();
    }

    @Override
    public IssueLabels labels() {
        return new Unchecked<>(this.origin).value().labels();
    }

    @Override
    public Iterable<Event> events() throws IOException {
        return new Unchecked<>(this.origin).value().events();
    }

    @Override
    public boolean exists() throws IOException {
        return new Unchecked<>(this.origin).value().exists();
    }

    @Override
    public void patch(final JsonObject json) throws IOException {
        new Unchecked<>(this.origin).value().patch(json);
    }

    @Override
    public JsonObject json() throws IOException {
        return new Unchecked<>(this.origin).value().json();
    }

    @Override
    public int compareTo(final Issue other) {
        return new Unchecked<>(this.origin).value().compareTo(other);
    }
}
