# TP2 · Persistencia, migraciones y arquitectura hexagonal

Evolución del proyecto del TP1 (API REST en capas): ahora con PostgreSQL, JPA/Hibernate, Flyway, listas de favoritos y transacciones.
API REST con arquitectura en capas (Controller → Service → Repository) que expone:
- **Catálogo de productos** (`/api/productos`, solo lectura) — consume la API externa DummyJSON y expone su propio contrato.
- **Favoritos** (`/api/favoritos`, CRUD completo) — persistido en PostgreSQL con JPA y Flyway.
- **Listas** (`/api/listas`, operaciones para agrupar) — permite organizar los favoritos en listas.

## Requisitos
- Java 25
- Maven (o usar el wrapper incluido, `mvnw` / `mvnw.cmd`)
- Docker (o una instalación local de PostgreSQL)

## Cómo levantar el proyecto
Primero hay que levantar la base de datos (ver sección TP2). Una vez que la base esté corriendo, desde la raíz del proyecto, ejecutar:

```bash
# Linux / Mac
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

La aplicación levanta en `http://localhost:8080`.

## Documentación (Swagger UI)
Con la aplicación corriendo, abrir en el navegador:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Ahí se pueden ver y probar todos los endpoints, agrupados en `productos`, `favoritos` y `listas`.

## Endpoints principales

