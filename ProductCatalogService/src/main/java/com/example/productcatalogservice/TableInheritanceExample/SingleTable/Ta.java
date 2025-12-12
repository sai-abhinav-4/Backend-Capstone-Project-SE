package com.example.productcatalogservice.TableInheritanceExample.SingleTable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="st_ta")
@DiscriminatorValue(value = "TA")
public class Ta extends User {
    private Integer helpRequests;
}
