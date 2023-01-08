package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class CsvParser {
    Connection connection = null;
    Statement statement;

    public void csvToSql(String path) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        ArrayList<Country> countries = readCsv(path);
        connection = DriverManager.getConnection("jdbc:sqlite:countries.sqlite");
        statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        createTables(statement);
        for (var c : countries) {
            insertData(statement, c);
        }
    }

    public ArrayList<Country> readCsv(String path) {
        ArrayList<Country> countries = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(path, StandardCharsets.UTF_8);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextLine;
            boolean header = false;
            while ((nextLine = csvReader.readNext()) != null) {
                if (!header) {
                    header = true;
                    continue;
                }
                countries.add(new Country(nextLine));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return countries;
    }

    private void createTables(Statement statement) throws SQLException {
        statement.executeUpdate("create table if not exists regions(id integer primary key, name string unique)");
        statement.executeUpdate("create table if not exists sub_regions(id integer primary key, name string unique, region_id integer, foreign key (region_id) references region(id))");
        statement.executeUpdate("create table if not exists city_stat(id integer primary key, name string unique, internet_users integer, population integer, sub_region_id integer, foreign key (sub_region_id) references sub_region(id))");
    }

    private void insertData(Statement statement, Country country) throws SQLException {
        statement.executeUpdate(String.format("insert or ignore into regions (name) values ('%s')", country.name));
        statement.executeUpdate(String.format("insert or ignore into sub_regions (name, region_id) values ('%s', (select id from regions where regions.name = '%s'))", country.subregion, country.region));
        statement.executeUpdate(String.format("insert or ignore into city_stat (name, sub_region_id, internet_users, population) values ('%s', (select id from sub_regions where sub_regions.name = '%s'), %d, %d)", country.name, country.subregion, country.users, country.population));
    }
}
