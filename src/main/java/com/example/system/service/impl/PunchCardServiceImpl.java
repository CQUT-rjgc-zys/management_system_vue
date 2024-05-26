package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.system.dto.Coordinate;
import com.example.system.dto.PunchCardDTO;
import com.example.system.entity.FieldTaskEntity;
import com.example.system.entity.PunchCardEntity;
import com.example.system.mapper.FieldTaskMapper;
import com.example.system.mapper.PunchCardMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.PunchCardService;
import com.example.system.util.UUIDUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PunchCardServiceImpl extends ServiceImpl<PunchCardMapper, PunchCardEntity> implements PunchCardService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FieldTaskMapper fieldTaskMapper;

    @Override
    public void addPunchCardInfo(PunchCardDTO punchCard) {
        Long userId = punchCard.getUserId();
        if (userMapper.selectById(userId) == null) {
            throw new IllegalArgumentException("不存在id为：" + userId + "的员工信息");
        }
        Long taskId = punchCard.getTaskId();
        FieldTaskEntity fieldTaskEntity = fieldTaskMapper.selectById(taskId);
        if (fieldTaskEntity == null) {
            throw new IllegalArgumentException("不存在id为：" + taskId + "的外勤任务信息");
        }
        PunchCardEntity punchCardEntity = new PunchCardEntity();
        punchCardEntity.setUserId(UUIDUtil.uuid());
        punchCardEntity.setTaskId(punchCard.getTaskId());
        punchCardEntity.setUserId(punchCard.getUserId());
        punchCardEntity.setTime(new Timestamp(System.currentTimeMillis()));
        punchCardEntity.setSpot(punchCard.getSpot().toString());
        save(punchCardEntity);
    }

    @Override
    public List<PunchCardDTO> getAllPunchCardInfo(Long taskID) {
        QueryWrapper<PunchCardEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskID);
        queryWrapper.orderByAsc("time");
        List<PunchCardEntity> punchCardEntities = list(queryWrapper);
        List<PunchCardDTO> punchCardDTOS = new ArrayList<>();
        punchCardEntities.forEach(punchCardEntity -> {
            PunchCardDTO punchCardDTO = new PunchCardDTO();
            BeanUtils.copyProperties(punchCardEntity, punchCardDTO);
            punchCardDTO.setSpot(new Coordinate(punchCardEntity.getSpot()));
            punchCardDTOS.add(punchCardDTO);
        });
        return punchCardDTOS;
    }
}
