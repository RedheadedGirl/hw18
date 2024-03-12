package ru.sbrf.config.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.sbrf.config.dto.Recipe;
import ru.sbrf.config.exceptions.SqlMappingException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.sbrf.config.util.SqlQueries.SELECT_RECIPE_BY_NAME;

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
                }).collect(Collectors.toList());

                if (existing.size() > 0) {
                    Recipe recipe = existing.get(0);
                    recipe.getComponents().add(new ru.sbrf.config.dto.Component(rs.getString("component"),
                            rs.getInt("amount")));
                } else {
                    Recipe recipe = new Recipe();
                    recipe.setName(rs.getString("name"));
                    recipe.getComponents().add(new ru.sbrf.config.dto.Component(rs.getString("component"),
                            rs.getInt("amount")));
                    list.add(recipe);
                }
            }
            return list;
        });

        System.out.println(recipes);
    }

}
