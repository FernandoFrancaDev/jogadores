package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class Sobre extends JDialog {

	Sobre() {

		// setIconImage(Sobre.class.getResource("/img/bolasobre.png"));
		setTitle("Sobre o Cadastro de Jogadores");
		setModal(true);
		setResizable(false);
		setSize(400, 300);
		setLocationRelativeTo(null);
		setLayout(null);

		JLabel lblprojeto = new JLabel("Projeto Cadastro de Jogadores");
		lblprojeto.setBounds(20, 20, 200, 30);
		lblprojeto.setFont(new Font("Arial", Font.BOLD, 12));
		lblprojeto.setForeground(Color.black);
		add(lblprojeto);

		JLabel lblautor = new JLabel("@author FernandoFrancaDev");
		lblautor.setBounds(20, 60, 200, 30);
		lblautor.setFont(new Font("Arial", Font.BOLD, 12));
		lblautor.setForeground(Color.black);
		add(lblautor);

		JLabel lbllicenca = new JLabel("Sob a licença Professor Jose de Assis MIT");
		lbllicenca.setBounds(20, 120, 250, 30);
		lbllicenca.setFont(new Font("Arial", Font.BOLD, 12));
		lbllicenca.setForeground(Color.black);
		add(lbllicenca);

		ImageIcon imglogo = new ImageIcon(Sobre.class.getResource("/img/bolasobre.png"));
		JLabel lbllogo = new JLabel(imglogo);
		lbllogo.setBounds(250, 20, 96, 96);
		lbllogo.setFont(new Font("Arial", Font.BOLD, 12));
		lbllogo.setForeground(Color.black);
		add(lbllogo);

		ImageIcon imggit = new ImageIcon(Sobre.class.getResource("/img/github.png"));
		JButton btngit = new JButton(imggit);
		btngit.setBounds(20, 170, 48, 48);
		btngit.setBorderPainted(false);
		btngit.setContentAreaFilled(false);
		btngit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btngit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				link("https://github.com/FernandoFrancaDev");
			}
		});
		add(btngit);

		JButton btnOK = new JButton("OK");
		btnOK.setBounds(250, 180, 70, 30);
		btnOK.setForeground(Color.black);
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(btnOK);

		setVisible(true);
		;
	}

	public static void main(String[] args) {
		new Sobre();
	}
	
	private void link(String url) {
		Desktop desktop = Desktop.getDesktop();
		try {
			URI uri = new URI(url);
			desktop.browse(uri);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
