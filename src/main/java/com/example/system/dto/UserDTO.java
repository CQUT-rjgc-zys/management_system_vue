package com.example.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jdk.nashorn.internal.runtime.Debug;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private String gender;

    private String password;

    private String jobNumber;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long department;

    private Integer taskAmount;
}
