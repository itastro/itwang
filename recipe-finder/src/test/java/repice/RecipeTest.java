package repice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.io.CharStreams;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import recipe.Ingredient;
import recipe.Recipe;
import recipe.Unit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static recipe.Unit.grams;
import static recipe.Unit.slices;

public final class RecipeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String NAME = "grilled cheese on toast";
    private static final String ITEM = "bread";
    private static String json;

    @BeforeClass
    public static void onlyOnce() throws IOException {
        try (final InputStream inputStream = RecipeTest.class.getResourceAsStream("/recipes.json");
             final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);) {
            json = CharStreams.toString(inputStreamReader);
        }
    }

    @Test
    public final void canDeserializeFromJson() throws IOException {
        final ObjectReader objectReader = MAPPER.readerFor(new TypeReference<List<Recipe>>() {
        });
        final List<Recipe> recipes = objectReader.readValue(json);
        assertNotNull(recipes);
        assertEquals(2, recipes.size());

        final Recipe recipe = recipes.get(0);
        assertEquals(NAME, recipe.getName());

        final List<Ingredient> ingredients = recipe.getIngredients();
        assertNotNull(ingredients);
        assertEquals(2, ingredients.size());

        final Ingredient ingredient = ingredients.get(0);
        assertEquals(ITEM, ingredient.getItem());
        assertEquals(2, ingredient.getAmount());
        assertEquals(slices, ingredient.getUnit());
    }

    @Test
    public final void canSerializeToJson() throws JsonProcessingException, JSONException {
        final Ingredient bread = getIngredient(ITEM, 2, slices);
        final List<Recipe> recipes = newArrayList(new Recipe.FridgeItemBuilder().withName(NAME).withIngredient(bread)
            .withIngredient(getIngredient("cheese", 2, slices)).build(), new Recipe.FridgeItemBuilder()
            .withName("salad sandwich").withIngredient(bread)
            .withIngredient(getIngredient("mixed salad", 100, grams)).build());

        final ObjectWriter objectWriter = MAPPER.writerFor(new TypeReference<List<Recipe>>() {
        });
        final JSONArray actual = new JSONArray(objectWriter.writeValueAsString(recipes));
        final JSONArray expected = new JSONArray(json);
        JSONAssert.assertEquals(expected, actual, true);
    }

    private Ingredient getIngredient(final String item, final int amount, final Unit unit) {
        return new Ingredient.IngredientBuilder().withItem(item).withAmount(amount).withUnit(unit).build();
    }
}
