package ru.sbrf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sbrf.config.service.DataSourceService;
import ru.sbrf.config.util.SqlQueries;

import java.util.Scanner;

@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        var factory = SpringApplication.run(MyApplication.class);
        var dataSourceService = factory.getBean(DataSourceService.class);

        dataSourceService.createTables(SqlQueries.CREATE_TABLE_COMPONENTS, SqlQueries.CREATE_TABLE_RECIPES,
                SqlQueries.CREATE_TABLE_RECIPES_COMPONENTS);

        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - find a recipe, 2 - add a recipe, 3 - delete, 0 - exit:");

        while(scanner.hasNext()) {
            String input = scanner.next();
            switch (input) {
                case "0":
                    break;
                case "1":
                    System.out.println("Enter a recipe name:");
                    dataSourceService.findRecipeByName(scanner.next());
                    break;
                case "2":
                    System.out.println("добавление");
                    break;
                case "3":
                    System.out.println("удаление");
                    break;
                default:
                    System.out.println("Повторите ввод");
            }
            System.out.println("1 - find a recipe, 2 - add a recipe, 3 - delete, 0 - exit:");
        }

    }


}