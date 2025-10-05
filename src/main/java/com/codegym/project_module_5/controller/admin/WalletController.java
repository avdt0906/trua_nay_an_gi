package com.codegym.project_module_5.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codegym.project_module_5.model.restaurant_model.WalletWithdraw;
import com.codegym.project_module_5.model.restaurant_model.WalletWithdraw.Status;
import com.codegym.project_module_5.service.restaurant_service.IWalletWithdrawService;

@Controller
@RequestMapping("/admin")
public class WalletController {
    @Autowired
    private IWalletWithdrawService walletWithdrawService;

    @GetMapping("/requests/pending")
    public String pendingRequests(Model model) {
        List<WalletWithdraw> pending = walletWithdrawService.getRequestsByStatus(Status.PENDING);
        model.addAttribute("requests", pending);
        return "admin/wallet/requests"; // template hiển thị
    }

    @PostMapping("wallet/approve/{id}")
    public String approve(@PathVariable Long id, Principal principal,
            RedirectAttributes redirectAttributes) {
        walletWithdrawService.approveRequest(id, principal.getName());
        redirectAttributes.addFlashAttribute("msg", "Đã duyệt yêu cầu #" + id);
        return "redirect:/admin/requests/pending";
    }

    @PostMapping("wallet/reject/{id}")
    public String reject(@PathVariable Long id,
            @RequestParam String note,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        walletWithdrawService.rejectRequest(id, principal.getName(), note);
        redirectAttributes.addFlashAttribute("msg", "Đã từ chối yêu cầu #" + id);
        return "redirect:/admin/requests/pending";
    }

}
