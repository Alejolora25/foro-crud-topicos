# foro-crud-topicos

# ForoHub API

CRUD de **tópicos** con **autenticación JWT**, **registro de usuarios**, paginación/filtros y **documentación OpenAPI (Swagger UI)**. Proyecto construido con **Spring Boot 3**, **Spring Security**, **Spring Data JPA**, **Flyway** y **MySQL** siguiendo una arquitectura por capas (domain → application → infrastructure → presentation).

---

## ✨ Funcionalidades

- Autenticación vía **JWT Bearer** (`/login`).
- Registro de usuarios (**público**) (`POST /usuarios`).
- Gestión de **tópicos**: crear, listar (paginado + filtros), ver detalle, actualizar y eliminar (`/topicos`).
- Validaciones con **Jakarta Validation** y reglas de negocio (anti-duplicados *título+mensaje*).
- Manejo uniforme de errores (400/401/403/404/409).
- **Swagger UI** para explorar y probar la API.

---

## 🧰 Stack técnico

- Java 17, Maven
- Spring Boot 3 (Web, Validation)
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- Flyway (migraciones)
- MySQL 8
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, **@MockitoBean** (tests de controlador con MockMvc)

---

## 🗂️ Arquitectura (resumen)

```
com.alura.foro
 ├─ domain/
 │   └─ topico/               # modelo de dominio y contrato del repositorio
 ├─ application/
 │   └─ topico/               # casos de uso (servicios de aplicación)
 │   └─ usuario/
 ├─ infrastructure/
 │   ├─ persistence/topico/   # entidades JPA, repos Spring Data y adapter de repositorio
 │   ├─ persistence/usuario/
 │   ├─ security/             # TokenService, filtro JWT, SecurityConfigurations
 │   ├─ docs/                 # configuración OpenAPI
 │   └─ exception/            # excepciones y GlobalExceptionHandler
 └─ presentation/
     └─ rest/                 # controllers y DTOs
```

**Reglas clave:**
- Anti-duplicados de tópico por (`titulo` + `mensaje`).
- Email de usuario **único**.
- Campos obligatorios en DTOs y commands con `@NotBlank`, `@NotNull`, etc.

---

## ✅ Requisitos

- Java 17+
- Maven 3.9+
- MySQL 8+

---

## ⚙️ Configuración

Edita `src/main/resources/application.properties` según tu entorno:

```properties
spring.application.name=foro-crud-topicos

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/forohub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Seguridad / JWT
api.security.token.secret=${JWT_SECRET:super-secreto-muy-largo}
api.security.token.issuer=forohub
api.security.token.expiration=PT2H

# Swagger (springdoc) URL:
#  - Swagger UI: /swagger-ui.html
#  - OpenAPI JSON: /v3/api-docs
```

> Sugerencia: exporta la variable `JWT_SECRET` en tu sistema para **no** usar el valor por defecto.

---

## 🗃️ Base de datos y migraciones

- Crea el schema vacío: `CREATE DATABASE forohub;`
- Las migraciones **Flyway** viven en `src/main/resources/db/migration` y se ejecutan al arrancar la app.
  - `V1__crear_tabla_topicos.sql`
  - `V2__crear_tabla_usuarios.sql` *(si aplica en tu repo)*

> Si agregas o cambias migraciones, **detén la app** antes de correrla nuevamente.

---

## ▶️ Ejecutar

```bash
# Windows
mvnw spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

La app levanta en `http://localhost:8080`.

---

## 🔐 Autenticación (flujo)

1. **Registrar usuario** (público): `POST /usuarios`
2. **Login** (público): `POST /login` → devuelve `accessToken` (JWT)
3. Enviar `Authorization: Bearer <token>` en las peticiones protegidas.

**Rutas públicas:**
- `POST /usuarios`
- `POST /login`
- `/swagger-ui/**`, `/v3/api-docs/**`

**Resto de rutas:** requieren **Bearer token** válido.

---

## 📘 Swagger (OpenAPI)

- UI: `http://localhost:8080/swagger-ui.html`
- Docs JSON: `http://localhost:8080/v3/api-docs`

> En Swagger UI, presiona **Authorize** e ingresa `Bearer <tu_token>` (incluyendo la palabra *Bearer*).

---

## 🧪 Tests

- **Controller tests** con **MockMvc** y `@WebMvcTest`, usando **`@MockitoBean`** para mockear dependencias (reemplazo moderno de `@MockBean` en Spring 3.4+).
- **Unit tests** de casos de uso con JUnit + Mockito (`@ExtendWith(MockitoExtension.class)` / `@Mock` / `@InjectMocks`).

Ejecutar:
```bash
mvn -q test
```

---

## 🔗 Endpoints principales

### 1) Usuarios

#### POST `/usuarios`  *(público)*
Crea un usuario con contraseña encriptada (BCrypt) y rol `USER`.

**Request**
```json
{
  "nombre": "Juan Perez",
  "email": "juan@forohub.com",
  "clave": "secreto123"
}
```

**Response 201**
```json
{
  "id": 1,
  "nombre": "Juan Perez",
  "email": "juan@forohub.com",
  "rol": "USER",
  "activo": true
}
```

