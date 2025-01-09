package org.example.springsecurity.Controllers;

import org.example.springsecurity.Models.AdminEntity;
import org.example.springsecurity.Repositories.AdminRepository;
import org.example.springsecurity.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class Admin_mvc {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/create")
    public String addAdmin(Model model) {
        AdminEntity adminEntity = new AdminEntity();
        model.addAttribute("adminForm", adminEntity);
        return "/admin/add_admin";
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute("adminForm") AdminEntity adminEntity, Model model) {
        if (adminEntity.getUsername() == null || adminEntity.getUsername().trim().isEmpty() ||
                adminEntity.getPassword() == null || adminEntity.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Nom d'utilisateur et mot de passe sont obligatoires");
            return "/admin/add_admin";
        }

        if (adminRepository.findByUsername(adminEntity.getUsername()) != null) {
            model.addAttribute("error", "Le nom d'utilisateur existe déjà");
            return "/admin/add_admin";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(adminEntity.getPassword());
        adminEntity.setPassword(hashedPassword);

        adminService.adminCreate(adminEntity);
        return "redirect:/admin/all";
    }

    @GetMapping("/all")
    public String all(Model model) {
        List<AdminEntity> admins = adminRepository.findAll();
        model.addAttribute("admins", admins);
        return "/admin/all_admins";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        adminService.deleteAdminById(id);
        return "redirect:/admin/all";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        AdminEntity admin = adminService.getAdminById(id);
        if (admin == null) {
            model.addAttribute("error", "Administrateur introuvable avec l'ID " + id);
            return "redirect:/admin/all";
        }
        model.addAttribute("adminForm", admin);
        return "/admin/update_admin";
    }

    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable("id") int id, @ModelAttribute("adminForm") AdminEntity adminEntity) {
        AdminEntity existingAdmin = adminService.getAdminById(id);
        if (existingAdmin != null) {
            existingAdmin.setUsername(adminEntity.getUsername());

            if (adminEntity.getPassword() != null && !adminEntity.getPassword().trim().isEmpty()) {
                existingAdmin.setPassword(new BCryptPasswordEncoder().encode(adminEntity.getPassword()));
            }


            adminService.adminUpdate(id, existingAdmin);
        }
        return "redirect:/admin/all";
    }

}
