package recipe;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author: itwang
 * @description:
 * @date: Created in 14:09 2019/2/23
 * @pdackage: recipe
 * @modified By:
 */

@JsonDeserialize(builder = Recipe.RecipeItemBuilder.class)
public class Recipe {

    private String name;

    private List<Ingredient> ingredients;

    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "name='" + name + '\'' +
            ", ingredients=" + ingredients +
            '}';
    }


    public static class RecipeItemBuilder {

        private String name;
        private List<Ingredient> ingredients = newArrayList();

        public Recipe build() {
            return new Recipe(name, ingredients);
        }

        public RecipeItemBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public RecipeItemBuilder withIngredients(final List<Ingredient> ingredients) {
            if (ingredients != null) {
                this.ingredients.addAll(ingredients);
            }
            return this;
        }

        public RecipeItemBuilder withIngredient(final Ingredient ingredient) {
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
            return this;
        }
    }
}
