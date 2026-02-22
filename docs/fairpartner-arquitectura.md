# Arquitectura de la Aplicación

La aplicación sigue una arquitectura en tres capas: clientes, backend y base de datos.  
Cada cliente (Android, escritorio y web) utiliza SQLite para almacenamiento local y se sincroniza con una API REST central.  
El backend está desarrollado en Java con Spring Boot, usando Spring Web, Spring Security y Spring Data JPA para gestionar peticiones, seguridad y persistencia.  
La base de datos principal es relacional (MySQL), donde se consolidan los datos compartidos.  
Esta estructura permite trabajar offline y mantener la integridad centralizada de la información.

### Componentes principales

| Componente            | Tipo                      | Descripción                                                                 |
|-----------------------|---------------------------|-----------------------------------------------------------------------------|
| **Android App**       | Cliente móvil             | Aplicación en Kotlin con base de datos local SQLite. Sincroniza con la API. |
| **Desktop App**       | Cliente escritorio        | Aplicación en WPF (.NET) con SQLite local. Comunica con la API REST.        |
| **Web App**           | Cliente web (futuro)      | Aplicación web (posiblemente con Spring Boot o frontend JS).                |
| **SQLite**            | Base de datos local       | Almacenamiento local en cada cliente para uso offline.                      |
| **API RESTful**       | Backend (Java + Spring)   | Servidor que gestiona la lógica del negocio y centraliza los datos.         |
| **Spring Web**        | Framework backend         | Gestiona rutas, controladores y peticiones REST.                            |
| **Spring Security**   | Framework de seguridad    | Control de acceso y autenticación.                                          |
| **Spring Data JPA**   | Framework persistencia    | Acceso a datos relacionales mediante JPA e Hibernate.                       |
| **SQL (MySQL)**       | Base de datos central     | Base de datos relacional para almacenar los datos compartidos.              |


### Diagrama de la arquitectura
<figure align="center">
  <img src="images/FairPartner-Architecture.png" width="800" />
</figure>
               
### Consideraciones
Esta arquitectura está diseñada para escalabilidad, soporte offline y una separación clara de responsabilidades entre cliente y servidor.

