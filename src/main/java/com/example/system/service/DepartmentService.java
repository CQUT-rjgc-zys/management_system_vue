package com.example.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.system.dto.DepartmentDTO;
import com.example.system.entity.DepartmentEntity;

import java.util.List;

public interface DepartmentService extends IService<DepartmentEntity> {

    void addDepartment(String name);

    void deleteDepartment(Long id);

    List<DepartmentDTO> getDepartments();

    DepartmentDTO getDepartment(Long id);
}
