# Accounts Service

## Descripcion

Microservicio encargado de gestionar las cuentas y usuarios del sistema de monopatines electricos. Permite crear, editar y consultar cuentas y usuarios, asi como gestionar la relacion many-to-many entre cuentas y usuarios. Tambien maneja la funcionalidad de anular/reactivar cuentas cuando sea necesario. E


## Dependencias

### spring-boot-starter-web
**Para que sirve:** Framework web de Spring Boot. Permite crear endpoints REST, manejar requests HTTP, y toda la funcionalidad web del servicio.

### spring-boot-starter-data-jpa
**Para que sirve:** Integracion con JPA/Hibernate. Permite trabajar con entidades, repositorios, y mapear objetos Java a tablas de base de datos. Hibernate crea las tablas automaticamente con `ddl-auto=update`.

### mariadb-java-client
**Para que sirve:** Driver JDBC para conectarse a la base de datos MariaDB. Permite que Spring Boot se comunique con la base de datos.

### spring-boot-starter-validation
**Para que sirve:** Validacion de datos de entrada. Permite validar requests (email valido, campos requeridos, etc.) usando anotaciones como `@Email`, `@NotNull`, `@NotBlank`.

### spring-boot-starter-actuator
**Para que sirve:** Health checks y metricas del servicio. Expone endpoints como `/actuator/health` para verificar que el servicio esta funcionando correctamente.

### springdoc-openapi-starter-webmvc-ui
**Para que sirve:** Documentacion automatica de la API con Swagger/OpenAPI. Expone la documentacion interactiva de los endpoints en `/swagger-ui/index.html` y el esquema OpenAPI en `/v3/api-docs`.

### spring-boot-starter-test
**Para que sirve:** Testing. Permite escribir y ejecutar tests unitarios e integracion del servicio.


## Endpoints

### Cuentas (Accounts)

#### POST /api/accounts
**Descripcion:** Crea una nueva cuenta asociada a una cuenta de Mercado Pago.
- **Body:** `AccountRequest` (numero identificatorio, ID de Mercado Pago, saldo inicial)
- **Respuesta:** `AccountResponse` con la cuenta creada (HTTP 201)
- **Errores:** HTTP 409 si el numero identificatorio ya existe

#### GET /api/accounts
**Descripcion:** Obtiene todas las cuentas del sistema.
- **Respuesta:** Lista de `AccountResponse` (HTTP 200)

