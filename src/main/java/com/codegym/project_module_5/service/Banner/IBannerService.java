package com.codegym.project_module_5.service.Banner;

import java.util.List;

import com.codegym.project_module_5.model.baner_model.Banner;

public interface IBannerService {
    void updateBannerForDish(Long dishId, boolean featured, boolean promotion);
    List<Banner> getFeaturedBanners();
    List<Banner> getPromotionBanners();
}
