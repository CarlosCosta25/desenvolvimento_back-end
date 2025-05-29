package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.util.Factory;
import br.edu.ifmg.produto.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.ResultActions;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
    @Autowired
    // metodo que faz a requisição HTTP
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String username;
    private  String password;
    private String token;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2000L;
        username ="maria@gmail.com";
        password = "123456";
        token = tokenUtil.obtainAccessToken(mockMvc, username, password);
    }

    @Test
    void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/product?page=0&size=10&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
    }

    @Test
    void updateShouldReturnDTOWhenExists() throws Exception {

        ProductDTO dto = Factory.createProductDTO();
        String dtoJson = objectMapper.writeValueAsString(dto);
        String nameExpected = dto.getName();
        String descriptionExpected = dto.getDescription();
        ResultActions result = mockMvc.perform(
                put("/product/{id}", existingId)
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(nameExpected));
        result.andExpect(jsonPath("$.description").value(descriptionExpected));

    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        ProductDTO dto = Factory.createProductDTO();
        String dtoJson = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(
                put("/product/{id}", nonExistingId)
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );
        result.andExpect(status().isNotFound());

    }

    @Test
    void insertShouldReturnNewObjectWhenDataAreCorrect() throws Exception {
        ProductDTO dto = Factory.createProductDTO();
        String dtoJson = objectMapper.writeValueAsString(dto);
        String nameExpected = dto.getName();
        ResultActions result = mockMvc.perform(
                post("/product")
                        .content(dtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(26L));
        result.andExpect(jsonPath("$.name").value(nameExpected));
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/product/{id}",existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );
        result.andExpect(status().isNoContent());
    }
@Test
    void deleteShouldReturnNoContentWhenDoesNotExists() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/product/{id}",nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );
        result.andExpect(status().isNotFound());
    }
    @Test
    void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/product/{id}",existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );
        result.andExpect(status().isOk());
        String resultJson = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultJson);
        ProductDTO dto = objectMapper.readValue(resultJson,ProductDTO.class);
        Assertions.assertEquals(existingId,dto.getId());
        Assertions.assertEquals("The Lord of the Rings",dto.getName());

    }
}
