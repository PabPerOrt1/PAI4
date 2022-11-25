from multiprocessing import context
import ssl,sys,time
from socket import *

def comprobarCredenciales(mensajeRecibido):
    return None
    "Multiconexion con hilos?"

    """recoger además de las peticiones de
    compras, la información necesaria para los indicadores
    exigidos en la Política de Seguridad Corporativa para que
    pueda usarse por el Gobierno de la Seguridad de la información de la
    Corporación respecto a la incorrecta autenticación de los
    clientes.
    """

    """El servidor deberá responder a las peticiones de los empleados con un
    mensaje en la pantalla de Petición OK o Petición INCORRECTA. La
    petición incorrecta viene determinada por una entrada invalida o
    por la verificación de la firma inválida.
    """
#Funcional para inputs por cmd
def get_conection():
    context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
    context.load_cert_chain('Keys/new.pem', 'Keys/private.key')
    soc = socket()
    soc.bind(("", int(Puerto)))
    print(f"El servidor está corriendo en el puerto '{int(Puerto)}'")
    print("Escuchando conexiones...")
    soc.listen(300)
    
    ssock = context.wrap_socket(soc, server_side=True)
    conn, addr = ssock.accept()
    print("Conectando con un cliente", addr)
    mensajeRecibido = conn.recv(4096).decode()
    print(mensajeRecibido)
    if comprobarCredenciales(mensajeRecibido)=="Comprobación exitosa":
        conn.send(("La comprobación ha sido exitosa, estamos guardando su mensaje").encode())
    else: 
        conn.send(("La comprobación ha sido errónea. Inténtelo de nuevo").encode())

    print("Desconectado el cliente", addr)
    
    conn.close()
    #sys.exit()

def get_conection_auto():
    
    while True:
        context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
        context.load_cert_chain('Keys/new.pem', 'Keys/private.key')
        soc = socket()
        soc.bind(("", int(Puerto)))
        print(f"El servidor está corriendo en el puerto '{int(Puerto)}'")
        print("Escuchando conexiones...")
        soc.listen(300)
        
        ssock = context.wrap_socket(soc, server_side=True)
        conn, addr = ssock.accept()
        print("Conectando con un cliente", addr)
        while True:
            mensajeRecibido = conn.recv(4096).decode()
            print(mensajeRecibido)
            if not mensajeRecibido:
                break
            else:
                if comprobarCredenciales(mensajeRecibido)=="Comprobación exitosa":
                    conn.send(("La comprobación ha sido exitosa, estamos guardando su mensaje").encode())
                else: 
                    conn.send(("La comprobación ha sido errónea. Inténtelo de nuevo").encode())

        print("Desconectado el cliente", addr)

        time.sleep(15)
        conn.close()
        #sys.exit()

if __name__ == "__main__":
    #get_conection()
    get_conection_auto()