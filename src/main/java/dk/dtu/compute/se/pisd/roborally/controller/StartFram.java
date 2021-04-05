package dk.dtu.compute.se.pisd.roborally.controller;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import javax.swing.*;
//
//public class solve extends JPanel implements ActionListener
//{
//    private JComboBox<String> mainComboBox;
//    private JComboBox<String> subComboBox;
//    private Hashtable<String, String[]> subItems = new Hashtable<String, String[]>();
//
//    public solve()
//    {
//        String[] items = { "Select Item", "Color", "Shape", "Fruit" };
//        mainComboBox = new JComboBox<String>( items );
//        mainComboBox.addActionListener( this );
//
//        //  prevent action events from being fired when the up/down arrow keys are used
//        mainComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
//        add( mainComboBox );
//
//        //  Create sub combo box with multiple models
//
//        subComboBox = new JComboBox<String>();
//        subComboBox.setPrototypeDisplayValue("XXXXXXXXXX"); // JDK1.4
//        add( subComboBox );
//
//        String[] subItems1 = { "Select Color", "Red", "Blue", "Green" };
//        subItems.put(items[1], subItems1);
//
//        String[] subItems2 = { "Select Shape", "Circle", "Square", "Triangle" };
//        subItems.put(items[2], subItems2);
//
//        String[] subItems3 = { "Select Fruit", "Apple", "Orange", "Banana" };
//        subItems.put(items[3], subItems3);
//    }
//
//    public void actionPerformed(ActionEvent e)
//    {
//        String item = (String)mainComboBox.getSelectedItem();
//        Object o = subItems.get( item );
//
//        if (o == null)
//        {
//            subComboBox.setModel( new DefaultComboBoxModel() );
//        }
//        else
//        {
//            subComboBox.setModel( new DefaultComboBoxModel( (String[])o ) );
//        }
//    }
//
//    private static void createAndShowUI()
//    {
//        JFrame frame = new JFrame("SSCCE");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add( new solve() );
//        frame.setLocationByPlatform( true );
//        frame.pack();
//        frame.setVisible( true );
//    }
//
//    public static void main(String[] args)
//    {
//        EventQueue.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                createAndShowUI();
//            }
//        });
//    }
//}





//
//import java.awt.event.*;
//import java.awt.*;
//import javax.swing.*;
//public class StartFram extends JFrame implements ItemListener {
//
//    // frame
//    static JFrame BORDER_TITLE;
//
//    // label
//    static JLabel l, l1, l3, l4;
//
//    // combobox
//    static JComboBox c1, c2;
//
//    // main class
//    public static void initialiseGame() {
//
//        // create a new frame
//        BORDER_TITLE = new JFrame("Initialise game");
//        JButton okButton = new JButton("ok"); //added for ok button
//        JButton cancelButton = new JButton("cancel");//added for cancel button
//
//
//        BORDER_TITLE.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//        BORDER_TITLE.add(okButton);
//        BORDER_TITLE.add(cancelButton);
//
//
//        // create a object
//        StartFram s = new StartFram();
//
//        // array of string contating cities
//        String s1[] = { "Jalpaiguri", "Mumbai", "Noida", "Kolkata", "New Delhi" };
//        String s2[] = { "male", "female", "others" };
//
//        // create checkbox
//        c1 = new JComboBox(s1);
//        c2 = new JComboBox(s2);
//
//
//        c1.setSelectedIndex(3);
//        c2.setSelectedIndex(0);
//
//        // add ItemListener
//        c1.addItemListener(s);
//        c2.addItemListener(s);
//
//        // set the checkbox as editable
//        c1.setEditable(true);
//
//        // create labels
//        l = new JLabel("select your city ");
//        l1 = new JLabel("Jalpaiguri selected");
//        l3 = new JLabel("select your gender ");
//        l4 = new JLabel("Male selected");
//
//        // set color of text
//        l.setForeground(Color.red);
//        l1.setForeground(Color.blue);
//        l3.setForeground(Color.red);
//        l4.setForeground(Color.blue);
//
//        // create a new panel
//        JPanel p = new JPanel();
//
//        p.add(l);
//
//        // add combobox to panel
//        p.add(c1);
//
//        p.add(l1);
//
//        p.add(l3);
//
//        // add combobox to panel
//        p.add(c2);
//
//        p.add(l4);
//
//        // set a layout for panel
//        p.setLayout(new FlowLayout());
//
//        // add panel to frame
//        BORDER_TITLE.add(p);
//
//        // set the size of frame
//        BORDER_TITLE.setSize(400, 200);
//
//        BORDER_TITLE.show();
//
//
//
//
//    }
//    public void itemStateChanged(ItemEvent e)
//    {
//        if (e.getSource() == c1) {
//
//            l1.setText(c1.getSelectedItem() + " selected");
//        }
//
//        // if state of combobox 2 is changed
//        else
//            l4.setText(c2.getSelectedItem() + " selected");
//    }

//}


