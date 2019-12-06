package wtf.g4s8.qabot;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

/**
 *
 * @since
 */
public final class Quality implements Text {

    private enum Values {
        GOOD,
        ACCEPTABLE,
        BAD
    }

    private final AtomicReference<Values> quality;

    public Quality() {
        this(new AtomicReference<>(Values.GOOD));
    }

    Quality(final AtomicReference<Values> quality) {
        this.quality = quality;
    }

    public Quality push(final String quality) {
        final Values change = Values.valueOf(quality.toUpperCase(Locale.US));
        final Values current = this.quality.get();
        if (change.compareTo(current) > 0) {
            this.quality.set(change);
        }
        return this;
    }

    @Override
    public String asString() {
        return this.quality.get().name().toLowerCase(Locale.US);
    }

    @Override
    public String toString() {
        return new UncheckedText(this).asString();
    }
}
