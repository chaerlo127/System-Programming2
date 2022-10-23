package main;

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

import constraint.Constants.EJButton;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private UI ui;
	private JLabel label;
	private JLabel count;
	private JLabel list;
	public MainFrame(UI ui) {
		this.setSize(400, 210); // 너비, 길이
		this.setLocationRelativeTo(null); // 가운데로 맞추기
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.ui = ui;
		JPanel btnPanel = new JPanel();
		JPanel processCount = new JPanel();
		JPanel listPanel = new JPanel();
		ActionHandler actionHandler = new ActionHandler();
	
		for (EJButton ejButton : EJButton.values()) {
			JButton btn = new JButton(ejButton.getLabel());
			btn.setActionCommand(ejButton.getName());
			btn.addActionListener(actionHandler);
			btnPanel.add(btn);
		}
		
		label = new JLabel("Click File Count: ");
		processCount.add(label);
		
		count = new JLabel(String.valueOf(ui.getCount()));
		processCount.add(count);
		
		list = new JLabel(ui.getFileList());
		list.setHorizontalAlignment(JLabel.CENTER);
		JScrollPane jScrollPane = new JScrollPane(list);
		jScrollPane.setPreferredSize(new Dimension(300, 100));
		listPanel.add(jScrollPane);
	
		
		this.add(btnPanel, BorderLayout.NORTH);
		this.add(processCount, BorderLayout.CENTER);
		this.add(listPanel, BorderLayout.SOUTH);
	}

	private File open() {
		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
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
				file = new File("data" + "/" + EJButton.eExe1.getName());
			}else if(e.getActionCommand().equals(EJButton.eExe2.getName())) {
				file = new File("data" + "/" + EJButton.eExe2.getName());
			}else if(e.getActionCommand().equals(EJButton.eExe3.getName())) {
				file = new File("data" + "/" + EJButton.eExe3.getName());
			}else if(e.getActionCommand().equals(EJButton.eExit.getName())) {
				ui.exitMtd(false);
				System.exit(0);
			}
			ui.setFile(file);
			count.setText(String.valueOf(ui.getCount()));
			list.setText(ui.getFileList());
		}
	}
}
