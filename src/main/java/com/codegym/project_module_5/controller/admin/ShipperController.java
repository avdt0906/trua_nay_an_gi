package com.codegym.project_module_5.controller.admin;

import com.codegym.project_module_5.model.shipper_model.Shipper;
import com.codegym.project_module_5.service.shipper_service.IShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/shippers")
public class ShipperController {
    @Autowired
    private IShipperService shipperService;

    @GetMapping("")
    public ModelAndView showShipperList() {
        ModelAndView mv = new ModelAndView("admin/shipper/shipper_list");
        Iterable<Shipper> shippers = shipperService.findAll();
        mv.addObject("shippers", shippers);
        mv.addObject("activePage", "shipper");
        return mv;
    }

    @GetMapping("/add_shipper_form")
    public ModelAndView showAddShipperForm() {
        ModelAndView mv = new ModelAndView("admin/shipper/add_shipper_form");
        mv.addObject("shipper", new Shipper());
        return mv;
    }

    @PostMapping("/add_shipper")
    public ModelAndView addShipper(@ModelAttribute("shipper") Shipper shipper) {
        ModelAndView mv = new ModelAndView("redirect:/admin/shippers");
        shipperService.save(shipper);
        mv.addObject("successMessage", "Thêm mới thành công!");
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView showShipperDetail(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("admin/shipper/shipper_detail");
        Shipper shipper = shipperService.findById(id).orElse(null);
        mv.addObject("shipper", shipper);
        return mv;
    }

    @PostMapping("/toggle_lock/{id}")
    public ModelAndView toggleShipperLock(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("redirect:/admin/shippers");
        shipperService.toggleLock(id);
        return mv;
    }
}
