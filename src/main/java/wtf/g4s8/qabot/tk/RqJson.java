package wtf.g4s8.qabot.tk;

import com.g4s8.mime.MimeType;
import com.g4s8.mime.MimeTypeOf;
import com.g4s8.mime.MimeTypeSmart;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import javax.json.Json;
import javax.json.JsonStructure;
import org.cactoos.Scalar;
import org.takes.HttpException;
import org.takes.Request;
import org.takes.rq.RqHeaders;

/**
 * Json structure from request.
 * <p>
 * Perform request {@code Content-type} header validation before parsing.
 * If request doesn't contain {@code application/json} mime-type,
 * {@code 400 Bad-request} error will
 * be thrown. Using mime-type charset for JSON decoding or system default
 * {@link Charset#defaultCharset()} if not present.
 * </p>
 * @since 1.0
 */
public final class RqJson implements Scalar<JsonStructure> {

    /**
     * Request.
     */
    private final Request req;

    /**
     * Ctor.
     * @param req Takes request
     */
    public RqJson(final Request req) {
        this.req = req;
    }

    @Override
    public JsonStructure value() throws IOException {
        final MimeType mime = new MimeTypeOf(new RqHeaders.Smart(this.req).single("content-type"));
        if (!RqJson.isJson(mime)) {
            throw new HttpException(
                HttpURLConnection.HTTP_UNSUPPORTED_TYPE,
                String.format("application/json content expected but was %s", mime)
            );
        }
        return Json.createReader(new InputStreamReader(this.req.body(), RqJson.charset(mime))).read();
    }

    /**
     * Check if mime type is JSON type.
     * @param mime Mime-type of the request
     * @return True if JSON
     * @throws IOException On failure
     */
    private static boolean isJson(final MimeType mime) throws IOException {
        return "application".equalsIgnoreCase(mime.type())
            && "json".equalsIgnoreCase(mime.subtype());
    }

    /**
     * Content charset.
     * @param mime Mime type of the request
     * @return Charset if present in mime-type or default JDK charset
     * @throws IOException On failure
     */
    private static Charset charset(final MimeType mime) throws IOException {
        final Charset charset;
        if (mime.params().containsKey("charset")) {
            charset = Charset.forName(new MimeTypeSmart(mime).charset());
        } else {
            charset = Charset.defaultCharset();
        }
        return charset;
    }
}
