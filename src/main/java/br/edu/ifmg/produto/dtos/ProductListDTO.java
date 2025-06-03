package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.projections.ProductProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ProductListDTO extends RepresentationModel<ProductListDTO>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Database genereted ID product")
    private long id;
    @Schema(description = "Product name")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    private String name;
    @Schema(description = "Product price")
    @Positive(message = "O preço deve ser um valor positivo")
    private double price;
    @Schema(description = "Product url Image")
    private String imageUrl;

    public ProductListDTO() {
    }
public ProductListDTO(long id, String name, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductListDTO(ProductListDTO productListDTO) {
        this.id = productListDTO.getId();
        this.name = productListDTO.getName();
        this.price = productListDTO.getPrice();
        this.imageUrl = productListDTO.getImageUrl();
    }

    public ProductListDTO(ProductProjection entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductListDTO product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
