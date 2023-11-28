/*
    Mark Fowler
    CEN-3024C-17125
    11/12/2023

    The MainFrame Class is responsible for setting up the GUI for the main menu
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JButton checkInButton;
    private JButton checkOutButton;
    private JButton removeBooksButton;
    private JButton seeLibraryButton;
    private JPanel mainPanel;

    public MainFrame() {
        setContentPane(mainPanel);
        setTitle("Library Management System");
        setSize(600,150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        seeLibraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListFrame list = new ListFrame();
            }
        });


        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckInFrame checkIn = new CheckInFrame();
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { CheckOutFrame checkOut = new CheckOutFrame(); }
        });

        removeBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { RemoveFrame remove = new RemoveFrame(); }
        });
    }
}