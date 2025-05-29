package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.services.ProductService;
import br.edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductResource.class,excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ProductResourceTest {
    //camada que quero testar
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService; //camada que quero mocar

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
        productDTO = Factory.createProductDTO();
        productDTO.setId(existingId);
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    void findAllShouldReturnAllPage() throws Exception {
        //cria o metodo mocado
        when(productService.findAll(ArgumentMatchers.any())).thenReturn(page);
        ResultActions result = mockMvc.perform(
                get("/product").
                        accept("application/json")
        );
        result.andExpect(status().isOk());
    }
@Test
    void findByIdshouldReturnProductWhenIdExists() throws Exception {
        when(productService.findById(existingId)).thenReturn(productDTO);
        ResultActions result = mockMvc.perform(
                get("/product/{id}").
                        accept("application/json")
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
    }
}
