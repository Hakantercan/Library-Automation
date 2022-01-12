import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database {
    private Connection connection;
    public Database() throws SQLException {
        connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "4456");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    public void close(Connection connection) throws SQLException {
        connection.close();
    }

    public void setConnection() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "4456");
    }

    public Connection getConnection() {
        return connection;
    }
}
