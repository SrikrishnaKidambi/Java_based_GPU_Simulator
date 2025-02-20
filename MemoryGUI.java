import javax.swing.*;
import java.awt.*;

public class MemoryGUI {
	private Memory mem;
	private JFrame frame;
	private JTable table;
	private JComboBox<String> coreSelect;
	private JComboBox<String> formatSelect;
	private JScrollPane scrollPane;
	
	
	public MemoryGUI(Memory mem) {
		this.mem=mem;
		frame=new JFrame("Memory");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
		
		String[] coreOptions= {"Core 0","Core 1","Core 2","Core 3"};
		coreSelect=new JComboBox<>(coreOptions);
		coreSelect.addActionListener(e->updateTable());
		
		String[] formatOpt= {"Signed","HexaDecimal","Binary"};
		formatSelect=new JComboBox<>(formatOpt);
		formatSelect.addActionListener(e->updateTable());
		
		table=createMemoryTable(0,"Signed");
		scrollPane=new JScrollPane(table);
//		scrollPane.setPreferredSize(new Dimension(900,300));
		
		JPanel tablePanel = new JPanel(new BorderLayout());
//        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tablePanel.add(scrollPane,BorderLayout.CENTER);
		
		JPanel topPanel=new JPanel();
		topPanel.add(new JLabel("Select Core:"));
		topPanel.add(coreSelect);
		topPanel.add(new JLabel("Display Format:"));
		topPanel.add(formatSelect);
		
		
		frame.add(topPanel,BorderLayout.NORTH);
		frame.add(tablePanel,BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	private JTable createMemoryTable(int core,String format){
		String[] columnNames=new String[32];
		for(int i=0;i<32;i++) {
			columnNames[i]=(core+i*4)+"";
		}
		Object[][] data = new Object[8][32]; 
		for (int row=0;row<8;row++) {
	        for (int col=0; col<32; col++) {
	            data[row][col]="0";
	        }
	    }
		int row=0, col=0;
		int memIdx=core;

		while (memIdx<mem.memory.length && row<8) { 
		    data[row][col]=formatting(mem.memory[memIdx], format);
		    col++;  
		    memIdx += 4;  
		    if (col==32) { 
		        col=0;  
		        row++;
		    }
		}


		JTable table=new JTable(data,columnNames);
		table.setRowHeight(35);
		table.setFont(new Font("Arial",Font.PLAIN,14));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    return table;
	}
	
	private void updateTable() {
		int core=coreSelect.getSelectedIndex();
		String format=(String) formatSelect.getSelectedItem();
		table=createMemoryTable(core,format);
		scrollPane.setViewportView(table);
		frame.revalidate();
		frame.repaint();
	}
	
	private String formatting(int val,String format) {
		return switch(format) {
		case "HexaDecimal"->String.format("0x%X", val);
		case "Binary"->"0b"+Integer.toBinaryString(val);
		default->Integer.toString(val);
		};
	}
	
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(MemoryGUI::new);
//	}
//		
}
