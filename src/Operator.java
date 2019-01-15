import java.util.*;
import java.util.regex.*;

public class Operator extends Sentence
{
  public Operator(Object o)
  {
    setSymbol((String) o);
    args=new Arguments();
  }
  public boolean isOperator()
  {
    return true;
  }
  public boolean add(Sentence in)
  {
    return args.add(in);
  }
  public String toString() // essentially join()
  {
    if(symbol.equals("~")) // prefix
      return "~"+args.firstElement();
    else
    {
      String out="";
      Iterator iter=args.iterator();
      for(int i=0;i<args.size()-1;i++)
        out+=iter.next()+symbol;
      return "("+out+iter.next()+")";
    }
  }
  public boolean equals(Object o)
  {
    if(sameClass(o))
    {
      Operator ino=(Operator) o;
      if(symbol.equals(ino.symbol))
      {
        if(symbol.equals("$"))
          return args.equals(ino.args); // compare conditionals
        else
          return args.equalsUnordered(ino.args); // compare other ops
      }
      else
        return false; // different Operators
    }
    else
    {
      if(args.size()==1)
      {
        return getArg(0).equals(o); // just use first object
      }
      else
        return false; // totally different objects
    }
  }
  public boolean has(Sentence o)
  {
    for(int i=0;i<args.size();i++)
      if(getArg(i).equals(o))
        return true;
    return false;
  }
  public Sentence getArg(int i)
  {
    return (Sentence) args.get(i);
  }
  Arguments args;
}