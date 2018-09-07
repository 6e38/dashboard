
package dashboard;

class PaletteFactory
{
  private static final Palette working = new Palette(0x00ff00, 0xbb0000, 0x000000);
  private static final Palette afterWork = new Palette(0xdd0000, 0xbb0000, 0x330000);
  private static final Palette beforeWork = new Palette(0x0000dd, 0x0000bb, 0x000033);
  private static final Palette weekend = new Palette(0x555555, 0x555555, 0x000000);
  private static final Palette nighttime = new Palette(0x555555, 0x555555, 0x000000);
  private static final Palette morning = new Palette(0x555555, 0x555555, 0x000000);

  private static final Palette[] palettes = {
    morning,
    beforeWork,
    working,
    afterWork,
    nighttime,
    weekend,
  };

  static int index = 0;

  static Palette get()
  {
    return palettes[index];
  }

  static Palette getNext()
  {
    index = (index + 1) % palettes.length;
    return palettes[index];
  }

  static Palette get(Data.State state)
  {
    Palette p = null;

    switch (state)
    {
      case WorkingHours:
        p = working;
        break;

      case AfterWork:
        p = afterWork;
        break;

      case BeforeWork:
        p = beforeWork;
        break;

      default:
      case Weekend:
        p = weekend;
        break;

      case Nighttime:
        p = nighttime;
        break;

      case Morning:
        p = morning;
        break;
    }

    updateIndex(p);

    return p;
  }

  private static void updateIndex(Palette p)
  {
    index = 0;

    for (int i = 0; i < palettes.length; ++i)
    {
      if (p == palettes[i])
      {
        index = i;
        break;
      }
    }
  }
}

