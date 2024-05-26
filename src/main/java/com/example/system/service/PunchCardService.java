package com.example.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.system.dto.PunchCardDTO;
import com.example.system.entity.PunchCardEntity;

import java.util.List;

public interface PunchCardService extends IService<PunchCardEntity> {

    void addPunchCardInfo(PunchCardDTO punchCard);

    List<PunchCardDTO> getAllPunchCardInfo(Long taskID);

}
