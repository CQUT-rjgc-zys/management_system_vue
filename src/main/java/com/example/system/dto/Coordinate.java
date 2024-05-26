package com.example.system.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Coordinate {

    @NotNull
    private double x;

    @NotNull
    private double y;

    public Coordinate(String coordinateStr) {
        String[] coordinates = coordinateStr.split(",");
        x = Double.parseDouble(coordinates[0]);
        y = Double.parseDouble(coordinates[1]);
    }

    public String toString() {
        return x + "," + y;
    }

}
