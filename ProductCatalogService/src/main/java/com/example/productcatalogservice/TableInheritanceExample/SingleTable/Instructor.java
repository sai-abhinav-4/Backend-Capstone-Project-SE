package com.example.productcatalogservice.TableInheritanceExample.SingleTable;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_instructor")
@DiscriminatorValue(value = "INSTRUCTOR")
public class Instructor extends User {
    private String company;
}
