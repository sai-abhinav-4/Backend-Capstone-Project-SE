package com.example.productcatalogservice.TableInheritanceExample.JoinedClass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity(name="jc_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    private String name;
    @Id
    private UUID id;
}
