CREATE TABLE Client
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
);

-- Creación de la tabla Ticket
CREATE TABLE Ticket
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    user_id INT REFERENCES Client (id) ON DELETE CASCADE,
    carnivalRow VARCHAR(255) NOT NULL,
);

