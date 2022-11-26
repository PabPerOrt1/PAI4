from socket import *
import sqlite3, threading
from datetime import datetime

con = sqlite3.connect('data.db')
cur = con.cursor()
direccionServidor = "localhost"
puertoServidor = 9899

def comprobarEnServidor(mensaje_recibido):
    splitao= mensaje_recibido.split("_")
    valores=splitao[0]
    firma=splitao[1]
    #Verificar el proceso de firma
    key=""
    rsakey = RSA.importKey(key)
    verifier = Signature_pkcs1_v1_5.new(rsakey)
    digest = SHA.new()
    # Assumes the data is base64 encoded to begin with
    digest.update(message)
    is_verify = verifier.verify(digest, base64.b64decode(signature))

    print(is_verify)

    return None

def worker(*args):
    conn = args[0]
    addr = args[1]
    print("Conectando con un cliente", addr)
    mensajeRecibido = conn.recv(4096).decode()
    splited = mensajeRecibido.split(",")
    id_empleado = splited[0]
    camas = splited[1]
    mesas = splited[2]
    sillas = splited[3]
    sillones = splited[3]
    timestamp = datetime.now().date()

    print(mensajeRecibido)
    ##Aquí va la comprobacion
    if comprobarEnServidor(mensajeRecibido)=="Comprobación exitosa":
        conn.send(("Peticion OK").encode())
        cur.execute(f"INSERT INTO mensajes VALUES ({str(id_empleado)},{str(camas)},{str(mesas)},{str(sillas)},{str(sillones)},times)")
    else: 
        conn.send(("Peticion INCORRECTA").encode())
        cur.execute(f"INSERT INTO mensajes VALUES (0,0,0,0,0)")

    print("Desconectado el cliente", addr)
    conn.close()

#Generamos un nuevo socket
socketServidor = socket(AF_INET, SOCK_STREAM)
#Establecemos la conexión
socketServidor.bind((direccionServidor,puertoServidor))
socketServidor.listen()

acc=1
while 1:
    conn, addr = socketServidor.accept()
    threading.Thread(target=worker, args=(conn, addr)).start()
    acc+=1