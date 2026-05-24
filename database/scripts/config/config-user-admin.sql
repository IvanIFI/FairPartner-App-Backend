-- Comprobar el id y email del usuario a actualizar.
DELETE FROM user_roles WHERE user_id = 1;
INSERT INTO user_roles (user_id, role_id)
VALUES 
  ((SELECT id FROM users WHERE email = 'user@example.com'),
   (SELECT id FROM roles WHERE name = 'USER')),
  ((SELECT id FROM users WHERE email = 'user@example.com'),
   (SELECT id FROM roles WHERE name = 'ADMIN'));