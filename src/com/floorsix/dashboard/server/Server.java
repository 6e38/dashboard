/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.server;

import com.floorsix.dashboard.Data;
import com.floorsix.json.*;
import java.io.InputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server implements Runnable
{
  private Data data;
  private boolean running;
  private boolean singleConnection;

  private Server(Data d)
  {
    data = d;
    running = false;
    singleConnection = false;
  }

  public void run()
  {
    running = true;

    SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

    try
    {
      SSLServerSocket server = (SSLServerSocket)factory.createServerSocket(5005);

      String[] suites = {
        "TLS_ECDH_anon_WITH_AES_128_CBC_SHA",
        "TLS_ECDH_anon_WITH_NULL_SHA",
      };
      server.setEnabledCipherSuites(suites);

      /*
      for (String s : factory.getSupportedCipherSuites())
      {
        System.out.println(s);
      }
      */

      while (running)
      {
        accept(server);

        if (singleConnection)
        {
          break;
        }
      }
    }
    catch (IOException e)
    {
      System.out.println("Failed to create server socket");
    }
  }

  public void stop()
  {
    running = false;
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
    StringBuilder s = new StringBuilder();

    try
    {
      while (true)
      {
        int c = in.read();
        s.append((char)c);
      }
    }
    catch (IOException e)
    {
    }

    parsePacket(s.toString());
  }

  private void parsePacket(String pkt)
  {
    try
    {
      JsonObject object = JsonParser.parse(pkt);
      handlePacket(object);
    }
    catch (InvalidJsonException e)
    {
      System.out.println("Parsing error? " + e);
    }
  }

  private void handlePacket(JsonObject object)
  {
    Json json = object.get("type");
    if (json != null && json instanceof JsonString)
    {
      String type = ((JsonString)json).get();
      if (type.equals("lock"))
      {
        json = object.get("timestamp");
        if (json != null && json instanceof JsonNumber)
        {
          long timestamp = ((JsonNumber)json).getLong();
          data.lockEvent(timestamp);
        }
      }
      else if (type.equals("unlock"))
      {
        json = object.get("timestamp");
        if (json != null && json instanceof JsonNumber)
        {
          long timestamp = ((JsonNumber)json).getLong();
          data.unlockEvent(timestamp);
        }
      }
    }
  }

  public static void launchServer(Data data)
  {
    Server server = new Server(data);
    Thread t = new Thread(server);
    t.start();
  }

  public static void main(String[] args)
  {
    Server server = new Server(new Data());
    server.singleConnection = true;
    Thread t = new Thread(server);
    t.start();

    try
    {
      t.join();
    }
    catch (InterruptedException e)
    {
      System.out.println("Error joining thread");
    }
  }
}

