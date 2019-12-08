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

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

/**
 * Quality state machine.
 * @since 1.0
 */
public final class Quality implements Text {

    /**
     * Possible values.
     * @since 1.0
     */
    private enum Values {
        GOOD,
        ACCEPTABLE,
        BAD
    }

    /**
     * Current state.
     */
    private final AtomicReference<Values> quality;

    /**
     * Ctor.
     */
    public Quality() {
        this(new AtomicReference<>(Values.GOOD));
    }

    /**
     * Ctor.
     * @param quality Initial state
     */
    Quality(final AtomicReference<Values> quality) {
        this.quality = quality;
    }

    /**
     * Push new quality.
     * <p>
     * It may change current state if new quality value is lower than current.
     * Possible scenarios:
     * <ul>
     *     <li>good -> * = *</li>
     *     <li>acceptable -> good = acceptable</li>
     *     <li>acceptable -> acceptable = acceptable</li>
     *     <li>acceptable -> bad = bad</li>
     *     <li>bad -> * = bad</li>
     * </ul>
     * where * is any quality.
     * </p>
     * @param quality New quality string
     * @return self
     */
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
