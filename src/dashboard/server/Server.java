/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.server;

import dashboard.Data;
import java.io.InputStream;
import java.io.IOException;
import java.net.ServerSocket;
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
      SSLServerSocket server = (SSLServerSocket)factory.createServerSocket(5005);

      server.setEnabledCipherSuites(factory.getSupportedCipherSuites());

      accept(server);
    }
    catch (IOException e)
    {
      System.out.println("Failed to create server socket");
    }
  }

  private void accept(ServerSocket server)
  {
    try
    {
      getInput(server.accept());
    }
    catch (IOException e)
    {
      System.out.println("Error waiting for client");
    }
  }

  private void getInput(Socket sock)
  {
    try
    {
      read(sock.getInputStream());
    }
    catch (IOException e)
    {
      System.out.println("Error reading data from client");
    }
  }

  private void read(InputStream in)
  {
    try
    {
      while (true)
      {
        int c = in.read();
        System.out.print((char)c);
      }
    }
    catch (IOException e)
    {
      System.out.println("Error reading byte from client");
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

