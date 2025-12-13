package com.algafoods.api.controller;

import com.algafoods.api.mappers.GrupoMapper;
import com.algafoods.api.mappers.UsuarioMapper;
import com.algafoods.api.model.GrupoModel;
import com.algafoods.api.model.input.UsuarioInputDTO;
import com.algafoods.api.model.output.RestauranteOutputDTO;
import com.algafoods.api.model.output.UsuarioOutputDTO;
import com.algafoods.domain.model.Usuario;
import com.algafoods.domain.service.GrupoService;
import com.algafoods.domain.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }


    @GetMapping
    public List<UsuarioOutputDTO> listar(){
        return usuarioService.findAll().stream()
                .map(usuarioMapper::toModel)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<UsuarioOutputDTO> adicionar(
            @RequestBody
            @Valid
            UsuarioInputDTO usuarioInputDTO
    ) {
        Usuario usuario = usuarioMapper.toDomain(usuarioInputDTO);
        UsuarioOutputDTO newUsuario =  usuarioMapper.toModel(usuarioService.create(usuario));

        return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);
    }
}
