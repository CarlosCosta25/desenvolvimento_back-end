package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.RoleDTO;
import br.edu.ifmg.produto.dtos.UserDTO;
import br.edu.ifmg.produto.dtos.UserInsertDTO;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.entities.Role;
import br.edu.ifmg.produto.entities.User;
import br.edu.ifmg.produto.exceptions.DataBaseException;
import br.edu.ifmg.produto.exceptions.ResourceNotFound;
import br.edu.ifmg.produto.repository.RoleRepository;
import br.edu.ifmg.produto.repository.UserRepository;
import br.edu.ifmg.produto.resources.ProductResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);

        return list.map(UserDTO::new);
    }

    @Transactional
    public UserDTO findById(Long id){
        Optional<User> obj = repository.findById(id);
        return new UserDTO(
                obj.orElseThrow(
                        () -> new ResourceNotFound("User not found")
                )
        );
    }

    public UserDTO insert(UserInsertDTO dto){
        User entity = new User();
        copyDtoTOEntity(dto,entity);

        entity.setPassword(
                passwordEncoder.
                        encode(dto.getPassword()
                        )
        );

        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyDtoTOEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("User not found " + id);
        }

    }

    @Transactional
    public void delete(long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFound("Product not found " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    public void copyDtoTOEntity(UserDTO dto, User entity){
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        for (RoleDTO role : dto.getRole()){
            Role r = roleRepository.getReferenceById(role.getId());
            entity.getRole().add(r);
        }
        /*entity.setRole(dto.getRole(dto.getId()).stream().map(
                role -> roleRepository.getReferenceById(role.getId());
                entity.getRole().add(new Role(role));
        ).toList());*/
    }

}
