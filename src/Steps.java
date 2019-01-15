import java.util.*;

// sometimes we need to pretend the
// individual steps are just sentences
// this class lets us access things tersely
public class Steps extends Vector
{
  public boolean contains(Sentence s)
  {
    for(int i=0;i<size();i++)
    {
      try
      {
        if(((Line) get(i)).what.equals(s))
          return true;
      }
      catch(ClassCastException e){}
    }
    return false;
  }
  public Line find(Sentence s)
  {
    for(int i=0;i<size();i++)
    {
      try
      {
        if(((Line) get(i)).what.equals(s))
          return (Line) get(i);
      }
      catch(ClassCastException e){}
    }
    return null;
  }
}