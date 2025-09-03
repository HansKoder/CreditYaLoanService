CREATE TABLE loans (
    loan_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_type_id INTEGER NOT NULL,
    document VARCHAR(100) NOT NULL,
    month_period INTEGER NOT NULL,
    year_period INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    amount NUMERIC
);

CREATE TABLE loan_types (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL
);

INSERT INTO loan_types (description, interest_rate) VALUES ('Casa', 1);