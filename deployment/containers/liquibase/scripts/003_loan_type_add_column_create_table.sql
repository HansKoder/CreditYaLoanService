--liquibase formatted sql

--changeset dev:credit_loan_service_02092025_2101
ALTER TABLE public.loans
    ADD COLUMN loan_type_id INTEGER NOT NULL;

--changeset dev:credit_loan_service_02092025_2102
CREATE TABLE loan_types (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL
);
