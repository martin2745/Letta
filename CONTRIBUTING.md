Grupo B de DAA: Fichero contributing

Introducción:
Este es el fichero contributing, en él, se detallan las normas que se deben seguir para el correcto funcionamiento del proyecto.

Desarrollo de una tarea:
1. En Kunagi selecciona la tarea de la que seas responsable que deseas desarrollar y lee bien la descripción de la misma (estas tareas serán repartidas en las reuniones semanales).
2. Abre el entorno de desarrollo.
3. Verifica que te encuentras en la rama develop. Si no es así, cámbiate a esta rama.
4. Haz pull de los últimos cambios y verifica que la rama develop de tu pareja sea idéntica.
5. Implementa la solución.
6. Haz un commit con cada parte estable (completa y sin errores) que desarrolles una vez hayas sincronizado tu rama.
7. Cada vez que hagas un commit envíalo al repositorio central Gitlab para compartirlo con el resto del equipo.
8. Cuando vuestra pareja acabe, es necesario marcar las tareas en Kunagi con las posibles actualizaciones y marcar las horas.

Entorno de desarrollo:
Para poder comenzar a desarrollar el proyecto, es necesario:
Maven 3:
Es un entorno de construcción de proyectos para Java. Esta será una herramienta clave, ya que es quien dirigirá todo el proyecto. Es necesario que tengas instalado Maven 3 en tu equipo de desarrollo para poder construir el proyecto.
Kunagi:
Es una herramienta de gestión de proyectos Scrum. En ella encontrarás toda la información sobre las funcionalidades desarrolladas y por desarrollar, el alcance de las publicaciones, el estado de desarrollo, etc.
Git y Gitlab:
Git es el sistema de control de versiones que se utiliza en el proyecto. Es un sistema de control de versiones distribuido que facilita la colaboración entre desarrolladores. Es necesario que tengas instalado Git en tu sistema para poder realizar cambios en el proyecto y colaborar con el resto del equipo. Por otro lado, Gitlab es un front-end del repositorio Git común. Esta herramienta facilita la visualización de los commits y ficheros del proyecto
MySQL:
Es el sistema gestor de base de datos (SGDB) que utilizará el sistema definitivo. En la explicación de cómo ejecutar el sistema en local utilizaremos este SGBD, por lo que deberás tenerlo instalado en tu equipo






Configuración de un entorno de desarrollo:
1. Instala Git y Maven. Si estás en un entorno Ubuntu es tan sencillo como ejecutar sudo apt-get install git maven. También es recomendable que instales algún visor de Git como gitk o qgit.
   2.Clona el repositorio Git utilizando el comando: git clone http://sing-group.org/dt/gitlab/dgss-2021- team0/microstories.git
3. Instala Eclipse for Java EE (opcional pero recomendado):
4. Descarga el IDE desde https://www.eclipse.org/downloads/eclipse-packages/
5. Importa el proyecto en Eclipse utilizando Import...->Existing Maven projects, selecciona el directorio del proyecto en Root directory y marca todos los proyectos que aparezcan.
6. Instalación de MySQL: existen dos formas de instalar MySQL, con apt (disponible en 	Ubuntu) o con Docker, a continuación se explica como instalarlo con apt:
   La instalación con apt es tan sencilla como hacer:
   sudo apt install mysql
   Cuando ejecutes esto se mostrará información sobre la versión concreta de MySQL que se instalará y podrás decidir si contiuar o no.
   Puedes consultar otras alternativas ejecutando:
   sudo apt-cache search mysql-server
   Este comando te mostrará una lista de paquetes con mysql-server en su nombre, con lo que podrás saber qué versiones están disponibles. Una vez instalado MySQL debes importar la base de datos.
   Control de versiones (Git):
   Se utilizarán, en principio, dos ramas en el modelo de control de versiones:
   Master: a esta rama solo se enviarán los commits cuando se llegue a una versión estable y publicable (una release). Estas versiones deberán estar etiquetadas con el número de versión correspondiente.
   Develop: esta será la rama principal de trabajo. Los commits que se envíen deben ser estables, por lo que debe funcionar de forma correcta la aplicación en local.

Guía de estilos:
Un elemento importante para poder colaborar es que exista una uniformidad en el código y otros elementos que forman parte del desarrollo. Esta sección sirve como una pequeña guía de estilo que debe respetarse al trabajar en el proyecto.

Código fuente:
Para uniformizar el código fuente deben respetarse las siguientes normas:
-Idioma: todo el código debe desarrollarse en inglés.
-Formato de código: el código debe estar formateado, preferiblemente, siguiendo la Guía de Estilo para Java de Google o, al menos, utilizando el formato de código de Eclipse (Ctrl+Mayus+F).
-Comentarios: debe evitarse completamente el código comentado y, en la medida de lo posible, los comentarios en el código.
-Documentación: las clases pueden incluir documentación que describa las responsabilidades de la misma. No es obligatorio documentar los métodos.

Control de versiones:
Una de las bases de desarrollo que utilizaremos en este proyecto es el integrar tan pronto como se pueda. Para ello, deben seguirse las siguientes normas:
-Contenido de los commits: los commits deben ser completos en el sentido de que no deben romper la construcción. Además, el código debe estar probado, para que el resto de desarrolladores puedan confiar en el código.
-Formato: el formato de los commits deberá respetar las siguientes normas:
Escritos en inglés.
Limitar el tamaño de línea a 80 columnas. Si se utiliza Eclipse, esto se hace de forma automática. 	
Cuerpo del commit descriptivo.
-Frecuencia de commit: los commits deben hacerse en pequeños pasos para que la frecuencia sea alta. Para ello es recomendable desarrollar de una forma ordenada, atacando partes concretas. Se espera que cada desarrollador genere, al menos, 2-3 commits cada semana. Además, deberán estar distribuidos a lo largo de toda la semana, evitando, especialmente, realizar todos los commits al final de la semana, pues esto afectaría a la integración continua.
-Frecuencia de push: siempre que se haga un commit debe hacerse un push. La única excepción a esta regla es que estemos haciendo pruebas locales para evaluar una posible solución. En tal caso, es recomendable que esto se haga en una rama independiente para evitar enviar commits accidentalmente a la rama develop remota




