package com.mchs.mental_health_system.domain.model.entities.user;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "managers")
@DiscriminatorValue("MANAGER")
@Getter
@Setter
public class Manager extends SystemUser {

}