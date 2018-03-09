package com.berko.ebaypricer.service.ebay;

public class EbayResponse {
    String ackToken;
    String itemId;
    String title;
    String itemUrl;
    String galleryUrl;
    String currentPrice;
    String condition;

    public String getAckToken() {
        return ackToken;
    }

    public void setAckToken(String ackToken) {
        this.ackToken = ackToken;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getGalleryUrl() {
        return galleryUrl;
    }

    public void setGalleryUrl(String galleryUrl) {
        this.galleryUrl = galleryUrl;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
