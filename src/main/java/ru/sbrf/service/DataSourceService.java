package ru.sbrf.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.sbrf.dto.Recipe;
import ru.sbrf.exceptions.SqlMappingException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sbrf.util.SqlQueries.*;

@Component
public class DataSourceService {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public DataSourceService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void createTables(String... sql) {
        jdbcTemplate.batchUpdate(sql);
    }

    public void findRecipeByName(String name) {
        SqlParameterSource parameters = new MapSqlParameterSource("recipe", "%" + name + "%");

        List<Recipe> recipes = namedJdbcTemplate.query(SELECT_RECIPE_BY_NAME, parameters, (ResultSetExtractor<List>) rs -> {
            List<Recipe> list = new ArrayList<>();
            while (rs.next()) {
                List<Recipe> existing = list.stream().filter(rec -> {
                    try {
                        return rec.getName().equals(rs.getString("name"));
                    } catch (SQLException e) {
                        throw new SqlMappingException(e);
                    }
                }).toList();
                if (!existing.isEmpty()) {
                    Recipe recipe = existing.getFirst();
                    recipe.getComponents().add(new ru.sbrf.dto.Component(rs.getString("component"),
                            rs.getInt("amount")));
                } else {
                    Recipe recipe = new Recipe();
                    recipe.setName(rs.getString("name"));
                    recipe.getComponents().add(new ru.sbrf.dto.Component(rs.getString("component"),
                            rs.getInt("amount")));
                    list.add(recipe);
                }
            }
            return list;
        });
        System.out.println(recipes);
    }

    public void addRecipe(Recipe recipe) {
        Long recipesId = jdbcTemplate.queryForObject("SELECT MAX(id) from recipes", Long.class);
        recipesId = incrementId(recipesId);
        jdbcTemplate.update(
                "INSERT INTO recipes (id, name) VALUES (?, ?)", recipesId, recipe.getName()
        );
        for (ru.sbrf.dto.Component component: recipe.getComponents()) {
            Long componentId = jdbcTemplate.queryForObject("SELECT MAX(id) from components", Long.class);
            componentId = incrementId(componentId);
            jdbcTemplate.update(
                    "INSERT INTO components (id, component) VALUES (?, ?)", componentId, component.getComponent()
            );

            Long recCompId = jdbcTemplate.queryForObject("SELECT MAX(id) from recipes_components", Long.class);
            recCompId = incrementId(recCompId);
            jdbcTemplate.update(
                    "INSERT INTO recipes_components (id, id_recipe, id_component, amount) VALUES (?, ?, ?, ?)",
                    recCompId, recipesId, componentId, component.getAmount()
            );
        }
    }

    private Long incrementId(Long id) {
        if (id == null) {
            id = 1L;
        } else {
            id += 1;
        }
        return id;
    }

    public void deleteRecipe(String recipe) {
        jdbcTemplate.update(DELETE_FROM_COMPONENTS, recipe);
        jdbcTemplate.update(DELETE_FROM_RECIPES, recipe);
    }

}
