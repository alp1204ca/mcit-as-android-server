--data.sql cannot be empty...
select 1 from dual;

--RUN ONCE (need to configure Liquibase)

--insert into as_role (id, code, description) values (SEQ_AS_ROLE.nextval, 'ROLE_ADMIN', 'System Administrator');
--insert into as_role (id, code, description) values (SEQ_AS_ROLE.nextval, 'ROLE_PROFESSOR', 'Professor');
--insert into as_role (id, code, description) values (SEQ_AS_ROLE.nextval, 'ROLE_STUDENT', 'Student');
--insert into as_user (id, user_name, password) values (SEQ_AS_USER.nextval, 'admin', '$2a$10$ujWn4SB8b0Hhq1aK9Awd8u8qg.vq9032DwKw4uKjXTuetpjPooPhC');
--insert into as_user_role (user_id, role_code) values (1, 'ROLE_ADMIN');