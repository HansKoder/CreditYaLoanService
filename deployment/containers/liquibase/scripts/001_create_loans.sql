--liquibase formatted sql

--changeset dev:credit_loan_service_30082025_2100
CREATE TABLE loans (
    loan_id UUID PRIMARY KEY,
    document VARCHAR(100) NOT NULL,
    month_period INTEGER NOT NULL,
    year_period INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    amount NUMERIC
);