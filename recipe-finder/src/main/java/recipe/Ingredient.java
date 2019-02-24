package recipe;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author: itwang
 * @description:
 * @date: Created in 14:04 2019/2/23
 * @pdackage: recipe
 * @modified By:
 */
@JsonDeserialize(builder = Ingredient.IngredientBuilder.class)
public class Ingredient {


    private String item;
    private int amount;
    private Unit unit;

    public Ingredient(String item, int amount, Unit unit) {
        this.item = item;
        this.amount = amount;
        this.unit = unit;
    }

    public String getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "item='" + item + '\'' +
            ", amount=" + amount +
            ", unit=" + unit +
            '}';
    }

    public static final class IngredientBuilder {

        private String item;

        private int amount;

        private Unit unit;


        public Ingredient build() {
            return new Ingredient(item, amount, unit);
        }

        public IngredientBuilder withItem(final String item) {
            this.item = item;
            return this;
        }

        public IngredientBuilder withAmount(final int amount) {
            this.amount = amount;
            return this;
        }

        public IngredientBuilder withUnit(final Unit unit) {
            this.unit = unit;
            return this;
        }
    }
}
