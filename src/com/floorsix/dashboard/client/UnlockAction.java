
package dashboard.client;

public class UnlockAction implements Action
{
  public UnlockAction() {}

  public String getCommandName()
  {
    return "unlock";
  }

  public void doCommand()
  {
    System.out.println("UNLOCK!");
  }
}

