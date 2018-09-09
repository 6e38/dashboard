/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

class SetHostAction implements Action
{
  public String getCommandName()
  {
    return "sethost";
  }

  public String getUsage()
  {
    return "sethost <host> <port>";
  }

  public String getDescription()
  {
    return "Sets the hostname and port the client will connect to";
  }

  public void doCommand(String[] args)
  {
    if (args.length == 3)
    {
      Client client = new Client();
      client.setHost(args[1], Integer.parseInt(args[2]));
    }
    else
    {
      System.out.println("Usage:");
      System.out.println(getUsage());
    }
  }
}

