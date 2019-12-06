package wtf.g4s8.qabot.misc;

import java.io.IOException;
import org.cactoos.Text;
import org.cactoos.scalar.IoChecked;

/**
 *
 * @since
 */
public final class IoText implements Text {

    private final IoChecked<String> src;

    public IoText(final Text text) {
        this.src = new IoChecked<>(text::asString);
    }

    @Override
    public String asString() throws IOException {
        return this.src.value();
    }
}
