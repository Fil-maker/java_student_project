package org.example;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.IOException;
import java.sql.*;


public class Main {
    private static Connection connection = null;
    private static Statement statement;
    public static void main(String[] args){
        try {
//             parse csv file to db sqlite
            CsvParser parser = new CsvParser();
            parser.csvToSql("src/main/resources/Country.csv");
            statement = parser.statement;
            connection = parser.connection;

            taskOne();
            taskTwo();
            taskThree();
        } catch (SQLException | IOException | ClassNotFoundException e) {
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
    private static void taskOne() throws SQLException, IOException {
        ResultSet rs = statement.executeQuery("select sr.name, round((100.0 * sum(internet_users) / sum(population)), 2) as 'part' from city_stat join sub_regions sr on sr.id = city_stat.sub_region_id group by sub_region_id order by 1.0 * sum(internet_users) / sum(population) desc");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        while (rs.next()) {
            dataset.setValue(rs.getDouble("part"), "процент пользователей интернета в регионе", rs.getString("name"));
        }
        new BarChart(dataset).launch();
    }
    private static void taskTwo() throws SQLException {
        ResultSet rs = statement.executeQuery("select city_stat.name, min(internet_users) as users from  city_stat join sub_regions sr on sr.id = city_stat.sub_region_id where sr.name = 'Western Europe' group by sr.name");
        System.out.println(String.format("Город в Восточной Европе с наименьшим кол-вом зарегистрированных в ин-ете: %s (%d)",
                rs.getString("name"), rs.getInt("users")));
    }
    private static void taskThree() throws SQLException {
        ResultSet rs = statement.executeQuery("select name, round((100.0 * internet_users / city_stat.population), 2) as part from city_stat where round(1.0 * internet_users / city_stat.population, 2) between 0.75 and 0.85");
        System.out.println("Страны, процент зарегистрированных в интернете пользователей которых находится в промежутке от 75% до 85%");
        while (rs.next()){
            System.out.println(String.format("%s %d%%", rs.getString("name"), rs.getInt("part")));
        }
    }
}