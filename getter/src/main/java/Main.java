import entities.Region;
import org.json.JSONException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static Connection connection;

    private static void runScrapFromKeySet(int i, Connection connection) throws SQLException, IOException {
        try {
            Parser parser = new Parser();
            parser.setKey(keyset[0]);
            for (Region r : Connector.getRegions(connection)) {
                parser.processWithRegion(r, connection);
            }
            System.out.println("DONE!");
        } catch (JSONException e) {
            runScrapFromKeySet(i + 1, connection);
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        try {
            connection = DriverManager.getConnection(url, user, password);
            runScrapFromKeySet(0, connection);
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
            connection.close();
        }
    }
}
