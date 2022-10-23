package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constraint.Constants.EJButton;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private UI ui;
	private JLabel count;
	public MainFrame(UI ui) {
		this.setSize(350, 100); // 너비, 길이
		this.setLocationRelativeTo(null); // 가운데로 맞추기
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.ui = ui;
		JPanel btnPanel = new JPanel();
		JPanel processCount = new JPanel();
		ActionHandler actionHandler = new ActionHandler();
	
		for (EJButton ejButton : EJButton.values()) {
			JButton btn = new JButton(ejButton.getName());
			btn.setActionCommand(ejButton.getName());
			btn.addActionListener(actionHandler);
			btnPanel.add(btn);
		}
		
		JLabel label = new JLabel("Open Process Count: ");
		processCount.add(label);
		count = new JLabel(String.valueOf(ui.getCount()));
		processCount.add(count);
		this.add(btnPanel, BorderLayout.NORTH);
		this.add(processCount, BorderLayout.SOUTH);
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
			ui.getFile(file);
			count.setText(String.valueOf(ui.getCount()));
		}
	}
}
