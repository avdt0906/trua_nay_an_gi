package com.codegym.project_module_5.repository.shipper_repository;

import com.codegym.project_module_5.model.shipper_model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShipperRepository extends JpaRepository<Shipper, Long> {
}
