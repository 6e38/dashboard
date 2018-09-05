/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.server;

import dashboard.Data;
import java.io.InputStream;
import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server implements Runnable
{
  private Data data;

  public Server(Data d)
  {
    data = d;
  }

  public void run()
  {
    SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

    try
    {
      SSLServerSocket sock = (SSLServerSocket)factory.createServerSocket(5005);

      sock.setEnabledCipherSuites(factory.getSupportedCipherSuites());

      try
      {
        Socket s = sock.accept();

        try
        {
          InputStream in = s.getInputStream();

          try
          {
            while (true)
            {
              int c = in.read();
              System.out.print((char)c);
            }
          }
          catch (IOException e1)
          {
            System.out.println("Error reading byte from client");
          }
        }
        catch (IOException e2)
        {
          System.out.println("Error reading data from client");
        }
      }
      catch (IOException e3)
      {
        System.out.println("Error waiting for client");
      }
    }
    catch (IOException e4)
    {
      System.out.println("Failed to create server socket");
    }
  }

  public static void main(String[] args)
  {
    Thread t = new Thread(new Server(new Data()));
    t.start();

    try
    {
      t.join();
    }
    catch (InterruptedException e)
    {
      System.out.println("Error joining thread");
    }

    System.out.println("Exit");
  }
}

