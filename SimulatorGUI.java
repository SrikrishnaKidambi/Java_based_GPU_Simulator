import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SimulatorGUI {
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(SimulatorGUI::makeGUI);
//	}
	public static void makeGUI() {
		JFrame frame=new JFrame("Simulator Console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,600);
		
		// implement the code editor and register view
		JPanel codeEditorPanel=new JPanel();
		JPanel registerPanel=new JPanel();
		
		// text area for printing the output.
		JTextArea console=new JTextArea();
		console.setEditable(false);
		JScrollPane consoleScroll=new JScrollPane(console);
		
		// creating a panel for the buttons 
		
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JButton run=new JButton("Run");
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.append("Running the program");  //printing the output after running the program
			}
			
		});
		
		JButton clear=new JButton("Clear");
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console.setText("");
			}
			
		});
		run.setFocusPainted(false);
		clear.setFocusPainted(false);
		buttonPanel.add(run);
		buttonPanel.add(clear);
		
		// organising the components in the console panel
		JPanel consolePanel=new JPanel(new BorderLayout());
		consolePanel.add(buttonPanel,BorderLayout.NORTH);
		consolePanel.add(consoleScroll,BorderLayout.CENTER);
		
		JSplitPane leftVerticalSplitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,codeEditorPanel,consolePanel);
		leftVerticalSplitPane.setDividerLocation((int)(0.65*600));
		leftVerticalSplitPane.setResizeWeight(0.65);
		
		JSplitPane mainSplitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftVerticalSplitPane,registerPanel);
		mainSplitPane.setDividerLocation((int)(0.75*1000));
		mainSplitPane.setResizeWeight(0.75);
		
		frame.add(mainSplitPane);
		frame.setVisible(true);
	}
}
