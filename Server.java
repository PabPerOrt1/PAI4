import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.net.ServerSocketFactory;

public class Server {

    public static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pk = "MIIDkzCCAnugAwIBAgIUI50SmVCZbqzOyrEYBSbEZAY5rGIwDQYJKoZIhvcNAQEF\n" +
                "BQAwWDELMAkGA1UEBhMCQVUxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoM\n" +
                "GEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZDERMA8GA1UEAwwIMTAuMC4yLjIwIBcN\n" +
                "MjIxMTIxMTIzNzUyWhgPMjEyMjEwMjgxMjM3NTJaMFgxCzAJBgNVBAYTAkFVMRMw\n" +
                "EQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0\n" +
                "eSBMdGQxETAPBgNVBAMMCDEwLjAuMi4yMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A\n" +
                "MIIBCgKCAQEA0YQLNJryXsDrUwcK8jae2kcYoVGRV9iqld8NTQf7KR4BzmZJCDZA\n" +
                "o5l/BCv9x/JsTz7d0n8TV06H9Po/jjpQqKhvteDXZCMuLgo0IpO/fOAnRidG52fE\n" +
                "Qk9NNsCwjHPOT2HjpL8hgFcFHVZZBduV+EHm+WztMqxC4NDmMhUCFmqw3EoVYjAK\n" +
                "BwWkzp5RzZ5vLJ+tyWJRwZnCFI+ME7QBrczlJ8N3+7nre+/rN95hcEnfDphirL0g\n" +
                "ee92G/HBTgxTzv+Ta/HF+OkBY5gr7n0u0oVYm69Vl2BWH3Q0jtQXEyN7qD+kUmUi\n" +
                "XKe2shisPKgaWw/xEW0yybXW0CMG9mgQ8wIDAQABo1MwUTAdBgNVHQ4EFgQU23ov\n" +
                "kv90sN7Xv/8/SEmXXSahgAswHwYDVR0jBBgwFoAU23ovkv90sN7Xv/8/SEmXXSah\n" +
                "gAswDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAX6F9T5OUp6/v\n" +
                "uHn+F3YxHJc0nSe+QJskFbn8e/VppqpLPxQAJ4VRSeFV0Gc2mBJ0GbrbDzlxiyjx\n" +
                "ZfIxIrl6wu6NXy4EQuXAC4hPB51wXs8LcXKIfEyz43GPIavvGrbZx+EsWfIPiRad\n" +
                "4H9XUtLx7Imo/T9VoNMIOGTwtxiUEhFjjTXKasUlfajWnv3A0qMFwzzwcT7YSBJO\n" +
                "DJRViSTPjdBOoI5XSPw/cJHXXlH4AI4HP39Wpg29PSlPyGajnuydKlqejbHuqZSm\n" +
                "sANS/IIC1KSsHWC9Ryz14Z5jsEHA4nnmANtn96uBIibex98LmeFBh40+E2R00MPD\n" +
                "roKLhdQs1A==";
        byte[] publicBytes = Base64.getDecoder().decode(pk);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        return pubKey;
    }

    public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pk = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDRhAs0mvJewOtT\n" +
                "BwryNp7aRxihUZFX2KqV3w1NB/spHgHOZkkINkCjmX8EK/3H8mxPPt3SfxNXTof0\n" +
                "+j+OOlCoqG+14NdkIy4uCjQik7984CdGJ0bnZ8RCT002wLCMc85PYeOkvyGAVwUd\n" +
                "VlkF25X4Qeb5bO0yrELg0OYyFQIWarDcShViMAoHBaTOnlHNnm8sn63JYlHBmcIU\n" +
                "j4wTtAGtzOUnw3f7uet77+s33mFwSd8OmGKsvSB573Yb8cFODFPO/5Nr8cX46QFj\n" +
                "mCvufS7ShVibr1WXYFYfdDSO1BcTI3uoP6RSZSJcp7ayGKw8qBpbD/ERbTLJtdbQ\n" +
                "Iwb2aBDzAgMBAAECggEAKvsYI2QQCaExBXjh5XuQDf5czqdBllBj+lfmTiGvhClc\n" +
                "PH/eRpl+nDE+jdgKeWzBjGY8slSQXq5FFNwLkr1i056uQBUOisPu0tIUDO3bcB45\n" +
                "4FnpYUp3yBSlpfBUp5OQRNezkBYHpOD1MyYGID4CX4oBfLS8WlNdaupf9vE5WuiL\n" +
                "/VbKer4sPswKSs5FCYgDoBIAmG6apqLj6lrJCDQ4Oiq+2TwlYHn7dYJfURNZOGT1\n" +
                "5VRAlcUGb0mDZtseLbcytpavpt4scEXqMKtRwMTv4c4APXtv/Bf6/2S/xKJ4Keuo\n" +
                "pGBw5wn5CHwtzaPTPQ0w3VQwp7Ezy+jPZFWU1TK5KQKBgQDYKqGG1kyh0ZXMkVlg\n" +
                "Xl+kIIuKAdQimrLtz6jJEEb4aQw2CgaA0auUk9b5erpnYDFzojz/rmfFgnRB1JXC\n" +
                "dHha3OIZUnop3Qi+DJxOhttTnWeWyoCuC/BGgXPgu6gpJr8NK8s4IRc8LhO3Av9h\n" +
                "a4DIOmsxfRxCGKl/dddUfp2hiQKBgQD4H6xjN6p+NiLPx0dwxGMhYOoc0jlmxsCL\n" +
                "t6PfPgLTUT6G/yOTnUSKpsBINMOVmy2/84wHeKlrJVVk7A5ka0ixud6Foy+0SGNC\n" +
                "Cxq0IZhN4Pc3fgkHlB8HfS3Sy0skTqrke+uHoaP21ZW3bQhizULRZhhvCYIa2zaH\n" +
                "57csGRtrmwKBgQC5fdN/rfKyRPODSBJmxHTTlO3IbgtH6nGx6ajPZ/1Oem31DSNJ\n" +
                "dSID69zj1bGQtZWZwVUQnesELA5w0ufzphgE+Fe4HPMhaz9uPcn/BG++T2qtlXVY\n" +
                "ZHw5NCpoZP9G2K3Gspa+wn56sJtQRu03/pTSvHZxUAdonFsOOeOd6MyA6QKBgQCk\n" +
                "IJdwfgb6O6clWAWKs217S9cwv2KW+IuvIAd3CVbnH4vD0ote/p2zNYBlgvmJQb+u\n" +
                "hFSxMICozmZBPuB6TnkkjvWB4qMQlv2JVz0PwFU11sTQ4T7p03H/Cy+/ixgmaR8j\n" +
                "ylDPQ/3orfm9/pKYgHxddTJeaKS2TSEyHvWMZwM7VwKBgQC7czvBDT43WV0vdRZX\n" +
                "RivetFUsnzlRqDOUyMB/aOUtScXSwk0hKwLENaCeSsKeWkIR/qUqOY0bqQ0MmFQf\n" +
                "7K2PSTZOYhCqNWTcVlg060KgQhv4J2pzNbs0Fg7izEZXjsB6r1XWs8AfXtRode7x\n" +
                "4B8xWeN1kMhJhF0W4/+6r9tfaA==";
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pk);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        return privKey;
    }

    public static boolean verificaFirmaDigital(String message, byte[] sign2, String str_firma)
			throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		boolean res = false;
		PublicKey publicKey = getPublicKey();

		Signature sign = Signature.getInstance("SHA256withRSA");
		sign.initVerify(publicKey);
		sign.update("1234".getBytes());
		res = sign.verify(sign2);
		return res;
	}

    public static void main(String[] args) throws Exception{
        ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
        ServerSocket serverSocket = (ServerSocket) socketFactory.createServerSocket(9899);
    
        while (true){
            try {
                System.out.println("\nEsperando conexiones...");
                Socket socket = serverSocket.accept();
                System.err.println("\nConectando con un cliente (" + socketFactory + ", " + serverSocket + ")");
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

				String values = input.readLine();
				String str_firma = input.readLine();

				ByteArrayOutputStream utf8String = new ByteArrayOutputStream();
				for (int i = 0; i < str_firma.length(); i += 2) {
					String str = str_firma.substring(i, i + 2);
					int byteVal = Integer.parseInt(str, 16);
					utf8String.write(byteVal);
				}
				byte[] firma = new String(utf8String.toByteArray(), Charset.forName("UTF-8")).getBytes();

                //Para la base de datos
                /*
                 // originalSigBytes will be the same as sig
				BBDDAccess bbdd = new BBDDAccess();

				// Verifico que puedo tener m�s conexiones en el d�a
				boolean overload = bbdd.checkConnectionLimit();

				System.out.println("OV: " + overload);
				if (!overload) {
                } 
                */

                boolean verified;
				try {
					verified = verificaFirmaDigital(values, firma, str_firma);
				} catch (Exception e) {
					verified = false;
				}

                String[] allvalues = values.split(",");
				Integer camas = Integer.parseInt(allvalues[0]);
				Integer sillas = Integer.parseInt(allvalues[1]);
				Integer sillones = Integer.parseInt(allvalues[2]);
				Integer mesas = Integer.parseInt(allvalues[3]);
				Integer idUsuario = Integer.parseInt(allvalues[4]);

                System.out.println("Valores: " + values);
                System.out.println("Firma: " + str_firma);

                /* 
                bbdd.insertOrder(mesas, sillas, sillones, camas, usuario, verified);
				bbdd.generateLog();
                */
            
                /*
                } else {
                    System.out.println("No puede hacer más de 4 peticiones en la misma hora");
                }
                 */

                
                output.close();
				input.close();
				socket.close();  
                
                System.err.println("\nDesconectado el cliente (" + socketFactory + ", " + serverSocket + ")");
                
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}