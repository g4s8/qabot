package wtf.g4s8.qabot.tk;

import javax.json.Json;
import org.cactoos.scalar.Folded;
import org.cactoos.scalar.IoChecked;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;
import wtf.g4s8.qabot.Tickets;

/**
 *
 * @since
 */
public final class TkTicketsJson implements Take {

    private final Tickets tkts;

    public TkTicketsJson(final Tickets tickets) {
        this.tkts = tickets;
    }

    @Override
    public Response act(final Request req) throws Exception {
        return new RsJson(
            new IoChecked<>(
                new Folded<>(
                    Json.createArrayBuilder(),
                    (acc, tkt) -> acc.add(
                        Json.createObjectBuilder()
                            .add("id", tkt.coordinates().asString())
                            .build()
                    ),
                    this.tkts
                )
            ).value().build()
        );
    }
}
