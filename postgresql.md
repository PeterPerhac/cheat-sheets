psql is the command line client
comes with postgres which can be installed via homebrew

```bash
brew install postgresql
```

connect to server like so:

  psql -h <host> -p <port> --user <user> -d <db> --password


if you want to avoid typing the password every time, you can setup a .pgpass file in your home directory. This file needs to be chmod'ed to 0600 like so
    `chmod 0600 .pgpass`

and the contents is host<colon>port<colon>database<colon>user<colon>password
e.g.:
    `localhost:5432:booklog:booklog:booklog`


psql is exited by `\q` or just by pressing Ctrl+D

there is a bunch of backslash commands
list them all with `\?`

`\l`  list all databases
`\dt` list all tables
    or
    SELECT * FROM pg_catalog.pg_tables;
`\d <table_name>` displays details about a table (like columns and indexes and all)
'\c [dbname | - user]' connect to a database or user


Use this query to find out the number of live tuples in each table:

```sql
SELECT schemaname, relname, n_live_tup FROM pg_stat_user_tables WHERE schemaname='<schema_name>' ORDER BY n_live_tup DESC;
```

