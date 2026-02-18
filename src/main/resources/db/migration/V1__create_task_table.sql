CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    completed BOOLEAN DEFAULT FALSE
);

INSERT INTO tasks (title, description, completed) 
VALUES 
('Finish homework', 'Complete the math assignment', FALSE),
('Clean the house', 'Vacuum and mop the floors', FALSE);