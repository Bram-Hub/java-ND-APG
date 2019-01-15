import java.util.*;
import java.util.regex.*;

public class Proof extends Step
{
  public Proof()
  {
    premises=new LinkedList();
    argument=new LinkedList();
  }

  public boolean isProof()
  {
    return true;
  }
  // checks if the proof is valid
  public boolean isValid()
  {
    for(int i=0;i<argument.size();i++)
      if(!((Step) argument.get(i)).isValid())
        return false;
    return true;
  }
  public boolean hasPremise(Step s)
  {
    return premises.contains(s);
  }
  public boolean hasArgument(Step s)
  {
    return argument.contains(s);
  }

  public Steps getBelow()
  {
    Steps below=new Steps();
    for(int i=0;i<argument.size();i++)
    {
      Step cur=(Step) argument.get(i);
      if(cur.isLine())
        below.add(cur);
      else if(cur.isProof())
        below.addAll(cur.getBelow());
    }
    return below;
  }

  public Step addPremise(Step s)
  {
    s.parent=this;
    premises.add(s);
    return s;
  }
  public Step addArgument(Step s)
  {
    s.parent=this;
    argument.add(s);
    return s;
  }
  public Step addAfter(Step s1,Step s2)
  {
    if(argument.contains(s2))
      return (Step) argument.get(argument.indexOf(s2));
    else
    {
      int index=argument.indexOf(s1);
      argument.add(index+1,s2);
      s2.parent=this;
      return s2;
    }
  }
  public Step addAfter(Step s1, Sentence s2)
  {
    return addAfter(s1,new Line(s2));
  }
  public Step addPremise(String s)
  {
    return addPremise(new Line(s));
  }
  public Step addArgument(String s)
  {
    return addArgument(new Line(s));
  }

  public int length()
  {
    int len=0;
    LinkedList all=allSteps();
    for(int i=0;i<all.size();i++)
      len+=((Step) all.get(i)).length();
    return len;
  }
  public LinkedList allSteps()
  {
    LinkedList all=new LinkedList(premises);
    all.addAll(argument);
    return all;
  }
  public Line getPremise()
  { // gets first premise (for subproofs)
    return (Line) premises.getFirst();
  }
  public Sentence getPremiseSentence()
  {
    return (Sentence) getPremise().what;
  }
  public Step getConclusion()
  {
    return (Step) allSteps().getLast();
  }
  public Sentence getConclusionSentence()
  {
    return (Sentence) ((Line) getConclusion()).what;
  }
  public String printStep()
  {
    return  Integer.toString(stepNumber())+"-"+
            Integer.toString(lastStepNumber());
  }
  public int lastStepNumber()
  {
    return (getConclusion().isProof())?
            ((Proof) getConclusion()).lastStepNumber():
            getConclusion().stepNumber();
  }
  public String toString()
  {
    String out="|";
    for(int i=0;i<premises.size();i++)
      out+=((Line) premises.get(i)).toString()+"\n";
    out+="---\n";
    for(int i=0;i<argument.size();i++)
      out+=((Step) argument.get(i)).toString()+"\n";
    out=Pattern.compile("\n$").matcher(out).replaceAll(""); // chomp
    out=Pattern.compile("\\n").matcher(out).replaceAll("\n|"); // add proof lines
    return out;
  }
  public String html()
  {
    String out="";
    for(int i=0;i<premises.size();i++)
      out+=((Line) premises.get(i)).html();
    out+="<hr align='left'>\n";
    for(int i=0;i<argument.size();i++)
      out+=((Step) argument.get(i)).html();
    out=Pattern.compile("(^|\\n)(?!$)").matcher(out).replaceAll("$1\t");
    out="<ol>\n"+out+"</ol>\n";
    return out;
  }

  LinkedList premises;
  LinkedList argument;
}