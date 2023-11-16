package br.com.votacao.service;

import br.com.votacao.model.Pauta;
import br.com.votacao.model.Sessao;
import br.com.votacao.repository.PautaRepository;
import br.com.votacao.repository.SessaoRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;

    public SessaoService(SessaoRepository sessaoRepository, PautaRepository pautaRepository) {
        this.sessaoRepository = sessaoRepository;
        this.pautaRepository = pautaRepository;
    }

    public List<Sessao> findAll() {
        return sessaoRepository.findAll();
    }

    @SneakyThrows
    public Sessao createSession(Long id, Sessao sessao) {
        Optional<Pauta> findById = pautaRepository.findById(id);
        if (!findById.isPresent()) {
            throw new Exception();
        }
        sessao.setPauta(findById.get());
        return save(sessao);
    }

    private Sessao save(final Sessao sessao) {
        if (sessao.getDataInicio() == null) {
            sessao.setDataInicio(LocalDateTime.now());
        }
        if (sessao.getMinutosValidade() == null) {
            sessao.setMinutosValidade(1L);
        }

        return sessaoRepository.save(sessao);

    }

    @SneakyThrows
    public void delete(Long id) {
        Optional<Sessao> sessaoById = sessaoRepository.findById(id);
        if (!sessaoById.isPresent()) {
            throw new Exception();
        }
        sessaoRepository.delete(sessaoById.get());
    }

    void deleteByPautaId(Long id) {
        Optional<List<Sessao>> sessaos = sessaoRepository.findByPautaId(id);
        sessaos.ifPresent(sessaoList -> sessaoList.forEach(sessaoRepository::delete));
    }

    @SneakyThrows
    public Sessao findById(Long id) {
        Optional<Sessao> findById = sessaoRepository.findById(id);
        if (!findById.isPresent()) {
            throw new Exception();
        }
        return findById.get();
    }

    @SneakyThrows
    public Sessao findByIdAndPautaId(Long idSessao, Long pautaId) {
        Optional<Sessao> findByIdAndPautaId = sessaoRepository.findByIdAndPautaId(idSessao, pautaId);
        if (!findByIdAndPautaId.isPresent()) {
            throw new Exception();
        }
        return findByIdAndPautaId.get();
    }
}
