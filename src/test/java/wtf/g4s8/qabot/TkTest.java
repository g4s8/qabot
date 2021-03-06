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
import org.cactoos.text.UncheckedText;
import org.takes.Take;
import org.takes.http.FtRemote;
import wtf.g4s8.oot.TestCase;
import wtf.g4s8.oot.TestReport;

/**
 * Takes test.
 * @since 1.0
 */
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public final class TkTest implements TestCase {

    /**
     * Test name.
     */
    private final Text nme;

    /**
     * Takes remote.
     */
    private final FtRemote rmt;

    /**
     * Script to run.
     */
    private final FtRemote.Script script;

    /**
     * Ctor.
     * @param take Take to test
     * @param script Script to run
     * @throws IOException On failure
     */
    public TkTest(final Take take, final FtRemote.Script script) throws IOException {
        this(new ClassName(take), new FtRemote(take), script);
    }

    /**
     * Ctor.
     * @param name Test name
     * @param remote Remote front
     * @param script Script to run
     */
    public TkTest(final Text name, final FtRemote remote, final FtRemote.Script script) {
        this.nme = name;
        this.rmt = remote;
        this.script = script;
    }

    @Override
    public String name() {
        return new UncheckedText(this.nme).asString();
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void run(final TestReport report) throws IOException {
        try {
            this.rmt.exec(this.script);
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception err) {
            throw new IOException(err);
        }
        report.success(this);
    }

    /**
     * Class name as text.
     * @since 1.0
     */
    private static final class ClassName implements Text {

        /**
         * Object.
         */
        private final Object object;

        /**
         * Ctor.
         * @param object Object to read the class
         */
        ClassName(final Object object) {
            this.object = object;
        }

        @Override
        public String asString() throws Exception {
            return this.object.getClass().getSimpleName();
        }
    }
}
