create table match_outcome
(
    id integer not null,
    match_id varchar(255) not null,
    market_id varchar(255) not null,
    outcome_id varchar(255) not null,
    specifiers varchar(255) not null,
    date_inserted timestamp not null default now(),
    primary key(id)
);