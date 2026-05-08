package com.hibernate;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hibernate.dao.AdopcionDAO;
import com.hibernate.dao.AnimalDAO;
import com.hibernate.dao.ClienteDAO;
import com.hibernate.dao.MedicinaDAO;
import com.hibernate.model.Adopcion;
import com.hibernate.model.Animal;
import com.hibernate.model.Cliente;
import com.hibernate.model.Medicina;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class App {

	private JFrame frame;
	private JTextField txtIdAn;
	private JTextField txtNombreAn;
	private JTextField txtEspecie;
	private JTextField txtRaza;
	private JTable table;
	//private JLabel lblFoto;
	private JTextField txtUrl;
	private JTextField txtEdadAn;
	AnimalDAO aDAO = new AnimalDAO();
	Animal a = new Animal();
	AdopcionDAO adDAO = new AdopcionDAO();
	Adopcion ad = new Adopcion();
	ClienteDAO cDAO = new ClienteDAO();
	Cliente c = new Cliente();
	MedicinaDAO mDAO = new MedicinaDAO();
	Medicina m = new Medicina();
	private JTextField txtIdM;
	private JTextField txtNombreM;
	private JTextField txtDesc;
	private JTable table_1;
	private JTextField txtIdC;
	private JTextField txtNombreC;
	private JTextField txtDni;
	private JTextField txtTelef;
	private JTextField txtGmail;
	private JTable table_2;
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
			model.addColumn("url");
			model.setRowCount(0);
			List<Animal> animales = aDAO.selectAllAnimal();
			for (Animal a : animales) {
				Object[] fila = { a.getId(), a.getNombre(), a.getEdad(), a.getEstado(), a.getEspecie(), a.getRaza(), a.getFoto() };
				model.addRow(fila);
			}
			table.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean esValido(Object objeto) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(objeto);

		if (!violations.isEmpty()) {
			// Unimos todos los mensajes de error en un solo String
			String errores = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));

			JOptionPane.showMessageDialog(frame, errores, "Error de validación", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
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
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		// O si usas FlatLaf, puedes redondearlo
		scrollPane.putClientProperty("JComponent.outline", Color.decode("#a00123"));
		scrollPane.setBounds(263, 13, 374, 208);
		//frame.getContentPane().add(scrollPane);
		
		JButton btnMostrar = new JButton("Mostrar");
		btnMostrar.setVisible(false);
		btnMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mostrarTabla();
			}
		});
		btnMostrar.setBounds(398, 342, 105, 27);
		panel_animal.add(btnMostrar);
		
		JLabel lblId = new JLabel("Id:");
		lblId.setBounds(12, 12, 60, 17);
		panel_animal.add(lblId);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(12, 54, 60, 17);
		panel_animal.add(lblNombre);
		
		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(12, 143, 60, 17);
		panel_animal.add(lblEstado);
		
		JLabel lblEspecie = new JLabel("Especie:");
		lblEspecie.setBounds(12, 190, 60, 17);
		panel_animal.add(lblEspecie);
		
		JLabel lblRaza = new JLabel("Raza:");
		lblRaza.setBounds(12, 231, 60, 17);
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
		comboBoxAnimal.setBounds(90, 135, 132, 33);
		panel_animal.add(comboBoxAnimal);
		
		txtEspecie = new JTextField();
		txtEspecie.setBounds(90, 188, 114, 21);
		panel_animal.add(txtEspecie);
		txtEspecie.setColumns(10);
		
		txtRaza = new JTextField();
		txtRaza.setBounds(77, 229, 114, 21);
		panel_animal.add(txtRaza);
		txtRaza.setColumns(10);
		
		txtUrl = new JTextField();
		txtUrl.setBounds(85, 274, 166, 21);
		panel_animal.add(txtUrl);
		txtUrl.setColumns(10);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String nombre = txtNombreAn.getText();
					int edad = Integer.parseInt(txtEdadAn.getText());
					String estado = comboBoxAnimal.getSelectedItem().toString();
					String especie = txtEspecie.getText();
					String raza = txtRaza.getText();
					String url = txtUrl.getText(); // <--- Capturar URL
					a = Animal.builder().nombre(nombre).edad(edad).estado(estado).especie(especie).raza(raza).foto(url).build();
					if (esValido(a)) {
						aDAO.insertAnimal(a);
						btnMostrar.doClick();
						JOptionPane.showMessageDialog(null, "Animal creado con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"texto error", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnGuardar.setBounds(532, 241, 105, 27);
		panel_animal.add(btnGuardar);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int idA = Integer.parseInt(txtIdAn.getText());
					String nombre = txtNombreAn.getText();
					int edad = Integer.parseInt(txtEdadAn.getText());
					String estado = comboBoxAnimal.getSelectedItem().toString();
					String especie = txtEspecie.getText();
					String raza = txtRaza.getText();
					String url = txtUrl.getText();
				
						a = aDAO.selectAnimalById(idA);
						a.setNombre(nombre);
						a.setEdad(edad);
						a.setEstado(estado);
						a.setEspecie(especie);
						a.setFoto(txtUrl.getText()); // <--- Actualizar URL
						aDAO.updateAnimal(a);
						btnMostrar.doClick();
						JOptionPane.showMessageDialog(null, "Animal actualizado con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "No debes dejar campos vacios", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}

			}
			
		});
		btnActualizar.setBounds(532, 290, 105, 27);
		panel_animal.add(btnActualizar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					try {
						int idA = Integer.parseInt(txtIdAn.getText());
						aDAO.deleteAnimal(idA);
						btnMostrar.doClick();
						JOptionPane.showMessageDialog(null, "Animal borrado con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Debes seleccionar una id para poder borrarla", "ERROR_MESSAGE",
								JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		btnBorrar.setBounds(532, 342, 105, 27);
		panel_animal.add(btnBorrar);
		

		
		table = new JTable();
		table.putClientProperty("Table.alternateRowColor", Color.decode("#252525"));
		table.setFillsViewportHeight(true);
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = table.getSelectedRow();
				if (fila != -1) {
					int id = Integer.parseInt(table.getValueAt(fila, 0).toString());
					// Buscamos el producto completo usando el DAO para traer la URL
					Animal animSeleccionado = aDAO.selectAnimalById(id);
					txtIdAn.setText(String.valueOf(animSeleccionado.getId()));
					txtEdadAn.setText(String.valueOf(animSeleccionado.getEdad()));
					txtNombreAn.setText(animSeleccionado.getNombre());
					comboBoxAnimal.getSelectedItem().toString();
					txtEspecie.setText(String.valueOf(animSeleccionado.getEspecie()));
					txtRaza.setText(String.valueOf(animSeleccionado.getRaza()));
					txtUrl.setText(String.valueOf(animSeleccionado.getFoto()));

					// Cargar la URL y la imagen
					//txtFotoUrl.setText(prodSeleccionado.getFotoUrl());
					//mostrarImagenProducto(prodSeleccionado.getFotoUrl());
				}
			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(table);
		panel_animal.add(scrollPane);
		
		
		JLabel lblFotoAnimal = new JLabel("URL Foto:");
		lblFotoAnimal.setBounds(12, 276, 81, 17);
		panel_animal.add(lblFotoAnimal);
		
		
		
		JLabel lblEdad = new JLabel("Edad:");
		lblEdad.setBounds(12, 94, 60, 17);
		panel_animal.add(lblEdad);
		
		txtEdadAn = new JTextField();
		txtEdadAn.setBounds(90, 92, 114, 21);
		panel_animal.add(txtEdadAn);
		txtEdadAn.setColumns(10);
		
		JPanel panel_medicina = new JPanel();
		tabbedPane.addTab("Medicina", null, panel_medicina, null);
		panel_medicina.setLayout(null);
		
		JLabel lblIdM = new JLabel("Id:");
		lblIdM.setBounds(26, 31, 60, 17);
		panel_medicina.add(lblIdM);
		
		txtIdM = new JTextField();
		txtIdM.setEditable(false);
		txtIdM.setBounds(75, 29, 114, 21);
		panel_medicina.add(txtIdM);
		txtIdM.setColumns(10);
		
		JLabel lblNombreM = new JLabel("Medicamento:");
		lblNombreM.setBounds(26, 81, 103, 17);
		panel_medicina.add(lblNombreM);
		
		txtNombreM = new JTextField();
		txtNombreM.setBounds(147, 79, 114, 21);
		panel_medicina.add(txtNombreM);
		txtNombreM.setColumns(10);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setBounds(26, 143, 60, 17);
		panel_medicina.add(lblTipo);
		
		JComboBox comboBoxMed = new JComboBox();
		comboBoxMed.setModel(new DefaultComboBoxModel(new String[] {"Pipeta", "Pastilla", "Inyeccion"}));
		comboBoxMed.setBounds(88, 138, 103, 34);
		panel_medicina.add(comboBoxMed);
		
		JLabel lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setBounds(26, 211, 91, 17);
		panel_medicina.add(lblDescripcion);
		
		txtDesc = new JTextField();
		txtDesc.setBounds(106, 213, 222, 120);
		panel_medicina.add(txtDesc);
		txtDesc.setColumns(10);
		
		table_1 = new JTable();
		table_1.setBounds(314, 26, 351, 174);
		panel_medicina.add(table_1);
		
		JButton btnGuardar_1 = new JButton("Guardar");
		btnGuardar_1.setBounds(541, 231, 105, 27);
		panel_medicina.add(btnGuardar_1);
		
		JButton btnActualizar_1 = new JButton("Actualizar");
		btnActualizar_1.setBounds(541, 281, 105, 27);
		panel_medicina.add(btnActualizar_1);
		
		JButton btnNewButton = new JButton("Borrar");
		btnNewButton.setBounds(541, 326, 105, 27);
		panel_medicina.add(btnNewButton);
		
		JButton btnMostrar_1 = new JButton("Mostrar");
		btnMostrar_1.setVisible(false);
		btnMostrar_1.setBounds(418, 231, 80, 27);
		panel_medicina.add(btnMostrar_1);
		
		JPanel panel_cliente = new JPanel();
		tabbedPane.addTab("Cliente", null, panel_cliente, null);
		panel_cliente.setLayout(null);
		
		JLabel lblId_1 = new JLabel("Id:");
		lblId_1.setBounds(12, 24, 60, 17);
		panel_cliente.add(lblId_1);
		
		txtIdC = new JTextField();
		txtIdC.setEditable(false);
		txtIdC.setBounds(61, 22, 114, 21);
		panel_cliente.add(txtIdC);
		txtIdC.setColumns(10);
		
		JLabel lblNombre_1 = new JLabel("Nombre:");
		lblNombre_1.setBounds(12, 72, 60, 17);
		panel_cliente.add(lblNombre_1);
		
		txtNombreC = new JTextField();
		txtNombreC.setBounds(94, 70, 114, 21);
		panel_cliente.add(txtNombreC);
		txtNombreC.setColumns(10);
		
		JLabel lblDni = new JLabel("Dni:");
		lblDni.setBounds(12, 118, 60, 17);
		panel_cliente.add(lblDni);
		
		txtDni = new JTextField();
		txtDni.setBounds(78, 116, 114, 21);
		panel_cliente.add(txtDni);
		txtDni.setColumns(10);
		
		JLabel lblTelef = new JLabel("Telef.");
		lblTelef.setBounds(12, 168, 60, 17);
		panel_cliente.add(lblTelef);
		
		txtTelef = new JTextField();
		txtTelef.setBounds(78, 166, 114, 21);
		panel_cliente.add(txtTelef);
		txtTelef.setColumns(10);
		
		JLabel lblGmail = new JLabel("Gmail:");
		lblGmail.setBounds(12, 214, 60, 17);
		panel_cliente.add(lblGmail);
		
		txtGmail = new JTextField();
		txtGmail.setBounds(78, 212, 114, 21);
		panel_cliente.add(txtGmail);
		txtGmail.setColumns(10);
		
		JButton btnGuardar_2 = new JButton("Guardar");
		btnGuardar_2.setBounds(28, 280, 105, 27);
		panel_cliente.add(btnGuardar_2);
		
		JButton btnActualizar_2 = new JButton("Actualizar");
		btnActualizar_2.setBounds(197, 280, 105, 27);
		panel_cliente.add(btnActualizar_2);
		
		JButton btnBorrar_1 = new JButton("Borrar");
		btnBorrar_1.setBounds(372, 280, 105, 27);
		panel_cliente.add(btnBorrar_1);
		
		table_2 = new JTable();
		table_2.setBounds(250, 12, 402, 237);
		panel_cliente.add(table_2);
		
		JButton btnMostrar_2 = new JButton("Mostrar");
		btnMostrar_2.setVisible(false);
		btnMostrar_2.setBounds(506, 342, 105, 27);
		panel_cliente.add(btnMostrar_2);
		
		JPanel panel = new JPanel();
		tabbedPane_1.addTab("Adopcion", null, panel, null);
		
		mostrarTabla();
	}
	}
