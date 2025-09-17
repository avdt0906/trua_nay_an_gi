package com.codegym.project_module_5.service.impl.evaluate_service_impl;

import com.codegym.project_module_5.model.evaluate_model.Evaluate;
import com.codegym.project_module_5.repository.evaluate_repository.IEvaluateRepository;
import com.codegym.project_module_5.service.evaluate_serivce.IEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluateService implements IEvaluateService {
    @Autowired
    private IEvaluateRepository iEvaluateRepository;

    @Override
    public Iterable<Evaluate> findAll() {
        return iEvaluateRepository.findAll();
    }

    @Override
    public Optional<Evaluate> findById(Long id) {
        return iEvaluateRepository.findById(id);
    }

    @Override
    public void save(Evaluate evaluate) {
        iEvaluateRepository.save(evaluate);
    }

    @Override
    public void delete(Long id) {
        iEvaluateRepository.deleteById(id);
    }
}
