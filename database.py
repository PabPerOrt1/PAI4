import sqlite3
con = sqlite3.connect('data.db')

cur = con.cursor()

# Create table
# cur.execute('''CREATE TABLE usuario
#                (mensaje text, id_empleado integer, firma text)''')

# Insert a row of data
#cur.execute("INSERT INTO usuario VALUES ('mensaje_de_prueba2','2','firmafirma2')")

############################Por si queremos añadir mucha gente a la vez
# data = [
#     ("Monty Python Live at the Hollywood Bowl", 1982, 7.9),
#     ("Monty Python's The Meaning of Life", 1983, 7.5),
#     ("Monty Python's Life of Brian", 1979, 8.0),
# ]
# cur.executemany("INSERT INTO movie VALUES(?, ?, ?)", data)
# con.commit()  # Remember to commit the transaction after executing INSERT.

res = cur.execute("SELECT * FROM usuario")
print(res.fetchall())
# Save (commit) the changes
con.commit()

# We can also close the connection if we are done with it.
# Just be sure any changes have been committed or they will be lost.
con.close()