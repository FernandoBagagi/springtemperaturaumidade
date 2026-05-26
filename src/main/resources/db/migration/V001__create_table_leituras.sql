
CREATE TABLE
    leituras (
        id BIGINT NOT NULL AUTO_INCREMENT,
        data_hora DATETIME NOT NULL,
        temperatura DECIMAL(5, 2) NOT NULL,
        umidade DECIMAL(5, 2) NOT NULL
    );
