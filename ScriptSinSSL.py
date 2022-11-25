from socket import *
import os, threading

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
    # if comprobarEnServidor(mensajeRecibido)=="Comprobación exitosa":
    #     conn.send(("La comprobación ha sido exitosa, estamos guardando su mensaje").encode())
    # else: 
    #     conn.send(("La comprobación ha sido errónea. Inténtelo de nuevo").encode())

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