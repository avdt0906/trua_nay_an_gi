package com.codegym.project_module_5.service.impl.shipper_service_impl;

import com.codegym.project_module_5.model.shipper_model.Shipper;
import com.codegym.project_module_5.repository.shipper_repository.IShipperRepository;
import com.codegym.project_module_5.service.shipper_service.IShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipperService implements IShipperService {
    @Autowired
    private IShipperRepository shipperRepository;

    @Override
    public Iterable<Shipper> findAll() {
        return shipperRepository.findAll();
    }

    @Override
    public Optional<Shipper> findById(Long id) {
        return shipperRepository.findById(id);
    }

    @Override
    public void save(Shipper shipper) {
        shipperRepository.save(shipper);
    }

    @Override
    public void delete(Long id) {
        shipperRepository.deleteById(id);
    }

    @Override
    public void toggleLock(Long id) {
        Shipper shipper = shipperRepository.findById(id).get();
        shipper.setIsLocked(!shipper.getIsLocked());
        shipperRepository.save(shipper);
    }
}
