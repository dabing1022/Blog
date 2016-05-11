# Download

[http://dev.mysql.com/downloads/mysql/](http://dev.mysql.com/downloads/mysql/)

# Default Password Info

2016-01-03T00:54:41.596610Z 1 [Note] A temporary password is generated for `root@localhost: rGED5izeCr-p`

If you lose this password, please consult the section How to Reset the Root Password in the MySQL reference manual.

# How to reset password?

```c
Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> user mysql;
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'user mysql' at line 1
mysql> use mysql;
ERROR 1820 (HY000): You must SET PASSWORD before executing this statement
mysql> UPDATE user SET password=PASSWORD('123456') WHERE user='root';
ERROR 1046 (3D000): No database selected
mysql> use mysql;
ERROR 1820 (HY000): You must SET PASSWORD before executing this statement
mysql> SET PASSWORD = PASSWORD('123456');
Query OK, 0 rows affected (0.10 sec)

mysql> use mysql;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql>
```
# Run MySQL

`System Perferences` -->`MySQL`-->`Run`

# Create db

```shell
> mysql -u root -p
> Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 477
Server version: 5.7.10 MySQL Community Server (GPL)

Copyright (c) 2000, 2015, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> create database myTestSQL_db;
Query OK, 1 row affected (0.00 sec)

mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| myTestSQL_db       |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.00 sec)
```

# PyMySQL

- `pip install PyMySQL`
- `mysql+pymysql://root:123456@localhost/myTestSQL_db`
