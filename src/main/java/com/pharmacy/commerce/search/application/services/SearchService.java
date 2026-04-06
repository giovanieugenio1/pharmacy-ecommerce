package com.pharmacy.commerce.search.application.services;

import com.pharmacy.commerce.brand.api.response.BrandResponse;
import com.pharmacy.commerce.brand.infrastructure.persistence.BrandJpaRepository;
import com.pharmacy.commerce.catalog.api.response.ProductSummaryResponse;
import com.pharmacy.commerce.catalog.application.services.ProductService;
import com.pharmacy.commerce.category.api.response.CategoryResponse;
import com.pharmacy.commerce.category.infrastructure.persistence.CategoryJpaRepository;
import com.pharmacy.commerce.search.api.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private static final int MAX_RESULTS_PER_TYPE = 10;

    private final ProductService productService;
    private final CategoryJpaRepository categoryRepository;
    private final BrandJpaRepository brandRepository;

    @Transactional(readOnly = true)
    public SearchResponse search(String query) {
        if (query == null || query.isBlank()) {
            return SearchResponse.builder()
                    .query(query)
                    .products(List.of())
                    .categories(List.of())
                    .brands(List.of())
                    .totalProducts(0).totalCategories(0).totalBrands(0)
                    .build();
        }

        String q = query.trim();

        List<ProductSummaryResponse> productResults = productService
                .findAll(true, null, null, null, q,
                        PageRequest.of(0, MAX_RESULTS_PER_TYPE))
                .getContent();

        List<CategoryResponse> categoryResults = categoryRepository
                .searchActive(q, PageRequest.of(0, MAX_RESULTS_PER_TYPE))
                .stream().map(CategoryResponse::from).toList();

        List<BrandResponse> brandResults = brandRepository
                .searchActive(q, PageRequest.of(0, MAX_RESULTS_PER_TYPE))
                .stream().map(BrandResponse::from).toList();

        return SearchResponse.builder()
                .query(q)
                .products(productResults)
                .categories(categoryResults)
                .brands(brandResults)
                .totalProducts(productResults.size())
                .totalCategories(categoryResults.size())
                .totalBrands(brandResults.size())
                .build();
    }
}
