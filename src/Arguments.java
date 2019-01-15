import java.util.*;

// a vector that can be accessed
// as an unordered set, if need be
public class Arguments extends Vector
{
  public boolean equalsUnordered(Arguments a)
  {
    Set ua1=new TreeSet(this);
    Set ua2=new TreeSet(a);
    return ua1.equals(ua2);
  }
}