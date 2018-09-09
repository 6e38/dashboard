/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

import com.floorsix.preferences.Preferences;
import java.io.IOException;
import java.io.OutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client
{
  private static final Action[] actions = {
    new LockAction(),
    new UnlockAction(),
    new SetHostAction(),
    new GetHostAction(),
    // show log
  };

  public static void main(String[] args)
  {
    if (args.length >= 1)
    {
      boolean foundCommand = false;

      for (Action a : actions)
      {
        if (args[0].equals(a.getCommandName()))
        {
          foundCommand = true;
          a.doCommand(args);
        }
      }

      if (!foundCommand)
      {
        System.out.println("\nUnknown command: " + args[0]);
        System.out.println("\nCommands:");

        for (Action a : actions)
        {
          System.out.println(String.format("  %-30s %s", a.getUsage(), a.getDescription()));
        }
      }
    }
    else
    {
      System.out.println("Wrong number of args");
    }
  }

  private Preferences prefs;

  Client()
  {
    prefs = Preferences.getUserPreferences(Client.class);
  }

  void setHost(String host, int port)
  {
    prefs.setString("host", host);
    prefs.setDouble("port", port);
    prefs.commit();
  }

  String getHost()
  {
    return prefs.getString("host", "localhost");
  }

  int getPort()
  {
    return (int)prefs.getDouble("port", 5005);
  }

  void sendPacket() throws IOException
  {
    SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    SSLSocket sock = (SSLSocket)factory.createSocket(getHost(), getPort());

    sock.setEnabledCipherSuites(factory.getSupportedCipherSuites());

    OutputStream out = sock.getOutputStream();

    out.write("This is the song that never ends\n".getBytes());
  }
}