#### GET /api/accounts/{id}
**Descripcion:** Obtiene una cuenta por su ID.
- **Path Variable:** `id` - ID de la cuenta
- **Respuesta:** `AccountResponse` con los datos de la cuenta (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta

#### GET /api/accounts/active
**Descripcion:** Obtiene todas las cuentas activas (no anuladas).
- **Respuesta:** Lista de `AccountResponse` con solo las cuentas activas (HTTP 200)

#### PUT /api/accounts/{id}
**Descripcion:** Actualiza los datos de una cuenta existente.
- **Path Variable:** `id` - ID de la cuenta a actualizar
- **Body:** `AccountRequest` con los datos actualizados
- **Respuesta:** `AccountResponse` con la cuenta actualizada (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta, HTTP 409 si el nuevo numero identificatorio ya existe

#### PUT /api/accounts/{id}/toggle_status
**Descripcion:** Anula o reactiva una cuenta dinámicamente. Si la cuenta está activa, la anula. Si está anulada, la reactiva.
- **Path Variable:** `id` - ID de la cuenta a anular/reactivar
- **Respuesta:** `AccountResponse` con la cuenta actualizada (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta

#### DELETE /api/accounts/{id}
**Descripcion:** Elimina una cuenta del sistema.
- **Path Variable:** `id` - ID de la cuenta a eliminar
- **Respuesta:** Sin contenido (HTTP 204)
- **Errores:** HTTP 404 si no se encuentra la cuenta

### Operaciones de Saldo (Balance)

#### GET /api/accounts/{id}/balance
**Descripcion:** Obtiene el saldo actual de una cuenta.
- **Path Variable:** `id` - ID de la cuenta
- **Respuesta:** `BalanceResponse` con el saldo actual (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta

#### PUT /api/accounts/{id}/balance
**Descripcion:** Carga saldo a una cuenta. Incrementa el saldo actual con el monto especificado.
- **Path Variable:** `id` - ID de la cuenta
- **Body:** `BalanceRequest` con el monto a cargar (debe ser positivo)
- **Respuesta:** `BalanceResponse` con el saldo actualizado (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta, HTTP 400 si la cuenta esta anulada

#### PUT /api/accounts/{id}/balance/deduct?amount={amount}
**Descripcion:** Descuenta saldo de una cuenta (usado por otros microservicios). Se utiliza cuando se activa un monopatin o se finaliza un viaje.
- **Path Variable:** `id` - ID de la cuenta
- **Query Parameter:** `amount` - Monto a descontar
- **Respuesta:** `BalanceResponse` con el saldo actualizado (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta, HTTP 400 si la cuenta esta anulada o no hay saldo suficiente

#### GET /api/accounts/{id}/active
**Descripcion:** Verifica si una cuenta esta activa (para otros microservicios).
- **Path Variable:** `id` - ID de la cuenta
- **Respuesta:** `AccountStatusResponse` con `accountId` y `status` (true/false) (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra la cuenta

### Usuarios (Users)

#### POST /api/accounts/users
**Descripcion:** Crea un nuevo usuario del sistema.
- **Body:** `CreateUserRequest` (nombre, apellido, email, telefono, password)
- **Respuesta:** `UserResponse` con el usuario creado, incluyendo roles asignados (HTTP 201)
- **Nota:** Al crear un usuario, se le asigna automaticamente el rol `ROLE_USER`. El password se almacena tal como se envía (en la primera parte no hay validación de autenticación).
- **Errores:** HTTP 409 si el email ya existe (el email debe ser unico)

#### GET /api/accounts/users/all
**Descripcion:** Obtiene todos los usuarios del sistema.
- **Respuesta:** Lista de `UserResponse`, cada uno incluyendo roles asignados (HTTP 200)

#### GET /api/accounts/users/{id}
**Descripcion:** Obtiene un usuario por su ID.
- **Path Variable:** `id` - ID del usuario
- **Respuesta:** `UserResponse` con los datos del usuario, incluyendo roles asignados (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra el usuario

#### GET /api/accounts/users?email={email}
**Descripcion:** Obtiene un usuario por su email. El email es unico en el sistema.
- **Query Parameter:** `email` - Email del usuario
- **Respuesta:** `UserResponse` con los datos del usuario, incluyendo roles asignados (HTTP 200)
- **Errores:** HTTP 404 si no se encuentra el usuario con ese email

#### PUT /api/accounts/users/{id}
**Descripcion:** Actualiza los datos de un usuario existente.
- **Path Variable:** `id` - ID del usuario a actualizar
- **Body:** `UpdateUserRequest` con los datos actualizados (nombre, apellido, email, telefono - sin password)
- **Respuesta:** `UserResponse` con el usuario actualizado, incluyendo roles asignados (HTTP 200)
- **Nota:** Este endpoint no actualiza el password.
- **Errores:** HTTP 404 si no se encuentra el usuario, HTTP 409 si el nuevo email ya existe

### Relaciones Cuenta-Usuario (Account-User)

#### POST /api/accounts/associate
**Descripcion:** Asocia un usuario a una cuenta. Crea una relacion many-to-many que permite que el usuario utilice los creditos cargados en esa cuenta.
- **Body:** `AccountUserRequest` con `accountId` y `userId`
- **Respuesta:** `AccountUserResponse` con informacion sobre la asociacion creada (HTTP 201)
  - Incluye: `accountId`, `userId`, `associatedAt` (fecha de asociacion), `message`
- **Errores:** HTTP 404 si no se encuentra la cuenta o el usuario, HTTP 500 si ya existe la asociacion

#### POST /api/accounts/disassociate
**Descripcion:** Desasocia un usuario de una cuenta. Elimina la relacion many-to-many, impidiendo que el usuario utilice los creditos de esa cuenta.
- **Body:** `AccountUserRequest` con `accountId` y `userId`
- **Respuesta:** `AccountUserResponse` con informacion sobre la desasociacion realizada (HTTP 200)
  - Incluye: `accountId`, `userId`, `associatedAt` (fecha original de asociacion), `message`
- **Errores:** HTTP 404 si no se encuentra la cuenta o el usuario, HTTP 500 si no existe la asociacion

#### GET /api/accounts/{accountId}/users
**Descripcion:** Obtiene todos los usuarios asociados a una cuenta. Retorna la lista de usuarios que pueden utilizar los creditos cargados en esa cuenta, junto con un mensaje informativo.
- **Path Variable:** `accountId` - ID de la cuenta
- **Respuesta:** `UsersByAccountResponse` con informacion sobre los usuarios asociados (HTTP 200)
  - Incluye: `accountId`, `users` (lista de usuarios con sus roles), `count` (cantidad de usuarios), `message` (mensaje informativo)
  - Si no hay usuarios: `users` sera una lista vacia y `message` indicara "La cuenta no tiene usuarios asociados"
- **Errores:** HTTP 404 si no se encuentra la cuenta

#### GET /api/accounts/users/{userId}/accounts
**Descripcion:** Obtiene todas las cuentas asociadas a un usuario. Retorna la lista de cuentas cuyos creditos puede utilizar el usuario, junto con un mensaje informativo. Un usuario puede estar asociado a multiples cuentas.
- **Path Variable:** `userId` - ID del usuario
- **Respuesta:** `AccountsByUserResponse` con informacion sobre las cuentas asociadas (HTTP 200)
  - Incluye: `userId`, `accounts` (lista de cuentas), `count` (cantidad de cuentas), `message` (mensaje informativo)
  - Si no hay cuentas: `accounts` sera una lista vacia y `message` indicara "El usuario no tiene cuentas asociadas"
- **Errores:** HTTP 404 si no se encuentra el usuario




