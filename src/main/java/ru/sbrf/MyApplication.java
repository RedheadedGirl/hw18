package ru.sbrf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sbrf.dto.Component;
import ru.sbrf.dto.Recipe;
import ru.sbrf.service.CookingBookService;
import ru.sbrf.util.SqlQueries;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        var factory = SpringApplication.run(MyApplication.class);
        var dataSourceService = factory.getBean(CookingBookService.class);

        dataSourceService.createTables(SqlQueries.CREATE_TABLE_COMPONENTS, SqlQueries.CREATE_TABLE_RECIPES,
                SqlQueries.CREATE_TABLE_RECIPES_COMPONENTS);

        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - find a recipe, 2 - add a recipe, 3 - delete, 0 - exit:");

        while(scanner.hasNext()) {
            String input = scanner.next();
            switch (input) {
                case "0":
                    System.exit(0);
                    break;
                case "1":
                    System.out.println("Enter a recipe name:");
                    dataSourceService.findRecipeByName(scanner.next());
                    break;
                case "2":
                    addRecipe(scanner, dataSourceService);
                    break;
                case "3":
                    System.out.println("Enter a full recipe name, which must be deleted");
                    dataSourceService.deleteRecipe(scanner.next());
                    break;
                default:
                    System.out.println("Повторите ввод");
            }
            System.out.println("\n1 - find a recipe, 2 - add a recipe, 3 - delete, 0 - exit:");
        }

    }

    private static void addRecipe(Scanner scanner, CookingBookService cookingBookService) {
        Recipe recipe = new Recipe();
        System.out.println("Enter a recipe name:");
        recipe.setName(scanner.next());
        List<Component> components = new ArrayList<>();
        while (true) {
            System.out.println("Enter a component name, 0 - to stop inserting components:");
            String name = scanner.next();
            if (name.equals("0")) {
                break;
            }
            System.out.println("Enter amount:");
            while (true) {
                try {
                    int amount = scanner.nextInt();
                    components.add(new Component(name, amount));
                    break;
                } catch (Exception exception) {
                    System.out.println("Wrong input, please repeat");
                    scanner = new Scanner(System.in);
                }
            }
        }
        recipe.setComponents(components);
        cookingBookService.addRecipe(recipe);
    }


}