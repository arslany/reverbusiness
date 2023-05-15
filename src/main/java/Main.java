import database.uitl.DatabaseUtils;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PropertyManager;

import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static String jdbcUrl = PropertyManager.getInstance().getProperty("JDBC_URL");
    public static String jdbcUsername = PropertyManager.getInstance().getProperty("JDBC_USERNAME");
    public static String jdbcPassword = PropertyManager.getInstance().getProperty("JDBC_PASSWORD");

    public static int REST_PORT = PropertyManager.getInstance().getPropertyAsInt("REST_PORT");
    public static void main(String[] args) {

        log.info("Starting up");

        log.info("Initializing the database connection");
        if (jdbcUrl == null) {
            jdbcUrl = "jjdbc:postgresql://localhost:5432/postgres;DB_CLOSE_DELAY=-1";
            jdbcUsername = "sa";
            jdbcPassword = "";
        }
        log.info("Connecting to " + jdbcUrl + " as " + jdbcUsername);

        //Connect to database
        DatabaseUtils.configureJdbiOrm(jdbcUrl,
                jdbcUsername, jdbcPassword);

        //run updated script
        DatabaseUtils.updateDatabase();

        Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.http.generateEtags = true;
            config.http.asyncTimeout = 10000l;
           })
                .routes(
                        () -> {
                            crud("/hello/{hello-id}", new HelloWorldController());
                            //crud("/users/:user-id", usersHandler);
                            //crud("/login/:login-id", new LoginController());

                            get("/", ctx -> {
                                ctx.result("up and running");

                            });
                        }
                )
                .start(REST_PORT);

    }

    /*

    Javalin app = Javalin.create( config ->
                config.defaultContentType = "application/json"
            config.autogenerateEtags = true
            config.staticFiles.add("/public")
            config.asyncRequestTimeout = 10_000L
            config.dynamicGzip = true
            config.enforceSsl = true
        }.routes {
            path("users") {
                get(UserController::getAll)
                post(UserController::create)
                path(":user-id") {
                    get(UserController::getOne)
                    patch(UserController::update)
                    delete(UserController::delete)
                }
                ws("events", userController::webSocketEvents)
            }
        }.start(port)

     */

}
