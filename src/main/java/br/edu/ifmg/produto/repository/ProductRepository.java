package br.edu.ifmg.produto.repository;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    SELECT (*) FROM
                    (
                    SELECT DISTINCT p.id, p.name, p.image_url, p.price
                    FROM tb_product p
                    INNER JOIN tb_product_category pc
                    ON pc.product_id = p.id
                    WHERE (:categoriesId IS NULL || pc.category_id in :categoriesId)
                     AND LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%'))
                     ) as tb_result;
                    """,
            countQuery = """
                    SELECT count(*) FROM
                    (
                        SELECT p.id, p.name, p.image_url, p.price
                        FROM tb_product p
                        INNER JOIN tb_product_category pc
                        ON pc.product_id = p.id
                        WHERE (:categoriesId IS NULL || pc.category_id in :categoriesId)
                         AND LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%'))
                     ) as tb_result;
                    """
    )
    public Page<ProductProjection> searchProduct(List<Long> categoriesId, String name, Pageable peageble);
}
