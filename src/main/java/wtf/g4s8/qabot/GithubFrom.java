package wtf.g4s8.qabot;

import com.jcabi.github.Gists;
import com.jcabi.github.Github;
import com.jcabi.github.Gitignores;
import com.jcabi.github.Limits;
import com.jcabi.github.Markdown;
import com.jcabi.github.Organizations;
import com.jcabi.github.Repos;
import com.jcabi.github.RtGithub;
import com.jcabi.github.Search;
import com.jcabi.github.Users;
import com.jcabi.http.Request;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.json.JsonObject;
import org.cactoos.Scalar;
import org.cactoos.scalar.Solid;
import org.cactoos.scalar.Unchecked;

/**
 *
 * @since
 */
public final class GithubFrom implements Github {

    private final Scalar<Github> origin;

    public GithubFrom() {
        this(System.getenv());
    }

    public GithubFrom(final Map<String, String> env) {
        this.origin = new Solid<>(
            () -> new RtGithub(Objects.requireNonNull(env.get("GH_TOKEN")))
        );
    }

    @Override
    public Request entry() {
        return new Unchecked<>(this.origin).value().entry();
    }

    @Override
    public Repos repos() {
        return new Unchecked<>(this.origin).value().repos();
    }

    @Override
    public Gists gists() {
        return new Unchecked<>(this.origin).value().gists();
    }

    @Override
    public Users users() {
        return new Unchecked<>(this.origin).value().users();
    }

    @Override
    public Organizations organizations() {
        return new Unchecked<>(this.origin).value().organizations();
    }

    @Override
    public Markdown markdown() {
        return new Unchecked<>(this.origin).value().markdown();
    }

    @Override
    public Limits limits() {
        return new Unchecked<>(this.origin).value().limits();
    }

    @Override
    public Search search() {
        return new Unchecked<>(this.origin).value().search();
    }

    @Override
    public Gitignores gitignores() throws IOException {
        return new Unchecked<>(this.origin).value().gitignores();
    }

    @Override
    public JsonObject meta() throws IOException {
        return new Unchecked<>(this.origin).value().meta();
    }

    @Override
    public JsonObject emojis() throws IOException {
        return new Unchecked<>(this.origin).value().emojis();
    }
}
