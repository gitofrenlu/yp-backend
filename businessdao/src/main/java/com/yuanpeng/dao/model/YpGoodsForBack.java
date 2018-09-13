package com.yuanpeng.dao.model;

import java.io.Serializable;

public class YpGoodsForBack extends YpGoods implements Serializable {

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}