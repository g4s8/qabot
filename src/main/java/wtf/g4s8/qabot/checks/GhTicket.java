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
 *
 * @since
 */
public final class GhTicket implements Issue {

    private static final BiFunc<Github, Ticket, Issue> fetch = new SolidBiFunc<>(
        (ghb, tkt) -> {
            final String[] parts = tkt.coordinates().asString().split("#");
            Logger.info(GhTicket.class, "resolving ticket %s", tkt.coordinates().asString());
            return ghb.repos().get(
                new Coordinates.Simple(parts[0].replace("gh:", ""))
            ).issues().get(Integer.parseInt(parts[1]));
        }
    );

    private final Scalar<Issue> origin;

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
