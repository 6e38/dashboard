/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

import com.floorsix.json.JsonObject;
import java.util.Date;

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
    Date date = new Date();

    JsonObject object = new JsonObject(null);
    object.set("type", "lock");
    object.set("timestamp", date.getTime());

    Client client = new Client();
    client.sendPacket(object.toString());
  }
}

