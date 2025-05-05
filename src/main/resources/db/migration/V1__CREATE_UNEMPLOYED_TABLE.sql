create table if not exists unemployed(
    id uuid primary key default gen_random_uuid(),
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    email varchar(80) not null,
    password varchar(80) not null,
    employmentStatus varchar(80) default 'unemployed'
)