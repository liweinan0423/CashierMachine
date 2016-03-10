package com.thoughtworks.cashiermachine.product;

public interface ProductCatalog {
    Product findByCode(String code);
}
