/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import java.util.ArrayList;
import java.util.List;

class PresenceData
{
  private ArrayList<PresenceEvent> events;

  PresenceData()
  {
    events = new ArrayList<PresenceEvent>();
  }

  void dayOfYearChanged()
  {
    events.clear();
  }

  void lockEvent(long timestamp)
  {
    if (events.size() != 0)
    {
      PresenceEvent e = events.get(events.size() - 1);

      if (!e.isLockSet())
      {
        e.lock = timestamp;
      }
      else
      {
        // Already have a lock timestamp
      }
    }
    else
    {
      events.add(new PresenceEvent(0, timestamp));
    }
  }

  void unlockEvent(long timestamp)
  {
    if (events.size() == 0)
    {
      events.add(new PresenceEvent(timestamp));
    }
    else
    {
      PresenceEvent e = events.get(events.size() - 1);

      if (e.isLockSet())
      {
        events.add(new PresenceEvent(timestamp));
      }
      else
      {
        // Already have an unlock timestamp
      }
    }
  }

  List<PresenceEvent> getPresenceEvents()
  {
    return events;
  }
}

