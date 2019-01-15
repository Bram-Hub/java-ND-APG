import java.io.*;
import java.util.*;

// example of proof/solver functionality
public class FitchATP
{
  public static void main(String[] args)
  {
    Proof main=new Proof();

    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Enter the premises, followed by a blank line.");
    while(true)
    {
      try
      {
        System.out.print("> ");
        String in=stdin.readLine();
        if(in.equals("")) break;
        main.addPremise(in);
      }
      catch(IOException e)
      {
        System.err.println("Error reading input.");
      }
    }

    System.out.println("Enter the conclusion.");
    try
    {
      System.out.print("> ");
      main.addArgument(stdin.readLine());
    }
    catch(IOException e)
    {
      System.err.println("Error reading input.");
    }

    Solver s=new Solver();
    s.solve(main);

    System.out.println(main);
    System.out.println("(proof was "+(main.isValid()?"":"not ")+"completed)");
  }
}