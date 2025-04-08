package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.exceptions.DataBaseException;
import br.edu.ifmg.produto.exceptions.ResourceNotFound;
import br.edu.ifmg.produto.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<CategoryDTO> findAll(Pageable pageable) { // Pageble é uma interface que permite paginar os dados
        Page<Category> list = categoryRepository.findAll(pageable);

    return list.map(category -> new CategoryDTO(category));
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
       Optional<Category> obj = categoryRepository.findById(id); // Optional é um objeto que pode ou não ter valor, se não tiver valor ele retorna null
        Category category = obj.orElseThrow(()->
                new ResourceNotFound("Category not found "+id)
        ); // se não tiver valor ele lança uma exceção
        return new CategoryDTO(category);
    }
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category = categoryRepository.save(category); // o metodo 'save' salva o objeto no banco de dados, e eu preciso
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id,CategoryDTO dto){
        try {
            Category entity = categoryRepository.getReferenceById(id); // o metodo 'getReferenceById' busca o objeto no banco de dados
            entity.setName(dto.getName()); // atualiza o objeto com os dados do dto
            entity = categoryRepository.save(entity); // o metodo 'save' salva o objeto no banco de dados, e eu preciso
            return new CategoryDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFound("Category not found "+id);
        }
    }

    @Transactional
    public void delete(Long id){
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFound("Category not found "+id);
        }
        try {
             categoryRepository.deleteById(id);
        }catch (DataIntegrityViolationException e ){
            throw new DataBaseException("Integrity violation");
        }

    }
}
