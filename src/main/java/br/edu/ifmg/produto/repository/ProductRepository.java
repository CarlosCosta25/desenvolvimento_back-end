package br.edu.ifmg.produto.repository;
import br.edu.ifmg.produto.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
