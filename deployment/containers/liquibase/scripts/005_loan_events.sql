--liquibase formatted sql

--changeset dev:credit_loan_service_08092025_2103
CREATE TABLE loan_events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- la DB genera la PK
    aggregate_id UUID DEFAULT gen_random_uuid(),         -- solo se genera la 1ª vez o si no lo mandas
    aggregate_type VARCHAR(50) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);