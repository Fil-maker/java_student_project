package org.example;

public class Country {
    public String name, subregion, region;
    public int users, population;
    public Country(String[] data){
        name = data[0].replace("'","`");
        subregion = data[1].replace("'","`");
        region = data[2].replace("'","`");
        users = Integer.parseInt(data[3].replace(",",""));
        population = Integer.parseInt(data[4].replace(",",""));
    }

    @Override
    public String toString() {
        return String.format("%s (%s %s): %d %d", name, subregion, region, users, population);
    }
}
