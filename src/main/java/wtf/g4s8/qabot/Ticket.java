package wtf.g4s8.qabot;

import java.io.IOException;
import org.cactoos.Text;

/**
 * Ticket to process.
 * @since 1.0
 */
public interface Ticket {

    /**
     * Ticket coordinates.
     * <p>
     * E.g. gh:test/test#1 for Github ticket #1 in test/test repo
     * </p>
     * @return Coordinates
     * @throws IOException If fails
     */
    Text coordinates() throws IOException;

    /**
     * Ticket performer.
     * @return Performer username
     * @throws IOException On failure
     */
    Text performer() throws IOException;
}
