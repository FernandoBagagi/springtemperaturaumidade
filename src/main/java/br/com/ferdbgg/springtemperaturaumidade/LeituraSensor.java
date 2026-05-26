package br.com.ferdbgg.springtemperaturaumidade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "LeituraSensor")
@Table(name = "leituras")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal temperatura;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal umidade;

    public LeituraSensor(BigDecimal temperatura, BigDecimal umidade) {
        this.id = null;
        this.dataHora = LocalDateTime.now();
        this.temperatura = temperatura;
        this.umidade = umidade;
    }

}
