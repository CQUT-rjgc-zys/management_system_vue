package com.example.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.system.dto.FieldTaskDTO;
import com.example.system.entity.FieldTaskEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FieldTaskService extends IService<FieldTaskEntity> {

    void addFieldTask(FieldTaskDTO fieldTask);

    void deleteFieldTask(Long id);

    void updateFieldTask(FieldTaskDTO fieldTask);

    List<FieldTaskDTO> getFieldTasks();

    FieldTaskDTO getFieldTaskById(Long id);

    void uploadFile(Long id, MultipartFile file);

    byte[] downloadFile(Long id);

    List<FieldTaskDTO> getAcceptedFieldTasksByUserId(Long userId);
}
