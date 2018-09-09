/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

class LockAction implements Action
{
  public String getCommandName()
  {
    return "lock";
  }

  public String getUsage()
  {
    return "lock";
  }

  public String getDescription()
  {
    return "Sends a lock info packet to the host";
  }

  public void doCommand(String[] args)
  {
    System.out.println("LOCKED!?");
  }
}

