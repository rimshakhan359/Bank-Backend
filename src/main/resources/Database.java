import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection getConnection(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankinfo","root","root");
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}