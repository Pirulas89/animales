package com.hibernate;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


import com.hibernate.dao.AnimalDAO;
import com.hibernate.dao.MedicinaDAO;
import com.hibernate.model.Animal;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class App {

	private JFrame frame;
	private JTextField txtIdAn;
	private JTextField txtNombreAn;
	private JTextField txtEspecie;
	private JTextField txtRaza;
	private JTable table;
	private JLabel lblFoto;
	private JTextField txtUrl;
	AnimalDAO aDAO = new AnimalDAO();
	/**
	 * Launch the application.
	 */
	private DefaultTableModel getModeloNoEditable() {
		return new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}
	/*
	 * private void mostrarImagenAnimal(String urlTexto) {
		if (urlTexto == null || urlTexto.trim().isEmpty()) {
			lblFoto.setIcon(null);
			lblFoto.setText("Sin imagen");
			return;
		}
		try {
			java.net.URL url = new java.net.URL(urlTexto);
			java.awt.Image img = javax.imageio.ImageIO.read(url);
			if (img != null) {
				// Escalamos la imagen al tamaño del label (105x105)
				java.awt.Image escalada = img.getScaledInstance(105, 105, java.awt.Image.SCALE_SMOOTH);
				lblFoto.setIcon(new javax.swing.ImageIcon(escalada));
				lblFoto.setText("");
			}
		} catch (Exception e) {
			lblFoto.setIcon(null);
			lblFoto.setText("URL inválida");
		}
	}
	 */
	void mostrarTabla() {
		try {
			DefaultTableModel model = getModeloNoEditable();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("edad");
			model.addColumn("estado");
			model.addColumn("especie");
			model.addColumn("raza");
			model.setRowCount(0);
			List<Animal> animales = aDAO.selectAllAnimal();
			for (Animal a : animales) {
				Object[] fila = { a.getId(), a.getNombre(), a.getEdad(), a.getEstado(), a.getEspecie(), a.getRaza() };
				model.addRow(fila);
			}
			table.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 820, 545);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(41, 25, 687, 439);
		frame.getContentPane().add(tabbedPane_1);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.addTab("Datos", null, tabbedPane, null);
		
		JPanel panel_animal = new JPanel();
		tabbedPane.addTab("Animal", null, panel_animal, null);
		panel_animal.setLayout(null);
		
		JLabel lblId = new JLabel("Id:");
		lblId.setBounds(12, 12, 60, 17);
		panel_animal.add(lblId);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(12, 54, 60, 17);
		panel_animal.add(lblNombre);
		
		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(12, 101, 60, 17);
		panel_animal.add(lblEstado);
		
		JLabel lblEspecie = new JLabel("Especie:");
		lblEspecie.setBounds(12, 151, 60, 17);
		panel_animal.add(lblEspecie);
		
		JLabel lblRaza = new JLabel("Raza:");
		lblRaza.setBounds(12, 204, 60, 17);
		panel_animal.add(lblRaza);
		
		txtIdAn = new JTextField();
		txtIdAn.setEditable(false);
		txtIdAn.setBounds(45, 10, 114, 21);
		panel_animal.add(txtIdAn);
		txtIdAn.setColumns(10);
		
		txtNombreAn = new JTextField();
		txtNombreAn.setBounds(90, 52, 114, 21);
		panel_animal.add(txtNombreAn);
		txtNombreAn.setColumns(10);
		
		JComboBox comboBoxAnimal = new JComboBox();
		comboBoxAnimal.setModel(new DefaultComboBoxModel(new String[] {"Disponible", "Adoptado", "En Tratamiento"}));
		comboBoxAnimal.setBounds(90, 96, 132, 33);
		panel_animal.add(comboBoxAnimal);
		
		txtEspecie = new JTextField();
		txtEspecie.setBounds(90, 149, 114, 21);
		panel_animal.add(txtEspecie);
		txtEspecie.setColumns(10);
		
		txtRaza = new JTextField();
		txtRaza.setBounds(76, 202, 114, 21);
		panel_animal.add(txtRaza);
		txtRaza.setColumns(10);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnGuardar.setBounds(263, 271, 105, 27);
		panel_animal.add(btnGuardar);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnActualizar.setBounds(398, 271, 105, 27);
		panel_animal.add(btnActualizar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnBorrar.setBounds(532, 271, 105, 27);
		panel_animal.add(btnBorrar);
		
		table = new JTable();
		table.setBounds(263, 13, 374, 208);
		panel_animal.add(table);
		
		JButton btnMostrar = new JButton("Mostrar");
		btnMostrar.setVisible(false);
		btnMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mostrarTabla();
			}
		});
		btnMostrar.setBounds(398, 342, 105, 27);
		panel_animal.add(btnMostrar);
		
		JLabel lblFotoAnimal = new JLabel("URL Foto:");
		lblFotoAnimal.setBounds(12, 246, 81, 17);
		panel_animal.add(lblFotoAnimal);
		
		txtUrl = new JTextField();
		txtUrl.setBounds(90, 244, 166, 21);
		panel_animal.add(txtUrl);
		txtUrl.setColumns(10);
		
		JPanel panel_medicina = new JPanel();
		tabbedPane.addTab("Medicina", null, panel_medicina, null);
		panel_medicina.setLayout(null);
		
		JPanel panel_cliente = new JPanel();
		tabbedPane.addTab("Cliente", null, panel_cliente, null);
		panel_cliente.setLayout(null);
		
		JPanel panel = new JPanel();
		tabbedPane_1.addTab("Adopcion", null, panel, null);
	}
}
