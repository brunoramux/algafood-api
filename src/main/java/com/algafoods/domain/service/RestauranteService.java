package com.algafoods.domain.service;

import com.algafoods.domain.exception.EntidadeEmUsoException;
import com.algafoods.domain.exception.EntidadeNaoEncontradaException;
import com.algafoods.domain.model.*;
import com.algafoods.domain.repository.CidadeRepository;
import com.algafoods.domain.repository.CozinhaRepository;
import com.algafoods.domain.repository.FormaPagamentoRepository;
import com.algafoods.domain.repository.RestauranteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    public static final String MENSAGEM_RESTAURANTE_NAO_ENCONTRADO = "Restaurante com o código %d não encontrado.";
    public static final String MENSAGEM_RESTAURANTE_EM_USO = "Restaurante em uso.";

    private final RestauranteRepository restauranteRepository;

    private final CozinhaRepository cozinhaRepository;

    private final FormaPagamentoRepository formaPagamentoRepository;

    private final CidadeRepository cidadeRepository;

    private final UsuarioService usuarioService;

    public RestauranteService(RestauranteRepository restauranteRepository, CozinhaRepository cozinhaRepository, FormaPagamentoRepository formaPagamentoRepository, CidadeRepository cidadeRepository, UsuarioService usuarioService) {
        this.restauranteRepository = restauranteRepository;
        this.cozinhaRepository = cozinhaRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.cidadeRepository = cidadeRepository;
        this.usuarioService = usuarioService;
    }

    public Restaurante find(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MENSAGEM_RESTAURANTE_NAO_ENCONTRADO,  restauranteId)));
    }

    public List<Restaurante> findAll(){
        return restauranteRepository.findAll();
    }

    public void save(Restaurante restaurante) {
        restauranteRepository.save(restaurante);
    }

    @Transactional
    public Restaurante create(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cozinha informada não foi encontrada."));

        restaurante.setCozinha(cozinha);

        Set<FormaPagamento> formasPagamento = restaurante.getFormasPagamento()
                .stream()
                .map(fp -> formaPagamentoRepository.findById(fp.getId())
                        .orElseThrow(() -> new EntidadeNaoEncontradaException(
                                "Forma de Pagamento informada não foi encontrada: " + fp.getId())))
                .collect(Collectors.toSet());

        restaurante.setFormasPagamento(formasPagamento.stream().toList());
        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public Restaurante update(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cozinha informada não foi encontrada."));

        Long cidadeId = restaurante.getEndereco().getCidade().getId();
        Cidade cidade = cidadeRepository.findById(cidadeId).orElseThrow(() -> new EntidadeNaoEncontradaException("Cidade não encontrada."));

        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);


        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public void delete(Long restauranteId) {
        Restaurante restaurante = this.find(restauranteId);
        try {
            restauranteRepository.deleteById(restauranteId);
            restauranteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(MENSAGEM_RESTAURANTE_EM_USO);
        }
    }

    @Transactional
    public void ativar(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Restaurante não encontrado")
        );

        restaurante.ativar();
        restauranteRepository.save(restaurante);
    }

    @Transactional
    public void ativar(List<Long> restauranteIds){
        for (Long restauranteId : restauranteIds) {
            this.ativar(restauranteId);
        }
    }

    @Transactional
    public void desativar(List<Long> restauranteIds){
        restauranteIds.forEach(this::desativar);
    }

    @Transactional
    public void desativar(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Restaurante não encontrado")
        );

        restaurante.desativar();
        restauranteRepository.save(restaurante);
    }

    @Transactional
    public void abrir(Long restauranteId) {
        Restaurante restaurante = this.find(restauranteId);
        restaurante.abrir();
        restauranteRepository.save(restaurante);
    }

    @Transactional
    public void fechar(Long restauranteId) {
        Restaurante restaurante = this.find(restauranteId);
        restaurante.fechar();
        restauranteRepository.save(restaurante);
    }

    @Transactional
    public void vincularUsuarioResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = this.find(restauranteId);
        Usuario usuario = usuarioService.find(usuarioId);

        restaurante.adicionarUsuario(usuario);

        restauranteRepository.save(restaurante);
    }

    @Transactional
    public void desvincularUsuarioResponsavel(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = this.find(restauranteId);
        Usuario usuario = usuarioService.find(usuarioId);

        restaurante.removerUsuario(usuario);
        restauranteRepository.save(restaurante);
    }

    public List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal) {
        return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
    }

    public List<Restaurante> findByNomeAndTaxaFrete(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {
        return restauranteRepository.find(nome, taxaInicial, taxaFinal);
    }

    public List<Restaurante> findByFreteGratis(String nome) {
        return restauranteRepository.findFreteGratis(nome);
    }

    public Optional<Restaurante> findFirst() {
        return restauranteRepository.buscarPrimeiro();
    }

    public Set<Usuario> findUsuariosResponsaveis(Long id) {
        Restaurante restaurante = this.find(id);

        return restaurante.getUsuarios();
    }
}
