
package dashboard.client;

public class LockAction implements Action
{
  public LockAction() {}

  public String getCommandName()
  {
    return "lock";
  }

  public void doCommand()
  {
    System.out.println("LOCKED!?");
  }
}

