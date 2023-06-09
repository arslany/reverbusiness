import controller.LoginController;
import controller.PermissionsController;
import controller.UserController;
import database.uitl.DatabaseUtils;
import exception.CustomException;
import exception.CustomExceptionHandler;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AccessPointRoles;
import util.PropertyManager;
import util.ServicesAccessManager;

import static io.javalin.apibuilder.ApiBuilder.*;

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

        //Create a Javalin instance with some configuration options
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.http.generateEtags = true;
            config.http.asyncTimeout = 10000l;
            config.accessManager(new ServicesAccessManager());
        });
        app.exception(CustomException.class, new CustomExceptionHandler());
        //Define the routes for the app
        app.routes(() -> {
            //Use crud methods for hello and user resources
            crud("/hello/{hello-id}", new HelloWorldController());
            crud("/user/{user-id}", new UserController());
            crud("/permissions/{permission-id}", new PermissionsController());
            //Use get method for finding user by username
            get("/users/username/{username}", ctx -> {
                new UserController().findByUserName(ctx, ctx.pathParam("username"));
            });
            //Use post method for logging in user with credentials
            post("/login", ctx -> {
                new UserController().login(ctx);
            }, AccessPointRoles.ANYONE);
            post("/logout",ctx -> {
               new LoginController().logout(ctx, ctx.pathParam("userName"));
            });

            //Use get method for the root path
            get("/", ctx -> {
                ctx.result("up and running");
            }, AccessPointRoles.ANYONE);
        });

        //Start the app on the specified port
                app.start(REST_PORT);

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
