package com.example.system.controller;

import com.example.system.dto.PunchCardDTO;
import com.example.system.result.CommonResult;
import com.example.system.service.PunchCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punchcard")
public class PunchCardController {

    @Autowired
    private PunchCardService punchCardService;

    /**
     * 新增打卡信息
     */
    @PostMapping("/addPunchCardInfo")
    public CommonResult<Void> addPunchCardInfo(@RequestBody @Validated PunchCardDTO punchCard) {
        punchCardService.addPunchCardInfo(punchCard);
        return CommonResult.success(null);
    }

    /**
     * 根据外勤任务获取打卡信息
     */
    @GetMapping("/getAllPunchCardInfo/{taskID}")
    public CommonResult<List<PunchCardDTO>> getAllPunchCardInfo(@PathVariable("taskID") Long taskID) {
        List<PunchCardDTO> punchCardList = punchCardService.getAllPunchCardInfo(taskID);
        return CommonResult.success(punchCardList);
    }
}
