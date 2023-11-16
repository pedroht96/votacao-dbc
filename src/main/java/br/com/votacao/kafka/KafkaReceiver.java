package br.com.votacao.kafka;

import br.com.votacao.domain.VotacaoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

    public void listen(VotacaoDto votacaoDto) {
        LOGGER.info("Mensagem recebida: '{}'", votacaoDto);
    }
}


