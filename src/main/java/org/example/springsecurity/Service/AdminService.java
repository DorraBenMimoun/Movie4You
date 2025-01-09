package org.example.springsecurity.Service;


import org.example.springsecurity.Models.AdminEntity;
import org.example.springsecurity.Repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public AdminEntity adminCreate(AdminEntity adminEntity) {
        return adminRepository.save(adminEntity);}

    public List<AdminEntity> getAllAdmins() {return adminRepository.findAll();}
    public AdminEntity getAdminById(int id) {return adminRepository.findById(id).get();}

    public void deleteAdminById(int id) {adminRepository.deleteById(id);}

    public AdminEntity adminUpdate(int id, AdminEntity adminEntity) {
        adminEntity.setId(id);
        return adminRepository.save(adminEntity);
    }




}
