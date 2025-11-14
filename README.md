# TP4 Arquitectura - Primera Parte

### Integrantes 

Grupo 3: Juan Encabo & Matias Bidinos. Microservicio asignado: Usuarios y Roles.

### Configuracion 
El applicacion properties "TP4_Arquitectura_1Parte\accounts-services\src\main\resources\application.properties"
ya se encuentra configurado para la siguiente base de datos y usuario.

### Creacion de la Base de Datos y Usuario

```
CREATE DATABASE accounts_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'accounts_user'@'localhost' IDENTIFIED BY 'accounts_pass';

GRANT ALL PRIVILEGES ON accounts_db.* TO 'accounts_user'@'localhost';

FLUSH PRIVILEGES;
```

Ademas en el application.properties hay una opcion para activar o desactivar un seeder y poblar las tablas del proyecto

```app.seed.enabled=false```

### Roles

El proyecto cuenta con 3 roles`ROLE_USER`, `ROLE_EMPLOYEE` y `ROLE_ADMIN` pero como en la primera parte no hay que usar JWT, ni autenticacion ni autorizacion (Para generar el token) simplemente especifican el rol del usuario. No validan nada. Ademas los roles se crean automaticamente si no existen al correr la aplicacion.


### Urls

- **API Gateway**: http://localhost:8080
- **Accounts Service**: http://localhost:8081


### Swagger 

- **A través del API Gateway**: http://localhost:8080/swagger-ui/index.html
- **Directamente al servicio**: http://localhost:8081/swagger-ui/index.html

### Postman 

Coleccion completa de endpoints para pruebas:
https://tp3777.postman.co/workspace/TP4_Arquitectura_1Parte~d658834b-273a-4759-b660-4c8abd34770f/collection/18011585-99695c1b-f5d7-4975-acff-f6970e8b341a?action=share&creator=18011585

### Archivos Readme

 [README de Accounts Service](accounts-services/README.md).

### Diagrama de clase de entidades

<div align="center">
  <img src="screenshots/screenshot_diagrama_clases.png" alt="Diagrama de Clases" />
</div>

