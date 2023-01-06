package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:countries.sqlite");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            createTables(statement);
            printCsv(statement, "src/main/resources/Country.csv");
//            ResultSet rs = statement.executeQuery("select * from person");
//            while(rs.next())
//            {
            // read the result set
//                System.out.println("name = " + rs.getString("name"));
//                System.out.println("id = " + rs.getInt("id"));
//            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
//    src/main/resources/Country.csv

    private static void createTables(Statement statement) throws SQLException {
        statement.executeUpdate("create table if not exists regions(id integer primary key, name string unique)");
        statement.executeUpdate("create table if not exists sub_regions(id integer primary key, name string unique, region_id integer, foreign key (region_id) references region(id))");
        statement.executeUpdate("create table if not exists city_stat(id integer primary key, name string unique, internet_users integer, population integer, sub_region_id integer, foreign key (sub_region_id) references sub_region(id))");
    }

    private static void insertData(Statement statement, String[] data) throws SQLException {
        statement.executeUpdate(String.format("insert or ignore into regions (name) values ('%s')", data[2]));
        statement.executeUpdate(String.format("insert or ignore into sub_regions (name, region_id) values ('%s', (select id from regions where regions.name = '%s'))", data[1], data[2]));
        int users, population;
        users = Integer.parseInt(data[3].replace(",",""));
        population = Integer.parseInt(data[4].replace(",",""));
        statement.executeUpdate(String.format("insert or ignore into city_stat (name, sub_region_id, internet_users, population) values ('%s', (select id from sub_regions where sub_regions.name = '%s'), %d, %d)", data[0].replace("'", ""), data[1], users, population));
    }

    public static void printCsv(Statement statement, String path) {
        try {
            FileReader fileReader = new FileReader(path, StandardCharsets.UTF_8);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextLine;
            boolean header = false;
            int c = 0;
            while ((nextLine = csvReader.readNext()) != null) {
                if (!header) {
                    header = true;
                    continue;
                }
                System.out.println(c);
                System.out.println(nextLine[0]);
                insertData(statement, nextLine);
                c++;
            }
        } catch (IOException | CsvValidationException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}