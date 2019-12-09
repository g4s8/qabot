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

/**
 * QA review report.
 *
 * @since 1.0
 */
public interface Report {

    /**
     * Report good quality.
     * @param name Check name
     * @param quality Quality to report
     * @param details Check details
     * @throws IOException On failure
     */
    void review(String name, String quality, String details) throws IOException;

    /**
     * Smart report.
     * @since 1.0
     */
    final class Smart implements Report {

        /**
         * Report.
         */
        private final Report origin;

        /**
         * Ctor.
         * @param origin Report
         */
        public Smart(final Report origin) {
            this.origin = origin;
        }

        @Override
        public void review(final String name, final String quality, final String details)
            throws IOException {
            this.origin.review(name, quality, details);
        }

        /**
         * Report good quality.
         * @param name Check name
         * @param details Details
         * @throws IOException On failure
         */
        public void good(final String name, final String details) throws IOException {
            this.review(name, "good", details);
        }

        /**
         * Report acceptable quality.
         * @param name Check name
         * @param details Details
         * @throws IOException On failure
         */
        public void acceptable(final String name, final String details) throws IOException {
            this.review(name, "acceptable", details);
        }

        /**
         * Report bad quality.
         * @param name Check name
         * @param details Details
         * @throws IOException On failure
         */
        public void bad(final String name, final String details) throws IOException {
            this.review(name, "bad", details);
        }
    }
}
