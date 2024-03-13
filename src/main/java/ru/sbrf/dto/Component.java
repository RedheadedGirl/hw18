package ru.sbrf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Component {

    private String component;
    private Integer amount;

}
