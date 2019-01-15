import java.util.*;
import java.util.regex.*;

/**
 * Future additions:
 *  Support for = operand?
 */

public class Parser
{
  // stuff for regexes
  static String name="[\\p{Alpha}^]+";
  static String infix="[&|$%]";
  static String prefix="[~]";
  static String litOrPrefix="^("+prefix+"+|"+name+"+)(.*)$";
  static Pattern isLitOrInfix=Pattern.compile("^("+name+"|"+infix+")(.*)$");
  static Pattern isPrefix=Pattern.compile("^("+prefix+")(.*)$");
  static Pattern isPredicate=Pattern.compile("^("+name+")\\((.+?)\\)(.*)$");

  public Parser(){}
  public Sentence parse(Object o)
  {
    String in=simplify(o);
    Vector v=getGroups(in);

    if(v.size()==1)
    {
      Matcher pr=isPrefix.matcher((String) v.get(0));
      Matcher pd=isPredicate.matcher((String) v.get(0));
      if(pr.matches()) // prefix
      {
        Operator op=new Operator(pr.group(1));
        op.add(parse(pr.group(2)));
        return op;
      }
      else if(pd.matches()) // Predicate
      {
        Predicate p=new Predicate(pd.group(1));
        String[] args=pd.group(2).split(",");
        for(int i=0;i<args.length;i++)
          p.add(args[i]);
        return p;
      }
      else // Literal
        return new Literal(v.get(0));
    }
    else if(v.size()>=3) // infix
    {
      Operator op=new Operator(v.get(1));
      for(int i=0;i<=v.size();i+=2) // breaks up on "a|b&c" etc.
        op.add(parse(v.get(i)));
      return op;
    }
    else
    {
      System.out.println("Cannot parse '"+in+"'");
      return new Literal("");
    }
  }
  private String simplify(Object o)
  {
    String in=(String) o;
    Vector match=getGroups(in);
    String f=(String) match.firstElement();
    if(match.size()==1)
      if(f.matches(litOrPrefix))
        return f;
      else
        return simplify(f);
    else
      return in;
  }
  private Vector getGroups(Object o)
  {
    Vector v=new Vector();
    Vector cur=getFirstGroup(o);
    if(cur.size()==1) // base case
      v.add(cur.get(0));
    else
    {
      v.add(cur.get(0));
      v.addAll(getGroups(cur.get(1)));
    }
    return v;
  }
  private Vector getFirstGroup(Object o)
  {
    String in=((String) o).trim();
    Vector v=new Vector();
    Matcher li=isLitOrInfix.matcher(in);
    Matcher pr=isPrefix.matcher(in);
    Matcher pd=isPredicate.matcher(in);
    if(in.startsWith("(")) // parentheses
    {
      int i;
      int c=0;
      for(i=0;i<in.length();i++)
      {
        String cur=String.valueOf(in.charAt(i));
        if(cur.equals("(")) c++;
        if(cur.equals(")")) c--;
        if(c==0) break;
      }
      v.add(in.substring(1,i));
      v.add(in.substring(i+1));
    }
    else if(pr.find()) // prefix
    {
      Vector cur=getFirstGroup(pr.group(2));
      v.add(pr.group(1)+"("+cur.get(0)+")");
      if(cur.size()>1) v.add(cur.get(1));
    }
    else if(pd.find()) // Predicate
    {
      v.add(pd.group(1)+"("+pd.group(2)+")");
      if(pd.groupCount()==3) v.add(pd.group(3));
    }
    else if(li.find()) // infix Operator or Literal
    {
      v.add(li.group(1));
      v.add(li.group(2));
    }
    else
    {
      System.out.println("No first group: "+in);
    }
    v.remove("");
    return v;
  }
}