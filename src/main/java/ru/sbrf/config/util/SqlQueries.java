package ru.sbrf.config.util;

public class SqlQueries {

    public static final String CREATE_TABLE_COMPONENTS = "CREATE TABLE IF NOT EXISTS components (" +
            "    id serial NOT NULL, " +
            "    component text NOT NULL, " +
            "    PRIMARY KEY (id) " +
            ");";

    public static final String CREATE_TABLE_RECIPES = "CREATE TABLE IF NOT EXISTS recipes (" +
            "    id serial NOT NULL, " +
            "    name text NOT NULL, " +
            "    PRIMARY KEY (id)" +
            ");";

    public static final String CREATE_TABLE_RECIPES_COMPONENTS = "CREATE TABLE IF NOT EXISTS recipes_components (" +
            "    id serial NOT NULL," +
            "    id_recipe integer NOT NULL," +
            "    id_component integer NOT NULL," +
            "    amount integer NOT NULL," +
            "    PRIMARY KEY (id)," +
            "    CONSTRAINT component_fk FOREIGN KEY (id_component)" +
            "        REFERENCES components (id), " +
            "    CONSTRAINT recipe_fk FOREIGN KEY (id_recipe)" +
            "        REFERENCES recipes (id) " +
            ");";

    public static final String SELECT_RECIPE_BY_NAME = "SELECT r.id, r.\"name\", c.component, rc.amount from recipes r \n" +
            "INNER JOIN recipes_components rc \n" +
            "ON rc.id_recipe = r.id\n" +
            "INNER JOIN components c\n" +
            "ON c.id = rc.id_component\n" +
            "WHERE r.\"name\" LIKE :recipe";
}