**Reglas de validación**
- `email` válido y único
- `clave` mínimo 6 caracteres

---

### 2) Autenticación

#### POST `/login`  *(público)*
**Request**
```json
{
  "email": "juan@forohub.com",
  "clave": "secreto123"
}
```
**Response 200**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 7200
}
```

> El **payload** del JWT incluye `sub` (email), `rol`, `iss` (issuer) y `exp` (expiración).

---

### 3) Tópicos  *(protegido → requiere Bearer token)*

#### POST `/topicos`
Crea un tópico (regla anti-duplicados por *título+mensaje*).

**Request**
```json
{
  "titulo": "Mi primer topico",
  "mensaje": "Contenido del topico",
  "autor": "jesus.lora",
  "curso": "Spring Boot 3"
}
```
**Response 201**
```json
{
  "id": 1,
  "titulo": "Mi primer topico",
  "mensaje": "Contenido del topico",
  "fechaCreacion": "2025-08-17T13:12:30",
  "estado": "ABIERTO",
  "autor": "jesus.lora",
  "curso": "Spring Boot 3"
}
```

---

#### GET `/topicos`
Listado paginado/filtrado.

**Query params opcionales**
- `curso` (búsqueda contiene, case-insensitive)
- `anio` (filtra por año de `fechaCreacion`)
- `page`, `size`, `sort` (Spring Data): por defecto `size=10`, `sort=fechaCreacion,asc`

**Ejemplos**
```
GET /topicos
GET /topicos?curso=Spring Boot 3
GET /topicos?anio=2025
GET /topicos?curso=Spring&anio=2025&page=0&size=5&sort=fechaCreacion,asc
```

**Response 200**
Objeto `Page` estándar de Spring (con `content`, `totalPages`, `totalElements`, etc.).

---

#### GET `/topicos/{id}`
Devuelve el detalle de un tópico.

**Response 200**
```json
{
  "id": 1,
  "titulo": "...",
  "mensaje": "...",
  "fechaCreacion": "2025-08-17T13:12:30",
  "estado": "ABIERTO",
  "autor": "jesus.lora",
  "curso": "Spring Boot 3"
}
```

---

#### PUT `/topicos/{id}`
Actualiza campos obligatorios (con regla anti-duplicados).

**Request**
```json
{
  "titulo": "Nuevo titulo",
  "mensaje": "Nuevo mensaje",
  "autor": "jesus.lora",
  "curso": "Spring Boot 3"
}
```

**Response 200** – Tópico actualizado.

---

#### DELETE `/topicos/{id}`
Elimina un tópico existente.

**Response 204** (No Content)

---

## 🧰 Ejemplos cURL

```bash
# 1) Crear usuario (público)
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","email":"juan@forohub.com","clave":"secreto123"}'

# 2) Login (público) → obtener token
TOKEN=$(curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"juan@forohub.com","clave":"secreto123"}' | jq -r .accessToken)

# 3) Crear tópico (protegido)
curl -X POST http://localhost:8080/topicos \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"titulo":"Hola","mensaje":"Primer post","autor":"juan","curso":"Spring"}'

# 4) Listar (paginado/filtrado)
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/topicos?curso=Spring&anio=2025&page=0&size=5&sort=fechaCreacion,asc"
```

---

## 🚦 Manejo de errores (modelo)

Ejemplos típicos:

**400 Bad Request** (validación)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "El campo 'titulo' es obligatorio",
  "timestamp": "2025-08-17T13:20:00-05:00"
}
```

**404 Not Found**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Tópico no encontrado",
  "timestamp": "2025-08-17T13:20:00-05:00"
}
```

**409 Conflict** (duplicado)
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un tópico con ese título y mensaje",
  "timestamp": "2025-08-17T13:20:00-05:00"
}
```

**401/403** (autenticación / autorización): sin token o token inválido/expirado.

---

## 🔒 Seguridad (detalle)

- `SecurityConfigurations` define **stateless** + filtro `JwtAuthenticationFilter` (valida token y pone `Authentication` en el contexto).
- **Rutas públicas**: `/login`, `POST /usuarios`, `/swagger-ui/**`, `/v3/api-docs/**`.
- **Resto**: requieren JWT Bearer.
- El token se firma con HMAC (secret configurable por `api.security.token.secret` / `JWT_SECRET`).

---

## 🧩 Notas y decisiones de diseño

- **TopicoRepository** expone métodos orientados a reglas del dominio (anti-duplicados, búsquedas flexibles).
- DTOs separados por capa de presentación (request/response).
- Se evita `hbm2ddl` en producción; **Flyway** versiona el esquema.
- **Tests** se concentran en:
  - `UsuariosController` (creación) con MockMvc + `@MockitoBean`.
  - `TopicoController` (actualización) con MockMvc + `@MockitoBean`.
  - `ActualizarTopicoUseCase` (unitario) con Mockito puro.

---

## 📄 Licencia

Uso educativo. Ajusta la licencia según tus necesidades (MIT/Apache-2.0, etc.).
