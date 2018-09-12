/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

public class PresenceEvent
{
  public enum Type {
    Lock,
    Unlock,
    Logon,
    Logoff,
  };

  public Type type;
  public long timestamp;

  public PresenceEvent(Type type, long timestamp)
  {
    this.type = type;
    this.timestamp = timestamp;
  }
}

