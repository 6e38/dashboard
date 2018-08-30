/*
 * Copyright (c) 2018 Nathan Jenne
 */

import java.io.IOException;
import java.io.OutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client
{
  public static void main(String[] args) throws IOException
  {
    SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    SSLSocket sock = (SSLSocket)factory.createSocket("localhost", 5005);

    sock.setEnabledCipherSuites(factory.getSupportedCipherSuites());

    OutputStream out = sock.getOutputStream();

    out.write("This is the song that never ends\n".getBytes());
  }
}

