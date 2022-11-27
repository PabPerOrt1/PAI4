DROP TABLE IF EXISTS valores;
CREATE TABLE valores(id_empleado INTEGER NOT NULL PRIMARY KEY, 
camas INTEGER NOT NULL, sillas INTEGER NOT NULL, 
sillones INTEGER NOT NULL, mesas INTEGER NOT NULL, fecha DATETIME NOT NULL, hora TIMESTAMP NOT NULL ,accepted TEXT NOT NULL );

INSERT INTO valores(id_empleado,camas,sillas,sillones,mesas,date,accepted) VALUES (1,2,3,4,5,2000-11-11, 'dinero');