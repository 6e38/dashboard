/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.Preferences;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client
{
  private static final Action[] actions = {
    new LockAction(),
    new UnlockAction(),
    // show log
    // set ip
  };

  public static void main(String[] args)
  {
    //Preferences prefs = Preferences.userNodeForPackage(Client.class);
    //System.out.println("Prefs is: " + prefs.toString());
    System.out.println(">>> " + System.getProperty("os.name"));
    System.out.println(">>> " + System.getProperty("user.home"));
    System.out.println(">>> " + Client.class.getName());
    System.out.println(">>> " + Client.class.getPackage());

    if (args.length == 1)
    {
      boolean foundCommand = false;

      for (Action a : actions)
      {
        if (args[0].equals(a.getCommandName()))
        {
          foundCommand = true;
          a.doCommand();
        }
      }

      if (!foundCommand)
      {
        System.out.println("Failed to find command :(");
      }
    }
    else
    {
      System.out.println("Wrong number of args");
    }
  }

  private void sendPacket() throws IOException
  {
    SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    SSLSocket sock = (SSLSocket)factory.createSocket("localhost", 5005);

    sock.setEnabledCipherSuites(factory.getSupportedCipherSuites());

    OutputStream out = sock.getOutputStream();

    out.write("This is the song that never ends\n".getBytes());
  }
}

