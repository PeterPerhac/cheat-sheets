psql is the command line client
comes with postgres which can be installed via homebrew

```bash
brew install postgresql
```

psql is exited by `\q`

there is a bunch of backslash commands
list them all with `\?`

`\l`  list all databases
`\dt` list all tables
`\d <table_name>` displays details about a table (like columns and indexes and all)

Use this query to find out the number of live tuples in each table:

```sql
SELECT schemaname, relname, n_live_tup FROM pg_stat_user_tables WHERE schemaname='<schema_name>' ORDER BY n_live_tup DESC;
```



