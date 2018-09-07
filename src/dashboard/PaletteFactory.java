
package dashboard;

class PaletteFactory
{
  static final Palette working = new Palette(0x00ff00, 0xbb0000, 0x000000);
  static final Palette afterWork = new Palette(0xdd0000, 0xbb0000, 0x330000);
  static final Palette beforeWork = new Palette(0x0000dd, 0x0000bb, 0x000033);
  static final Palette weekend = new Palette(0x555555, 0x555555, 0x000000);
  static final Palette nighttime = new Palette(0x555555, 0x555555, 0x000000);
  static final Palette morning = new Palette(0x555555, 0x555555, 0x000000);

  static final Palette[] palettes = {
    morning,
    beforeWork,
    working,
    afterWork,
    nighttime,
    weekend,
  };

  static int index;

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
    switch (state)
    {
      case WorkingHours:
        return working;

      case AfterWork:
        return afterWork;

      case BeforeWork:
        return beforeWork;

      default:
      case Weekend:
        return weekend;

      case Nighttime:
        return nighttime;

      case Morning:
        return morning;
    }
  }
}

