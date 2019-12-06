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
     * @param details Check details
     * @throws IOException On failure
     */
    void review(String name, String quality, String details) throws IOException;

    /**
     * Smart report.
     * @since 1.0
     */
    final class Smart implements Report {

        private final Report origin;

        public Smart(final Report origin) {
            this.origin = origin;
        }

        @Override
        public void review(final String name, final String quality, final String details)
            throws IOException {
            this.origin.review(name, quality, details);
        }

        public void good(final String name, final String details) throws IOException {
            this.review(name, "good", details);
        }

        public void acceptable(final String name, final String details) throws IOException {
            this.review(name, "acceptable", details);
        }

        public void bad(final String name, final String details) throws IOException {
            this.review(name, "bad", details);
        }
    }
}
