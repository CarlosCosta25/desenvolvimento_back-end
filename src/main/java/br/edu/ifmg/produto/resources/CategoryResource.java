package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController // diz que cai responder a requisições Web
@RequestMapping(value = "/category") //defive a rota para acessar essa classe
public class CategoryResource {
    private CategoryService categoryService;

    @GetMapping//diz que essa função responde a uma requisição do tipo get
    public ResponseEntity<List<Category>> findAll(){ // ResponseEntity Monta a resposta http (cabeço, etc) para a api

       return ResponseEntity.ok().body(categoryService.findAll()); // ok e o codigo 200 do http e o body e o conteudo retornado
    }

}
