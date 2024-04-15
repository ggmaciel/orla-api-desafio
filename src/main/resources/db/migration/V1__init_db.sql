CREATE TABLE employee (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	cpf VARCHAR(11) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	salary NUMERIC(10, 2) NOT NULL
);

CREATE TABLE project (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL UNIQUE,
	date_of_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE employee_project (
    employee_id INT REFERENCES employee(id),
    project_id INT REFERENCES project(id),
    PRIMARY KEY (employee_id, project_id)
);