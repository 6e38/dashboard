/*
 * Copyright (c) 2018 Nathan Jenne
 */

package dashboard;

import java.awt.Color;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class Data
{
  private Calendar calendar;
  private Calendar fivepm;
  private Calendar eightam;
  DateFormatSymbols dfs;

  private int dayOfYear;

  private String dateString;
  private String timeString;
  private String ampmString;
  private String remainingString;

  public Data()
  {
    dayOfYear = -1;

    dfs = new DateFormatSymbols();
  }

  public void update()
  {
    calendar = Calendar.getInstance();

    updateDayOfYear();

    dateString = String.format("%s, %s %d, %d",
        dfs.getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)],
        dfs.getMonths()[calendar.get(Calendar.MONTH)],
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.YEAR));

    int hour = calendar.get(Calendar.HOUR);
    if (hour == 0)
    {
      hour = 12;
    }
    timeString = String.format("%d:%02d", hour, calendar.get(Calendar.MINUTE));

    ampmString = dfs.getAmPmStrings()[calendar.get(Calendar.AM_PM)];

    calculateRemaining();
  }

  public String getDateString()
  {
    return dateString;
  }

  public String getTimeString()
  {
    return timeString;
  }

  public String getAmPmString()
  {
    return ampmString;
  }

  public String getRemainingString()
  {
    return remainingString;
  }

  public boolean isWorkingHours()
  {
    return !isWeekend()
        && calendar.after(eightam)
        && calendar.before(fivepm);
  }

  public boolean isWeekend()
  {
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
  }

  public boolean isAfterHours()
  {
    return !isWeekend() && calendar.after(fivepm);
  }

  public boolean isEarly()
  {
    return !isWeekend() && calendar.before(eightam);
  }

  private void updateDayOfYear()
  {
    if (calendar.get(Calendar.DAY_OF_YEAR) != dayOfYear)
    {
      fivepm = (Calendar)calendar.clone();
      fivepm.set(Calendar.HOUR_OF_DAY, 17);
      fivepm.set(Calendar.MINUTE, 0);
      fivepm.set(Calendar.SECOND, 0);
      fivepm.set(Calendar.MILLISECOND, 0);

      eightam = (Calendar)calendar.clone();
      eightam.set(Calendar.HOUR_OF_DAY, 8);
      eightam.set(Calendar.MINUTE, 0);
      eightam.set(Calendar.SECOND, 0);
      eightam.set(Calendar.MILLISECOND, 0);
    }
  }

  private void calculateRemaining()
  {
    if (isWorkingHours())
    {
      long ms = fivepm.getTime().getTime() - calendar.getTime().getTime();
      int hours = (int)(ms / 1000 / 60 / 60);
      int mins = (int)(ms / 1000 / 60) % 60;

      remainingString = String.format("%d:%02d to go", hours, mins);
    }
  }
}

