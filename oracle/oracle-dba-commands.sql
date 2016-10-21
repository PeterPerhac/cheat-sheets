SQLPlus commands
================

    sqlplus / as sysdba
    startup
    spool /some/path/to/file [APPEND]
    conn user/pwd


Administering Oracle
====================
create user some_user identified by some_password;
grant connect to some_user;
grant dba to some_user with admin option;
create tablespace data_01 datafile '/path/to/data/file' size 200M reuse autoextend on NEXT 100M maxsize 1000M;
create or replace directory somedir as '/path/to/some/directory';

