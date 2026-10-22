# 📚 Biblioteca API — Spring Boot 4 & Spring Security 7

<div align="center">

![Java](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-7.1-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Stateless-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![JUnit 5](https://img.shields.io/badge/Tests-16%20Passed-25A162?style=for-the-badge&logo=junit5&logoColor=white)

**API RESTful empresarial para la gestión integral de préstamos, clientes y libros, construida con arquitectura limpia, seguridad moderna basada en roles y validaciones estrictas de negocio.**

Desarrollado por **[Juan Diego Merchán](https://juandiegodev.vercel.app/)** · *Backend Software Developer*

[🌐 Portafolio Web](https://juandiegodev.vercel.app/) • [💼 LinkedIn](https://www.linkedin.com/) • [📬 Contacto](https://juandiegodev.vercel.app/#contacto)

</div>

---

## 📌 Descripción del Proyecto

**Biblioteca API** es un servicio backend robusto diseñado bajo las mejores prácticas del ecosistema Java contemporáneo. Implementa el desacople total de capas (DTOs con Java Records, Servicios transaccionales, Mapeos sin fuga de entidades y Repositorios JPA), blindando el acceso mediante tokens JWT firmados digitalmente sin persistencia de sesiones en el servidor (**100% Stateless**).

El núcleo del negocio cuenta con una suite completa de **pruebas unitarias** con **JUnit 5 y Mockito**, garantizando la integridad de las reglas críticas (como control de deudas, disponibilidad de copias y prevención de duplicados).

---

## 🚀 Arquitectura y Tecnologías

```
src/main/java/com/juandidev/biblioteca_api/
├── controller/         # Endpoints REST (Auth, Libros, Clientes, Préstamos)
├── dto/                # Records inmutables con validación (@Valid, Bean Validation)
├── enums/              # Rol (ADMIN, CLIENTE) y tipos de dominio
├── model/              # Entidades JPA (Usuario, Libro, Cliente, Prestamo)
├── repository/         # Interfaces Spring Data JPA con derived queries
├── security/           # JwtService, JwtAuthenticationFilter, SecurityConfig, ApplicationConfig
└── service/            # Lógica de negocio (IPrestamoService, IClienteService, ILibroService, IAuthService)
```

- **Lenguaje:** Java 21 (LTS).
- **Framework:** Spring Boot 4.1.1 (`spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`).
- **Seguridad:** Spring Security 7 + JJWT 0.12.6 (algoritmo HMAC-SHA256, clave criptográfica de 256 bits).
- **Persistencia:** PostgreSQL 14 con HikariCP como pool de conexiones.
- **Documentación Interactiva:** OpenAPI 3 / Swagger UI (`springdoc-openapi`) con integración de Bearer JWT.
- **Testing:** JUnit 5, Mockito, AssertJ.
- **Productividad:** Project Lombok (`@Builder`, `@RequiredArgsConstructor`).
- **Control de API:** Postman Collection oficial estructurada con variables globales y herencia de autenticación.

---

## 🔐 Blindaje de Seguridad (Spring Security + JWT)

El sistema opera bajo un esquema **Stateless**:
1. **Registro & Login:** El usuario envía credenciales a `/auth/register` o `/auth/login`. Las contraseñas se almacenan hasheadas con `BCryptPasswordEncoder`.
2. **Generación de Token:** `JwtService` emite un token firmado matemáticamente con vigencia de 24 horas (`HMAC-SHA256`).
3. **Filtro Interceptor:** Cada petición entrante a rutas protegidas es evaluada por `JwtAuthenticationFilter` (`OncePerRequestFilter`), extrayendo el encabezado `Authorization: Bearer <token>`.
4. **Desacople Arquitectónico:** Se implementó una clara separación entre `ApplicationConfig` (beans de autenticación no-web) y `SecurityConfig` (`SecurityFilterChain`), eliminando referencias circulares entre componentes.
5. **Manejo Defensivo:** Ante tokens malformados, manipulados o expirados, el filtro bloquea el acceso de forma controlada respondiendo `403 Forbidden` sin comprometer la estabilidad del servidor.

---

## 📋 Catálogo Completo de Endpoints (17 Rutas)

### 1. 🔐 Autenticación (Público)
| Método | Endpoint | Descripción | Estado HTTP |
|:---:|---|---|:---:|
| `POST` | `/auth/register` | Registro de usuario con rol (`ADMIN` / `CLIENTE`) | `201 CREATED` |
| `POST` | `/auth/login` | Autenticación con BCrypt y emisión de Bearer Token | `200 OK` |

### 2. 📚 Libros (Protegido - Bearer Token)
| Método | Endpoint | Descripción |
|:---:|---|---|
| `GET` | `/libros` | Listar todo el catálogo de libros |
| `GET` | `/libros/{id}` | Consultar detalle de un libro por su ID |
| `POST` | `/libros/crear` | Registrar un nuevo libro en el catálogo |
| `PUT` | `/libros/{id}` | Actualizar datos de un libro existente |
| `DELETE` | `/libros/{id}` | Eliminar libro por ID |

### 3. 👤 Clientes (Protegido - Bearer Token)
| Método | Endpoint | Descripción |
|:---:|---|---|
| `GET` | `/clientes` | Listar clientes junto con su historial de préstamos |
| `GET` | `/clientes/{id}` | Consultar detalle de cliente por ID |
| `POST` | `/clientes/crear` | Registrar nuevo cliente |
| `PUT` | `/clientes/{id}` | Actualizar datos del cliente |
| `DELETE` | `/clientes/{id}` | Eliminar cliente por ID |

### 4. 🎟️ Préstamos (Protegido - Reglas de Negocio)
| Método | Endpoint | Descripción | Estado HTTP |
|:---:|---|---|:---:|
| `GET` | `/prestamos` | Listar todos los préstamos de la biblioteca | `200 OK` |
| `GET` | `/prestamos/{id}` | Consultar un préstamo por ID | `200 OK` |
| `GET` | `/prestamos/cliente/{id}` | Listar todos los préstamos de un cliente | `200 OK` |
| `POST` | `/prestamos` | Crear préstamo validando disponibilidad y ausencia de deudas | `201 CREATED` / `409 CONFLICT` |
| `PUT` | `/prestamos/{id}/devolver` | Registrar devolución de un libro prestado | `200 OK` / `409 CONFLICT` |

---

## 🧪 Estrategia de Testing (JUnit 5 + Mockito)

La lógica de negocio se encuentra 100% blindada mediante **16 pruebas unitarias**:

- **`PrestamoServiceImplTest` (6 tests):**
  - Caminos felices de creación y devolución de préstamos.
  - Validación de regla de negocio: conflicto `409` si el libro ya está prestado.
  - Validación de regla de negocio: conflicto `409` si el cliente tiene deuda activa.
  - Validación de regla de negocio: conflicto `409` si el préstamo ya fue devuelto previamente.
  - Control de excepciones `404` ante identificadores inexistentes.

- **`ClienteServiceImplTest` (5 tests):**
  - Creación exitosa y conversión estricta a DTO.
  - Búsqueda por ID existente y manejo de `ClienteNoEncontradoException`.
  - Actualización exitosa verificando persistencia con `thenAnswer`.
  - Eliminación segura con verificación `verify(repo, never()).delete(...)`.

- **`LibroServiceImplTest` (5 tests):**
  - Creación y mapeo de atributos ISBN/género.
  - Búsqueda por ID existente y fallback con `LibroNoEncontradoException`.
  - Actualización y aislamiento de eliminaciones inválidas.

```bash
# Ejecución de la suite completa de pruebas:
./mvnw test
```

---

## 🐳 Ejecución con Docker (Recomendada)

El proyecto está completamente contenerizado mediante una arquitectura **multi-stage build** y orquestado con **Docker Compose** para levantar la API y PostgreSQL con persistencia garantizada.

### 1. Clonar y configurar variables de entorno
```bash
# Copiar plantilla de variables de entorno
cp .env.example .env
```
*(Edita el archivo `.env` si deseas personalizar tus credenciales locales; este archivo está protegido por `.gitignore`).*

### 2. Levantar la solución completa
```bash
docker compose up -d
```
Docker descargará la imagen de PostgreSQL 14 Alpine, construirá la imagen de Spring Boot 4 y pondrá en marcha los servicios en la red virtual privada.

### 3. Verificar estado y logs
```bash
# Ver estado de los contenedores
docker compose ps

# Seguir logs de la API en tiempo real
docker compose logs -f api
```

### 4. Acceso a la aplicación
- **API y Swagger UI:** `http://localhost:8081/swagger-ui/index.html`
- **PostgreSQL:** Puerto `5432` expuesto para herramientas GUI como DBeaver o DataGrip.

### 5. Detener la solución
```bash
# Pausar y remover contenedores (preserva datos del volumen)
docker compose down

# Limpieza total incluyendo datos de la base de datos
docker compose down -v
```

---

## ⚙️ Configuración y Ejecución Local Tradicional (Sin Docker)

### 1. Prerrequisitos
- JDK 21 instalado (`java -version`).
- PostgreSQL 14 en ejecución.

### 2. Variables de Entorno
Configurar las siguientes variables en la *Run Configuration* de su IDE o en el entorno del sistema:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/biblioteca_db"
export DB_USERNAME="tu_usuario_postgres"
export DB_PASSWORD="tu_password_postgres"
export JWT_SECRET_KEY="tu_clave_secreta_en_hex_256_bits"
```

### 3. Compilar y Levantar
```bash
# Compilar el proyecto
./mvnw clean compile

# Iniciar la aplicación en el puerto 8081
./mvnw spring-boot:run
```

---

## 📖 Documentación Interactiva (Swagger UI)

Una vez que la aplicación esté en ejecución en el puerto `8081`, la documentación visual interactiva se encuentra disponible en:

👉 **[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)**

### Características de la documentación:
- **Organización secuencial:** Endpoints ordenados lógicamente (`1. Autenticación`, `2. Libros`, `3. Clientes`, `4. Préstamos`).
- **Autenticación en vivo:** Permite obtener el JWT desde `/auth/login` y pegarlo en el botón superior **`Authorize` 🔓** para probar endpoints protegidos directamente desde el navegador.
- **Esquemas JSON:** Vista interactiva de Request Bodies y Responses con validaciones de campo.

---

## 📬 Colección de Postman

El proyecto incluye soporte para pruebas automatizadas con Postman:
- **Variable `{{baseUrl}}`:** `http://localhost:8081`
- **Variable `{{token}}`:** Bearer Token autogestionado con herencia en todas las carpetas protegidas.

---

## 👨‍💻 Autor

**Juan Diego Merchán**  
Desarrollador de Software especializado en Backend con Java & Spring Boot.

- 🌐 **Portafolio:** [juandiegodev.vercel.app](https://juandiegodev.vercel.app/)
- 🐙 **GitHub:** [@Juandmj82](https://github.com/Juandmj82)
