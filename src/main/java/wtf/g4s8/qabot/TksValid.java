package wtf.g4s8.qabot;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.cactoos.scalar.IoChecked;

/**
 * Valid tickets.
 *
 * @since 1.0
 */
public final class TksValid implements Tickets {

    /**
     * Valid Github ticket coordinates pattern.
     */
    private static final Pattern PTN = Pattern.compile("gh:[a-zA-Z0-9_-]+/[a-zA-Z0-9_-]+#\\d+");

    /**
     * Origin tickets.
     */
    private final Tickets origin;

    /**
     * Ctor.
     * @param origin Origin tickets
     */
    public TksValid(final Tickets origin) {
        this.origin = origin;
    }

    @Override
    public void put(final Ticket ticket) throws IOException {
        final var value = new IoChecked<>(() -> ticket.coordinates().asString()).value();
        if (!TksValid.PTN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                String.format("Invalid ticket coordinates: %s (expected %s)", value, TksValid.PTN)
            );
        }
        this.origin.put(ticket);
    }

    @Override
    public Ticket find(final String coord) throws IOException {
        return this.origin.find(coord);
    }

    @Override
    public Iterable<Ticket> take() throws IOException {
        return this.origin.take();
    }

    @Override
    public Iterator<Ticket> iterator() {
        return this.origin.iterator();
    }
}
