import sqlite3
con = sqlite3.connect('data.db')

cur = con.cursor()

# Create table
cur.execute('''CREATE TABLE usuario
               (mensaje text, id_empleado integer, firma text)''')

# Insert a row of data
cur.execute("INSERT INTO usuario VALUES ('mensaje_de_prueba','1','firmafirma')")

# Save (commit) the changes
con.commit()

# We can also close the connection if we are done with it.
# Just be sure any changes have been committed or they will be lost.
con.close()