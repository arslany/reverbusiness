import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class HelloWorldController implements  CrudHandler {
    /**
     * post: http://localhost:7000/hello?Hello=This is hello&World=This is world
     * body :
     * {
     *     "first": " first param value",
     *     "second": "second param value"
     * }
     *
     * header:
     * Authorization:
     * "Bearer " + UUID;
     *
     * @param context
     */
    @Override
    public void create(@NotNull Context context) {
        String strBody = context.body();
        byte[] bytBody = context.bodyAsBytes();
        InputStream inputStream = context.bodyInputStream();
        Map<String, String> params = context.pathParamMap();
        Map<String, List<String>> queryParaMap = context.queryParamMap();
        String firstFormParam = context.formParam("Hello");
        String firstQueryParam = context.queryParam("Hello");
        BasicAuthCredentials cred = context.basicAuthCredentials();
        Map<String, String> headerMap = context.headerMap();
        String authToken = context.header("Authorization");

        context.result("Ok" + strBody);
        context.status(201);
    }

    @Override
    public void delete(@NotNull Context context, @NotNull String s) {
        context.result("Delete Ok");
        context.status(200);
    }

    /**
     * http://localhost:7000/hello
     * @param context
     */
    @Override
    public void getAll(@NotNull Context context) {
        context.json("{abc}");
        context.status(200);
    }

    /**
     * http://localhost:7000/hello/1
     * @param context
     * @param s
     */
    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {
        String str = "Hello from getOne " + s;
        context.json(str);
        context.status(HttpStatus.ACCEPTED);
    }

    /**
     * http://localhost:7000/hello/1
     * Type :PATCH
     * @param context
     * @param s
     */
    @Override
    public void update(@NotNull Context context, @NotNull String s) {
        String str = "Hello from update " + s;
        context.json(str);
        context.status(HttpStatus.ACCEPTED);
    }
}
