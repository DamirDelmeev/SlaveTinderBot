CREATE TABLE users
(
    chat_id     bigint NOT NULL UNIQUE,
    name        VARCHAR(30),
    gender      VARCHAR(30),
    preference  VARCHAR(10) DEFAULT 'all',
    description text        DEFAULT 'Описания нет',
    PRIMARY KEY (chat_id)
);


CREATE TABLE watch_lover
(
    id      bigint UNIQUE,
    counter bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users (chat_id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE attitude
(
    id             serial NOT NULL PRIMARY KEY,
    initiator      bigint NOT NULL,
    target         bigint NOT NULL,
    name_of_action VARCHAR(30),
    FOREIGN KEY (initiator) REFERENCES users (chat_id),
    FOREIGN KEY (target) REFERENCES users (chat_id)
);