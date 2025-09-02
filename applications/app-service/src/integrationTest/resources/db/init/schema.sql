CREATE TABLE loans (
    loan_id UUID PRIMARY KEY,
    loan_type_id INTEGER NOT NULL,
    document VARCHAR(100) NOT NULL,
    month_period INTEGER NOT NULL,
    year_period INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    amount NUMERIC
);