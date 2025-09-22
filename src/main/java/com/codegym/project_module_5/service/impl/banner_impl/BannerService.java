package com.codegym.project_module_5.service.impl.banner_impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codegym.project_module_5.model.baner_model.Banner;
import com.codegym.project_module_5.model.restaurant_model.Dish;
import com.codegym.project_module_5.repository.banner.IBannerRepository;
import com.codegym.project_module_5.repository.restaurant_repository.IDishRepository;
import com.codegym.project_module_5.service.Banner.IBannerService;

import jakarta.transaction.Transactional;

@Service
public class BannerService implements IBannerService {
    private final IBannerRepository bannerRepository;
    private final IDishRepository dishRepository;

    public BannerService(IBannerRepository bannerRepository, IDishRepository dishRepository) {
        this.bannerRepository = bannerRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    @Transactional
    public void updateBannerForDish(Long dishId, boolean featured, boolean promotion) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn id=" + dishId));

        Banner banner = dish.getBanner();
        if (banner == null) {
            banner = new Banner();
            banner.setDish(dish);
        }

        banner.setFeatured(featured);
        banner.setPromotion(promotion);
        bannerRepository.save(banner);
    }

    @Override
    public List<Banner> getFeaturedBanners() {
        return bannerRepository.findByFeaturedTrue();
    }

    @Override
    public List<Banner> getPromotionBanners() {
        return bannerRepository.findByPromotionTrue();
    }
}
