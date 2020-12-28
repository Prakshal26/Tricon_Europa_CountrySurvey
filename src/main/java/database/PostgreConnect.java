package database;

import pojo.Country;

import java.sql.*;

public class PostgreConnect {

    static Connection connection;

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/europa_country_survey",
                            "postgres", "root");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }
        return connection;
    }

    public static void insertCountry(Country country) {

        int id = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT into country_data (xml_id,iso, author)" + "VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            statement.setString(i++, country.getXmlId());
            statement.setString(i++, country.getIso());
            statement.setString(i++, country.getAuthor());
            statement.execute();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
