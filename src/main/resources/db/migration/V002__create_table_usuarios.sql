
CREATE TABLE
    usuarios (
        id BIGINT NOT NULL AUTO_INCREMENT,
        username VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        PRIMARY KEY (id)
    );
