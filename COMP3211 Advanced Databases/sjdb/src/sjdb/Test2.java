package sjdb;

import java.io.*;
import java.util.ArrayList;
import sjdb.DatabaseException;

public class Test2 {
    private Catalogue catalogue;

    public Test2() {
    }

    public static void main(String[] args) throws Exception {
        Catalogue catalogue = createCatalogue();
        Inspector inspector = new Inspector();
        Estimator estimator = new Estimator();

        Operator plan = query(catalogue);
        plan.accept(estimator);
        plan.accept(inspector);

        Optimiser2 optimiser = new Optimiser2(catalogue);
        Operator planopt = optimiser.optimise(plan);
        planopt.accept(estimator);
        planopt.accept(inspector);
    }

    public static Catalogue createCatalogue() {
        Catalogue cat = new Catalogue();
        cat.createRelation("A", 100);
        cat.createAttribute("A", "a1", 100);
        cat.createAttribute("A", "a2", 15);
        cat.createRelation("B", 150);
        cat.createAttribute("B", "b1", 150);
        cat.createAttribute("B", "b2", 100);
        cat.createAttribute("B", "b3", 5);
        cat.createRelation("C", 200);
        cat.createAttribute("C", "c1", 50);
        cat.createAttribute("C", "c2", 25);

        return cat;
    }

    public static Operator query(Catalogue cat) throws Exception {
        Scan a = new Scan(cat.getRelation("A"));
        Scan b = new Scan(cat.getRelation("B"));
        Scan c = new Scan(cat.getRelation("C"));

        // Query 1
        Product p1 = new Product(b, c);
        Product p2 = new Product(a, p1);
        Select s1 = new Select(p2, new Predicate(new Attribute("a1"), "value"));
        Select s2 = new Select(s1, new Predicate(new Attribute("a2"), new Attribute("b2")));

        ArrayList<Attribute> atts = new ArrayList<>();
        atts.add(new Attribute("a1"));
        atts.add(new Attribute("b1"));

        Project plan = new Project(s2, atts);

        return plan;
    }

}

