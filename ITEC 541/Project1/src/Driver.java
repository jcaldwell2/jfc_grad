/**
 * @Author James Caldwell
 * @Version 11/09/2016
 * ITEC 541 Java Algebra Project
 *
 * Driver demonstrates Restrict, Project, = > < >= <= != operations, Join, and Display to show a table
 * Join operation takes 2 tables, but the smaller table must have the joining column listed first, but the code
 * can be modified to find the join column but that will impede performance. Can be anywhere in the larger join table
 */

public class Driver {


    public static void main(String args[]) {


        //load the cars table into memory

        ttable cars = new ttable("cars.txt");

        //restrict the cars ttable to toyotas producing a ttable named toyotas

        ttable toyotas = Algebra.Restrict(cars, "MAKE='Toyota'");

        //project just three columns from the toyotas ttable producing a ttable named answer

        ttable answer = Algebra.Project(toyotas, "Make,Model,Price");

        //show answer table

        Algebra.Display(answer);

        //restrict cars ttable to Price equals 18,000

        ttable price = Algebra.Restrict(cars, "PRICE=18000");

        //show  price table

        Algebra.Display(price);

        //restrict cars ttable to Price greater than 18,000

        ttable price2 = Algebra.Restrict(cars, "PRICE>18000");

        //display price2 ttable

        Algebra.Display(price2);

        //restrict cars ttable to Price less than 35,000

        ttable price3 = Algebra.Restrict(cars, "PRICE<35000");

        //restrict cars ttable to Make is equal to 'Ford'

        ttable cheapFords = Algebra.Restrict(price3, "MAKE='Ford'");

        //project the Model and Price of the cheapFords ttable

        ttable answer2 = Algebra.Project(cheapFords, "Model,Price");

        //display the answer2 ttable

        Algebra.Display(answer2);

        ttable lessThanEquals = Algebra.Restrict(cars, "PRICE<=35000");

        Algebra.Display(lessThanEquals);

        ttable greaterThanEquals = Algebra.Restrict(cars,"PRICE>=21000");

        Algebra.Display(greaterThanEquals);

        ttable notToyotas = Algebra.Restrict(cars, "MAKE!='Toyota'");

        Algebra.Display(notToyotas);

        ttable manufacturer = new ttable("manufacturer.txt");

        //join the cars table and the manufacturer table
        //the join column listed first, in this case, the tables are joined by the column Make, which can be located
        //anywhere in the larger table but must be first in the smaller table

        ttable firstJoin = Algebra.Join(cars, manufacturer);

        ttable USA = Algebra.Restrict(firstJoin,"'HQ=USA'");

        ttable answer3 = Algebra.Project(USA, "Make,Price,Model,HQ, CEO");

        Algebra.Display(answer3);

        ttable notFords = Algebra.Restrict(cars, "MAKE!='Ford'");

        Algebra.Display(notFords);


    }


}





