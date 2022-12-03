package org.example;

public record Alimento(Integer id, String nome, Integer porcao, Integer calorias, Integer colesterol, Double proteinas,
                       Boolean gluten, Double gordurasSaturadas, Double sodio, Double acucar, Double lactose) {
}
