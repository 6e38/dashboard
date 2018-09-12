/*
 * Copyright (c) 2018 Nathan Jenne
 */

package com.floorsix.dashboard;

import com.floorsix.dashboard.PresenceEvent.Type;
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
    events.add(new PresenceEvent(Type.Lock, timestamp));
  }

  void unlockEvent(long timestamp)
  {
    events.add(new PresenceEvent(Type.Unlock, timestamp));
  }

  List<PresenceEvent> getPresenceEvents()
  {
    return events;
  }
}

