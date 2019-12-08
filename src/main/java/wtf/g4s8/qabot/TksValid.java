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
