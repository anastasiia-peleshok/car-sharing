package com.example.carsharing.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDto {
    private String columnName;

    private Object columnValue;

}
