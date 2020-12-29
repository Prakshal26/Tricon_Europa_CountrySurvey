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
            PreparedStatement statement = connection.prepareStatement("INSERT into country_data (xml_id,iso,heading, introduction, subheading, author, general_data, chron_data)" + "VALUES (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            statement.setString(i++, country.getXmlId());
            statement.setString(i++, country.getIso());
            statement.setString(i++, country.getHeading());
            statement.setString(i++, country.getIntroduction());
            statement.setString(i++, country.getSubheading());
            statement.setString(i++, country.getAuthor());
            statement.setString(i++, country.getGeneralData());
            statement.setString(i++, country.getChronData());
            statement.execute();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
