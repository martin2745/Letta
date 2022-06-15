LETTA
==========

Aplicación y arquitectura de ejemplo para la asignatura Desarrollo Ágil de
Aplicaciones del Grado en Ingeniería Informática de la Escuela Superior de
Ingeniería Informática de la Universidad de Vigo.

## Dependencias
Este proyecto está diseñado para ser desarrollado en un entorno con:

* Maven 3
* Java 8
* MySQL 5.7.6+ o 8+

Además, se recomienda emplear la última versión de Eclipse IDE for Enterprise
Java Developers.

## Ejecución con Maven
La configuración de Maven ha sido preparada para permitir varios tipos de
ejecución.

### Ejecución de la aplicación con Tomcat y MySQL

El proyecto está configurado para poder ejecutar la aplicación sin tener que
realizar ninguna configuración adicional salvo tener disponible un servidor
MySQL en local.

Los ficheros del proyecto `db/mysql.sql` y 'db/mysql-with-inserts.sql' contienen
todas las consultas necesarias para crear la base de datos y el usuario
requeridos, con o sin datos de ejemplo, respectivamente. Por lo tanto, podemos
configurar inicialmente la base de datos con cualquiera de los siguientes
comandos (desde la raíz el proyecto):

* Sin datos: `mysql -u root -p < db/mysql.sql`
* Con datos: `mysql -u root -p < db/mysql-with-inserts.sql`

Una vez configurada la base de datos podemos lanzar la ejecución con el comando:

`mvn -Prun -DskipTests=true package cargo:run`

La aplicación se servirá en la URL local: http://localhost:9089/LETTA

Para detener la ejecución podemos utilizar `Ctrl+C`.

### Ejecución de la aplicación con Tomcat y MySQL con redespliegue automático

Durante el desarrollo es interesante que la apliación se redespliegue de forma
automática cada vez que se hace un cambio. Para ello podemos utilizar el
siguiente comand:

`mvn -Prun -DskipTests=true package cargo:start fizzed-watcher:run`

La aplicación se servirá en la URL local: http://localhost:9080/LETTA

Para detener la ejecución podemos utilizar `Ctrl+C`.


