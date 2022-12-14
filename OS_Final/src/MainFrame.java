import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import constraint.Config;
import constraint.EJButton;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// attribute 
	private UI ui;
	private JLabel label;
	private JLabel count;
	private JLabel list;
	
	// constructor
	public MainFrame(UI ui) {
		this.setSize(400, 210); // 너비, 길이
		this.setLocationRelativeTo(null); // 가운데로 맞추기
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.ui = ui;
		JPanel btnPanel = new JPanel();
		JPanel processCount = new JPanel();
		JPanel listPanel = new JPanel();
		ActionHandler actionHandler = new ActionHandler();
	
		// button part
		for (EJButton ejButton : EJButton.values()) {
			JButton btn = new JButton(ejButton.getLabel());
			btn.setActionCommand(ejButton.getName());
			btn.addActionListener(actionHandler);
			btnPanel.add(btn);
		}
		
		// count part
		label = new JLabel(Config.ClickCountSentence);
		processCount.add(label);
		
		count = new JLabel();
		processCount.add(count);
		
		// list part
		list = new JLabel();
		list.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jScrollPane = new JScrollPane(list);
		jScrollPane.setPreferredSize(new Dimension(300, 100));
		listPanel.add(jScrollPane);
	
		// frame add part
		this.add(btnPanel, BorderLayout.NORTH);
		this.add(processCount, BorderLayout.CENTER);
		this.add(listPanel, BorderLayout.SOUTH);
	}

	private File open() {
		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty(Config.userOpenFileDir)));
		int ret = fileChooser.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	private class ActionHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = null;
			if(e.getActionCommand().equals(EJButton.eSelect.getName())) {
				file = open();
			}else if(e.getActionCommand().equals(EJButton.eExe1.getName())) {
				file = new File(Config.directory + EJButton.eExe1.getName());
			}else if(e.getActionCommand().equals(EJButton.eExe2.getName())) {
				file = new File(Config.directory + EJButton.eExe2.getName());
			}else if(e.getActionCommand().equals(EJButton.eExe3.getName())) {
				file = new File(Config.directory + EJButton.eExe3.getName());
			}else if(e.getActionCommand().equals(EJButton.eExit.getName())) {
				ui.exitMtd(false);
			}
			ui.setFile(file); // UI 에 File 전달
			count.setText(String.valueOf(ui.getCount())); // count의 이름 변경
			list.setText(ui.getFileList()); // list의 ui file List를 불러와 변경하기
		}
	}
}
