/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

class GetHostAction implements Action
{
  public String getCommandName()
  {
    return "gethost";
  }

  public String getUsage()
  {
    return "gethost";
  }

  public String getDescription()
  {
    return "Displays the hostname and port the client will connect to";
  }

  public void doCommand(String[] args)
  {
    if (args.length == 1)
    {
      Client client = new Client();
      System.out.println("Host is " + client.getHost());
      System.out.println("Port is " + client.getPort());
    }
    else
    {
      System.out.println("Usage:");
      System.out.println(getUsage());
    }
  }
}

