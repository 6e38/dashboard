/*
 * Copyright (c) 2018 Nathan Jenne
 */

import java.io.InputStream;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server
{
  public static void main(String[] args) throws Exception
  {
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

