package view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import modal.DAO;
import utils.Validador;

public class Jogador extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO(); // criando objeto dao usado para ter acesso ao método conectar.
	private Connection con; // criando objetos classe conection que se chamará con.
	private FileInputStream fis; // instanciar objeto para o fluxo de byte
	private int tamanho;// variavel global para armazenar o tamanho da imagem(bytes).
	private PreparedStatement pst;
	private ResultSet rs;
	private boolean fotoCarregada = false;

	JTextField txtRA = new JTextField();
	JButton btnBuscar = new JButton("Buscar");
	JTextField txtNome = new JTextField();
	ImageIcon cadastrar = new ImageIcon(Jogador.class.getResource("/img/cad.png"));
	JButton btnCadastrar = new JButton(cadastrar);
	ImageIcon atualizar = new ImageIcon(Jogador.class.getResource("/img/atualizar.png"));
	JButton btnAtualizar = new JButton(atualizar);
	ImageIcon excluir = new ImageIcon(Jogador.class.getResource("/img/lixeira.png"));
	JButton btnExcluir = new JButton(excluir);
	ImageIcon pdf = new ImageIcon(Jogador.class.getResource("/img/pdf3.png"));
	JButton btnPdf = new JButton(pdf);
	ImageIcon reset = new ImageIcon(Jogador.class.getResource("/img/atualizar2.png"));
	JButton btnReset = new JButton(reset);
	ImageIcon status = new ImageIcon(Jogador.class.getResource("/img/databaseoff.png"));
	JLabel lblstatus = new JLabel(status);
	JLabel lblData = new JLabel("teste");
	ImageIcon player = new ImageIcon(Jogador.class.getResource("/img/player.png"));
	JLabel lblFoto = new JLabel(player);
	JButton btnCarregar = new JButton("Carregar Foto");
	JScrollPane scrollPaneLista = new JScrollPane();
	JList<String> listNomes = new JList<String>();
	ImageIcon imginfo = new ImageIcon(Jogador.class.getResource("/img/info.png"));
	JButton btninfo = new JButton(imginfo);

	public Jogador() {

		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				status();
				setarData();
			}
		});

		setSize(640, 360);
		ImageIcon bola = new ImageIcon(Jogador.class.getResource("/img/bola.png"));
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cadastro de Jogadores");
		setLayout(null);

		JLabel lblRA = new JLabel("RA");
		lblRA.setBounds(10, 5, 50, 20);
		lblRA.setFont(new Font("Arial", Font.BOLD, 12));
		lblRA.setForeground(Color.black);
		add(lblRA);

		txtRA.setBounds(50, 7, 100, 22);
		txtRA.setFont(new Font("Arial", Font.BOLD, 12));
		txtRA.setForeground(Color.black);
		txtRA.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		txtRA.setDocument(new Validador(6));

		add(txtRA);

		btnBuscar.setBounds(160, 7, 100, 20);
		btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
		btnBuscar.setForeground(Color.black);
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarRA();
			}
		});
		add(btnBuscar);
		
		btninfo.setBounds(262, 7, 48, 52);
		btninfo.setForeground(Color.black);
		btninfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sobre sobre = new Sobre();
				sobre.setVisible(true);
			}
		});
		add(btninfo);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10, 40, 50, 20);
		lblNome.setFont(new Font("Arial", Font.BOLD, 12));
		lblNome.setForeground(Color.black);
		add(lblNome);

		txtNome.setBounds(50, 40, 210, 20);
		txtNome.setFont(new Font("Arial", Font.BOLD, 12));
		txtNome.setForeground(Color.black);
		txtNome.setDocument(new Validador(30));
		txtNome.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				listarNomes();
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					scrollPaneLista.setVisible(false);
					int confirma = JOptionPane.showConfirmDialog(null,
							"Jogador não cadastrado.\nDeseja cadastrar este jogador?", "Aviso",
							JOptionPane.YES_OPTION);
					if (confirma == JOptionPane.YES_OPTION) {
						txtRA.setEditable(false);
						btnBuscar.setEnabled(false);	
						btnCarregar.setEnabled(true);
						btnCadastrar.setEnabled(true);

					} else {
						reset();
					}
				}
			}
		});
		add(txtNome);

		scrollPaneLista.setBounds(50, 59, 210, 75);
		scrollPaneLista.setForeground(Color.black);
		scrollPaneLista.setFont(new Font("Arial", Font.BOLD, 12));
		scrollPaneLista.setVisible(false);
		add(scrollPaneLista);

		scrollPaneLista.setViewportView(listNomes);

		listNomes.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				buscarNome();
			}
		});

		btnCadastrar.setBounds(5, 225, 64, 64);
		btnCadastrar.setToolTipText("Cadastrar");
		btnCadastrar.setEnabled(false);
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		add(btnCadastrar);

		btnAtualizar.setBounds(75, 225, 64, 64);
		btnAtualizar.setToolTipText("Atualizar");
		btnAtualizar.setEnabled(false);
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editar();
			}
		});

		add(btnAtualizar);

		btnExcluir.setBounds(145, 225, 64, 64);
		btnExcluir.setToolTipText("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluir();
			}
		});
		add(btnExcluir);

		btnPdf.setBounds(216, 225, 64, 64);
		btnPdf.setToolTipText("Relatório");
		btnPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarPdf();
			}
		});
		add(btnPdf);

		btnReset.setBounds(289, 225, 64, 64);
		btnReset.setToolTipText("Limpar");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		add(btnReset);

		lblstatus.setBounds(590, 290, 32, 32);
		add(lblstatus);

		lblData.setBounds(5, 290, 200, 30);
		lblData.setFont(new Font("Arial", Font.ITALIC, 12));
		lblData.setForeground(Color.blue);
		add(lblData);

		lblFoto.setBounds(370, 0, 256, 256);
		lblFoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(lblFoto);

		btnCarregar.setBounds(415, 260, 150, 30);
		btnCarregar.setFont(new Font("Arial", Font.BOLD, 12));
		btnCarregar.setEnabled(false);
		btnCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}
		});
		add(btnCarregar);

		setVisible(true);

	}

	private void status() {
		try {
			con = dao.conectar();
			if (con == null) {
				// System.out.println("Erro de conexão");
				lblstatus.setIcon(new ImageIcon(Jogador.class.getResource("/img/databaseoff.png")));
			} else {
				// System.out.println("Banco de dados conectado");
				lblstatus.setIcon(new ImageIcon(Jogador.class.getResource("/img/databaseon.png")));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void setarData() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblData.setText(formatador.format(data));
	}

	private void carregarFoto() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar arquivo");
		jfc.setFileFilter(
				new FileNameExtensionFilter("Arquivo de Imagens (*.PNG, *.JPG, *.JPEG", "png", "jpg", "jpeg"));
		int resultado = jfc.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION)
			;
		try {
			fis = new FileInputStream(jfc.getSelectedFile());
			tamanho = (int) jfc.getSelectedFile().length();
			Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(),
					Image.SCALE_SMOOTH);
			lblFoto.setIcon(new ImageIcon(foto));
			lblFoto.updateUI();
			fotoCarregada = true;

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void adicionar() {
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome");
			txtNome.requestFocus();

		} else if (tamanho == 0) {
			JOptionPane.showMessageDialog(null, "Selecione a foto");
		} else {
			String insert = "insert into jogador (nome,foto) value(?,?)";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(insert);
				pst.setString(1, txtNome.getText());
				pst.setBlob(2, fis, tamanho);
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Jogador cadastrado com sucesso!");
					reset();
				} else {
					JOptionPane.showMessageDialog(null, "Erro! Jogador não cadastrado");
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}

	private void buscarRA() {
		if (txtRA.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Digite o RA");
			txtRA.requestFocus();

		} else {
			String readRA = "select * from jogador where ra = ?";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readRA);
				pst.setString(1, txtRA.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					txtNome.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(),
							lblFoto.getHeight(), Image.SCALE_SMOOTH));
					lblFoto.setIcon(foto);
					txtRA.setEditable(false);
					btnBuscar.setEnabled(false);
					btnCarregar.setEnabled(true);
					btnAtualizar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnPdf.setEnabled(false);
				} else {
					int confirma = JOptionPane.showConfirmDialog(null,
							"Jogador não cadastrado.\nDeseja iniciar um novo cadastro?", "Aviso",
							JOptionPane.YES_OPTION);
					if (confirma == JOptionPane.YES_OPTION) {
						txtRA.setEditable(false);
						btnBuscar.setEnabled(false);
						txtNome.setText(null);
						txtNome.requestFocus();
						btnCarregar.setEnabled(true);
						btnCadastrar.setEnabled(true);

					} else {
						reset();
					}
				}
				con.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	public void listarNomes() {
		DefaultListModel<String> modelo = new DefaultListModel<>();
		listNomes.setModel(modelo);
		String readLista = "select * from jogador where nome like '" + txtNome.getText() + "%'" + "order by nome";
		try {
			con = dao.conectar();
			pst = con.prepareStatement(readLista);
			rs = pst.executeQuery();
			while (rs.next()) {
				scrollPaneLista.setVisible(true);
				modelo.addElement(rs.getString(2));
				if (txtNome.getText().isEmpty()) {
					scrollPaneLista.setVisible(false);
				}

			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void buscarNome() {
		int linha = listNomes.getSelectedIndex();
		if (linha >= 0) {
			String readNome = "select * from jogador where nome like '" + txtNome.getText() + "%'"
					+ "order by nome limit " + (linha) + ", 1";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readNome);
				rs = pst.executeQuery();
				while (rs.next()) {
					scrollPaneLista.setVisible(false);
					txtRA.setText(rs.getString(1));
					txtNome.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(),
							lblFoto.getHeight(), Image.SCALE_SMOOTH));
					lblFoto.setIcon(foto);
					lblFoto.setIcon(foto);
					txtRA.setEditable(false);
					btnBuscar.setEnabled(false);
					btnCarregar.setEnabled(true);
					btnAtualizar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnPdf.setEnabled(false);
					btnPdf.setEnabled(false);
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			scrollPaneLista.setVisible(false);

		}
	}

	private void editar() {
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome");
			txtNome.requestFocus();
		} else {
			if (fotoCarregada == true) {
				String update = "update jogador set nome=? ,foto=? where ra=?";
				try {
					con = dao.conectar();
					pst = con.prepareStatement(update);
					pst.setString(1, txtNome.getText());
					pst.setBlob(2, fis, tamanho);
					pst.setString(3, txtRA.getText());
					int confirma = pst.executeUpdate();
					if (confirma == 1) {
						JOptionPane.showMessageDialog(null, "Dados do Jogador Alterados!");
						reset();
					} else {
						JOptionPane.showMessageDialog(null, "Erro! Dados do Jogador não alterados");
					}
					con.close();
				} catch (Exception e) {
					System.out.println(e);
				}

			} else {
				String update = "update jogador set nome=? where ra=?";
				try {
					con = dao.conectar();
					pst = con.prepareStatement(update);
					pst.setString(1, txtNome.getText());
					pst.setString(2, txtRA.getText());
					int confirma = pst.executeUpdate();
					if (confirma == 1) {
						JOptionPane.showMessageDialog(null, "Dados do Jogador Alterados!");
						reset();
					} else {
						JOptionPane.showMessageDialog(null, "Erro! Dados do Jogador não alterados");
					}
					con.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		}

	}

	private void excluir() {
		int confirmaExcluir = JOptionPane.showConfirmDialog(null, "Confirma a exclusão deste jogador?", "Atenção!",
				JOptionPane.YES_NO_OPTION);
		if (confirmaExcluir == JOptionPane.YES_OPTION) {
			String delete = "delete from jogador where ra=?";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(delete);
				pst.setString(1, txtRA.getText());
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					reset();
					JOptionPane.showMessageDialog(null, "Jogador excluido");
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}
	
	private void gerarPdf() {
		Document document = new Document(); // Important! importar da bilioteca com.itext
		try {
			PdfWriter.getInstance(document, new FileOutputStream("jogadores.pdf")); // --> Cria o documento de nome jogadores.pdf
			document.open(); // --> abre o documento 
			Date data = new Date();
			DateFormat formatador  = DateFormat.getDateInstance(DateFormat.FULL);
			document.add(new Paragraph(formatador.format(data)));
			document.add(new Paragraph ("Listagem de jogadores: ")); // --> adicona um paragrafo ao documento PDF
			document.add(new Paragraph(" ")); // --> pula uma linha
			PdfPTable tabela = new PdfPTable(3); // criação da tabela com duas colunas para o documento pdf
			PdfPCell col1 = new PdfPCell(new Paragraph("RA"));// --> cria a coluna 1 com paragrafo
			tabela.addCell(col1);
			PdfPCell col2 = new PdfPCell(new Paragraph("Nome"));// --> cria a coluna 1 com paragrafo
			tabela.addCell(col2);
			PdfPCell col3 = new PdfPCell(new Paragraph("Foto"));// --> cria a coluna 1 com paragrafo
			tabela.addCell(col3);
			String readLista = "select * from jogador";
			try {
				con = dao.conectar();
				pst = con.prepareStatement(readLista);
				rs = pst.executeQuery();
				while(rs.next()) {
					tabela.addCell(rs.getString(1));// --> tras o conteúdo do campo RA 
					tabela.addCell(rs.getString(2));// --> tras o conteúdo do campo nome 
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(img);
					tabela.addCell(image);
					
				}
						
			} catch (Exception e) {
				System.out.println();
			}
			con.close();
			
			
			document.add(tabela);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			document.close();
		}
		//--> para abrir o pdf no leitor padrão do sistema
		try {
			Desktop.getDesktop().open(new File("jogadores.pdf"));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	private void reset() {
		scrollPaneLista.setVisible(false);
		txtRA.setText(null);
		txtNome.setText(null);
		lblFoto.setIcon(new ImageIcon(Jogador.class.getResource("/img/player.png")));
		txtNome.requestFocus();
		fotoCarregada = false;
		tamanho = 0;
		txtRA.setEditable(true);
		btnBuscar.setEnabled(true);
		btnCarregar.setEnabled(false);
		btnCadastrar.setEnabled(false);
		btnAtualizar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnPdf.setEnabled(true);

	}

	public static void main(String[] args) {
		new Jogador();
	}

}
