# ContactosEnServidor

Gestor de contactos para android usando multi-hilos, peticiones http y SQLite.

## Funcionamiento

La app descarga la lista de contactos desde un servidor web mediante un fichero json, declarados en el archivo PHP que estará en el directorio htdocs del servidor apache, 
estos cotactos se grabarán en la BBDD SQLite, desde la interfaz podremos realizar las operaciones CRUD a los contactos.
La aplicación dispone de 3 Activities, Main para la lista desplegable (spinner) de contactos, Secondary para ver toda la información del contacto y modificarlo, y Tertiary para crear nuevos contactos.
