package com.pharmacy.commerce.category.infrastructure.persistence;

import com.pharmacy.commerce.category.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<CategoryEntity> findAllByActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.parent WHERE c.active = true ORDER BY c.displayOrder ASC")
    List<CategoryEntity> findAllActiveWithParent();

    Page<CategoryEntity> findAll(Pageable pageable);

    @Query("SELECT c FROM CategoryEntity c WHERE c.active = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%')) ORDER BY c.displayOrder ASC")
    List<CategoryEntity> searchActive(@Param("q") String q, Pageable pageable);
}
