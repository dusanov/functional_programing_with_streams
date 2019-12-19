package me.dusanov.functional.stream.example;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

// i could have used @Data so i only put props and no constructor
public class Employee{
    @Getter @Setter private String name;
    @Getter @Setter private int salary;

    public Employee(String name, int salary){
        this.name = name;
        this.salary = salary;
    }


}
