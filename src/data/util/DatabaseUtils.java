package data.util;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseUtils {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final Logger logger = Logger.getLogger(DatabaseUtils.class.getName());
    private static final String exceptionFormat = "Exception in %s , Message %s , Code %s";

    private static Connection connection;


    public static Connection getConnection(){
        if(connection == null){
            synchronized (DatabaseUtils.class){
                try {
                    if (connection == null){
                        connection = DriverManager.getConnection(URL);
                    }
                } catch (SQLException e) {
                    handleSQLexception("DatabaseUtils.getConnection" , e,logger);
                }
            }
        }
        return connection;
    }

    public static void handleSQLexception(String method , SQLException e , Logger log){
        log.warning(String.format(exceptionFormat,method ,e.getMessage(),e.getErrorCode()));
        throw new RuntimeException(e);
    }
}
