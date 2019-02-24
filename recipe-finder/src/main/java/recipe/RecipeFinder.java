package recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Sets.newTreeSet;

/**
 * @author: itwang
 * @description:
 * @date: Created in 14:22 2019/2/23
 * @pdackage: recipe
 * @modified By:
 */
public class RecipeFinder {

    private static final Logger LOGGER = Logger.getLogger(RecipeFinder.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) {

        if (args == null || args.length != 2) {

            LOGGER.severe("Must provide fridge CSV and recipes JSON file paths");
            System.exit(0);
        }

        final RecipeFinder recipeFinder = new RecipeFinder();

    }


    /**
     * @Description: get repice for today
     * @params cvsPath
     * @params jsonPath
     * @Return: java.lang.String
     * @Author: itwang
     * @Date: 2019/2/24 11:17
     */
    public String getRecipeForToday(String cvsPath, String jsonPath) throws IOException {

        //get fridge item
        Set<FridgeItem> usableFridgeItems = getUsableFridgeItems(cvsPath);

        // get recipes
        List<Recipe> recipes = readRecipes(jsonPath);

        return getRecipeForToday(usableFridgeItems, recipes);

    }


    /**
     * @Description: get repice for today
     * @params usableFridgeItems, recipes
     * @Return: java.lang.String
     * @Author: itwang
     * @Date: 2019/2/24 11:18
     */
    public String getRecipeForToday(final Set<FridgeItem> usableFridgeItems, final List<Recipe> recipes) throws IOException {
        for (final FridgeItem fridgeItem : usableFridgeItems) {
            final Optional<Recipe> recipeOptional = tryFind(recipes, new Predicate<Recipe>() {
                @Override
                public boolean apply(final Recipe recipe) {
                    final List<Ingredient> ingredients = recipe.getIngredients();
                    if (!isIngredientInFridge(ingredients, fridgeItem)) {
                        return false;
                    }
                    final int eligibleFridgeItems = size(filter(ingredients, new Predicate<Ingredient>() {
                        @Override
                        public boolean apply(final Ingredient ingredient) {
                            return !isEmpty(filter(usableFridgeItems, new Predicate<FridgeItem>() {
                                @Override
                                public boolean apply(final FridgeItem fridgeItem) {
                                    return fridgeItem.getItem().equals(ingredient.getItem()) && fridgeItem
                                        .getAmount() >= ingredient.getAmount() && fridgeItem.getUnit() == ingredient
                                        .getUnit();
                                }
                            }));
                        }
                    }));
                    return eligibleFridgeItems == ingredients.size();
                }
            });
            if (recipeOptional.isPresent()) {
                return recipeOptional.get().getName();
            }
        }
        return "Order Takeout";
    }

    /**
     * @Description: get Use able Fridge Items
     * @params cvsFilePath
     * @Return: java.util.Set<recipe.FridgeItem>
     * @Author: itwang
     * @Date: 2019/2/24 11:17
     */
    private Set<FridgeItem> getUsableFridgeItems(String cvsFilePath) throws IOException {

        DateTime today = DateTime.now();

        List<String> rows = Files.readAllLines(Paths.get(cvsFilePath), Charset.defaultCharset());

        return newTreeSet(filter(transform(rows, new Function<String, FridgeItem>() {
            @Override
            public FridgeItem apply(final String row) {
                final Optional<FridgeItem> fridgeItemOptional = getFridgeItem(row);
                if (fridgeItemOptional.isPresent()) {
                    final FridgeItem fridgeItem = fridgeItemOptional.get();
                    if (Days.daysBetween(today, fridgeItem.getUseBy()).getDays() >= 0) {
                        return fridgeItem;
                    }
                }
                return null;
            }
        }), notNull()));
    }


    /**
     * @Description: get fridge item
     * @params row
     * @Return: com.google.common.base.Optional<recipe.FridgeItem>
     * @Author: itwang
     * @Date: 2019/2/24 11:15
     */
    private Optional<FridgeItem> getFridgeItem(final String row) {
        if (Strings.isNullOrEmpty(row)) {
            return absent();
        }
        final List<String> fridgeItemElements = Splitter.on(",").splitToList(row);
        if (fridgeItemElements.size() != 4) {
            return absent();
        }
        final String item = fridgeItemElements.get(0);
        final int amount = Integer.parseInt(fridgeItemElements.get(1));
        final Unit unit = Unit.valueOf(fridgeItemElements.get(2));
        final DateTime useBy = DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(fridgeItemElements.get(3));
        return of(new FridgeItem.FridgeItemBuilder().withItem(item).withAmount(amount).withUnit(unit).withUseBy(useBy).build());
    }


    /**
     * @Description: Read the recipes in the json file
     * @params recipesFilePath
     * @Return: java.util.List<recipe.Recipe>
     * @Author: itwang
     * @Date: 2019/2/24 11:19
     */
    private List<Recipe> readRecipes(final String recipesFilePath) throws IOException {
        final ObjectReader objectReader = MAPPER.reader(new TypeReference<List<Recipe>>() {
        });
        return objectReader.readValue(Files.readAllBytes(Paths.get(recipesFilePath)));
    }

    /**
     * @Description: Whether the ingredients are in the recipe
     * @params ingredients, fridgeItem
     * @Return: boolean
     * @Author: itwang
     * @Date: 2019/2/24 11:21
     */
    private boolean isIngredientInFridge(final List<Ingredient> ingredients, final FridgeItem fridgeItem) {
        return tryFind(ingredients, new Predicate<Ingredient>() {
            @Override
            public boolean apply(final Ingredient ingredient) {
                return ingredient.getItem().equals(fridgeItem.getItem());
            }
        }).isPresent();
    }
}
