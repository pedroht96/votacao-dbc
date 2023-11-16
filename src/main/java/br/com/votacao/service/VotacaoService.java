package br.com.votacao.service;

import br.com.votacao.domain.VotacaoDto;
import br.com.votacao.kafka.KafkaSender;
import br.com.votacao.model.Pauta;
import br.com.votacao.model.Voto;
import br.com.votacao.repository.SessaoRepository;
import br.com.votacao.repository.VotoRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class VotacaoService {

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final KafkaSender kafkaSender;

    public VotacaoService(VotoRepository votoRepository, SessaoRepository sessaoRepository, KafkaSender kafkaSender) {
        this.votoRepository = votoRepository;
        this.sessaoRepository = sessaoRepository;
        this.kafkaSender = kafkaSender;
    }

    public Voto save(final Voto voto) {
        verifyIfExists(voto);
        return votoRepository.save(voto);
    }

    @SneakyThrows
    private void verifyIfExists(final Voto voto) {
        Optional<Voto> votoByCpfAndPauta = votoRepository.findByCpf(voto.getCpf());

        if (votoByCpfAndPauta.isPresent() && (voto.isNew() || isNotUnique(voto, votoByCpfAndPauta.get()))) {
            throw new Exception();
        }
    }

    private boolean isNotUnique(Voto voto, Voto votoByCpfAndPauta) {
        return voto.alreadyExist() && !votoByCpfAndPauta.equals(voto);
    }

    public List<Voto> findAll() {
        return votoRepository.findAll();
    }

    @SneakyThrows
    public void delete(Voto voto) {
        Optional<Voto> votoById = votoRepository.findById(voto.getId());
        if (!votoById.isPresent()) {
            throw new Exception();
        }
        votoRepository.delete(voto);
    }

    @SneakyThrows
    public List<Voto> findVotosByPautaId(Long id) {
        Optional<List<Voto>> findByPautaId = votoRepository.findByPautaId(id);

        if (!findByPautaId.isPresent()) {
            throw new Exception();
        }

        return findByPautaId.get();
    }

    public VotacaoDto getResultVotacao(Long id) {
        VotacaoDto votacaoPauta = buildVotacaoPauta(id);
        kafkaSender.sendMessage(votacaoPauta);
        return votacaoPauta;
    }

    @SneakyThrows
    public VotacaoDto buildVotacaoPauta(Long id) {
        Optional<List<Voto>> votosByPauta = votoRepository.findByPautaId(id);
        if (!votosByPauta.isPresent() || votosByPauta.get().isEmpty()) {
            throw new Exception();
        }

        Pauta pauta = votosByPauta.get().iterator().next().getPauta();

        Long totalSessoes = sessaoRepository.countByPautaId(pauta.getId());


        Integer total = votosByPauta.get().size();

        Integer totalSim = (int) votosByPauta.get().stream().filter(voto -> Boolean.TRUE.equals(voto.getEscolha()))
                .count();

        Integer totalNao = total - totalSim;

        return VotacaoDto.builder().pauta(pauta).totalVotos(total).totalSessoes(totalSessoes.intValue())
                .totalSim(totalSim).totalNao(totalNao).build();

    }

}
