package com.pharmacy.commerce.cms.api.response;

import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.category.api.response.CategoryResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponse {

    private List<BannerResponse> banners;
    private List<CategoryResponse> categories;
    private List<ProductSummaryResponse> featuredProducts;
}
