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

package wtf.g4s8.qabot.misc;

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
