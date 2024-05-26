package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.system.dto.Coordinate;
import com.example.system.dto.FieldTaskDTO;
import com.example.system.entity.FieldTaskEntity;
import com.example.system.mapper.FieldTaskMapper;
import com.example.system.service.FieldTaskService;
import com.example.system.util.UUIDUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldTaskTaskServiceImpl extends ServiceImpl<FieldTaskMapper, FieldTaskEntity> implements FieldTaskService {

    @Autowired
    private FieldTaskMapper fieldTaskMapper;

    @Override
    public void addFieldTask(FieldTaskDTO fieldTask) {
        String taskName = fieldTask.getName();
        QueryWrapper<FieldTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", taskName);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new IllegalArgumentException("任务名称已存在");
        }

        String taskBrief = fieldTask.getTaskBrief();
        if (taskBrief == null || taskBrief.trim().isEmpty()) {
            throw new IllegalArgumentException("任务简介不能为空");
        }

        Timestamp startTime = fieldTask.getStartTime();
        if (startTime == null) {
            throw new IllegalArgumentException("开始时间不能为空");
        }
        if (startTime.before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("开始时间不能早于当前时间");
        }
        Timestamp endTime = fieldTask.getEndTime();
        if (endTime == null) {
            throw new IllegalArgumentException("结束时间不能为空");
        }
        if (endTime.before(startTime)) {
            throw new IllegalArgumentException("结束时间不能早于开始时间");
        }

        FieldTaskEntity fieldTaskEntity = new FieldTaskEntity();
        fieldTaskEntity.setId(UUIDUtil.uuid());
        fieldTaskEntity.setName(taskName);
        fieldTaskEntity.setTaskBrief(taskBrief);
        fieldTaskEntity.setStartTime(startTime);
        fieldTaskEntity.setEndTime(endTime);
        fieldTaskEntity.setStatus(0);
        save(fieldTaskEntity);
    }

    @Override
    public void deleteFieldTask(Long id) {
        FieldTaskEntity taskEntity = getById(id);
        if (taskEntity == null) {
            throw new IllegalArgumentException("不存在id为：" + id + "的任务信息");
        }
        if (taskEntity.getStatus() != 2) {
            throw new IllegalArgumentException("未完成的任务不能删除");
        }

        this.removeById(id);
    }

    @Override
    public void updateFieldTask(FieldTaskDTO fieldTask) {
        String taskName = fieldTask.getName();
        QueryWrapper<FieldTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", taskName);
        queryWrapper.ne("id", fieldTask.getId());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new IllegalArgumentException("任务名称已存在");
        }
        FieldTaskEntity taskEntity = getById(fieldTask.getId());
        if (taskEntity == null) {
            throw new IllegalArgumentException("不存在id为：" + fieldTask.getId() + "的任务信息");
        }

        Timestamp startTime = fieldTask.getStartTime();
        if (startTime == null) {
            throw new IllegalArgumentException("开始时间不能为空");
        }
        if (startTime.before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("开始时间不能早于当前时间");
        }
        Timestamp endTime = fieldTask.getEndTime();
        if (endTime == null) {
            throw new IllegalArgumentException("结束时间不能为空");
        }
        if (endTime.before(startTime)) {
            throw new IllegalArgumentException("结束时间不能早于开始时间");
        }
        BeanUtils.copyProperties(fieldTask, taskEntity);
//        taskEntity.setTaskSpot(fieldTask.getTaskSpot().toString());

        updateById(taskEntity);

    }

    @Override
    public List<FieldTaskDTO> getFieldTasks() {
        List<FieldTaskEntity> list = fieldTaskMapper.selectList(null);
        List<FieldTaskDTO> results = new ArrayList<>();
        list.forEach(task -> {
            FieldTaskDTO fieldTaskDTO = new FieldTaskDTO();
            BeanUtils.copyProperties(task, fieldTaskDTO);
            String taskSpot = task.getTaskSpot();
            if (taskSpot == null) {
                fieldTaskDTO.setTaskSpot(null);
            } else {
                fieldTaskDTO.setTaskSpot(new Coordinate(taskSpot));
            }
            results.add(fieldTaskDTO);
        });
        return results;
    }

    @Override
    public FieldTaskDTO getFieldTaskById(Long id) {
        FieldTaskEntity taskEntity = getById(id);
        if (taskEntity == null) {
            throw new IllegalArgumentException("不存在id为：" + id + "的任务信息");
        }
        FieldTaskDTO fieldTaskDTO = new FieldTaskDTO();
        BeanUtils.copyProperties(taskEntity, fieldTaskDTO);
        String taskSpot = taskEntity.getTaskSpot();
        if (taskSpot == null) {
            fieldTaskDTO.setTaskSpot(null);
        } else {
            fieldTaskDTO.setTaskSpot(new Coordinate(taskSpot));
        }
        return fieldTaskDTO;
    }
}
