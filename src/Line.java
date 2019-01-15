import java.util.*;

public class Line extends Step
{
  public Line(Sentence s)
  {
    what=s;
    why="";
    support=new Steps();
  }
  public Line(String s)
  {
    what=p.parse(s);
    why="";
    support=new Steps();
  }
  public boolean isLine()
  {
    return true;
  }
  public boolean equals(Object o)
  {
    return what.equals(((Line) o).what);
  }
  public int length()
  {
    return 1;
  }
  public Steps getBelow()
  {
    Steps below=new Steps();
    for(int i=parent.argument.indexOf(this)+1;i<parent.argument.size();i++)
    {
      Step cur=(Step) parent.argument.get(i);
      if(cur.isLine())
        below.add(cur);
      else if(cur.isProof())
        below.add(cur.getBelow());
    }
    return below;
  }
  // When we just need the sentence.
  public Sentence getSupport(int i)
  {
    return (Sentence) ((Line) support.get(i)).what;
  }
  // find a support that matches the operand or literal
  public Sentence findSupport(String symbol)
    throws Exception
  {
    for(int i=0;i<support.size();i++)
    {
      Sentence cur=getSupport(i);
      if(cur.symbol.equals(symbol))
        return cur;
    }
    throw new Exception();
  }
  // find a support that matches the sentence
  public Sentence findSupport(Sentence sen)
    throws Exception
  {
    for(int i=0;i<support.size();i++)
    {
      Sentence cur=getSupport(i);
      if(cur.equals(sen))
        return cur;
    }
    throw new Exception();
  }
  public boolean hasSupport(Sentence s)
  {
    return support.contains(s);
  }
  public boolean isValid()
  {

    if(why==null)
      return false;
    else
    {
      // the following chunk of code will throw an error if the rule is invalid.
      try{
        if(why.equals("reit"))
        {
          if(support.size()!=1) return false;
          return what.equals(getSupport(0));
        }
        else if(why.equals("& elim"))
        {
          if(support.size()!=1) return false;
          Operator and=(Operator) getSupport(0); // get first support
          return and.has(what); // and check that this is somewhere in it
        }
        else if(why.equals("& intro"))
        {
          if(support.size()<1) return false;
          Operator and=(Operator) what;
          for(int i=0;i<and.args.size();i++) // with each conjunct
            if(!hasSupport(and.getArg(i))) // make sure it's supported
              return false;
          return true;
        }
        else if(why.equals("| intro"))
        {
          if(support.size()!=1) return false;
          Operator or=(Operator) what;
          return  or.symbol.equals("|")&&
                  or.has(getSupport(0));
        }
        else if(why.equals("| elim"))
        {
          if(support.size()<3) return false;
          Operator or=null;
          Vector subproofs=new Vector();
          Arguments premises=new Arguments();
          for(int i=0;i<support.size();i++)
            if(((Step) support.get(i)).isProof())
            {
              Proof cur=(Proof) support.get(i);
              premises.add(cur.getPremiseSentence());
              if(!cur.getConclusionSentence().equals(what))
                return false;
            }
            else
              if(or==null&&getSupport(i).symbol.equals("|"))
                or=(Operator) getSupport(i);
              else
                return false;
          return premises.equalsUnordered(((Operator) or).args);
        }
        else if(why.equals("$ elim"))
        {
          if(support.size()!=2) return false;
          Operator cond=(Operator) findSupport("$");
          Sentence prem=findSupport(cond.getArg(0));
          return cond.getArg(1).equals(what);
        }
        else if(why.equals("$ intro"))
        {
          if(support.size()!=1) return false;
          Proof subproof=(Proof) support.get(0);
          Operator cond=(Operator) what;
          return  cond.symbol.equals("$")&& // is a conditional
                  subproof.premises.size()==1&& // only one premise
                  cond.getArg(0).equals(subproof.getPremiseSentence())&&
                  cond.getArg(1).equals(subproof.getConclusionSentence());
        }
        else if(why.equals("% elim"))
        {
          if(support.size()!=2) return false;
          Operator bicond=null;
          Arguments landr=new Arguments();
          landr.add(what);
          for(int i=0;i<2;i++)
            if(getSupport(i).symbol.equals("%"))
              bicond=(Operator) getSupport(i);
            else
              landr.add(getSupport(i));
          return landr.equalsUnordered(bicond.args);
        }
        else if(why.equals("% intro"))
        {
          Operator bicond=(Operator) what; // this line
          Proof s1=(Proof) support.get(0); // support subproof 1
          Proof s2=(Proof) support.get(1); // support subproof 2
          Arguments premandconc=new Arguments();
          premandconc.add(s1.getPremiseSentence());
          premandconc.add(s1.getConclusionSentence());
          return  bicond.symbol.equals("%")&&
                  s1.getConclusionSentence().equals(s2.getPremiseSentence())&&
                  s2.getConclusionSentence().equals(s1.getPremiseSentence())&&
                  premandconc.equalsUnordered(bicond.args);
        }
        else if(why.equals("~ elim"))
        {
          if(support.size()!=1) return false;
          Operator not1=(Operator) getSupport(0);
          Operator not2=(Operator) not1.getArg(0);
          Sentence inner=not2.getArg(0);
          return  (not1.symbol.equals("~")) && // one not
                  (not2.symbol.equals("~")) && // two not
                  (inner.equals(what)); // matches inside
        }
        else if(why.equals("~ intro"))
        {
          if(support.size()!=1) return false;
          Proof subproof=(Proof) support.get(0);
          Sentence conc=subproof.getConclusionSentence();
          Sentence prem=subproof.getPremiseSentence();
          return  subproof.premises.size()==1&& // only one premise
                  conc.symbol.equals("^")&& // conclusion is contradiction
                  what.symbol.equals("~")&& // this is a negation
                  ((Operator) what).getArg(0).equals(prem); // inner claim is the premise
        }
        else if(why.equals("^ elim"))
        {
          if(support.size()!=1) return false;
          return getSupport(0).symbol.equals("^"); // matches inside
        }
        else if(why.equals("^ intro"))
        { // bug: this won't work if there's a double negation as the positive claim (second position)
          if(support.size()!=2) return false;
          Operator neg=(Operator) findSupport("~"); // find the negation
          Sentence pos=findSupport(neg.getArg(0)); // and the claim being negated
          return true; // if no exceptions, we're good to go
        }
        else
        {
          return false;
        }
      }
      catch(Exception e)
      {
        return false;
      }
    }
  }
  public void setRule(String s)
  {
    why=s;
  }
  public void addSupport(Step s)
  {
    if(knows(s))
      support.add(s);
  }
  public boolean isPremise()
  {
    return parent.hasPremise(this);
  }
  public boolean isArgument()
  {
    return !isPremise();
  }
  public boolean knows(Step s)
  {
    return getContext().contains(s);
  }
  public String printStep()
  {
    return Integer.toString(stepNumber());
  }
  public String toString()
  {
    String out=stepNumber()+" "+what.toString();
    if(isArgument()) out+=" ("+whyToString()+")";
    return out;
  }
  public String html()
  {
    return "<li>"+what+"</li>\n";
  }
  public String whyToString()
  {
    Vector steps=new Vector();
    for(int i=0;i<support.size();i++)
      steps.add(((Step) support.get(i)).printStep());
    return why+" "+steps+(isValid()?"":" X");
  }

  Sentence what; // sentence
  String why; // rule name, eg "& elim"
  Steps support; // support for rule
}