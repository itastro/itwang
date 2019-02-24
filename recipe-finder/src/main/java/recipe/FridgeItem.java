package recipe;

import com.google.common.collect.ComparisonChain;
import org.joda.time.DateTime;


/**
 * @author: itwang
 * @description:
 * @date: Created in 13:41 2019/2/23
 * @pdackage: recipe
 * @modified By:
 */
public class FridgeItem implements Comparable<FridgeItem> {

    private String item;

    private int amount;

    private Unit unit;

    private DateTime useBy;


    public FridgeItem(String item, int amount, Unit unit, DateTime useBy) {
        this.item = item;
        this.amount = amount;
        this.unit = unit;
        this.useBy = useBy;
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

    public DateTime getUseBy() {
        return useBy;
    }

    @Override
    public int compareTo(FridgeItem o) {
        return ComparisonChain.start().compare(useBy, o.getUseBy()).compare(item, o.getItem()).result();
    }

    @Override
    public String toString() {
        return "FridgeItem{" +
            "item='" + item + '\'' +
            ", amount=" + amount +
            ", unit=" + unit +
            ", useBy=" + useBy +
            '}';
    }


    public static class FridgeItemBuilder {

        private String item;

        private int amount;

        private Unit unit;

        private DateTime useBy;


        public FridgeItem build() {

            return new FridgeItem(item, amount, unit, useBy);
        }


        public FridgeItemBuilder withItem(final String item) {
            this.item = item;
            return this;
        }


        public FridgeItemBuilder withAmount(final int amount) {
            this.amount = amount;
            return this;
        }


        public FridgeItemBuilder withUnit(final Unit unit) {
            this.unit = unit;
            return this;
        }

        public FridgeItemBuilder withUseBy(final DateTime useBy) {
            this.useBy = useBy;
            return this;
        }

    }
}
