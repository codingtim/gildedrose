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

    private QualityRule getQualityRuleFor(String name) {
        if (name.equals("Sulfuras, Hand of Ragnaros")) {
            return new LegendaryItemRule();
        } else if (name.equals("Aged Brie")) {
            return new AgingItemRule();
        } else if (name.equals("Backstage passes to a TAFKAL80ETC concert")) {
            return new PassRule();
        } else {
            return new NormalItemRule();
        }
    }

    @FunctionalInterface
    private interface QualityRule {
        void updateQualityOf(Item item);
    }

    private static class LegendaryItemRule implements QualityRule {
        @Override
        public void updateQualityOf(Item item) {
            //legendary items do not change in quality
        }
    }

    private static class NormalItemRule implements QualityRule {
        @Override
        public void updateQualityOf(Item item) {
            item.sellIn = item.sellIn - 1;
            if (item.quality > 0) {
                item.quality = item.quality - 1;
            }
            if(item.sellIn < 0) {
                if (item.quality > 0) {
                    item.quality = item.quality - 1;
                }
            }
        }
    }

    private static class AgingItemRule implements QualityRule {
        @Override
        public void updateQualityOf(Item item) {
            item.sellIn = item.sellIn - 1;
            if (item.quality < 50) {
                item.quality = item.quality + 1;
            }
            if (item.sellIn < 0) {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }
        }
    }

    private static class PassRule implements QualityRule {
        @Override
        public void updateQualityOf(Item item) {
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
        }

    }
}