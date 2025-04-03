package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.services.CategoryService;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController // diz que cai responder a requisições Web
@RequestMapping(value = "/category") //defive a rota para acessar essa classe
public class CategoryResource {
    @Autowired  // faz a injeção de dependência, ou seja, cria um objeto do tipo CategoryService
    private CategoryService categoryService;

    @GetMapping//diz que essa função responde a uma requisição do tipo get
    public ResponseEntity<List<CategoryDTO>> findAll(){ // ResponseEntity Monta a resposta http (cabeço, etc) para a api
        List<CategoryDTO> listCategoryDTO = categoryService.findAll(); // chama o serviço que vai buscar no banco de dados
       return ResponseEntity.ok().body(listCategoryDTO); // ok e o codigo 200 do http e o body e o conteudo retornado
    }

    @GetMapping(value = "/{id}")//diz que essa função responde a uma requisição do tipo get para um id
    public ResponseEntity<CategoryDTO> findById( @PathVariable Long id){ // @PathVariable diz que o id vai vir na url
        return ResponseEntity.ok().body(categoryService.findById(id)); // ok e o codigo 200 do http e o body e o conteudo retornado
    }
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){

        dto = categoryService.insert(dto); // chama o serviço que vai buscar no banco de dados

        URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                        .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
    @PutMapping(value = "/{id}") //diz que essa função responde a uma requisição do tipo put para um id
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
        dto = categoryService.update(id, dto); // chama o serviço que vai buscar no banco de dados
        return ResponseEntity.ok().body(dto);
    }

}
