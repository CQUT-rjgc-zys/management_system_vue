package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.system.dto.DepartmentDTO;
import com.example.system.entity.DepartmentEntity;
import com.example.system.entity.UserEntity;
import com.example.system.mapper.DepartmentMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.DepartmentService;
import com.example.system.util.UUIDUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity> implements DepartmentService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addDepartment(String name) {
        QueryWrapper<DepartmentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        if (this.count(queryWrapper) > 0) {
            throw new IllegalArgumentException("部门名称已存在");
        }
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(UUIDUtil.uuid());
        departmentEntity.setName(name);
        this.save(departmentEntity);
    }

    @Override
    public void deleteDepartment(Long id) {
        DepartmentEntity department = getById(id);
        if (department == null) {
            throw new IllegalArgumentException("不存在id为：" + id + "的部门信息");
        }
        QueryWrapper<UserEntity> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("department_id", id);
        List<UserEntity> userEntities = userMapper.selectList(userQueryWrapper);
        if (userEntities.size() > 0) {
            throw new IllegalArgumentException("该部门下存在用户，不能删除");
        }
        this.removeById(id);
    }

    @Override
    public List<DepartmentDTO> getDepartments() {
        return list().stream()
                .map(department -> {
                    DepartmentDTO departmentDTO = new DepartmentDTO();
                    BeanUtils.copyProperties(department, departmentDTO);
                    return departmentDTO;
                })
                .collect(Collectors.toList());
    }
}
