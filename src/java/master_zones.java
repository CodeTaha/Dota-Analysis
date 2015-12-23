
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Taha
 */
public class master_zones {
    
    double t,tsync, match,tper,tstd;
    String player,tier,zone;
    int won,x,y, team;
    public master_zones(String args[],long lineNumber)
    {
        try{
        t=Double.parseDouble(args[0]);
        x=Integer.parseInt(args[1]);
        y=Integer.parseInt(args[2]);
        match=Double.parseDouble(args[3]);
        team=1;
        System.out.println("TEAM "+args[4]);
        if(args[4].equals("\"radiant\"")) {
            team=0;
        }
        System.out.println("TEAM "+args[4]+" "+team);
        player=args[5];
        won=Integer.parseInt(args[6]);
        tstd=Double.parseDouble(args[7]);
        tsync=Double.parseDouble(args[8]);
        tper=Double.parseDouble(args[9]);
        tier=args[10];
        zone=args[11];
        }
        catch(Exception e)
        {
            String test[]=args;
            System.out.println(Arrays.toString(test));
            System.out.println("Exception in masterzone linenumber="+lineNumber+" Exception="+e);
        }
    }
    
}