//Advanced Programming 2015 Final Project
//Author: Daniele Gadler
//Title: JSnake(BanglaSnake)

package it.unibz.ap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class TopMenu {

    private final String exitCommand = "Exit";
    private final String aboutCommand = "About";
    private final String helpCommand = "Help";
    
  
    private final String aboutText = "Program created by Daniele Gadler for the 2015 Advanced Programming course at UniBZ";
    private final String helpText = "Don't ask me, just play...";
    JFrame frame;

    public TopMenu(JFrame frame)
    {
        this.frame = frame; 
        
        JMenu menu = new JMenu("Menu");
        MenuActionListener menuAcListener = new MenuActionListener();
        
       
        //about button
        JMenuItem aboutMenu = new JMenuItem(aboutCommand);
        aboutMenu.addActionListener(menuAcListener);
        menu.add(aboutMenu);
        
        //help button
        JMenuItem helpMenu = new JMenuItem(helpCommand);
        helpMenu.addActionListener(menuAcListener);
        menu.add(helpMenu);
        
        //exit button
        JMenuItem exitMenu = new JMenuItem(exitCommand);
        exitMenu.addActionListener(menuAcListener);
        menu.add(exitMenu);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        
    }

    private class MenuActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt) {
            JMenuItem source = (JMenuItem) evt.getSource();
            String text = source.getText();
            
            //if clicking on the exit button
            if (text.equals(exitCommand)) {
                System.exit(0);
            }
            //if clicking on the about button
            if (text.equals(aboutCommand)) {
            	JOptionPane.showMessageDialog(null, aboutText);     
            }
            
            //if clicking on the help button
            if (text.equals(helpCommand)) {
                JOptionPane.showMessageDialog(null, helpText);     
            }
        }
    }
}
