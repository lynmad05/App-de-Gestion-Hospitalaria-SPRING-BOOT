# Sistema de Gestión Hospitalaria 🏥
Sistema web para la administración de pacientes, citas, médicos y otros procesos hospitalarios, con un backend API REST construido con Spring Boot y un frontend single-page application (SPA) con HTML, Bootstrap y JavaScript puro.
<img width="1917" height="915" alt="image" src="https://github.com/user-attachments/assets/5a017244-541d-4d4e-b7ba-2b176e30e5bf" />

## Tecnologías Utilizadas 🛠️
Backend: Java, Spring Boot 3, Spring Data JPA, Spring Security, JWT.

Frontend: HTML5, CSS3, Bootstrap 5 (Tema Minty + Custom CSS), JavaScript (Vanilla JS con Fetch API).

Base de Datos: MySQL (Configurado, puede usarse H2 también).

Seguridad: Autenticación basada en Tokens JWT, Encriptación de contraseñas (BCrypt), Autorización por Roles.

Otros: Maven, Lombok, AOP (para Bitácora).

## Módulos Implementados 📋
Se implementaron las funcionalidades centrales para los 7 módulos definidos en el enunciado:

Pacientes: Registro, consulta (con búsqueda por DNI/nombre), actualización y desactivación (CRUD completo). Creación automática de Historia Clínica.

Citas Médicas: Agendamiento, consulta, reprogramación (editar) y cancelación (CRUD completo).

Médicos y Especialidades: Registro y consulta de médicos y sus especialidades.

Consultas y Diagnósticos: Registro básico (entidades y relaciones listas).

Hospitalización: Registro básico y consulta de estado de habitaciones.

Facturación: Registro básico y consulta.

Administración y Seguridad: Creación de usuarios con roles, bitácora automática de acciones y control de acceso basado en roles.

## Requerimientos Cumplidos ✅
Funcionales (RF)
RF1-RF3 (Pacientes): CRUD completo implementado, incluyendo búsqueda y creación automática de historia.

RF4-RF6 (Citas): CRUD completo implementado (agendar, reprogramar, cancelar, consultar estados).

RF7-RF8 (Médicos): Funcionalidad de registro y consulta implementada.

RF10-RF12 (Consultas): Estructura de datos y relaciones implementadas.

RF13-RF15 (Hospitalización): Funcionalidad básica implementada.

RF16-RF18 (Facturación): Funcionalidad básica implementada.

RF19 (Usuarios/Roles): Implementado (creación de usuarios con roles definidos).

RF20 (Bitácora): Implementado automáticamente usando AOP para todas las acciones en los controladores.

RF21 (Acceso por Rol): Implementado en el backend (Spring Security restringe acceso a endpoints según rol).

No Funcionales (RNF)
RNF1 (Confidencialidad): Cumplido mediante autenticación JWT y autorización por roles.

RNF2 (Encriptación Contraseñas): Cumplido usando BCrypt.

RNF3 (Bitácora Accesos): Cumplido (ver RF20).

RNF8 (Interfaz Intuitiva): Cumplido con Bootstrap, tema Minty y layout de sidebar.

RNF9 (Idioma): Cumplido (Interfaz en Español).

RNF10 (Modular): Cumplido. Arquitectura desacoplada API REST (Backend) + SPA (Frontend).

RNF11 (Integración): Cumplido. La API REST está diseñada para ser consumida por cualquier sistema.

RNF12 (Escalabilidad): Cumplido. Spring Boot es escalable por naturaleza.

(RNF4, RNF5, RNF6, RNF7 son relativos a despliegue/infraestructura, no directamente al código desarrollado en local).

## Arquitectura 🏛️
El sistema sigue una arquitectura API REST desacoplada:

Backend (Spring Boot): Expone endpoints RESTful (/api/v1/...) que devuelven datos en formato JSON. Se encarga de la lógica de negocio, persistencia y seguridad.

Frontend (HTML/JS): Una aplicación de página única (SPA) que consume la API REST usando fetch. Manipula el DOM para mostrar la interfaz dinámicamente. No depende del backend para renderizar HTML.

## Ejecución ▶️
Configurar la conexión a la base de datos MySQL en application.properties.

Ejecutar la clase principal GestionHospitalariaApplication.java desde el IDE.

Acceder a http://localhost:8086 en el navegador.

Crear un primer usuario (admin) usando Postman (POST /api/v1/usuarios).

Iniciar sesión con las credenciales creadas.
