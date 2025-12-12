package com.example.productcatalogservice.TableInheritanceExample.SingleTable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="st_mentor")
@DiscriminatorValue(value = "MENTOR")
public class Mentor extends User {
    private double rating;
}
