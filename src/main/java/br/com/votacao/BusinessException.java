package br.com.votacao;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class BusinessException extends RuntimeException {

    private final String code;
    private final HttpStatus status;


}
