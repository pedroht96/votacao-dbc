package br.com.votacao.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PAUTA")
public class Pauta implements Serializable {
    @Id
    @SequenceGenerator(name = "pauta_seq", sequenceName = "pauta_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pauta_seq")
    private Long id;

    @NotBlank(message = "pauta-1")
    private String nome;
}
