--liquibase formatted sql

--changeset dev:credit_loan_service_02092025_2100
ALTER TABLE public.loans
    ALTER COLUMN loan_id SET DEFAULT gen_random_uuid();