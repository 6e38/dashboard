/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

public class PresenceEvent
{
  public long unlock;
  public long lock;

  public PresenceEvent(long unlock, long lock)
  {
    this.unlock = unlock;
    this.lock = lock;
  }

  public PresenceEvent(long unlock)
  {
    this.unlock = unlock;
    this.lock = 0;
  }

  public boolean isLockSet()
  {
    return lock != 0;
  }

  public boolean isUnlockSet()
  {
    return unlock != 0;
  }
}

