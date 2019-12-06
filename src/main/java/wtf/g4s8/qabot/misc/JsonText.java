package wtf.g4s8.qabot.misc;

import java.util.Objects;
import javax.json.JsonObject;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

/**
 * Text from JSON object.
 * @since 1.0
 */
public final class JsonText implements Text {

    /**
     * JSON object.
     */
    private final JsonObject json;

    /**
     * String key.
     */
    private final String key;

    /**
     * Ctor.
     * @param json JSON object
     * @param key String key
     */
    public JsonText(final JsonObject json, final String key) {
        this.json = json;
        this.key = key;
    }

    @Override
    public String asString() {
        return this.json.getString(this.key);
    }

    @Override
    public String toString() {
        return new UncheckedText(this).asString();
    }
}
