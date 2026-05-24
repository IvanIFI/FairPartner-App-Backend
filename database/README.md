# Consideraciones de diseño

## Escalabilidad del modelo de gastos compartidos

Aunque la versión actual de la API está orientada a grupos de gasto de hasta dos usuarios, el modelo relacional de la base de datos fue diseñado desde el inicio para soportar grupos con múltiples participantes.

Esta decisión permite escalar la aplicación en futuras versiones sin necesidad de rediseñar la estructura principal de la base de datos.

### Motivos de diseño

La implementación utiliza relaciones N:M mediante tablas intermedias:

- `participates`
- `payment`
- `expense_share`

Esto permite:

- asociar múltiples usuarios a un mismo grupo,
- dividir gastos entre varios participantes,
- registrar pagos parciales o personalizados,
- y calcular balances dinámicamente entre usuarios.

### Expense Share

La tabla `expense_share` representa el reparto individual de un gasto entre los participantes.

Gracias a este enfoque:

- cada gasto puede dividirse de forma flexible,
- los importes no tienen por qué repartirse equitativamente,
- y el sistema puede adaptarse fácilmente a escenarios con múltiples usuarios.

### Restricción actual

Actualmente, la lógica de aplicación limita los grupos a un máximo de dos usuarios debido al enfoque funcional inicial del proyecto (gestión de gastos en pareja).

Sin embargo, esta limitación se encuentra únicamente en la capa de negocio y no en el diseño estructural de la base de datos.