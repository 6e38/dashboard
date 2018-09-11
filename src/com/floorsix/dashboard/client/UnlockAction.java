/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard.client;

import com.floorsix.json.JsonObject;
import java.util.Date;

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
    Date date = new Date();

    JsonObject object = new JsonObject(null);
    object.set("type", "unlock");
    object.set("timestamp", date.getTime());

    Client client = new Client();
    client.sendPacket(object.toString());
  }
}

