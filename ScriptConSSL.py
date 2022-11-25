from socket import *
import os, threading, ssl

direccionServidor = "localhost"
puertoServidor = 9899

def comprobarEnServidor(mensaje_recibido):
    return None

def worker(*args):
    conn = args[0]
    addr = args[1]
    print("Conectando con un cliente", addr)
    mensajeRecibido = conn.recv(4096).decode()
    print(mensajeRecibido)
    ##Aquí va la comprobacion
    if comprobarEnServidor(mensajeRecibido)=="Comprobación exitosa":
        conn.send(("Peticion OK").encode())
    else: 
        conn.send(("Peticion INCORRECTA").encode())

    print("Desconectado el cliente", addr)
    conn.close()

#Generamos un nuevo socket
context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
context.load_cert_chain('Keys/new.pem', 'Keys/private.key')
socketServidor = socket()
#Establecemos la conexión
socketServidor.bind((direccionServidor,puertoServidor))
socketServidor.listen()
ssock = context.wrap_socket(socketServidor, server_side=True)

acc=1
while 1:
    conn, addr = ssock.accept()
    threading.Thread(target=worker, args=(conn, addr)).start()
    acc+=1