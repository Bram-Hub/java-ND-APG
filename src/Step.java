import java.util.*;

public abstract class Step
{
  abstract int length();
  abstract boolean isValid();
  abstract String html();
  abstract String printStep();
  public boolean isLine()
  {
    return false;
  }
  public boolean isProof()
  {
    return false;
  }
  public Step prev()
  {
    try
    {
      return (Step) parent.argument.get((parent.argument.indexOf(this)-1));
    }
    catch(Exception e)
    {
      return null;
    }
  }
  public int stepNumber()
  {
    if(parent==null)
      return 1;
    else
    {
      int len=parent.stepNumber();
      LinkedList all=((Proof) parent).allSteps();
      for(int i=0;!equals(all.get(i));i++) // up to this step
        len+=((Step) all.get(i)).length(); // add up lengths
      return len;
    }
  }
  // all steps accessible from here
  public Steps getContext()
  {
    Steps context=new Steps();
    for(int i=parent.argument.indexOf(this)-1;i>=0;i--)
      context.add(parent.argument.get(i));
    context.addAll(parent.premises);
    if(parent.parent!=null)
      context.addAll(parent.getContext());
    return context;
  }
  // all steps that can access this one
  abstract Steps getBelow();

  static Parser p=new Parser();
  Proof parent;
}