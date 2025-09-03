--liquibase formatted sql

--changeset dev:credit_loan_service_02092025_2103
INSERT INTO loan_types (description, interest_rate) VALUES 
('PERSONAL', 18),
('MORTAGE', 10),
('AUTO', 12);