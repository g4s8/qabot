package wtf.g4s8.qabot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.json.JsonObject;
import org.cactoos.text.UncheckedText;

/**
 *
 * @since
 */
public final class Reviews {

    private final Map<String, JsonObject> store;

    public Reviews() {
        this(new HashMap<>());
    }

    public Reviews(final Map<String, JsonObject> store) {
        this.store = store;
    }

    public void put(final Ticket ticket, final JsonObject json) throws IOException {
        this.store.put(new UncheckedText(ticket.coordinates()).asString(), json);
    }

    public JsonObject review(final Ticket ticket) throws IOException {
        return Optional.ofNullable(this.store.get(new UncheckedText(ticket.coordinates()).asString())).orElseThrow();
    }
}
