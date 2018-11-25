package com.gildedrose;

import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class GildedRoseTest {

    @Test
    public void updateQualityUpdatesAllItems() {
        Item[] items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Elixir of the Mongoose", 2, 10)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("+5 Dexterity Vest", 9, 19));
        assertThat(app.items[1], isItemWith("Elixir of the Mongoose", 1, 9));
    }

    @Test
    public void legendaryItemNeverDecreasesInQuality() {
        Item[] items = new Item[]{new Item("Sulfuras, Hand of Ragnaros", 0, 80)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Sulfuras, Hand of Ragnaros", 0, 80));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Sulfuras, Hand of Ragnaros", 0, 80));
    }

    @Test
    public void itemQualityDegradesOverTime() {
        Item[] items = new Item[]{new Item("Elixir of the Mongoose", 5, 7)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", 4, 6));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", 3, 5));
    }

    @Test
    public void itemQualityDecreasesDoubleSpeedAfterSellIn() {
        Item[] items = new Item[]{new Item("Elixir of the Mongoose", 1, 6)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", 0, 5));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", -1, 3));
    }

    @Test
    public void itemQualityMinimumIsZero() {
        Item[] items = new Item[]{new Item("Elixir of the Mongoose", 3, 1)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", 2, 0));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", 1, 0));
    }

    @Test
    public void itemQualityMinimumIsZeroAfterSellIn() {
        Item[] items = new Item[]{new Item("Elixir of the Mongoose", 0, 1)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", -1, 0));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Elixir of the Mongoose", -2, 0));
    }

    @Test
    public void agedBrieIncreasesInQuality() {
        Item[] items = new Item[]{new Item("Aged Brie", 3, 8)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Aged Brie", 2, 9));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Aged Brie", 1, 10));
    }

    @Test
    public void agedBrieIncreasesInQualityDoubleSpeedAfterSellIn() {
        Item[] items = new Item[]{new Item("Aged Brie", 1, 18)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Aged Brie", 0, 19));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Aged Brie", -1, 21));
    }

    @Test
    public void maximumQualityIs50() {
        Item[] items = new Item[]{new Item("Aged Brie", 7, 49)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Aged Brie", 6, 50));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Aged Brie", 5, 50));
    }

    @Test
    public void backStagesPassesIncreaseInQuality() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 17, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 16, 11));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 15, 12));
    }

    @Test
    public void backStagesPassesIncreaseInQualityDoubleSpeedWhenConcertApproaches() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 11, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 10, 11));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 9, 13));
    }

    @Test
    public void backStagesPassesIncreaseInQualityTripleSpeedWhenConcertNear() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 6, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 5, 12));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 4, 15));
    }

    @Test
    public void backStagesPassesHaveNoQualityAfterSellIn() {
        Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 1, 10)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", 0, 13));
        app.updateQuality();
        assertThat(app.items[0], isItemWith("Backstage passes to a TAFKAL80ETC concert", -1, 0));
    }

    private Matcher<Item> isItemWith(String name, int sellIn, int quality) {
        return new TypeSafeMatcher<Item>() {
            @Override
            protected boolean matchesSafely(Item item) {
                return item.name.equals(name) && item.quality == quality && item.sellIn == sellIn;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(new Item(name, sellIn, quality).toString());
            }
        };
    }

}