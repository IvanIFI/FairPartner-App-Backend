> **Estado:** Documentación pendiente de actualización.  
> El modelo descrito puede no reflejar completamente los últimos cambios realizados en la implementación.

# Modelo lógico de la base de datos

### Diagrama
<figure align="center">
  <img src="../images/db-diagram-logical.png" width="800" />
</figure>

*   El atributo ‘resume’ de la tabla EXPENSE GROUP es derivado por lo tanto no aparece en el diagrama. Representa un resumen de la deuda entre los usuarios del grupo de gasto. No se almacena en base de datos, sino que se calcula a partir de los pagos y gastos registrados.

*   REGLA: Un grupo de gastos (id_expense_group) solo puede tener como máximo 2 usuarios asociados. Esta restricción no se implementa mediante claves, sino en la lógica de aplicación o mediante un trigger.

### Especificación
---
User (<u>id</u>, name, email, password, registration_data)   
PK (id)    
NN (name, email, password, registration_data)    
UK (email)     
       
Expense_Group (<u>id</u>, description, icon)    
PK (id)
NN (icon)     
         
Participates (<u>id_user, id_expense_group</u>)    
PK (id_user, id_expense_group)    
FK (id_user) -> User(id)     
FK (id_expense_group) -> Expense_Group(id)    
         
Expense (<u>id</u>, id_expense_group, id_category, name, description, date, icon, cant)    
PK (id)     
FK (id_expense_group) -> Expense_Group(id)     
FK (id_category) -> Category(id)      
NN (id_expense_group, id_category, name, date, icon, cant)     
CHECK (cant > 0)    
        
Payment (<u>id_user, id_expense</u>, cant)    
PK (id_user, id_expense)     
FK (id_user) -> User(id)    
FK (id_expense) -> Expense(id)     
NN (cant)      
CHECK (cant > 0)    
      
Category (<u>id</u>, name, icon)    
PK (id)    
NN (name, icon)      
       
Pantry (<u>id</u>, description, icon)    
PK (id)    
NN (description, icon)     
      
Share (<u>id_user, id_pantry</u>)    
PK (id_user, id_pantry)    
FK (id_user) -> User(id)    
FK (id_pantry) -> Pantry(id)   
     
Shopping_List (<u>id</u>, id_pantry, name)   
PK (id)   
FK (id_pantry) -> Pantry(id)     
NN (name, id_pantry)     
      
Product (<u>id</u>, id_pantry, id_category, id_shopping_list, name, description, icon, cant, expiration_date)    
PK (id)     
FK (id_pantry) -> Pantry(id)      
FK (id_category) -> Category(id)     
FK (id_shopping_list) -> Shopping_List(id)    
NN (id_pantry, id_category, name, icon, cant)     
CHECK (cant >= 0)     

---