public class Literal extends Sentence
{
  public Literal(Object o)
  {
    setSymbol((String) o);
  }
  public boolean isLiteral()
  {
    return true;
  }
  public String toString()
  {
    return symbol;
  }
  public boolean equals(Object o)
  {
    return sameClass(o) && symbol.equals(((Literal) o).symbol);
  }
}