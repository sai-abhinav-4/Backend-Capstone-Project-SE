package com.example.productcatalogservice.TableInheritanceExample.JoinedClass;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="jc_mentor")
@PrimaryKeyJoinColumn(name="user_id")
public class Mentor extends User {
    private double rating;
}
