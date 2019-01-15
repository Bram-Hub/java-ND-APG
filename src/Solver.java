import java.util.*;

public class Solver
{
  public Solver(){}
  public void solve(Proof p)
  {
    proof=p;
    for(Step cur=(Step) proof.argument.getLast();cur!=null;cur=cur.prev())
    {
      // System.out.println("Working with "+cur);
      if(!cur.isValid())
      {
        if(cur.isLine())
        {
          end=(Line) cur;
          context=cur.getContext();
          below=cur.getBelow();

          /**
           * work forwards
           */
          int tot;
          do
          {
            tot=0;
            for(int i=0;i<context.size();i++)
            {
              try
              {
                Line l=(Line) context.get(i);
                Operator o=(Operator) l.what;
                // decompose conjunctions
                if(o.symbol.equals("&"))
                {
                  for(int j=0;j<o.args.size();j++)
                  {
                    Sentence conjunct=(Sentence) o.args.get(j);
                    Line c=(Line) proof.addAfter(l,conjunct);
                    if(c.why.equals(""))
                    {
                      c.setRule("& elim");
                      c.addSupport(l);
                      tot++;
                    }
                  }
                }
                // decompose conditionals
                else if(o.symbol.equals("$"))
                {
                  Line prem=context.find(o.getArg(0));
                  if(prem!=null)
                  {
                    Line bottom=((prem.stepNumber()>l.stepNumber())?prem:l);
                    Line c=(Line) proof.addAfter(bottom,o.getArg(1));
                    if(c.why.equals(""))
                    {
                      c.setRule("$ elim");
                      c.addSupport(l);
                      c.addSupport(prem);
                      tot++;
                    }
                  }
                }
                // decompose biconditionals
                else if(o.symbol.equals("%"))
                {
                  boolean first=context.contains(o.getArg(0));
                  Line prem=context.find(o.getArg(first?0:1));
                  if(prem!=null)
                  {
                    Line bottom=((prem.stepNumber()>l.stepNumber())?prem:l);
                    Line c=(Line) proof.addAfter(bottom,o.getArg(first?1:0));
                    if(c.why.equals(""))
                    {
                      c.setRule("% elim");
                      c.addSupport(l);
                      c.addSupport(prem);
                      tot++;
                    }
                  }
                }
              }
              catch(Exception e){}
            }
            context=cur.getContext(); // update
            below=cur.getBelow(); // update
          } while(tot!=0); // so long as we're still decomposing

          /**
           * work backwards
           */

        }
        else if(cur.isProof())
        {
          // add code for recursion here
          // call new Solver(this)
          // and have the sub-solver set its "parent"
        }
      }
    }
  }

  Proof proof;
  Steps context;
  Steps below;
  Line end;
}