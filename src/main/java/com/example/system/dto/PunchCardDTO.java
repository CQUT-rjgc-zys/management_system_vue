package com.example.system.dto;

import cn.hutool.core.util.CoordinateUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class PunchCardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long taskId;

    private Timestamp time;

    @NotNull
    @Valid
    private Coordinate spot;
}
