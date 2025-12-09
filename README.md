# Legacy Migration PoC: Oracle Forms to Java/JSF

Este proyecto es una **Prueba de Concepto (PoC)** que simula la arquitectura de modernización de aplicaciones legacy (estilo Oracle Forms) hacia un stack tecnológico basado en Java y Web.

El objetivo es demostrar una **integración robusta entre la capa de presentación (JSF) y la lógica de negocio residente en Base de Datos (PL/SQL)**, manteniendo la integridad transaccional en Oracle.

## 🚀 Stack Tecnológico

*   **Java 8**: Compatibilidad con entornos legacy/bancarios.
*   **Spring Boot 2.7**: Gestión de dependencias e inyección (`JdbcTemplate`).
*   **JSF (JoinFaces) + PrimeFaces 11**: Capa de presentación visual (View).
*   **Oracle Database 21c Express Edition**: Base de datos contenerizada (Docker).
*   **PL/SQL**: Lógica de negocio encapsulada en Stored Procedures.
*   **Maven**: Gestión de construcción.

## 🏛️ Arquitectura Implementada

Se ha seguido el patrón de diseño **"The Morphis Way"**, desacoplando la lógica de la vista:

1.  **Vista (.xhtml):** Interfaz reactiva mediante AJAX (PrimeFaces).
2.  **Controlador (Java Bean):** Gestiona el estado de la sesión (`@ViewScoped`) y actúa como orquestador.
3.  **Servicio de Datos (Spring JDBC):** Invoca procedimientos almacenados usando `CallableStatement`, gestionando parámetros de entrada (`IN`) y salida (`OUT`).
4.  **Base de Datos (Oracle):** Ejecución de la lógica transaccional y persistencia.

## 🛠️ Despliegue Local

### 1. Base de Datos (Docker)
El proyecto requiere una instancia de Oracle. Se utiliza la imagen ligera de `gvenzl` mapeada al puerto **51521** para evitar conflictos locales.
