create schema airports;
GRANT ALL PRIVILEGES ON *.* TO 'gingersnap_user';
create table airports.Aircraft (
                          id bigint not null,
                          iataMain varchar(255),
                          iataSub varchar(255),
                          longDescription varchar(255),
                          shortDescription varchar(255),
                          primary key (id)
);

create table airports.Airline (
                         id bigint not null,
                         iata varchar(255),
                         icao varchar(255),
                         nvls varchar(255),
                         publicName varchar(255),
                         primary key (id)
);

create table airports.Airport (
                         id bigint not null,
                         city varchar(255),
                         elevation_ft double precision not null,
                         iata varchar(255),
                         latitude_deg double precision not null,
                         longitude_deg double precision not null,
                         name varchar(255),
                         country_id bigint,
                         primary key (id)
);

create table airports.Country (
                         id bigint not null,
                         continent varchar(255),
                         isoCode varchar(255),
                         name varchar(255),
                         primary key (id)
);

create table airports.Flight (
                        id bigint not null,
                        code varchar(255),
                        dayOfWeek integer not null,
                        direction varchar(255),
                        name varchar(255),
                        scheduleTime varchar(255),
                        state varchar(255),
                        terminal integer not null,
                        aircraft_id bigint,
                        airline_id bigint,
                        destination_id bigint,
                        primary key (id)
);

create table airports.hibernate_sequence (
    next_val bigint
);

insert into airports.hibernate_sequence values ( 1 );

alter table airports.Airport
    add constraint FKj04qjklwe8vkj8sfsehbcmjr2
        foreign key (country_id)
            references airports.Country (id);

alter table airports.Flight
    add constraint FK9r3jdupj6x0bqgb5fnk7dr5cn
        foreign key (aircraft_id)
            references airports.Aircraft (id);

alter table airports.Flight
    add constraint FKiovu1yeejovoyfdigekqm2poq
        foreign key (airline_id)
            references airports.Airline (id);

alter table airports.Flight
    add constraint FK5dis6my7uigtlf3yplj9a8tf2
        foreign key (destination_id)
            references airports.Airport (id);