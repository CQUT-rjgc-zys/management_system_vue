package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.entity.DepartmentEntity;
import com.example.system.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper extends BaseMapper<DepartmentEntity> {

    @Select("SELECT id FROM departments WHERE name = #{departmentName}")
    Long selectByName(String departmentName);
}
