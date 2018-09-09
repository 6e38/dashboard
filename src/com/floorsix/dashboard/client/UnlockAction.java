/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

class UnlockAction implements Action
{
  public String getCommandName()
  {
    return "unlock";
  }

  public String getUsage()
  {
    return "unlock";
  }

  public String getDescription()
  {
    return "Sends an unlock info packet to the host";
  }

  public void doCommand(String[] args)
  {
    System.out.println("UNLOCK!");
  }
}

