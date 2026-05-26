package com.innovation.training.module.user.controller;

import com.innovation.training.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Operation(summary = "健康检查")
    @GetMapping("/api/health")
    public Result<Map<String, String>> health() {
        return Result.success(Map.of("status", "ok"));
    }
}
