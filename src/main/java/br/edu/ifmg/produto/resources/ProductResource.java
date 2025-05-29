package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.ProductListDTO;
import br.edu.ifmg.produto.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/product")
@Tag(name = "Product", description = "Controller/Resource for products")
public class ProductResource {


    @Autowired
    private ProductService productService;


    @GetMapping(produces = "application/json")
    @Operation(
            description = "Get all product",
            summary = "Get all product",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200")
            }
    )
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(productService.findAll(pageable));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Get a product",
            summary = "Get a product",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    public ResponseEntity<ProductDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping(produces = "application/json")
    @Operation(
            description = "Create a new product",
            summary = "Create a new product",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO product) {
        URI uri = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(uri).body(productService.insert(product));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Update a product",
            summary = "Update a product",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ResponseEntity<ProductDTO> update(@PathVariable long id, @Valid @RequestBody ProductDTO product) {
        return ResponseEntity.ok().body(productService.update(id, product));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            description = "Delete a product",
            summary = "Delete a product",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbbiden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = "application/json", value = "/paged")
    @Operation(
            description = "Get all product paged",
            summary = "Get all product paged",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200")
            }
    )
    public ResponseEntity<Page<ProductListDTO>> findAllPaged(
            Pageable pageable,
            @RequestParam(value = "CategoryId", defaultValue = "0") String categoryId,
            @RequestParam(value = "name", defaultValue = "") String name
    ) {
        return ResponseEntity.ok().body(productService.findAllPaged(categoryId,name,pageable));
    }

}
