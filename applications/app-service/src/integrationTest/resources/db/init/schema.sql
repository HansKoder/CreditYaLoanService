CREATE TABLE loan_types (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL
);

INSERT INTO loan_types (description, interest_rate) VALUES ('Casa', 1);


CREATE TABLE public.loan_events (
	event_id uuid DEFAULT gen_random_uuid() NOT NULL,
	aggregate_id uuid DEFAULT gen_random_uuid() NULL,
	aggregate_type varchar(50) NOT NULL,
	event_type varchar(100) NOT NULL,
	payload jsonb NOT NULL,
	created_at timestamp DEFAULT now() NOT NULL,
	CONSTRAINT loan_events_pkey PRIMARY KEY (event_id)
);

create table OUTBOX_EVENTS (
    outbox_id uuid DEFAULT gen_random_uuid() NOT NULL,
    aggregate_id VARCHAR(200),
    aggregate_name VARCHAR(100),
    event_type VARCHAR(100),
    outbox_status VARCHAR(20) NOT NULL CHECK (outbox_status IN ('STARTED', 'COMPLETED', 'FAILED')),
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);