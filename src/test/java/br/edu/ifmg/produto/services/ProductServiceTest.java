package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.exceptions.ResourceNotFound;
import br.edu.ifmg.produto.repository.ProductRepository;
import br.edu.ifmg.produto.util.Factory;
import net.bytebuddy.asm.MemberSubstitution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private VerificationMode times;
    //private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
       /* ProductDTO product = Factory.createProductDTO();
        page = PageImpl<>(List.of(product));*/
    }

    @Test
    @DisplayName("Verificando se o objeto foi deletado do BD")
    void deleteShouldDoNothingWenIdExists() {

        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);
        Assertions.assertDoesNotThrow(
                () -> productService.delete(existingId)
        );

        verify(
                productRepository,
                Mockito.times(1))
                        .deleteById(existingId);
    }

    @Test
    @DisplayName("Verificando se levanta uma exceção se o não existe BD")
    void deleteShouldThrowExceptionWhenIdNonExists() {

        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        /*doNothing().when(productRepository).deleteById(existingId);*/
        Assertions.assertThrows(ResourceNotFound.class,
                () -> productService.delete(nonExistingId)
        );

        verify(
                productRepository,
                Mockito.times(0))
                .deleteById(existingId);
    }

    @Test
    @DisplayName("Verificando se o findAll retorna os dados páginados")
    void findAllShouldReturnOnePage() {

        when(productRepository
                .findAll((Pageable) any()))
                .thenReturn(
                        new PageImpl<Product>
                                (List.of(Factory.createProduct())
                                ));
        Pageable pagina = PageRequest.of(0,10);
        Page <ProductDTO> result = productService.findAll(Pageable.unpaged());
        verify(productRepository, times(1))
                .findAll(pagina);
        //Assertions.assertNotNull((result));
    }

    @Test
    @DisplayName("Verificando se o findById retorna o objeto correto")
    void findByIdShouldReturnProductWhenIdExists() {
        Product p = Factory.createProduct();
        p.setId(existingId);
        //moca os dados
        when(productRepository.findById(existingId)).thenReturn(Optional.of(p));

        ProductDTO dto = productService.findById(existingId);
        Assertions.assertNotNull(dto);
        verify(productRepository, times(1))
                .findById(existingId);
    }

}