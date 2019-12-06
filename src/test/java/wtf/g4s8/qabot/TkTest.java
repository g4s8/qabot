package wtf.g4s8.qabot;

import java.io.IOException;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;
import org.takes.Take;
import org.takes.http.FtRemote;
import wtf.g4s8.oot.TestCase;
import wtf.g4s8.oot.TestReport;

/**
 *
 * @since
 */
public final class TkTest implements TestCase {

    private final Text name;

    private final FtRemote rmt;

    private final FtRemote.Script script;

    public TkTest(final Take take, FtRemote.Script script) throws IOException {
        this(new ClassName(take), new FtRemote(take), script);
    }

    public TkTest(final Text name, final FtRemote remote, final FtRemote.Script script) {
        this.name = name;
        this.rmt = remote;
        this.script = script;
    }

    @Override
    public String name() {
        return new UncheckedText(this.name).asString();
    }

    @Override
    public void run(final TestReport report) throws IOException {
        try {
            this.rmt.exec(this.script);
        } catch (final Exception err) {
            throw new IOException(err);
        }
        report.success(this);
    }

    private static final class ClassName implements Text {

        private final Object object;

        private ClassName(final Object object) {
            this.object = object;
        }

        @Override
        public String asString() throws Exception {
            return this.object.getClass().getSimpleName();
        }
    }
}
