package com.project.dscatalog.services;

import com.project.dscatalog.dto.RoleDTO;
import com.project.dscatalog.dto.UserDTO;
import com.project.dscatalog.dto.UserInsertDTO;
import com.project.dscatalog.entities.Role;
import com.project.dscatalog.entities.User;
import com.project.dscatalog.repositories.RoleRepository;
import com.project.dscatalog.repositories.UserRepository;
import com.project.dscatalog.services.exceptions.DataBaseException;
import com.project.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        User userEntity = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(userEntity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userDTO) {
        User userEntity = new User();
        copyDtoToEntity(userDTO, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassWord()));
        userEntity = userRepository.save(userEntity);
        return new UserDTO(userEntity);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        try {
            User userEntity = userRepository.getOne(id);
            copyDtoToEntity(userDTO, userEntity);
            return new UserDTO(userRepository.save(userEntity));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id: " + id + " not found");
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ID: " + id + " not found.");
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User userEntity) {
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setLastName(userDTO.getLastName());

        userEntity.getRoles().clear();
        for (RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.getOne(roleDTO.getId());
            userEntity.getRoles().add(role);
        }
    }
}
