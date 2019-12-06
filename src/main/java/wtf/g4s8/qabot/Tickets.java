package wtf.g4s8.qabot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.iterable.Filtered;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.IoChecked;
import org.cactoos.scalar.ItemAt;

/**
 * Tickets store.
 * @since 1.0
 */
public interface Tickets extends Iterable<Ticket> {

    /**
     * Put new ticket to review queue.
     * @param ticket New ticket
     * @throws IOException On failure
     */
    void put(Ticket ticket) throws IOException;

    /**
     * Find ticket by coordinates.
     * @param coord Coordinates
     * @return Ticket
     * @throws IOException On failure
     */
    Ticket find(String coord) throws IOException;

    /**
     * Take all tickets for processing and remove them.
     * @return Tickets iterable
     * @throws IOException On failure
     */
    Iterable<Ticket> take() throws IOException;

    /**
     * Simple tickets in memory storage.
     */
    final class Simple implements Tickets {

        /**
         * List store.
         */
        private final List<Ticket> store;

        /**
         * Ctor.
         */
        public Simple() {
            this(new LinkedList<>());
        }

        public Simple(final List<Ticket> store) {
            this.store = store;
        }

        @Override
        public void put(final Ticket ticket) {
            synchronized (this.store) {
                this.store.add(ticket);
            }
        }

        @Override
        public Iterator<Ticket> iterator() {
            synchronized (this.store) {
                return new ListOf<>(this.store).iterator();
            }
        }

        @Override
        public Ticket find(final String coord) throws IOException {
            synchronized (this.store) {
                return new IoChecked<>(
                    new ItemAt<>(
                        0,
                        new Filtered<>(
                            tkt -> tkt.coordinates().asString().equals(coord),
                            this.store
                        )
                    )
                ).value();
            }
        }

        @Override
        public Iterable<Ticket> take() throws IOException {
            synchronized (this.store) {
                final List<Ticket> copy = new ArrayList<>(this.store);
                this.store.clear();
                return Collections.unmodifiableList(copy);
            }
        }
    }
}
