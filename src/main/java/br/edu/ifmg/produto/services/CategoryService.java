package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService{ // camada que busca no banco de dados

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true) // apenas consulta aos dados não modifica os dados
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();

        return list.stream().
                map(category -> new CategoryDTO(category))
                .collect(
                        Collectors.toList()
                );

    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       Optional<Category> obj = categoryRepository.findById(id); // Optional é um objeto que pode ou não ter valor, se não tiver valor ele retorna null
        Category category = obj.get(); // se não tiver valor ele lança uma exceção
        return new CategoryDTO(category);
    }
}
