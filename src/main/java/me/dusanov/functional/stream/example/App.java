package me.dusanov.functional.stream.example;

import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

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

//	staticTextField = null;
	//localTextField = null;


//	Container contentPane = frame.getContentPane();
	frame.getContentPane().setLayout(new FlowLayout());
	frame.getContentPane().add(staticTextField);
	frame.getContentPane().add(localTextField);
	frame.getContentPane().add(helloButton);
	frame.getContentPane().add(goodbyeButton);

	frame.setAlwaysOnTop(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);

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
   



    }
}
