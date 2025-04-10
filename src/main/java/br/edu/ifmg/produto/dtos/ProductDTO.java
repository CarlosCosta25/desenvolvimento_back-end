package br.edu.ifmg.produto.dtos;

import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String Description;
    private double price;
    private String imageUrl;
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(long id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        Description = description;
        this.price = price;
        this.imageUrl = imageUrl;

    }
    public ProductDTO() {
    }
    public ProductDTO(Product entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.Description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        entity.getCategories()
                .forEach(category ->
                        this.categories.add(new CategoryDTO(category)
                        )
                );
    }

    public ProductDTO(Product product, Set<Category> categories){
        this(product);//chama um construtor que recebe o construtor da entidade produto
         categories.forEach(category ->
                 this.categories.add(new CategoryDTO(category))
         );
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
        if (!(o instanceof ProductDTO product)) return false;
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
                ", Description='" + Description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories=" + categories +
                '}';
    }
}
