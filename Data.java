import java.util.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;

/**
 * Program to determine shot distributions and effective field goal percentages for each zone given x and y coordinates
 * Hit run method to get results
 * Results should be printed on a terminal
 * @author Lu
 * @version 1
 */
public class Data
{
    private final double CORNER_Y=7.8;
    private final double CORNER3_DISTANCE_X=22;
    private final double NON_CORNER3_DIST=23.75;
    private String[][]data;
    // instance variables - replace the example below with your own

    /**
     * Methods to find the distance to determine whether or not it's a three pointer taking x and y coordinates
     */
    public double distance(double x, double y)
    {
        double ans=0.0;
        double inside=Math.abs(x*x+y*y);
        ans=Math.sqrt(inside);
        return ans;
    }

    /**
     * Method to determine whether or not it's a corner three
     * if y coordinates is indicating it's at the corner, check the x value of the coordinate
     */
    public boolean isCorner3(double x, double y)
    {
        if(Math.abs(y)<=CORNER_Y)
        {
            if(Math.abs(x)>=CORNER3_DISTANCE_X)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to determine whether or not it is a non-corner three
     * if indicates it is a corner three, return false
     * find the distance to the rim
     * if distance is larger than non-corner three distance, return true
     */
    public boolean isNC3(double x, double y)
    {
        if(isCorner3(x,y))
        {
            return false;
        }
        else
        {
            double d=distance(x, y);
            if(d>=NON_CORNER3_DIST)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to calculate the effective field goal percentage
     * take in parameters for field goal made, three point made, and field goal attempts
     * use the formula given and return the anser
     */
    public double eFG(int FG_Made, int threePM, int FGA)
    {
        double ans=0.0;
        ans=(FG_Made+0.5+threePM)/FGA;
        ans=ans*100;
        return ans;
    }

    /**
     * Method to read the csv file
     * take into each line
     * make a list of string arrays that containes each line
     * split each element into a new string by finding comma
     * convert the arraylist into a 2D array to traversal later
     */
    public void read() throws java.io.IOException 
    {
        try
        {
            String fName="shots_data.csv";
            String thisLine; 
            int count=0; 
            FileInputStream fis = new FileInputStream(fName);
            DataInputStream myInput = new DataInputStream(fis);
            //int i=0;
            List<String[]> lines = new ArrayList<String[]>();
            while ((thisLine = myInput.readLine()) != null) {
                lines.add(thisLine.split(","));
            }

            // convert our list to a String array.
            data = new String[lines.size()][0];
            lines.toArray(data);
        }
        catch(FileNotFoundException e1)
        {

        }
        // put your code here

    }

    /**
     * the run method that runs the program
     * create two hashmaps contains the distributions with strings indicating each value
     * read the csv file
     * loop through team A and team B data
     * Count the attempts and field goal mades for each zones
     * Calculate distributions and effective field goal percentages for each zone
     * Print out the results
     */
    public void run()
    {
        HashMap<String, Double> distributionA=new HashMap();//2PT, NC3, C3
        HashMap<String, Double> distributionB=new HashMap();
        int aMake2=0;
        int aMake3C=0;
        int aMake3=0;
        int bMake2=0;
        int bMake3C=0;
        int bMake3=0;
        int attempA2=0;
        int attempA3C=0;
        int attempA3=0;
        int attempB2=0;
        int attempB3C=0;
        int attempB3=0;
        int totalA=0;
        int totalB=0;

        try
        {
            read();
        }
        catch (java.io.IOException ioe)
        {
            ioe.printStackTrace();
        }
        for(int i=0; i<data.length; i++)
        {
            if(data[i][0].equals("Team A"))
            {
                double x=new Double(data[i][1]);
                double y=new Double(data[i][2]);
                int make=new Integer(data[i][3]);
                if(isCorner3(x, y))
                {
                    if(make==1)
                    {
                        aMake3C++;
                    }
                    attempA3C++;
                }
                else if(isNC3(x, y))
                {
                    if(make==1)
                    {
                        aMake3++;
                    }
                    attempA3++;
                }
                else
                {
                    if(make==1)
                    {
                        aMake2++;
                    }
                    attempA2++;
                }
            }
            else if(data[i][0].equals("Team B"))
            {
                for(int j=1; j<data[i].length; j++)
                {
                    double x=new Double(data[i][1]);
                    double y=new Double(data[i][2]);
                    int make=new Integer(data[i][3]);
                    if(isCorner3(x, y))
                    {
                        if(make==1)
                        {
                            bMake3C++;
                        }
                        attempB3C++;
                    }
                    else if(isNC3(x, y))
                    {
                        if(make==1)
                        {
                            bMake3++;
                        }
                        attempB3++;
                    }
                    else
                    {
                        if(make==1)
                        {
                            bMake2++;
                        }
                        attempB2++;
                    }
                }
            }
        }
        totalA=attempA2+attempA3C+attempA3;

        double distA2=((double)attempA2/totalA)*100;
        double distA3C=((double)attempA3/totalA)*100;
        double distA3=((double)attempA3C/totalA)*100;
        distributionA=makeHash("2 Pointer", "Non-corner 3", "Corner 3", distA2, distA3, distA3C);
        totalB=attempB2+attempB3C+attempB3;
        double distB2=((double)attempB2/totalB)*100;
        double distB3C=((double)attempB3/totalB)*100;
        double distB3=((double)attempB3C/totalB)*100;
        distributionB=makeHash("2 Pointer", "Non-corner 3", "Corner 3", distB2, distB3, distB3C);
        int AFG_Made=aMake2+aMake3C+aMake3;
        int A3_Made=aMake3+aMake3C;
        double A_EFG2=eFG(aMake2, 0, attempA2);
        double A_EFG3=eFG(aMake3, aMake3, attempA3);
        double A_EFG3C=eFG(aMake3C, aMake3C, attempA3C);
        double B_EFG2=eFG(bMake2, 0, attempB2);
        double B_EFG3=eFG(bMake3, bMake3, attempB3);
        double B_EFG3C=eFG(bMake3C, bMake3C, attempB3C);
        double eFGA=eFG(AFG_Made, A3_Made, totalA);
        int BFG_Made=bMake2+bMake3C+bMake3;
        int B3_Made=bMake3+bMake3C;
        double eFGB=eFG(BFG_Made, B3_Made, totalB);
        print(distributionA, distributionB, eFGA, eFGB, A_EFG2, A_EFG3, A_EFG3C, B_EFG2, B_EFG3, B_EFG3C);
    }

    /**
     * Print method to print out results
     * Take in the 2 hashmaps and the percentages values
     * Print out the deliverables
     */
    public void print(HashMap<String, Double> h1, HashMap<String, Double> h2, double eFGA, double eFGB,double A_EFG2,double A_EFG3,double A_EFG3C,double B_EFG2,double B_EFG3,double B_EFG3C)
    {
        printHash(h1, "Team A");
        printHash(h2, "Team B");
        System.out.println();
        System.out.println("Team A's Effective Field Goal Percentage is: "+eFGA+"%");
        System.out.println("Team B's Effective Field Goal Percentage is: "+eFGB+"%");
        System.out.println();
        System.out.println("Team A's Effective Field Goal Percentage for 2 pointer is: "+A_EFG2+"%");
        System.out.println("Team A's Effective Field Goal Percentage for non-corner threes is: "+A_EFG3+"%");
        System.out.println("Team A's Effective Field Goal Percentage for corner threes is: "+A_EFG3C+"%");
        System.out.println();
        System.out.println("Team B's Effective Field Goal Percentage for 2 pointer is: "+B_EFG2+"%");
        System.out.println("Team B's Effective Field Goal Percentage for non-corner threes is: "+B_EFG3+"%");
        System.out.println("Team B's Effective Field Goal Percentage for corner threes is: "+B_EFG3C+"%");
    }

    /**
     * Method to iterate through hashmaps and print out each keys and values
     */
    public void printHash(HashMap<String, Double> h, String teamName)
    {
        Iterator iter=h.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry tmp=(Map.Entry)iter.next();
            double dist=(double)tmp.getValue();
            System.out.println(teamName+"'s "+tmp.getKey()+" distribution is: "+dist+"%");
        }
    }

    /**
     * Methods to create hashmaps to link the zones with distributions for data storage
     */
    public HashMap<String, Double> makeHash(String str1, String str2, String str3, Double d1, Double d2, Double d3)
    {
        HashMap<String, Double> h=new HashMap();
        h.put(str1, d1);
        h.put(str2, d2);
        h.put(str3, d3);
        return h;
    }
}
