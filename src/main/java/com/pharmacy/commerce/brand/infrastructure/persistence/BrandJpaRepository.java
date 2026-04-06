package com.pharmacy.commerce.brand.infrastructure.persistence;

import com.pharmacy.commerce.brand.infrastructure.persistence.entity.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandJpaRepository extends JpaRepository<BrandEntity, Long> {

    Optional<BrandEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<BrandEntity> findAllByActiveTrueOrderByNameAsc();

    Page<BrandEntity> findAll(Pageable pageable);

    @Query("SELECT b FROM BrandEntity b WHERE b.active = true AND LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%')) ORDER BY b.name ASC")
    List<BrandEntity> searchActive(@Param("q") String q, Pageable pageable);
}
