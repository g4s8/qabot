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
