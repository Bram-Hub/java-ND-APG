import java.util.*;

public abstract class Sentence implements Comparable
{
  public int compareTo(Object o)
  {
    return this.toString().compareTo(o.toString());
  }
  public boolean sameClass(Object o)
  {
    return getClass().equals(o.getClass());
  }
  public void setSymbol(String in)
  {
    symbol=in.trim();
  }
  public boolean isOperator()
  {
    return false;
  }
  public boolean isLiteral()
  {
    return false;
  }
  public boolean isPredicate()
  {
    return false;
  }
  String symbol;
}