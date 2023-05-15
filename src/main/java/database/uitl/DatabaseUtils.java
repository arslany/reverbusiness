package database.uitl;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flywaydb.core.Flyway;

import static com.gitlab.mvysny.jdbiorm.JdbiOrm.jdbi;

/**
 * Utility class implementing support for most popular databases.
 * @author mavi
 */
public class DatabaseUtils {
    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);
    public static void ddl(@Language("sql") @NotNull String sql) {
        jdbi().withHandle(handle -> handle
                .createUpdate(sql)
                .execute());
    }

    public static void configureJdbiOrm(@NotNull String jdbcUrl, @NotNull String username, @NotNull String password) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMinimumIdle(0);

        log.info("Connecting to " + jdbcUrl);
        JdbiOrm.setDataSource(new HikariDataSource(hikariConfig));
    }

    public static void updateDatabase(){
        log.info("Migrating database to newest version");
        // see https://flywaydb.org/ for more information. In short, Flyway will
        // apply scripts from src/main/resources/db/migration/, but only those that
        // haven't been applied yet.
        final Flyway flyway = Flyway.configure()
                .dataSource(JdbiOrm.getDataSource())
                .locations("db/migration/common")
                .load();
        flyway.migrate();

        //log.info("Generating testing data");
        //generateTestingData();

        log.info("Started");
    }


    /**
     * Experiment on PostgreSQL. Run PostgreSQL in Docker simply:
     * <pre>
     * docker run --rm -ti -e POSTGRES_PASSWORD=mysecretpassword -p 127.0.0.1:5432:5432 postgres:10.3
     * </pre>
     */
    public static void postgreSQL(@NotNull Runnable block) {
        configureJdbiOrm("jdbc:postgresql://localhost:5432/postgres",
            "postgres", "mysecretpassword");
        try {
            ddl("create table if not exists Person (\n" +
                    "                id bigserial primary key,\n" +
                    "                name varchar(400) not null,\n" +
                    "                age integer not null,\n" +
                    "                dateOfBirth date,\n" +
                    "                created timestamp,\n" +
                    "                modified timestamp,\n" +
                    "                alive boolean,\n" +
                    "                maritalStatus varchar(200)" +
                    ")");
            block.run();
        } finally {
            JdbiOrm.destroy();
        }
    }

    /**
     * Starts an in-memory H2 database. After the main() method finishes, the database is gone, along with its contents.
     */
    public static void h2(@NotNull Runnable block) {
        configureJdbiOrm("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        try {
            ddl("create table if not exists Person (\n" +
                    "                id bigint primary key auto_increment,\n" +
                    "                name varchar not null,\n" +
                    "                age integer not null,\n" +
                    "                dateOfBirth date,\n" +
                    "                created timestamp,\n" +
                    "                modified timestamp,\n" +
                    "                alive boolean,\n" +
                    "                maritalStatus varchar" +
                    ")");
            block.run();
        } finally {
            JdbiOrm.destroy();
        }
    }

    /**
     * Experiment on MariaDB. Run MariaDB 10.1.31 in Docker simply:
     * <pre>
     * docker run --rm -ti -e MYSQL_ROOT_PASSWORD=mysqlpassword -e MYSQL_DATABASE=db -e MYSQL_USER=testuser -e MYSQL_PASSWORD=mysqlpassword -p 127.0.0.1:3306:3306 mariadb:10.1.31
     * </pre>
     */
    public static void mariadb(@NotNull Runnable block) {
        configureJdbiOrm("jdbc:mariadb://localhost:3306/db", "testuser", "mysqlpassword");
        try {
            ddl("create table if not exists Person (\n" +
                    "                id bigint primary key auto_increment,\n" +
                    "                name varchar(400) not null,\n" +
                    "                age integer not null,\n" +
                    "                dateOfBirth date,\n" +
                    "                created timestamp(3) NULL,\n" +
                    "                modified timestamp(3) NULL,\n" +
                    "                alive boolean,\n" +
                    "                maritalStatus varchar(200)\n" +
                    "                 )");
            block.run();
        } finally {
            JdbiOrm.destroy();
        }
    }

    /**
     * Experiment on MySQL. Run MySQL 5.7.21 in Docker simply:
     * <pre>
     * docker run --rm -ti -e MYSQL_ROOT_PASSWORD=mysqlpassword -e MYSQL_DATABASE=db -e MYSQL_USER=testuser -e MYSQL_PASSWORD=mysqlpassword -p 127.0.0.1:3306:3306 mysql:5.7.21
     * </pre>
     */
    public static void mysql(@NotNull Runnable block) {
        configureJdbiOrm("jdbc:mysql://localhost:3306/db", "testuser", "mysqlpassword");
        try {
            ddl("create table if not exists Person (\n" +
                    "                id bigint primary key auto_increment,\n" +
                    "                name varchar(400) not null,\n" +
                    "                age integer not null,\n" +
                    "                dateOfBirth date,\n" +
                    "                created timestamp(3) NULL,\n" +
                    "                modified timestamp(3) NULL,\n" +
                    "                alive boolean,\n" +
                    "                maritalStatus varchar(200)\n" +
                    "                 )");
            block.run();
        } finally {
            JdbiOrm.destroy();
        }
    }

    /**
     * Experiment on MySQL. Run MySQL 5.7.21 in Docker simply:
     * <pre>
     * docker run --rm -ti -e MYSQL_ROOT_PASSWORD=mysqlpassword -e MYSQL_DATABASE=db -e MYSQL_USER=testuser -e MYSQL_PASSWORD=mysqlpassword -p 127.0.0.1:3306:3306 mysql:5.7.21
     * </pre>
     */
    public static void mssql(@NotNull Runnable block) {
        configureJdbiOrm("jdbc:sqlserver://localhost:1433;database=tempdb", "sa", "myPASSWD123");
        try {
            ddl("IF OBJECT_ID('Person', 'U') IS NOT NULL DROP TABLE Person;");
            ddl("create table Person (\n" +
                "id bigint primary key IDENTITY(1,1) not null,\n" +
                "name varchar(400) not null,\n" +
                "age integer not null,\n" +
                "dateOfBirth datetime NULL,\n" +
                "created datetime NULL,\n" +
                "modified datetime NULL,\n" +
                "alive bit,\n" +
                "maritalStatus varchar(200)\n" +
                 ")");
            block.run();
        } finally {
            JdbiOrm.destroy();
        }
    }
}
