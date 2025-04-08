package br.edu.ifmg.produto.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity //Define que no banco vai ter uma tabela do tipo category
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Diz que é um chave que vai se alto incrementer sozinha
    private Long id;
    private String name;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //Define que o tipo da coluna é timestamp
    private Instant createdAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") //Define que o tipo da coluna é timestamp
    private Instant updatedAt;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    @PrePersist //Define que esse metodo vai ser chamado antes de persistir(criar) no banco
    private void prePersist() {
        createdAt = Instant.now();
    }
    @PreUpdate //Define que esse metodo vai ser chamado antes de atualizar no banco
    private void preUpdate() {
        updatedAt = Instant.now();
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
