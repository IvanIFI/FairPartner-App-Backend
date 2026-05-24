-- Mostrar zona horaria actual de MySQL
SELECT @@global.time_zone AS global_time_zone,
       @@session.time_zone AS session_time_zone;

-- Establecer zona horaria global en UTC
SET GLOBAL time_zone = '+00:00';