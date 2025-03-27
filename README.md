
---

### **2. README para el Backend**

**`task-list-backend/README.md`:**

```markdown
# Task List Backend

Este proyecto es la parte backend de una aplicación de lista de tareas desarrollada como parte del **Challenge Técnico FullStack Developer**. Está construido con **Java**, utiliza **Firebase Firestore** para la persistencia de datos, y está desplegado en **Google Cloud Functions**.

## Descripción

El backend proporciona una API RESTful para gestionar usuarios y tareas, con autenticación basada en JWT. Se integra con un frontend en Angular 17 para permitir a los usuarios iniciar sesión, crear tareas, listarlas, marcarlas como completadas, y eliminarlas.

- **URL del Frontend Desplegado:** [https://clusterdb-eaaa6.web.app/login]
- **Repositorio Frontend:** [task-list-frontend](https://github.com/cisbarey/task-list-frontend)

## Características

- **Autenticación:** Login y creación de usuarios con JWT.
- **Gestión de Tareas:** Endpoints para crear, listar, actualizar y eliminar tareas.
- **Persistencia:** Uso de Firebase Firestore para almacenar usuarios y tareas.
- **Seguridad:** Verificación de tokens JWT y configuración de CORS.
- **Buenas Prácticas:** Código modular y limpio, siguiendo principios como DRY y KISS.

## Tecnologías

- Java 17
- Maven
- Firebase Admin SDK
- Google Cloud Functions
- Firebase Firestore
- JWT (JSON Web Tokens)

## Endpoints

- **POST /CreateUserFunction**: Crea un usuario con el correo proporcionado.
- **POST /LoginFunction**: Autentica al usuario y devuelve un token JWT.
- **GET /GetTasksFunction**: Lista las tareas del usuario autenticado.
- **POST /CreateTaskFunction**: Crea una nueva tarea.
- **PUT /CompleteTaskFunction/{id}**: Marca una tarea como completada.
- **DELETE /DeleteTaskFunction/{id}**: Elimina una tarea.

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/cisbarey/task-list-backend.git
   cd task-list-backend
