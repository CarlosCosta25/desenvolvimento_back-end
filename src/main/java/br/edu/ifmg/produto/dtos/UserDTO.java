package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Role;
import br.edu.ifmg.produto.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDTO {
    long id;
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Favor informar um e-mail válido")
    private String email;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO() {
    }

    public UserDTO(UserDTO entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
    }

    public UserDTO(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();

        user.getRole().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public UserDTO(UserDTO entity, Set<RoleDTO> roles) {
        this(entity);
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Set<RoleDTO> getRole() {
        return roles;
    }

    public void setRole(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserDTO userDTO)) return false;
        return id == userDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

