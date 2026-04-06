package com.pharmacy.commerce.cms.infrastructure.persistence;

import com.pharmacy.commerce.cms.infrastructure.persistence.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerJpaRepository extends JpaRepository<BannerEntity, Long> {

    @Query("SELECT b FROM BannerEntity b WHERE b.active = true " +
           "AND b.position = :position " +
           "AND (b.startAt IS NULL OR b.startAt <= :now) " +
           "AND (b.endAt IS NULL OR b.endAt >= :now) " +
           "ORDER BY b.displayOrder ASC")
    List<BannerEntity> findActiveByPosition(@Param("position") String position, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM BannerEntity b WHERE b.active = true " +
           "AND (b.startAt IS NULL OR b.startAt <= :now) " +
           "AND (b.endAt IS NULL OR b.endAt >= :now) " +
           "ORDER BY b.position ASC, b.displayOrder ASC")
    List<BannerEntity> findAllActive(@Param("now") LocalDateTime now);
}
