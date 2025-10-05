package com.codegym.project_module_5.repository.banner;

import com.codegym.project_module_5.model.baner_model.Banner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByFeaturedTrue();

    List<Banner> findByPromotionTrue();

}
