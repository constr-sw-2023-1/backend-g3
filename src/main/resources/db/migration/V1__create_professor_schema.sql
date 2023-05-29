CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create schema if not exists professors;

create table if not exists professors.professor (
    id uuid not null DEFAULT uuid_generate_v4() primary key,
    registration text not null unique,
    name text not null,
    born_date date not null,
    admission_date date not null,
    active boolean not null
);
create index if not exists registration_idx on professors.professor using btree (registration);

create table if not exists professors.certification (
    id uuid not null DEFAULT uuid_generate_v4() primary key,
    "level" text not null,
    "name" text not null,
    institution text not null
);

create table if not exists professors.professors_certifications (
    id uuid not null DEFAULT uuid_generate_v4() primary key,
    certification_id uuid not null,
    professor_id uuid not null,
    "year" date not null default now(),
    "semester" text not null,
    unique(certification_id, professor_id)
);
create index if not exists professors_certifications_idx on professors.professors_certifications using btree (certification_id, professor_id);
create index if not exists professors_certifications_professor_idx on professors.professors_certifications using btree (professor_id);

create table if not exists professors.identification (
    id uuid not null default uuid_generate_v4(),
    professor_id uuid not null,
    type text not null,
    "value" text not null,
    constraint identification_professor_fk foreign key (professor_id)
            references professors.professor (id)
);
create index if not exists identification_professor_id_idx on professors.identification using btree(professor_id);