
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Server
{
  public static void main(String[] args) throws Exception
  {
    /*
    KeyManagerFactory kmfact = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    kmfact.init(ks, "foo".toCharArray());

    TrustManagerFactory tmfact = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

    SSLContext context = SSLContext.getDefault();
    context.init(kmfact.getKeyManagers(), tmfact.getTrustManagers(), new SecureRandom());
    */

    SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
    SSLServerSocket sock = (SSLServerSocket)factory.createServerSocket(5005);

    for (String s : factory.getSupportedCipherSuites())
    {
      System.out.println(s);
    }

    sock.setEnabledCipherSuites(factory.getSupportedCipherSuites());

    Socket s = sock.accept();

    InputStream in = s.getInputStream();

    while (true)
    {
      int c = in.read();
      System.out.print((char)c);
    }
  }
}

