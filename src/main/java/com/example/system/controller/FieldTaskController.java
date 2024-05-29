package com.example.system.controller;

import com.example.system.dto.FieldTaskDTO;
import com.example.system.result.CommonResult;
import com.example.system.service.FieldTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fieldTask")
public class FieldTaskController {

    @Autowired
    private FieldTaskService fieldTaskService;

    /**
     * 新增任务
     */
    @PostMapping("/addFieldTask")
    public CommonResult<Void> addFieldTask(@RequestBody @Validated FieldTaskDTO fieldTask) {
        fieldTaskService.addFieldTask(fieldTask);
        return CommonResult.success();
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/deleteFieldTask/{id}")
    public CommonResult<Void> deleteFieldTask(@PathVariable("id") Long id) {
        fieldTaskService.deleteFieldTask(id);
        return CommonResult.success();
    }

    /**
     * 修改任务
     */
    @PutMapping("/updateFieldTask")
    public CommonResult<Void> updateFieldTask(@RequestBody FieldTaskDTO fieldTask) {
        fieldTaskService.updateFieldTask(fieldTask);
        return CommonResult.success();
    }

    /**
     * 获取任务列表
     */
    @GetMapping("/getFieldTasks")
    public CommonResult<List<FieldTaskDTO>> getFieldTasks() {
        List<FieldTaskDTO> results = fieldTaskService.getFieldTasks();
        return CommonResult.success(results);
    }

    /**
     * 根据用户id获取已接受的任务列表
     */
    @GetMapping("/getAcceptedFieldTasksByUserId/{userId}")
    public CommonResult<List<FieldTaskDTO>> getAcceptedFieldTasksByUserId(@PathVariable("userId") Long userId) {
        List<FieldTaskDTO> results = fieldTaskService.getAcceptedFieldTasksByUserId(userId);
        return CommonResult.success(results);
    }

    /**
     * 根据id获取任务
     */
    @GetMapping("/getFieldTaskById/{id}")
    public CommonResult<FieldTaskDTO> getFieldTaskById(@PathVariable("id") Long id) {
        FieldTaskDTO result = fieldTaskService.getFieldTaskById(id);
        return CommonResult.success(result);
    }

    /**
     * 上传相关文件
     */
    @PostMapping("/uploadFile/{id}")
    public CommonResult<Void> uploadFile(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        fieldTaskService.uploadFile(id, file);
        return CommonResult.success();
    }

    /**
     * 下载相关文件
     */
    @GetMapping("/downloadFile/{id}")
    public CommonResult<byte[]> downloadFile(@PathVariable("id") Long id) {
        byte[] data =fieldTaskService.downloadFile(id);
        return CommonResult.success(data);
    }
}
