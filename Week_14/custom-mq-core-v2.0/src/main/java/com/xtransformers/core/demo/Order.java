package com.xtransformers.core.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Order {

    private Long id;
    private Long ts;
    private String symbol;
    private Double price;

}