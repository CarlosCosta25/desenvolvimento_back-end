package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.ProductListDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.exceptions.DataBaseException;
import br.edu.ifmg.produto.exceptions.ResourceNotFound;
import br.edu.ifmg.produto.projections.ProductProjection;
import br.edu.ifmg.produto.repository.ProductRepository;
import br.edu.ifmg.produto.resources.ProductResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {

        Page<Product> list = productRepository.findAll(pageable);
        return list.map(product -> new ProductDTO(product)
                .add(linkTo(methodOn(ProductResource.class).findAll(null)).withSelfRel())
                .add(linkTo(methodOn(ProductResource.class).findById(product.getId())).withRel("Get a product")
                )
        );
    }


    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        ProductDTO product = new ProductDTO(
                obj.orElseThrow(
                        () -> new ResourceNotFound("Produto não encontrado")
                )
        );

        return product
                .add(linkTo(methodOn(ProductResource.class).findById(product.getId())).withSelfRel())
                .add(linkTo(methodOn(ProductResource.class).findAll(null)).withRel("All products"))
                .add(linkTo(methodOn(ProductResource.class).update(product.getId(), product)).withRel("Upadate product"))
                .add(linkTo(methodOn(ProductResource.class).delete(product.getId())).withRel("Delete product"));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDTOtoEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity)
                .add(linkTo(methodOn(ProductResource.class).findById(entity.getId())).withRel("Find a product"))
                .add(linkTo(methodOn(ProductResource.class).findAll(null)).withRel("All products"))
                .add(linkTo(methodOn(ProductResource.class).update(entity.getId(), new ProductDTO(entity))).withRel("Upadate product"))
                .add(linkTo(methodOn(ProductResource.class).delete(entity.getId())).withRel("Delete product"));
    }

    @Transactional
    public ProductDTO update(long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDTOtoEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity)
                    .add(linkTo(methodOn(ProductResource.class).findById(entity.getId())).withRel("find a product"))
                    .add(linkTo(methodOn(ProductResource.class).findAll(null)).withRel("All products"))
                    .add(linkTo(methodOn(ProductResource.class).delete(entity.getId())).withRel("Delete product"));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Product not found " + id);
        }

    }

    @Transactional
    public void delete(long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFound("Product not found " + id);
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDTOtoEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());
        dto.getCategories()
                .forEach(categoryDTO ->
                        entity.getCategories().add(new Category(categoryDTO)
                        )
                );
    }

    public Page<ProductListDTO> findAllPaged(String categoryId, String name, Pageable pageable) {

        List<Long> ids = null;
        if (categoryId.equals("0"))
            Arrays.stream(categoryId.split(";")).map(
                    id -> Long.parseLong(id)
            ).toList();
        Page<ProductProjection> productsProjection = productRepository.searchProduct(ids, name, pageable);

        List<ProductListDTO> dtos = productsProjection
                .stream().
                    map(
                            ProductListDTO::new
                ).toList();
        return new PageImpl<>(dtos, pageable, productsProjection.getTotalElements());
    }
}

