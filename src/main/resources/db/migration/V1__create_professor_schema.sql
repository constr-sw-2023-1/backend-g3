CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create schema if not exists professors;

create table if not exists professors.professor (
    id uuid not null DEFAULT uuid_generate_v4() primary key,
    registration text not null unique,
    name text not null,
    bornDate date not null,
    admissionDate date not null,
    active boolean not null
);
create index if not exists registration_idx on professors.professor using btree (registration);

create table if not exists professors.certification (
    id uuid not null DEFAULT uuid_generate_v4() primary key,
    "year" date not null default now(),
    "level" text not null,
    description text not null,
    professor_id uuid not null,
    constraint certification_professor_fk foreign key (professor_id)
        references professors.professor (id)
);
create index if not exists certification_professor_id_idx on professors.certification using btree(professor_id);

create table if not exists professors.identification (
    id uuid not null default uuid_generate_v4(),
    professor_id uuid not null,
    type text not null,
    "value" text not null,
    constraint identification_professor_fk foreign key (professor_id)
            references professors.professor (id)
);
create index if not exists identification_professor_id_idx on professors.identification using btree(professor_id);