package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> { // camada responsavel por acessar o banco de dados
    // JpaRepository é uma interface que já tem os metodos prontos para acessar o banco de dados
    // Category é a entidade que eu quero acessar e Long é o tipo do id da entidade

}
