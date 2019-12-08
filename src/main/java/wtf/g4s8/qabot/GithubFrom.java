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
 * Github from environment variables.
 * @since 1.0
 */
public final class GithubFrom implements Github {

    /**
     * Origin github.
     */
    private final Scalar<Github> origin;

    /**
     * Ctor.
     */
    public GithubFrom() {
        this(System.getenv());
    }

    /**
     * Ctor.
     * @param env Environment
     */
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
