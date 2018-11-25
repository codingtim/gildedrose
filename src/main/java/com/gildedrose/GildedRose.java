package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            getQualityRuleFor(item.name).updateQualityOf(item);
        }
    }

    @FunctionalInterface
    private interface QualityRule {
        void updateQualityOf(Item item);
    }

    private QualityRule getQualityRuleFor(String name) {
        if (name.equals("Sulfuras, Hand of Ragnaros")) {
            return item -> {
            };
        }
        if (name.equals("Aged Brie")) {
            return item -> {
                item.sellIn = item.sellIn - 1;
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
                if (item.sellIn < 0) {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            };
        } else if (name.equals("Backstage passes to a TAFKAL80ETC concert")) {
            return item -> {
                item.sellIn = item.sellIn - 1;
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                    if (item.sellIn < 10) {
                        if (item.quality < 50) {
                            item.quality = item.quality + 1;
                        }
                    }

                    if (item.sellIn < 5) {
                        if (item.quality < 50) {
                            item.quality = item.quality + 1;
                        }
                    }
                }
                if (item.sellIn < 0) {
                    item.quality = item.quality - item.quality;
                }
            };
        } else {
            return item -> {
                item.sellIn = item.sellIn - 1;
                if (item.quality > 0) {
                    item.quality = item.quality - 1;
                }
                if (item.sellIn < 0) {
                    if (item.quality > 0) {
                        item.quality = item.quality - 1;
                    }
                }
            };
        }
    }
}