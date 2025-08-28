CREATE TABLE loans (
    loan_id UUID PRIMARY KEY,
    document VARCHAR(100) NOT NULL,
    period date NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'))
    amount NUMERIC
);