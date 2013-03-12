/* Cria Tabelas */
/* Usuário */
CREATE TABLE IF NOT EXISTS usuario(
	idUsuario INTEGER NOT NULL UNIQUE,
	username VARCHAR(40) NOT NULL,
	senha VARCHAR(20) NOT NULL,
	status VARCHAR(1) DEFAULT '0',
	CONSTRAINT pk_usuario PRIMARY KEY (username)
);
/* Sequência */
CREATE SEQUENCE seq_usuario START WITH 1;
