package me.dusanov.functional.stream.example;

import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.io.*;
import java.util.function.*;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.TreeSet;
import java.util.Map;
/**
 * Hello world!
 *
 */
public class App 
{

    private final static int FIELD_WIDTH = 20;
    private static JTextField staticTextField;

    public static void main( String[] args )
    {

        Employee mike = new Employee("Mike",2000),
                 louise = new Employee("Louise",2500);

        Comparator<Employee> byName = new Comparator<Employee>(){
            public int compare(Employee a, Employee b){
                return a.getName().compareTo(b.getName());
            }
        };

        // a static method in Comparator
        Comparator<Employee> byNameThenNull = Comparator.nullsLast(byName);
        // a default method in comparator
        Comparator<Employee> nullThenByDecreasingName = byNameThenNull.reversed();

        System.out.println( "Compare by name:" );
        System.out.println(byName.compare(mike,louise));
        System.out.println( "Compare by name then null:" );
        System.out.println(byNameThenNull.compare(mike,null));
        System.out.println( "Reversed:" );
        System.out.println(nullThenByDecreasingName.compare(mike,louise));
        System.out.println(nullThenByDecreasingName.compare(mike,null));

        // our first lambda expression
        Comparator<Employee> byNameLambda1 = 
            (Employee a, Employee b) -> {return a.getName().compareTo(b.getName());};

        // our first lambda expression minimized
        Comparator<Employee> byNameLambda2 = 
            (a, b) -> a.getName().compareTo(b.getName());
        
        Comparator<Employee> byNameLambdaNullable = Comparator.nullsLast(byNameLambda2);
        
        System.out.println( "lambdas:" );
        System.out.println(byNameLambda2.compare(mike,louise));
        System.out.println(byNameLambdaNullable.compare(mike,null));

        // Lambda Inference
        // Standard syntax
        Consumer<String> consumer1 = msg -> System.out.println(msg.length());

        // with the cast
        Object x1 = (Consumer<String>)((String msg) -> System.out.println(msg.length()));
        // omited input type
        Object x2 = (Consumer<String>)(msg -> System.out.println(msg.length()));
        // inferred parameter type is Object
        Consumer<?> consumer2 = msg -> System.out.println(msg);
        // added manifest type to parameter
        Consumer<?> consumer3 = (String msg) -> System.out.println(msg.length());
 

	//Functinal composition
	Function<Employee,String> getName = Employee::getName;
	Function<String,Character> getFirstLetter = name -> name.charAt(0);
	Function<Employee,Character> initial = getName.andThen(getFirstLetter);
	//Functional composition - comparing 
	Comparator<Employee> compareByName = Comparator.comparing(Employee::getName);
	Comparator<Employee> compareBySalary = Comparator.comparingInt(Employee::getSalary);
	Comparator<Employee> compareByNameAndSalary = compareByName.thenComparing(compareBySalary);

	//Streams - section 4, first lesson
	Employee[] employees = {mike, louise,
				new Employee("Djura",2300),
				new Employee("Pera",2700) };

	try (PrintWriter writer = new PrintWriter("somefile.txt");) {
		Consumer<String> logger = writer::println;
		Consumer<String> screener = System.out::println;
		Consumer<String> both = screener.andThen(logger);
		both.accept("Program started with initial: " + initial.apply(mike));
		
		//Monads section
		both.accept("monads section:");
		Stream<String> splitStringIntoStream = Stream.of("Hello monads");
		splitStringIntoStream.forEach(both::accept);
	
		both.accept("================");
		both.accept("====Section 5: transforming & rearanging=====");

		Stream<Employee> streamOfEmployees = Stream.of(employees);
		streamOfEmployees.sorted(
					Comparator.comparingInt(Employee::getSalary).reversed()
				).limit(3)
				 .map(Employee::getName)
				 .forEachOrdered(both::accept);

		both.accept("================");
		both.accept("===== Reduce & collect ======");
		//quadratic complexity - no good
		Stream<Employee> streamOfEmployees2 = Stream.of(employees);
		both.accept( streamOfEmployees2.map(Employee::getName)
					.reduce("",(a,b)->a + " " + b)
		);

		both.accept( "long version: " + 
			Stream.of(employees).map(Employee::getName).collect(
					()-> new StringBuilder(),
					(StringBuilder builder, String s)->builder.append(s),
					(StringBuilder builder1, StringBuilder builder2)->builder1.append(builder2)
			).toString()
		);
		both.accept( "medium version: " + 
			Stream.of(employees).map(Employee::getName).collect(
					StringBuilder::new,
					StringBuilder::append,
					StringBuilder::append
			).toString()
		);
		both.accept( "short version: " + 
			Stream.of(employees).map(Employee::getName)
				.collect(Collectors.joining(", "))
			.toString()
		);

		TreeSet<Employee> treeO = Stream.of(employees).collect(
			Collectors.toCollection(
				()-> new TreeSet<Employee>(Comparator.comparingInt(Employee::getSalary))
			)
		);
		both.accept(" tree of employees: " + treeO); 

		both.accept(" == Salary map: " + Stream.of(employees).collect(
			Collectors.toMap(Employee::getName, Employee::getSalary)));
		
		both.accept(" == Salary grouping by: " + Stream.of(employees).collect(
			Collectors.groupingBy(e->e.getSalary()/1250)));


		both.accept("================");
		Arrays.stream(employees).filter( e -> e.getSalary() >= 2500 )
					.map(Employee::getName)
					.sorted()
					.forEach(both::accept);

		//Generate each element of the stream separately
		final Random random = new Random();
		Stream<Integer> randomIntegers = Stream.generate(()->{
							Integer randInt = random.nextInt();
							both.accept(" generated: " + randInt.toString());
							return randInt;
						});

		both.accept("spit out random ints greater than 0: ");
		randomIntegers.filter(n->n>=0).limit(10).map(n->n.toString()).forEach(both::accept);	
		//Creating each element from previous one
		Stream<String> as = Stream.iterate("A", s -> s + "A").limit(10);
		as.forEach(both::accept);	
			

	} catch (FileNotFoundException fnfe){
		System.out.println("file not found ");
	}


        JFrame frame = new JFrame();
        staticTextField = new JTextField(FIELD_WIDTH);
        staticTextField.setText("Static Field");

        JTextField localTextField = new JTextField(FIELD_WIDTH);
        localTextField.setText("Local variable");

        JButton helloButton = new JButton("Say hello");
        // Regular anonymous class
        helloButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent event) {
            staticTextField.setText("Hello, world !");
            localTextField.setText("Hello, world !");
           }
        });

        JButton goodbyeButton = new JButton("Say goodbye");
        // lambda expression (block)
        goodbyeButton.addActionListener( event -> {
            staticTextField.setText("Good bye, world !");
            localTextField.setText("Good bye, world !");
        });

	//staticTextField = null;
	//localTextField = null;

	frame.getContentPane().setLayout(new FlowLayout());
	frame.getContentPane().add(staticTextField);
	frame.getContentPane().add(localTextField);
	frame.getContentPane().add(helloButton);
	frame.getContentPane().add(goodbyeButton);

	frame.setAlwaysOnTop(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);  

    }
}
