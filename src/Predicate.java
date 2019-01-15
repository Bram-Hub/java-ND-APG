import java.util.*;

public class Predicate extends Sentence
{
  public Predicate(Object o)
  {
    setSymbol((String) o);
    args=new Vector();
  }
  public boolean isPredicate()
  {
    return true;
  }
  public String toString()
  {
    String out=symbol+"(";
    for(int i=0;i<args.size()-1;i++)
      out+=args.get(i)+",";
    out+=args.lastElement()+")";
    return out;
  }
  public boolean add(String t)
  {
    return args.add(t); // could use "thing" objects instead
  }
  public boolean equals(Object o)
  {
    return
      sameClass(o) &&
      ((Predicate) o).symbol.equals(symbol) &&
      ((Predicate) o).args.equals(args);
  }
  Vector args;
}