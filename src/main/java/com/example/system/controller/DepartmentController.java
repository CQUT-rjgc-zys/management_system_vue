package com.example.system.controller;

import com.example.system.dto.DepartmentDTO;
import com.example.system.result.CommonResult;
import com.example.system.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 添加部门
     */
    @PostMapping("/addDepartment")
    public CommonResult<Void> addDepartment(@RequestParam("name") String name) {
        departmentService.addDepartment(name);
        return CommonResult.success();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/deleteDepartment")
    public CommonResult<Void> deleteDepartment(@RequestParam("id") Long id) {
        departmentService.deleteDepartment(id);
        return CommonResult.success();
    }

    /**
     * 获取所有部门
     */
    @GetMapping("/getDepartments")
    public CommonResult<List<DepartmentDTO>> getDepartments() {
        List<DepartmentDTO> departments = departmentService.getDepartments();
        return CommonResult.success(departments);
    }
}
