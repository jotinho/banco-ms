CREATE TABLE cliente (
  id UUID PRIMARY KEY,
  contrasena VARCHAR(255) NOT NULL,
  estado BOOLEAN NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  genero VARCHAR(10),
  edad SMALLINT,
  identificacion VARCHAR(30),
  direccion VARCHAR(200),
  telefono VARCHAR(30)
);

CREATE TABLE cuenta (
  id UUID PRIMARY KEY,
  numero_cuenta VARCHAR(30) UNIQUE NOT NULL,
  tipo VARCHAR(12) NOT NULL,
  saldo NUMERIC(15,2) NOT NULL DEFAULT 0,
  estado BOOLEAN NOT NULL,
  id_cliente UUID NOT NULL REFERENCES cliente(id)
);

CREATE TABLE movimiento (
  id UUID PRIMARY KEY,
  fecha TIMESTAMP NOT NULL,
  tipo VARCHAR(12) NOT NULL,
  valor NUMERIC(15,2) NOT NULL,
  saldo NUMERIC(15,2) NOT NULL,
  id_cuenta UUID NOT NULL REFERENCES cuenta(id)
);
