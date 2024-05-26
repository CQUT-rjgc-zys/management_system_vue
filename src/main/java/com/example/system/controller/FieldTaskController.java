package com.example.system.controller;

import com.example.system.dto.FieldTaskDTO;
import com.example.system.result.CommonResult;
import com.example.system.service.FieldTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getFieldTaskById/{id}")
    public CommonResult<FieldTaskDTO> getFieldTaskById(@PathVariable("id") Long id) {
        FieldTaskDTO result = fieldTaskService.getFieldTaskById(id);
        return CommonResult.success(result);
    }
}
