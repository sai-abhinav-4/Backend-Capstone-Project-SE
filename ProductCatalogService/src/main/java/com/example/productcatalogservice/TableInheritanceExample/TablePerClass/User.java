package com.example.productcatalogservice.TableInheritanceExample.TablePerClass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity(name="tpc_user")
@Inheritance(strategy = jakarta.persistence.InheritanceType.TABLE_PER_CLASS)
public class User {
    private String name;
    @Id
    private UUID id;
}
