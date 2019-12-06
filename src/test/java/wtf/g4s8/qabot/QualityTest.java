package wtf.g4s8.qabot;

import org.llorllale.cactoos.matchers.TextHasString;
import wtf.g4s8.oot.SimpleTest;
import wtf.g4s8.oot.TestCase;

/**
 *
 * @since
 */
public final class QualityTest extends TestCase.Wrap {

    public QualityTest() {
        super(
            new SimpleTest<>(
                "quality can be updated",
                new Quality()
                    .push("acceptable")
                    .push("good"),
                new TextHasString("acceptable")
            )
        );
    }
}
