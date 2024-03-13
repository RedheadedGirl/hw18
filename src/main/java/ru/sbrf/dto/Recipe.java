package ru.sbrf.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Recipe {

    private String name;
    private List<Component> components;

    public Recipe() {
        this.components = new ArrayList<>();
    }

}
