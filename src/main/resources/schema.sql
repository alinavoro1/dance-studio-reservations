CREATE TABLE IF NOT EXISTS studiouri (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    adresa VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sali (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    capacitate INT,
    studio_id INT,
    FOREIGN KEY (studio_id) REFERENCES studiouri(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS profesori (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefon VARCHAR(20),
    specializare VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS clienti (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefon VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS clase_dans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    stil VARCHAR(50),
    dificultate VARCHAR(30),
    pret_sedinta DOUBLE,
    sala_id INT,
    profesor_id INT,
    FOREIGN KEY (sala_id) REFERENCES sali(id),
    FOREIGN KEY (profesor_id) REFERENCES profesori(id)
);



CREATE TABLE IF NOT EXISTS rezervari (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    clasa_id INT,
    data_rezervare TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clienti(id),
    FOREIGN KEY (clasa_id) REFERENCES clase_dans(id)
);

CREATE TABLE IF NOT EXISTS abonamente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    tip_abonament VARCHAR(50),
    sedinte_ramase INT,
    data_expirare DATE,
    FOREIGN KEY (client_id) REFERENCES clienti(id)
);

CREATE TABLE IF NOT EXISTS program_clase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clasa_id INT NOT NULL,
    ziua VARCHAR(20) NOT NULL,
    ora_inceput TIME NOT NULL,
    ora_sfarsit TIME NOT NULL,
    FOREIGN KEY (clasa_id) REFERENCES clase_dans(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviewuri (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    clasa_id INT,
    profesor_id INT,
    nota INT,
    comentariu TEXT,
    FOREIGN KEY (client_id) REFERENCES clienti(id),
    FOREIGN KEY (clasa_id) REFERENCES clase_dans(id),
    FOREIGN KEY (profesor_id) REFERENCES profesori(id)
);

CREATE TABLE IF NOT EXISTS plati (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    abonament_id INT,
    suma DOUBLE,
    metoda VARCHAR(20),
    data_plata TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clienti(id),
    FOREIGN KEY (abonament_id) REFERENCES abonamente(id)
);

CREATE TABLE IF NOT EXISTS profesor_stil (
    id INT AUTO_INCREMENT PRIMARY KEY,
    profesor_id INT NOT NULL,
    stil VARCHAR(50) NOT NULL,
    FOREIGN KEY (profesor_id) REFERENCES profesori(id) ON DELETE CASCADE
);

INSERT INTO studiouri (nume, adresa)
VALUES ('Cobo', 'Bucuresti - Centru'),
       ('Prestige', 'Bucuresti - Sector 2'),
       ('Piticu', 'Bucuresti - Sector 4'),
       ('Urban Groove', 'Cluj-Napoca - Centru'),
       ('Dance Hub', 'Timisoara - Complex');


INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 1', 20, 1);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 2', 15, 1);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 3', 10, 1);

INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 1', 20, 2);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 2', 17, 2);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 3', 15, 2);

INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 1', 30, 3);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 2', 20, 3);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Sala 3', 10, 3);

INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Urban A', 25, 4);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Urban B', 20, 4);
INSERT INTO sali (nume, capacitate, studio_id)
VALUES ('Main Hall', 40, 5);


INSERT INTO profesori (nume, email, telefon)
VALUES ('Ana', 'ana@mail.com', '0711000000'),
       ('Ioana', 'ioana@mail.com', '0712000000'),
       ('Alex', 'alex@mail.com', '0713000000'),
       ('Beatrice', 'beatrice@mail.com', '0714000000'),
       ('Ema', 'ema@mail.com', '0715000000'),
       ('Andra', 'andra@mail.com', '0716000000'),
       ('Vlad', 'vlad@mail.com', '0717000000'),
       ('Maria', 'maria.prof@mail.com', '0718000000'),
       ('David', 'david@mail.com', '0719000000');

INSERT INTO profesor_stil (profesor_id, stil) VALUES(1,'HIPHOP'),(2,'CHOREO'),(3,'COMMERCIAL'),
                                                    (4,'WAACKING'),
                                                  (5,'KPOP'),
                                                  (6,'CONTEMPORAN'),
                                                  (7,'HEELS'),
                                                  (8,'DANCEHALL'),
                                                  (9,'KRUMP');


INSERT INTO clase_dans (nume, stil, dificultate, sala_id, profesor_id)
VALUES ('Commercial - Beginners', 'COMMERCIAL', 'BEGINNER', 2, 3),  -- Sala 2 Cobo, Prof Alex
       ('Hip-Hop Choreography', 'HIPHOP', 'INTERMEDIATE', 1, 1),    -- Sala 1 Cobo, Prof Ana
       ('K-Pop Cover Dance', 'KPOP', 'BEGINNER', 4, 5),             -- Sala 1 Prestige, Prof Ema
       ('Waacking Basics', 'WAACKING', 'BEGINNER', 5, 4),           -- Sala 2 Prestige, Prof Beatrice
       ('Contemporan Flow', 'CONTEMPORAN', 'ADVANCED', 7, 6),       -- Sala 1 Piticu, Prof Andra
       ('Choreo Masterclass', 'CHOREO', 'ADVANCED', 8, 2),          -- Sala 2 Piticu, Prof Ioana
       ('Krump Foundations', 'KRUMP', 'BEGINNER', 10, 9),
       ('Dancehall Vibes', 'DANCEHALL', 'INTERMEDIATE', 11, 8),     -- Urban B, Prof David
       ('Heels Basics', 'HEELS', 'BEGINNER', 12, 7);


INSERT INTO clienti (nume, email, telefon)
VALUES ('Maria Popescu', 'maria@mail.com', '0722111222'),
       ('Alex Ionescu', 'alex@mail.com', '0733111222'),
       ('Elena Dumitru', 'elena@mail.com', '0744111222'),
       ('Andrei Radu', 'andrei@mail.com', '0755111222'),
       ('Diana Stan', 'diana@mail.com', '0766111222'),
       ('Stefan Mihai', 'stefan@mail.com', '0777111222'),
       ('Carmen Albu', 'carmen@mail.com', '0788111222'),
       ('Victor Popa', 'victor@mail.com', '0799111222');

INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (1, 'MONDAY', '18:00', '19:00');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (1, 'WEDNESDAY', '18:00', '19:00');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (2, 'TUESDAY', '19:30', '21:00');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (3, 'MONDAY', '17:30', '19:00');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (4, 'WEDNESDAY', '20:00', '21:30');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (5, 'SATURDAY', '10:00', '12:00');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (6, 'TUESDAY', '18:00', '19:30');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (7, 'MONDAY', '20:00', '21:30');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (8, 'FRIDAY', '19:00', '20:30');
INSERT INTO program_clase (clasa_id, ziua, ora_inceput, ora_sfarsit) VALUES (9, 'WEDNESDAY', '17:00', '18:30');