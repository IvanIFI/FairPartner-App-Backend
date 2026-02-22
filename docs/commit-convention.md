# Convención de Commits

Este repositorio sigue la convención de [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/).

## Tipos de Commit

- **`feat`**: Nueva funcionalidad para el usuario.
- **`fix`**: Corrección de errores para el usuario.
- **`docs`**: Cambios en la documentación.
- **`style`**: Cambios que no afectan la lógica (espacios, formato, comas).
- **`refactor`**: Cambios en el código que no corrigen bugs ni añaden features.
- **`perf`**: Mejoras en el rendimiento.
- **`test`**: Cambios o adiciones a los tests.
- **`build`**: Cambios que afectan el sistema de build (npm, Maven, etc.).
- **`ci`**: Cambios en la configuración de integración continua.
- **`chore`**: Tareas internas de mantenimiento que no afectan a la app ni a los tests.
- **`revert`**: Reversión de un commit anterior.

## Estructura de los commits

Cada commit debe seguir esta estructura:  
\<tipo\>(\<alcance opcional\>): \<mensaje corto y descriptivo\>


### Ejemplos:

- `chore (init): estructura inicial del proyecto (backend, desktop, mobile, docs)`
- `feat: agregar endpoint para crear gastos`
- `fix: corregir error de cálculo en el total de gastos`
- `docs: actualizar README con instrucciones de instalación`

## Notas

- Usa `chore (init)` para el primer commit.
- La descripción debe ser clara, concisa y explicativa.
- Utiliza **`[tipo]`** según lo que estés haciendo.

