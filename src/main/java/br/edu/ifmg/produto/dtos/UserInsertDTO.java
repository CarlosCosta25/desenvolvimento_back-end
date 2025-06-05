package br.edu.ifmg.produto.dtos;

import jakarta.validation.constraints.NotBlank;

public class UserInsertDTO extends UserDTO{
    @NotBlank
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
