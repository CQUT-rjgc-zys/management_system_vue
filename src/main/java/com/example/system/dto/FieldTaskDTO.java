package com.example.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class FieldTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Timestamp startTime;

    @NotNull
    private Timestamp endTime;

    @NotNull
    private String taskBrief;

    private String description;

    private Coordinate taskSpot;

    private int status;

    private String fileName;
}
