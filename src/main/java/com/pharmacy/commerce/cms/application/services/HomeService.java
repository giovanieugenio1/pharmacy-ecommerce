package com.pharmacy.commerce.cms.application.services;

import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.catalog.application.services.ProductService;
import com.pharmacy.commerce.category.api.response.CategoryResponse;
import com.pharmacy.commerce.category.application.services.CategoryService;
import com.pharmacy.commerce.cms.api.response.BannerResponse;
import com.pharmacy.commerce.cms.api.response.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final BannerService bannerService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public HomeResponse getHome() {
        List<BannerResponse> banners = bannerService.findAllActive();

        List<CategoryResponse> categories = categoryService.findAllActive();

        List<ProductSummaryResponse> featuredProducts = productService
                .findAll(true, true, null, null, null,
                        PageRequest.of(0, 8, Sort.by("name").ascending()))
                .getContent();

        return HomeResponse.builder()
                .banners(banners)
                .categories(categories)
                .featuredProducts(featuredProducts)
                .build();
    }
}
