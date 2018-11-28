package com.gildedrose;

class GildedRose {
    private static final int MAX_QUALITY = 50;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            getQualityRuleFor(item.name).updateQualityOf(item);
        }
    }

    private QualityRule getQualityRuleFor(String name) {
        if (legendaryItem(name)) {
            return new LegendaryItemRule();
        } else if (agingItem(name)) {
            return new AgingItemRule();
        } else if (concertBackstagePass(name)) {
            return new BackstagePassRule();
        } else if (conjuredItem(name)) {
            return new ConjuredItemRule();
        } else {
            return new NormalItemRule();
        }
    }

    private boolean conjuredItem(String name) {
        return name.equals("Conjured Mana Cake");
    }

    private boolean concertBackstagePass(String name) {
        return name.equals("Backstage passes to a TAFKAL80ETC concert");
    }

    private boolean agingItem(String name) {
        return name.equals("Aged Brie");
    }

    private boolean legendaryItem(String name) {
        return name.equals("Sulfuras, Hand of Ragnaros");
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
        private QualityRule delegate;
        NormalItemRule() {
            delegate = new DegradingItemRule(1);
        }

        @Override
        public void updateQualityOf(Item item) {
            delegate.updateQualityOf(item);
        }
    }

    private static class ConjuredItemRule implements QualityRule {
        private QualityRule delegate;
        ConjuredItemRule() {
            delegate = new DegradingItemRule(2);
        }

        @Override
        public void updateQualityOf(Item item) {
            delegate.updateQualityOf(item);
        }
    }

    private static class DegradingItemRule implements QualityRule {

        private int degradingSpeed;

        DegradingItemRule(int degradingSpeed) {
            this.degradingSpeed = degradingSpeed;
        }

        @Override
        public void updateQualityOf(Item item) {
            item.sellIn = item.sellIn - 1;
            int qualityAfterDegrade;
            if(item.sellIn < 0) {
                qualityAfterDegrade = item.quality - 2 * degradingSpeed;
            } else {
                qualityAfterDegrade = item.quality - 1 * degradingSpeed;
            }
            item.quality = qualityAfterDegrade < 0 ? 0 : qualityAfterDegrade;
        }
    }

    private static class AgingItemRule implements QualityRule {

        @Override
        public void updateQualityOf(Item item) {
            item.sellIn = item.sellIn - 1;
            int qualityAfterIncrease;
            if(item.sellIn < 0) {
                qualityAfterIncrease = item.quality + 2;
            } else {
                qualityAfterIncrease = item.quality + 1;
            }
            item.quality = qualityAfterIncrease > MAX_QUALITY ? MAX_QUALITY : qualityAfterIncrease;
        }
    }

    private static class BackstagePassRule implements QualityRule {

        @Override
        public void updateQualityOf(Item item) {
            item.sellIn = item.sellIn - 1;
            if (expired(item)) {
                item.quality = 0;
            } else {
                int qualityAfterIncrease;
                if(startsInLessThanDays(5, item)) {
                    qualityAfterIncrease = item.quality + 3;
                } else if (startsInLessThanDays(10, item)){
                    qualityAfterIncrease = item.quality + 2;
                } else {
                    qualityAfterIncrease = item.quality + 1;
                }
                item.quality = qualityAfterIncrease > MAX_QUALITY ? MAX_QUALITY : qualityAfterIncrease;
            }
        }

        private boolean startsInLessThanDays(int days, Item item) {
            return item.sellIn < days;
        }

        private boolean expired(Item item) {
            return item.sellIn < 0;
        }

    }
}