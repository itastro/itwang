package repice;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import recipe.RecipeFinder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RecipeFinderTest {

    private static String csvPath;
    private static String jsonPath;

    private RecipeFinder recipeFinder;

    @BeforeClass
    public static void onlyOnce() {
        csvPath = getPath("/fridge.csv");
        jsonPath = getPath("/recipes.json");
    }

    @Before
    public void setUp() {
        recipeFinder = new RecipeFinder();
    }

    @After
    public void resetSystemTime() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void canGetRecipeForToday() throws IOException {
        assertRecipe("salad sandwich");
    }

    @Test
    public void canGetRecipeForFutureDate() throws IOException {
        setFutureDate(2013, 12, 27);
        assertRecipe("grilled cheese on toast");
    }

    @Test
    public void canGetRecipeWithExpiredIngredients() throws IOException {
        setFutureDate(2014, 12, 31);
        assertRecipe("Order Takeout");
    }

    private void assertRecipe(final String name) throws IOException {
        assertEquals(name, recipeFinder.getRecipeForToday(csvPath, jsonPath));
    }

    private static String getPath(final String resource) {
        return new File(RecipeFinderTest.class.getResource(resource).getPath()).getAbsolutePath();
    }

    private void setFutureDate(int year, int month, int day) {
        DateTimeUtils.setCurrentMillisFixed(new DateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(day)
                .getMillis());
    }
}
