package com.pharmacy.commerce.search.api.response;

import com.pharmacy.commerce.brand.api.response.BrandResponse;
import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.category.api.response.CategoryResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResponse {

    private String query;
    private int totalProducts;
    private int totalCategories;
    private int totalBrands;

    private List<ProductSummaryResponse> products;
    private List<CategoryResponse> categories;
    private List<BrandResponse> brands;
}