### Productos (solo lectura)
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos/{id}` | Obtiene un producto por id |

### Favoritos (CRUD)
| Método | Endpoint | Código de éxito | Descripción |
|---|---|---|---|
| POST | `/api/favoritos` | 201 Created | Crea un favorito |
| GET | `/api/favoritos` | 200 OK | Lista todos los favoritos |
| GET | `/api/favoritos/{id}` | 200 OK | Obtiene un favorito por id |
| PUT | `/api/favoritos/{id}` | 200 OK | Actualiza un favorito |
| DELETE | `/api/favoritos/{id}` | 204 No Content | Elimina un favorito |

## Ejemplo de body para crear/actualizar un favorito
```json
{
  "productoId": 1,
  "nota": "me encanta este producto",
  "listaId": 1
}
```
*(Nota: la lista indicada en listaId debe existir previamente).*

## Manejo de errores
Todas las respuestas de error siguen el formato estándar RFC 7807 (`ProblemDetail`):
```json
{
  "status": 404,
  "title": "Not Found",
  "detail": "No existe el favorito con id 999"
}
```
Casos contemplados:
- **404** — favorito, producto o lista inexistente
- **400** — validación fallida en el body, o intento de mover favoritos a la misma lista
- **409** — intento de borrar una lista que todavía tiene favoritos
- **502** — falla al consumir DummyJSON (caída o timeout)

## TP2 - Persistencia con PostgreSQL

### Cómo levantar la base de datos
Para levantar PostgreSQL con Docker, usá este comando en la raíz del proyecto:
```bash
docker compose up -d
```
Podés ver que esté corriendo con `docker compose ps`. Los datos de conexión son:
- **Base de datos:** tp2db
- **Usuario:** tp2user
- **Clave:** tp2pass
- **Puerto:** 5432

Si preferís usar una instalación local de PostgreSQL en vez de Docker, también funciona, siempre y cuando configures la base y el usuario con esos mismos datos.

### Cómo arrancar la app y verificar migraciones
Arrancá la aplicación como siempre:
```bash
.\mvnw.cmd spring-boot:run
```
Para confirmar que las migraciones corrieron bien, buscá en la consola un mensaje que dice algo como `Successfully applied ... now at version v4`. 
También podés meterte al contenedor y ver la tabla de Flyway directamente:
```bash
docker exec -it tp2-postgres psql -U tp2user -d tp2db -c "SELECT version, description, success FROM flyway_schema_history;"
```

### Nota para usuarios de Windows
Si al arrancar te tira un error tipo `invalid value for parameter "TimeZone": "America/Buenos_Aires"`, es porque la JVM saca ese nombre viejo de Windows y Postgres lo rechaza. Se soluciona forzándolo al principio del main: `TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));`. Ya lo dejé configurado en la clase principal.

### Qué cambió y qué no respecto del TP1
Lo más copado de usar Arquitectura Hexagonal es que `FavoritoRepository` es un puerto (un contrato). Tanto el repositorio en memoria viejo del TP1 como el nuevo `FavoritoRepositoryAdapter` (que usa JPA) son adaptadores intercambiables detrás de esa interfaz. Por eso no hizo falta romper todo el proyecto para cambiar la persistencia.

| Categoría | Clases afectadas |
|---|---|
| **Clases creadas** | `FavoritoEntity`, `FavoritoJpaRepository`, `FavoritoRepositoryAdapter`, todo el módulo nuevo de `listas` (dominio, dto, repository, entidad, service y controller), y las excepciones `ListaNoVaciaException` y `OperacionInvalidaException`. |
| **Clases borradas** | `InMemoryFavoritoRepository` (se reemplazó por el adapter de JPA). |
| **Clases modificadas** | `Favorito` (se sumó listaId), `FavoritoRequest`/`FavoritoResponse` (se sumó listaId), `FavoritoRepository` (se agregó findByListaId), `FavoritoService` (solo para pasar y validar listaId), `GlobalExceptionHandler` (dos handlers nuevos), `FavoritoController` (solo para agregar anotaciones de Swagger), `Tp1FavoritosApplication` (se le sumó el TimeZone.setDefault) y `OpenApiConfig` (título y descripción). |
| **Clases intactas** | `ProductoController`, `ProductoService`, DTOs de productos, `RestClientConfig`, `RecursoNoEncontradoException`, `ServicioExternoException`. |

*Nota aclaratoria: Las migraciones V1, V2 y V3 no se editaron para mantener la consistencia histórica.*

### Endpoints de Listas
| Método | Endpoint | Código de éxito | Descripción |
|---|---|---|---|
| POST | `/api/listas` | 201 Created | Crea una nueva lista |
| GET | `/api/listas` | 200 OK | Trae todas las listas |
| GET | `/api/listas/{id}` | 200 OK | Obtiene una lista por su id |
| GET | `/api/listas/{id}/favoritos` | 200 OK | Trae los favoritos de una lista |
| DELETE | `/api/listas/{id}` | 204 No Content | Elimina una lista vacía |
| POST | `/api/listas/{origenId}/mover-favoritos` | 204 No Content | Mueve favoritos a otra lista y borra la de origen |

**Ejemplo de body para mover favoritos:**
```json
{
  "listaDestinoId": 3
}
```
*(Nota: la lista origen se pasa en la URL. Si la lista origen o destino no existen devuelve 404, y si son la misma devuelve 400).*

### Evolución del esquema: por qué V4 es una migración nueva
Lo que pasa con la migración V4 es que los favoritos que ya teníamos cargados en la base no tenían ningún lista_id. Yo quería que esa columna sea sí o sí NOT NULL, pero si editaba la V3 que ya había corrido, el checksum de Flyway no iba a coincidir y se iba a trabar todo el arranque. Flyway hace esto para que una base que ya corrió la V3 vieja no quede distinta de una base nueva que corre la V3 editada. Por eso armé una V4 nueva para no romper la compatibilidad. Ahí primero creo la lista "Sin clasificar", actualizo los favoritos viejos para que apunten ahí, y recién al final le pongo el NOT NULL a la columna. Lo probé con datos que ya tenía en PostgreSQL y funcionó bárbaro, quedaron todos ahí.

### Transacciones: por qué @Transactional en mover-favoritos
El tema con el método moverFavoritos es que hace varias escrituras: reasigna los favoritos a la lista destino guardándolos uno por uno, y después borra la lista origen. Si no usara `@Transactional`, cada escritura se confirmaría por separado en la base. Si fallara el borrado de la lista al final, o se cayera la conexión en el medio, la base quedaría a medias. Imaginate que fallara algo y nos quedaran algunos favoritos ya movidos a la lista destino, pero la lista origen todavía siguiera existiendo con los demás. Básicamente se perdería la A de atomicidad de ACID, porque no se ejecutaría todo como un solo bloque. Con la anotación, si algo revienta se hace rollback de todo y no queda nada aplicado. Los chequeos de 404 se hacen antes de arrancar a escribir.
