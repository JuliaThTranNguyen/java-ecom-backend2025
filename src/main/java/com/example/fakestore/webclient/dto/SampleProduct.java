package com.example.fakestore.webclient.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SampleProduct {
    private String title;
    private double price;
    private String description;
    String image;
}
