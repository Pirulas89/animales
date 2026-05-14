package com.hibernate;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.toedter.calendar.JDateChooser;
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

public class App {
	
    // =======================================================
    //                      ATRIBUTOS
    // =======================================================

    // --- Frame principal ---
    private JFrame frame;

    // --- Panel Animal ---
    private JComboBox<String> comboEstadoAnimal;
    private JTextField txtIdAnimal, txtNombreAnimal, txtEdadAnimal, txtEspecie, txtRaza;
    private JLabel lblFotoAnimal;
    private byte[] fotoAnimalBytes;
    private JTable tablaAnimal;
    private JComboBox<Medicina> comboMedAnimal;

    // --- Panel Medicina ---
    private JTextField txtIdMed, txtNombreMed;
    private JComboBox<String> comboTipoMed;
    private JTextArea txtDescMed;
    private JTable tablaMedicina;

    // --- Panel Cliente ---
    private JTextField txtIdCliente, txtNombreCliente, txtDniCliente, txtTelCliente, txtGmailCliente;
    private JTextField txtCalle, txtNumero, txtCiudad, txtCp;
    private JTable tablaCliente;

    // --- Panel Adopción ---
    private JTextField txtIdAdopcion;
    private JDateChooser dateChooserAdopcion;
    private JComboBox<Cliente> comboClienteAdopcion;
    private JComboBox<Animal> comboAnimalAdopcion;
    private JTextArea txtObservacionesAdopcion;
    private JTable tablaAdopcion;
    private JLabel lblFotoAdop;
    private JLabel txtMedsAdop;

    // --- DAOs ---
    private AnimalDAO animalDAO       = new AnimalDAO();
    private MedicinaDAO medicinaDAO   = new MedicinaDAO();
    private ClienteDAO clienteDAO     = new ClienteDAO();
    private AdopcionDAO adopcionDAO   = new AdopcionDAO();

    // =======================================================
    //                         MAIN
    // =======================================================

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new App();
    }

    public App() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Refugio de Animales");
        frame.setBounds(100, 100, 1100, 700);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                int r = JOptionPane.showConfirmDialog(frame,
                        "¿Deseas salir de la aplicación?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) frame.dispose();
            }
        });

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBounds(0, 0, 1100, 700);
        frame.getContentPane().add(tabs);

        JPanel panelAnimal   = new JPanel(null);
        JPanel panelMedicina = new JPanel(null);
        JPanel panelCliente  = new JPanel(null);
        JPanel panelAdop     = new JPanel(null);

        tabs.addTab("🐾 Animales",   panelAnimal);
        tabs.addTab("💊 Medicinas",  panelMedicina);
        tabs.addTab("👤 Clientes",   panelCliente);
        tabs.addTab("🏠 Adopciones", panelAdop);

        iniPanelAnimal(panelAnimal);
        iniPanelMedicina(panelMedicina);
        iniPanelCliente(panelCliente);
        iniPanelAdopcion(panelAdop);

        frame.setVisible(true);
    }

    // =======================================================
    //                   MÉTODOS AUXILIARES
    // =======================================================

    private boolean validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}[A-Za-z]")) {
            JOptionPane.showMessageDialog(frame,
                    "El DNI debe tener 8 números seguidos de una letra (ej: 12345678A).",
                    "DNI inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numero = Integer.parseInt(dni.substring(0, 8));
        char letraEsperada = letras.charAt(numero % 23);
        char letraIntroducida = Character.toUpperCase(dni.charAt(8));
        if (letraIntroducida != letraEsperada) {
            JOptionPane.showMessageDialog(frame,
                    "La letra del DNI no es correcta. Para " + dni.substring(0, 8)
                    + " la letra correcta es: " + letraEsperada,
                    "DNI inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private String calcularLetraDni(String numeros) {
        if (numeros == null || !numeros.matches("\\d{8}")) return "";
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        return String.valueOf(letras.charAt(Integer.parseInt(numeros) % 23));
    }
    
    private boolean esValido(Object obj) {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        Validator v = f.getValidator();
        Set<ConstraintViolation<Object>> errores = v.validate(obj);
        if (!errores.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> cv : errores)
                sb.append("- ").append(cv.getMessage()).append("\n");
            JOptionPane.showMessageDialog(frame, sb.toString(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarAnimal() {
        if (txtNombreAnimal.getText().trim().isEmpty() ||
            txtEdadAnimal.getText().trim().isEmpty()   ||
            txtEspecie.getText().trim().isEmpty()      ||
            txtRaza.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Todos los campos del animal deben estar completos.",
                    "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtEdadAnimal.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "La edad debe ser un número válido.",
                    "Edad inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (fotoAnimalBytes == null) {
            JOptionPane.showMessageDialog(frame,
                    "El animal debe tener una foto.",
                    "Foto requerida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private byte[] fileToBytes(File file) {
        try {
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error leyendo la imagen.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private ImageIcon bytesToImage(byte[] data, int w, int h) {
        if (data == null) return null;
        try {
            Image img = Toolkit.getDefaultToolkit().createImage(data);
            Image esc = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(esc);
        } catch (Exception e) { return null; }
    }

    private void seleccionarImagenAnimal() {
        JFileChooser ch = new JFileChooser();
        if (ch.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File img = ch.getSelectedFile();
            fotoAnimalBytes = fileToBytes(img);
            lblFotoAnimal.setIcon(bytesToImage(fotoAnimalBytes, 250, 250));
        }
    }

    private void colorearBoton(JButton b) {
        b.setBackground(new Color(255, 140, 0));
        b.setForeground(Color.BLACK);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        b.setFocusPainted(false);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(255, 165, 0)); }
            public void mouseExited(MouseEvent e)  { b.setBackground(new Color(255, 140, 0)); }
        });
    }

    // =======================================================
    //               MÉTODOS DE REFRESCO DE TABLAS
    // =======================================================

    private void mostrarAnimales() {
        List<Animal> list = animalDAO.selectAllAnimal();
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Especie", "Raza", "Edad", "Estado"}, 0);
        for (Animal a : list)
            m.addRow(new Object[]{a.getId(), a.getNombre(), a.getEspecie(),
                    a.getRaza(), a.getEdad(), a.getEstado()});
        tablaAnimal.setModel(m);
    }

    private void mostrarMedicinas() {
        List<Medicina> l = medicinaDAO.selectAllMedicina();
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Tipo", "Descripción", "Stock"}, 0);
        for (Medicina md : l)
            m.addRow(new Object[]{md.getId(), md.getNombre(), md.getTipoMed(),
                    md.getDescripcion(), md.getStock()});
        tablaMedicina.setModel(m);
    }

    private void mostrarClientes() {
        List<Cliente> l = clienteDAO.selectAllCliente();
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "DNI", "Teléfono", "Email"}, 0);
        for (Cliente c : l)
            m.addRow(new Object[]{c.getId(), c.getNombre(), c.getDni(),
                    c.getTelefono(), c.getGmail()});
        tablaCliente.setModel(m);
    }

    private void mostrarAdopciones() {
        List<Adopcion> l = adopcionDAO.selectAllAdopcion();
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"ID", "Fecha", "Cliente", "Animal"}, 0);
        for (Adopcion ad : l)
            m.addRow(new Object[]{ad.getId(), ad.getFecha(),
                    ad.getCliente().getNombre(), ad.getAnimal().getNombre()});
        tablaAdopcion.setModel(m);
    }

    // =======================================================
    //               MÉTODOS DE LIMPIEZA DE CAMPOS
    // =======================================================

    private void limpiarCamposAnimal() {
        txtIdAnimal.setText("");
        txtNombreAnimal.setText("");
        txtEdadAnimal.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        lblFotoAnimal.setIcon(null);
        fotoAnimalBytes = null;
        comboEstadoAnimal.setEnabled(true);
    }

    private void limpiarCamposMedicina() {
        txtIdMed.setText("");
        txtNombreMed.setText("");
        txtDescMed.setText("");
    }

    private void limpiarCamposCliente() {
        txtIdCliente.setText("");
        txtNombreCliente.setText("");
        txtDniCliente.setText("");
        txtTelCliente.setText("");
        txtGmailCliente.setText("");
        txtCalle.setText("");
        txtNumero.setText("");
        txtCiudad.setText("");
        txtCp.setText("");
    }

    private void limpiarCamposAdopcion() {
        txtIdAdopcion.setText("");
        dateChooserAdopcion.setDate(null);
        txtObservacionesAdopcion.setText("");
        comboAnimalAdopcion.setEnabled(true);
    }

    // =======================================================
    //               MÉTODOS DE CARGA DE COMBOS
    // =======================================================

    private void cargarClientesEnCombo() {
        comboClienteAdopcion.removeAllItems();
        for (Cliente c : clienteDAO.selectAllCliente())
            comboClienteAdopcion.addItem(c);
    }

    private void cargarAnimalesDisponibles() {
        comboAnimalAdopcion.removeAllItems();
        for (Animal a : animalDAO.selectAllAnimalConMedicinas())
            if (a.getEstado().equalsIgnoreCase("Disponible"))
                comboAnimalAdopcion.addItem(a);
    }

    private void actualizarComboMedicinas() {
        comboMedAnimal.removeAllItems();
        for (Medicina m : medicinaDAO.selectAllMedicina())
            if (m.getStock() > 0)
                comboMedAnimal.addItem(m);
    }

    // =======================================================
    //               MÉTODOS DE CARGA DE DATOS
    // =======================================================
    private void cargarDatosAdopcion(Adopcion ad) {
        txtIdAdopcion.setText(String.valueOf(ad.getId()));
        dateChooserAdopcion.setDate(Date.from(
                ad.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        txtObservacionesAdopcion.setText(ad.getObservaciones());

        comboAnimalAdopcion.removeAllItems();
        for (Animal a : animalDAO.selectAllAnimalConMedicinas()) {
            if (a.getEstado().equalsIgnoreCase("Disponible")
                    || a.getId() == ad.getAnimal().getId()) {
                comboAnimalAdopcion.addItem(a);
            }
        }

        comboClienteAdopcion.setSelectedItem(ad.getCliente());
        comboAnimalAdopcion.setSelectedItem(ad.getAnimal());
    }

    private void mostrarFichaAdopcion(Adopcion ad) {
        JDialog d = new JDialog(frame, "Ficha completa", true);
        d.setSize(480, 620);
        d.getContentPane().setLayout(null);
        d.setLocationRelativeTo(frame);

        Animal a  = animalDAO.selectAnimalByIdConMedicinas(ad.getAnimal().getId());
        Cliente c = ad.getCliente();

        // --- Foto ---
        JLabel foto = new JLabel();
        foto.setBounds(20, 20, 200, 200);
        foto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        foto.setIcon(bytesToImage(a.getFoto(), 200, 200));
        d.getContentPane().add(foto);

        // --- Datos del animal ---
        JLabel lblNombreAnimal = new JLabel("Nombre: " + a.getNombre());
        lblNombreAnimal.setBounds(20, 240, 450, 20);
        d.getContentPane().add(lblNombreAnimal);

        JLabel lblEspecieAnimal = new JLabel("Especie: " + a.getEspecie());
        lblEspecieAnimal.setBounds(20, 260, 450, 20);
        d.getContentPane().add(lblEspecieAnimal);

        JLabel lblRazaAnimal = new JLabel("Raza: " + a.getRaza());
        lblRazaAnimal.setBounds(20, 280, 450, 20);
        d.getContentPane().add(lblRazaAnimal);

        JLabel lblEdadAnimal = new JLabel("Edad: " + a.getEdad());
        lblEdadAnimal.setBounds(20, 300, 450, 20);
        d.getContentPane().add(lblEdadAnimal);

        JLabel lblMedicinas = new JLabel("Medicamento:");
        lblMedicinas.setBounds(20, 330, 120, 20);
        d.getContentPane().add(lblMedicinas);

        String medTexto;
        if (a.getMedicinaNombre() != null && !a.getMedicinaNombre().isBlank()) {
            medTexto = a.getMedicinaNombre() + " (" + a.getMedicinaTipo() + ")";
        } else if (a.getMedicinas() != null && !a.getMedicinas().isEmpty()) {
            Medicina m = a.getMedicinas().get(0);
            medTexto = m.getNombre() + " (" + m.getTipoMed() + ")";
        } else {
            medTexto = "Sin medicamento";
        }

        JLabel lblMedsValor = new JLabel(medTexto);
        lblMedsValor.setBounds(145, 330, 300, 20);
        d.getContentPane().add(lblMedsValor);

        // --- Datos del cliente ---
        JLabel lblNombreCliente = new JLabel("Cliente: " + c.getNombre());
        lblNombreCliente.setBounds(20, 440, 450, 20);
        d.getContentPane().add(lblNombreCliente);

        JLabel lblDireccion = new JLabel(c.getCalle() + " " + c.getNumero() + " (" + c.getCp() + ")");
        lblDireccion.setBounds(20, 460, 450, 20);
        d.getContentPane().add(lblDireccion);

        JLabel lblFecha = new JLabel("Fecha adopción: " + ad.getFecha());
        lblFecha.setBounds(20, 480, 450, 20);
        d.getContentPane().add(lblFecha);

        // --- Observaciones ---
        JLabel lblObs = new JLabel("Observaciones:");
        lblObs.setBounds(20, 510, 450, 20);
        d.getContentPane().add(lblObs);

        JLabel lblObsValor = new JLabel("<html>" + ad.getObservaciones() + "</html>");
        lblObsValor.setBounds(20, 530, 420, 60);
        lblObsValor.setVerticalAlignment(JLabel.TOP);
        d.getContentPane().add(lblObsValor);

        d.setVisible(true);
    }

    // =======================================================
    //                     PANEL ANIMAL
    // =======================================================

    private void iniPanelAnimal(JPanel panel) {

        // --- Foto ---
        lblFotoAnimal = new JLabel();
        lblFotoAnimal.setBounds(40, 20, 250, 250);
        lblFotoAnimal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFotoAnimal.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblFotoAnimal);

        JButton btnSeleccionarFoto = new JButton("Seleccionar imagen");
        btnSeleccionarFoto.setBounds(40, 280, 250, 30);
        colorearBoton(btnSeleccionarFoto);
        btnSeleccionarFoto.addActionListener(e -> seleccionarImagenAnimal());
        panel.add(btnSeleccionarFoto);

        // --- Campos ---
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(40, 330, 100, 20);
        panel.add(lblId);
        txtIdAnimal = new JTextField();
        txtIdAnimal.setBounds(140, 330, 150, 25);
        txtIdAnimal.setEditable(false);
        panel.add(txtIdAnimal);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(40, 360, 100, 20);
        panel.add(lblNombre);
        txtNombreAnimal = new JTextField();
        txtNombreAnimal.setBounds(140, 360, 150, 25);
        panel.add(txtNombreAnimal);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setBounds(40, 390, 100, 20);
        panel.add(lblEdad);
        txtEdadAnimal = new JTextField();
        txtEdadAnimal.setBounds(140, 390, 150, 25);
        panel.add(txtEdadAnimal);

        JLabel lblEspecie = new JLabel("Especie:");
        lblEspecie.setBounds(40, 420, 100, 20);
        panel.add(lblEspecie);
        txtEspecie = new JTextField();
        txtEspecie.setBounds(140, 420, 150, 25);
        panel.add(txtEspecie);

        JLabel lblRaza = new JLabel("Raza:");
        lblRaza.setBounds(40, 450, 100, 20);
        panel.add(lblRaza);
        txtRaza = new JTextField();
        txtRaza.setBounds(140, 450, 150, 25);
        panel.add(txtRaza);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(40, 480, 100, 20);
        panel.add(lblEstado);
        comboEstadoAnimal = new JComboBox<>();
        comboEstadoAnimal.addItem("Disponible");
        comboEstadoAnimal.addItem("Adoptado");
        comboEstadoAnimal.addItem("En tratamiento");
        comboEstadoAnimal.setBounds(140, 480, 150, 25);
        panel.add(comboEstadoAnimal);

        // --- Medicina ---
        JLabel lblMed = new JLabel("Medicina:");
        lblMed.setBounds(40, 515, 100, 20);
        panel.add(lblMed);
        comboMedAnimal = new JComboBox<>();
        comboMedAnimal.setBounds(140, 515, 250, 25);
        panel.add(comboMedAnimal);
        for (Medicina m : medicinaDAO.selectAllMedicina())
            if (m.getStock() > 0)
                comboMedAnimal.addItem(m);

        // --- Botones ---
        JButton btnGuardar    = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnBorrar     = new JButton("Borrar");
        JButton btnLimpiar    = new JButton("Limpiar");
        btnGuardar.setBounds(12, 560, 95, 30);
        btnActualizar.setBounds(119, 560, 100, 30);
        btnBorrar.setBounds(231, 560, 80, 30);
        btnLimpiar.setBounds(323, 560, 95, 30);
        colorearBoton(btnGuardar);
        colorearBoton(btnActualizar);
        colorearBoton(btnBorrar);
        colorearBoton(btnLimpiar);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnBorrar);
        panel.add(btnLimpiar);

        // --- Tabla ---
        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(420, 20, 600, 600);
        panel.add(scroll);
        tablaAnimal = new JTable();
        scroll.setViewportView(tablaAnimal);
        mostrarAnimales();

        // --- Acción Guardar ---
        btnGuardar.addActionListener(e -> {
            if (!validarAnimal()) return;
            Medicina medSel = (Medicina) comboMedAnimal.getSelectedItem();
            Animal a = Animal.builder()
                    .nombre(txtNombreAnimal.getText().trim())
                    .edad(Integer.parseInt(txtEdadAnimal.getText().trim()))
                    .especie(txtEspecie.getText().trim())
                    .raza(txtRaza.getText().trim())
                    .estado(comboEstadoAnimal.getSelectedItem().toString())
                    .foto(fotoAnimalBytes)
                    .medicinaNombre(medSel != null ? medSel.getNombre() : null)
                    .medicinaTipo(medSel != null ? medSel.getTipoMed() : null)
                    .medicinas(medSel != null ? new ArrayList<>(List.of(medSel)) : new ArrayList<>())
                    .build();
            if (!esValido(a)) return;
            if (medSel != null) {
                medSel.setStock(medSel.getStock() - 1);
                medicinaDAO.updateMedicina(medSel);
            }
            animalDAO.insertAnimal(a);
            mostrarAnimales();
            mostrarMedicinas();
            actualizarComboMedicinas();
            cargarAnimalesDisponibles();
            limpiarCamposAnimal();
            JOptionPane.showMessageDialog(frame,
                    a.getNombre() + " ha sido registrado en el refugio. ✓",
                    "Animal guardado", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Actualizar ---
        btnActualizar.addActionListener(e -> {
            if (txtIdAnimal.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona un animal de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validarAnimal()) return;
            Animal a = animalDAO.selectAnimalById(Integer.parseInt(txtIdAnimal.getText()));
            Medicina medAnterior = medicinaDAO.selectMedicinaByNombreTipo(
                    a.getMedicinaNombre(), a.getMedicinaTipo());
            Medicina medNueva = (Medicina) comboMedAnimal.getSelectedItem();
            if (medAnterior != null && !medAnterior.equals(medNueva)) {
                medAnterior.setStock(medAnterior.getStock() + 1);
                medicinaDAO.updateMedicina(medAnterior);
            }
            if (medNueva != null && !medNueva.equals(medAnterior)) {
                medNueva.setStock(medNueva.getStock() - 1);
                medicinaDAO.updateMedicina(medNueva);
            }
            a.setNombre(txtNombreAnimal.getText().trim());
            a.setEdad(Integer.parseInt(txtEdadAnimal.getText().trim()));
            a.setEspecie(txtEspecie.getText().trim());
            a.setRaza(txtRaza.getText().trim());
            a.setEstado(comboEstadoAnimal.getSelectedItem().toString());
            a.setFoto(fotoAnimalBytes);
            a.setMedicinaNombre(medNueva != null ? medNueva.getNombre() : null);
            a.setMedicinaTipo(medNueva != null ? medNueva.getTipoMed() : null);
            if (!esValido(a)) return;
            animalDAO.updateAnimal(a);
            mostrarAnimales();
            mostrarMedicinas();
            actualizarComboMedicinas();
            cargarAnimalesDisponibles();
            JOptionPane.showMessageDialog(frame,
                    a.getNombre() + " actualizado correctamente. ✓",
                    "Animal actualizado", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Borrar ---
        btnBorrar.addActionListener(e -> {
            if (txtIdAnimal.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona un animal de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Animal a = animalDAO.selectAnimalById(Integer.parseInt(txtIdAnimal.getText()));
            int r = JOptionPane.showOptionDialog(frame,
                    "¿Eliminar a " + a.getNombre() + "? Esta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, new String[]{"Sí, eliminar", "Cancelar"}, "Cancelar");
            if (r != 0) return;
            Medicina med = medicinaDAO.selectMedicinaByNombreTipo(
                    a.getMedicinaNombre(), a.getMedicinaTipo());
            if (med != null) {
                med.setStock(med.getStock() + 1);
                medicinaDAO.updateMedicina(med);
            }
            animalDAO.deleteAnimal(Integer.parseInt(txtIdAnimal.getText()));
            mostrarAnimales();
            mostrarMedicinas();
            actualizarComboMedicinas();
            cargarAnimalesDisponibles();
            limpiarCamposAnimal();
            JOptionPane.showMessageDialog(frame,
                    a.getNombre() + " eliminado del refugio.",
                    "Animal eliminado", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Limpiar ---
        btnLimpiar.addActionListener(e -> {
            limpiarCamposAnimal();
            actualizarComboMedicinas();
            tablaAnimal.clearSelection();
        });

        // --- Selección en tabla ---
        tablaAnimal.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaAnimal.getSelectedRow() != -1) {
                int id = Integer.parseInt(tablaAnimal.getValueAt(
                        tablaAnimal.getSelectedRow(), 0).toString());
                Animal a = animalDAO.selectAnimalById(id);
                txtIdAnimal.setText(String.valueOf(a.getId()));
                txtNombreAnimal.setText(a.getNombre());
                txtEdadAnimal.setText(String.valueOf(a.getEdad()));
                txtEspecie.setText(a.getEspecie());
                txtRaza.setText(a.getRaza());
                comboEstadoAnimal.setSelectedItem(a.getEstado());
                fotoAnimalBytes = a.getFoto();
                lblFotoAnimal.setIcon(bytesToImage(a.getFoto(), 250, 250));

                // Cargar combo incluyendo la medicina actual del animal aunque tenga stock 0
                comboMedAnimal.removeAllItems();
                Medicina medActual = medicinaDAO.selectMedicinaByNombreTipo(
                        a.getMedicinaNombre(), a.getMedicinaTipo());
                for (Medicina m : medicinaDAO.selectAllMedicina()) {
                    if (m.getStock() > 0 || (medActual != null && m.getId() == medActual.getId()))
                        comboMedAnimal.addItem(m);
                }
                comboMedAnimal.setSelectedItem(medActual);
                comboEstadoAnimal.setEnabled(!a.getEstado().equalsIgnoreCase("Adoptado"));
            }
        });
    }

    // =======================================================
    //                    PANEL MEDICINA
    // =======================================================

    private void iniPanelMedicina(JPanel panel) {

        // --- Campos ---
        JLabel lblIdMed = new JLabel("ID:");
        lblIdMed.setBounds(40, 30, 100, 20);
        panel.add(lblIdMed);
        txtIdMed = new JTextField();
        txtIdMed.setBounds(140, 30, 150, 25);
        txtIdMed.setEditable(false);
        panel.add(txtIdMed);

        JLabel lblNombreMed = new JLabel("Nombre:");
        lblNombreMed.setBounds(40, 70, 100, 20);
        panel.add(lblNombreMed);
        txtNombreMed = new JTextField();
        txtNombreMed.setBounds(140, 70, 150, 25);
        panel.add(txtNombreMed);

        JLabel lblTipoMed = new JLabel("Tipo:");
        lblTipoMed.setBounds(40, 110, 100, 20);
        panel.add(lblTipoMed);
        comboTipoMed = new JComboBox<>();
        comboTipoMed.addItem("Vacuna");
        comboTipoMed.addItem("Pipeta");
        comboTipoMed.addItem("Antibiótico");
        comboTipoMed.addItem("Desparasitación");
        comboTipoMed.addItem("Otro");
        comboTipoMed.setBounds(140, 110, 150, 25);
        panel.add(comboTipoMed);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(40, 150, 100, 20);
        panel.add(lblDescripcion);
        txtDescMed = new JTextArea();
        txtDescMed.setLineWrap(true);
        txtDescMed.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescMed);
        scrollDesc.setBounds(40, 180, 250, 100);
        panel.add(scrollDesc);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(40, 290, 100, 20);
        panel.add(lblStock);
        JTextField txtStock = new JTextField("0");
        txtStock.setBounds(140, 290, 80, 25);
        panel.add(txtStock);

        // --- Botones ---
        JButton btnGuardar    = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnBorrar     = new JButton("Borrar");
        JButton btnLimpiar    = new JButton("Limpiar");
        btnGuardar.setBounds(40, 330, 90, 30);
        btnActualizar.setBounds(140, 330, 100, 30);
        btnBorrar.setBounds(142, 400, 90, 30);
        btnLimpiar.setBounds(40, 400, 90, 30);
        colorearBoton(btnGuardar);
        colorearBoton(btnActualizar);
        colorearBoton(btnBorrar);
        colorearBoton(btnLimpiar);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnBorrar);
        panel.add(btnLimpiar);

        // --- Tabla ---
        JScrollPane scrollTabla = new JScrollPane();
        scrollTabla.setBounds(370, 20, 680, 600);
        panel.add(scrollTabla);
        tablaMedicina = new JTable();
        scrollTabla.setViewportView(tablaMedicina);
        mostrarMedicinas();

        // --- Acción Guardar ---
        btnGuardar.addActionListener(e -> {
            if (txtNombreMed.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "El nombre del medicamento no puede estar vacío.",
                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtDescMed.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "La descripción no puede estar vacía.",
                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int stock;
            try {
                stock = Integer.parseInt(txtStock.getText().trim());
                if (stock < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "El stock debe ser un número entero positivo.",
                        "Stock inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Medicina m = Medicina.builder()
                    .nombre(txtNombreMed.getText().trim())
                    .tipoMed(comboTipoMed.getSelectedItem().toString())
                    .descripcion(txtDescMed.getText().trim())
                    .stock(stock)
                    .build();
            if (!esValido(m)) return;
            medicinaDAO.insertMedicina(m);
            mostrarMedicinas();
            actualizarComboMedicinas();
            limpiarCamposMedicina();
            txtStock.setText("0");
            JOptionPane.showMessageDialog(frame,
                    "\"" + m.getNombre() + "\" guardada con " + stock + " unidades en stock. ✓",
                    "Medicina guardada", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Actualizar ---
        btnActualizar.addActionListener(e -> {
            if (txtIdMed.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona una medicina de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtNombreMed.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "El nombre del medicamento no puede estar vacío.",
                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtDescMed.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "La descripción no puede estar vacía.",
                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int stock;
            try {
                stock = Integer.parseInt(txtStock.getText().trim());
                if (stock < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "El stock debe ser un número entero positivo.",
                        "Stock inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Medicina m = medicinaDAO.selectMedicinaById(Integer.parseInt(txtIdMed.getText()));
            m.setNombre(txtNombreMed.getText().trim());
            m.setTipoMed(comboTipoMed.getSelectedItem().toString());
            m.setDescripcion(txtDescMed.getText().trim());
            m.setStock(stock);
            if (!esValido(m)) return;
            medicinaDAO.updateMedicina(m);
            mostrarMedicinas();
            actualizarComboMedicinas();
            JOptionPane.showMessageDialog(frame,
                    "\"" + m.getNombre() + "\" actualizada correctamente. ✓",
                    "Medicina actualizada", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Borrar ---
        btnBorrar.addActionListener(e -> {
            if (txtIdMed.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona una medicina de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtIdMed.getText());
            Medicina m = medicinaDAO.selectMedicinaById(id);
            if (medicinaDAO.tieneAnimalesAsignados(id)) {
                JOptionPane.showMessageDialog(frame,
                        "No se puede eliminar \"" + m.getNombre() + "\" porque está\n" +
                        "asignada a uno o más animales. Retírala primero.",
                        "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int r = JOptionPane.showOptionDialog(frame,
                    "¿Eliminar \"" + m.getNombre() + "\"? Esta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, new String[]{"Sí, eliminar", "Cancelar"}, "Cancelar");
            if (r != 0) return;
            medicinaDAO.deleteMedicina(id);
            mostrarMedicinas();
            actualizarComboMedicinas();
            limpiarCamposMedicina();
            txtStock.setText("0");
            JOptionPane.showMessageDialog(frame,
                    "\"" + m.getNombre() + "\" eliminada correctamente.",
                    "Medicina eliminada", JOptionPane.INFORMATION_MESSAGE);
        });
        
     // --- Acción Limpiar ---
        btnLimpiar.addActionListener(e -> {
            limpiarCamposMedicina();
            txtStock.setText("0");
            comboTipoMed.setSelectedIndex(0);
            tablaMedicina.clearSelection();
        });
        
        // --- Selección en tabla ---
        tablaMedicina.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaMedicina.getSelectedRow() != -1) {
                int idSel = Integer.parseInt(tablaMedicina.getValueAt(
                        tablaMedicina.getSelectedRow(), 0).toString());
                Medicina m = medicinaDAO.selectMedicinaById(idSel);
                txtIdMed.setText(String.valueOf(m.getId()));
                txtNombreMed.setText(m.getNombre());
                comboTipoMed.setSelectedItem(m.getTipoMed());
                txtDescMed.setText(m.getDescripcion());
                txtStock.setText(String.valueOf(m.getStock()));
            }
        });
    }

    // =======================================================
    //                    PANEL CLIENTE
    // =======================================================

    private void iniPanelCliente(JPanel panel) {

        // --- Campos ---
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(40, 30, 100, 20);
        panel.add(lblId);
        txtIdCliente = new JTextField();
        txtIdCliente.setBounds(140, 30, 150, 25);
        txtIdCliente.setEditable(false);
        panel.add(txtIdCliente);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(40, 70, 100, 20);
        panel.add(lblNombre);
        txtNombreCliente = new JTextField();
        txtNombreCliente.setBounds(140, 70, 150, 25);
        panel.add(txtNombreCliente);

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setBounds(40, 110, 100, 20);
        panel.add(lblDni);
        txtDniCliente = new JTextField();
        txtDniCliente.setBounds(140, 110, 150, 25);
        panel.add(txtDniCliente);

        // Calcula la letra automáticamente al escribir 8 números
        txtDniCliente.getDocument().addDocumentListener(new DocumentListener() {
            private void actualizar() {
                String texto = txtDniCliente.getText().trim();
                if (texto.matches("\\d{8}")) {
                    String letra = calcularLetraDni(texto);
                    // Evitar bucle infinito al setear el texto
                    SwingUtilities.invokeLater(() -> {
                        int pos = txtDniCliente.getCaretPosition();
                        txtDniCliente.setText(texto + letra);
                        try { txtDniCliente.setCaretPosition(pos); } catch (Exception ignored) {}
                    });
                }
            }
            public void insertUpdate(DocumentEvent e)  { actualizar(); }
            public void removeUpdate(DocumentEvent e)  { actualizar(); }
            public void changedUpdate(DocumentEvent e) { actualizar(); }
        });

        JLabel lblTel = new JLabel("Teléfono:");
        lblTel.setBounds(40, 150, 100, 20);
        panel.add(lblTel);
        txtTelCliente = new JTextField();
        txtTelCliente.setBounds(140, 150, 150, 25);
        panel.add(txtTelCliente);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(40, 190, 100, 20);
        panel.add(lblEmail);
        txtGmailCliente = new JTextField();
        txtGmailCliente.setBounds(140, 190, 150, 25);
        panel.add(txtGmailCliente);

        JLabel lblCalle = new JLabel("Calle:");
        lblCalle.setBounds(40, 230, 100, 20);
        panel.add(lblCalle);
        txtCalle = new JTextField();
        txtCalle.setBounds(140, 230, 200, 25);
        panel.add(txtCalle);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(40, 270, 100, 20);
        panel.add(lblNumero);
        txtNumero = new JTextField();
        txtNumero.setBounds(140, 270, 80, 25);
        panel.add(txtNumero);

        JLabel lblCiudad = new JLabel("Ciudad:");
        lblCiudad.setBounds(40, 310, 100, 20);
        panel.add(lblCiudad);
        txtCiudad = new JTextField();
        txtCiudad.setBounds(140, 310, 150, 25);
        panel.add(txtCiudad);

        JLabel lblCp = new JLabel("CP:");
        lblCp.setBounds(40, 350, 100, 20);
        panel.add(lblCp);
        txtCp = new JTextField();
        txtCp.setBounds(140, 350, 80, 25);
        panel.add(txtCp);

        // --- Botones ---
        JButton btnGuardar    = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnBorrar     = new JButton("Borrar");
        JButton btnLimpiar    = new JButton("Limpiar");
        btnGuardar.setBounds(40, 400, 90, 30);
        btnActualizar.setBounds(140, 400, 100, 30);
        btnBorrar.setBounds(150, 460, 90, 30);
        btnLimpiar.setBounds(40, 460, 90, 30);
        colorearBoton(btnGuardar);
        colorearBoton(btnActualizar);
        colorearBoton(btnBorrar);
        colorearBoton(btnLimpiar);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnBorrar);
        panel.add(btnLimpiar);

        // --- Tabla ---
        JScrollPane scrollTabla = new JScrollPane();
        scrollTabla.setBounds(360, 20, 500, 600);
        panel.add(scrollTabla);
        tablaCliente = new JTable();
        scrollTabla.setViewportView(tablaCliente);
        mostrarClientes();

     // Guardar
        btnGuardar.addActionListener(e -> {
            String dni = txtDniCliente.getText().trim();
            if (validarDni(dni)) {
                JOptionPane.showMessageDialog(frame,
                        "Este DNI ya está registrado. No se permiten duplicados.",
                        "DNI duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Cliente c = Cliente.builder()
                    .nombre(txtNombreCliente.getText().trim())
                    .dni(dni)
                    .telefono(txtTelCliente.getText().trim())
                    .gmail(txtGmailCliente.getText().trim())
                    .calle(txtCalle.getText().trim())
                    .numero(txtNumero.getText().trim())
                    .ciudad(txtCiudad.getText().trim())
                    .cp(txtCp.getText().trim())
                    .build();
            if (!esValido(c)) return; // <-- aquí dispara todas las validaciones del modelo
            clienteDAO.insertCliente(c);
            mostrarClientes();
            cargarClientesEnCombo();
            limpiarCamposCliente();
            JOptionPane.showMessageDialog(frame,
                    c.getNombre() + " registrado correctamente. ✓",
                    "Cliente guardado", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Borrar ---
        btnBorrar.addActionListener(e -> {
            if (txtIdCliente.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona un cliente de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Cliente c = clienteDAO.selectClienteById(
                    Integer.parseInt(txtIdCliente.getText()));
            int r = JOptionPane.showOptionDialog(frame,
                    "¿Eliminar a " + c.getNombre() + "? Esta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, new String[]{"Sí, eliminar", "Cancelar"}, "Cancelar");
            if (r != 0) return;
            clienteDAO.deleteCliente(Integer.parseInt(txtIdCliente.getText()));
            mostrarClientes();
            cargarClientesEnCombo();
            mostrarAdopciones();
            limpiarCamposCliente();
            JOptionPane.showMessageDialog(frame,
                    c.getNombre() + " eliminado correctamente.",
                    "Cliente eliminado", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Limpiar ---
        btnLimpiar.addActionListener(e -> {
            limpiarCamposCliente();
            tablaCliente.clearSelection();
        });
        
        // --- Selección en tabla ---
        tablaCliente.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCliente.getSelectedRow() != -1) {
                int idSel = Integer.parseInt(tablaCliente.getValueAt(
                        tablaCliente.getSelectedRow(), 0).toString());
                Cliente c = clienteDAO.selectClienteById(idSel);
                txtIdCliente.setText(String.valueOf(c.getId()));
                txtNombreCliente.setText(c.getNombre());
                txtDniCliente.setText(c.getDni());
                txtTelCliente.setText(c.getTelefono());
                txtGmailCliente.setText(c.getGmail());
                txtCalle.setText(c.getCalle());
                txtNumero.setText(c.getNumero());
                txtCiudad.setText(c.getCiudad());
                txtCp.setText(c.getCp());
            }
        });
    }

    // =======================================================
    //                   PANEL ADOPCIÓN
    // =======================================================

    private void iniPanelAdopcion(JPanel panel) {

        // --- Campos ---
    
    	
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(40, 30, 100, 20);
        panel.add(lblId);
        txtIdAdopcion = new JTextField();
        txtIdAdopcion.setBounds(140, 30, 150, 25);
        txtIdAdopcion.setEditable(false);
        panel.add(txtIdAdopcion);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(40, 70, 100, 20);
        panel.add(lblFecha);
        dateChooserAdopcion = new JDateChooser();
        dateChooserAdopcion.setBounds(140, 70, 150, 25);
        panel.add(dateChooserAdopcion);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setBounds(40, 110, 100, 20);
        panel.add(lblCliente);
        comboClienteAdopcion = new JComboBox<>();
        comboClienteAdopcion.setBounds(140, 110, 200, 25);
        panel.add(comboClienteAdopcion);
        cargarClientesEnCombo();

        JLabel lblAnimal = new JLabel("Animal:");
        lblAnimal.setBounds(40, 150, 100, 20);
        panel.add(lblAnimal);
        comboAnimalAdopcion = new JComboBox<>();
        comboAnimalAdopcion.setBounds(140, 150, 300, 25);
        panel.add(comboAnimalAdopcion);
        cargarAnimalesDisponibles();
        
        

        // --- Foto y medicinas del animal seleccionado ---
        lblFotoAdop = new JLabel();
        lblFotoAdop.setBounds(40, 200, 150, 150);
        lblFotoAdop.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFotoAdop.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblFotoAdop);

        JLabel lblMedsAdopTitulo = new JLabel("Medicinas del animal:");
        lblMedsAdopTitulo.setBounds(210, 200, 200, 20);
        panel.add(lblMedsAdopTitulo);

        txtMedsAdop = new JLabel();
        txtMedsAdop.setBounds(210, 225, 260, 125);
        txtMedsAdop.setVerticalAlignment(JLabel.TOP);
        panel.add(txtMedsAdop);

        comboAnimalAdopcion.addActionListener(e -> {
            Animal a = (Animal) comboAnimalAdopcion.getSelectedItem();
            if (a != null) {
                Animal aConMedicinas = animalDAO.selectAnimalByIdConMedicinas(a.getId());
                lblFotoAdop.setIcon(aConMedicinas.getFoto() != null
                        ? bytesToImage(aConMedicinas.getFoto(), 150, 150) : null);
                StringBuilder sb = new StringBuilder();
                if (aConMedicinas.getMedicinas() != null && !aConMedicinas.getMedicinas().isEmpty())
                    for (Medicina m : aConMedicinas.getMedicinas())
                        sb.append("- ").append(m.getNombre())
                          .append(" (").append(m.getTipoMed()).append(")\n");
                txtMedsAdop.setText("<html>" + sb.toString().replace("\n", "<br>") + "</html>");
            } else {
                lblFotoAdop.setIcon(null);
                txtMedsAdop.setText("");
            }
        });

        // --- Observaciones ---
        JLabel lblObs = new JLabel("Observaciones:");
        lblObs.setBounds(40, 370, 140, 20);
        panel.add(lblObs);
        txtObservacionesAdopcion = new JTextArea();
        txtObservacionesAdopcion.setLineWrap(true);
        txtObservacionesAdopcion.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObservacionesAdopcion);
        scrollObs.setBounds(40, 395, 260, 100);
        panel.add(scrollObs);

        // --- Botones ---
        JButton btnGuardar    = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnBorrar     = new JButton("Borrar");
        JButton btnLimpiar    = new JButton("Limpiar");
        btnGuardar.setBounds(40, 510, 90, 30);
        btnActualizar.setBounds(140, 510, 100, 30);
        btnBorrar.setBounds(250, 510, 90, 30);
        btnLimpiar.setBounds(350, 510, 90, 30);
        colorearBoton(btnGuardar);
        colorearBoton(btnActualizar);
        colorearBoton(btnBorrar);
        colorearBoton(btnLimpiar);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnBorrar);
        panel.add(btnLimpiar);
        
        // --- Tabla ---
        JScrollPane scrollTabla = new JScrollPane();
        scrollTabla.setBounds(500, 20, 550, 600);
        panel.add(scrollTabla);
        tablaAdopcion = new JTable();
        scrollTabla.setViewportView(tablaAdopcion);
        mostrarAdopciones();

        // --- Acción Guardar ---
     // --- Acción Guardar ---
        btnGuardar.addActionListener(e -> {
            if (comboClienteAdopcion.getSelectedItem() == null ||
                comboAnimalAdopcion.getSelectedItem() == null  ||
                dateChooserAdopcion.getDate() == null) {
                JOptionPane.showMessageDialog(frame,
                        "Necesitas seleccionar cliente, animal y fecha para continuar.",
                        "Faltan datos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Animal animalSel   = (Animal) comboAnimalAdopcion.getSelectedItem();
            Cliente clienteSel = (Cliente) comboClienteAdopcion.getSelectedItem();

            if (adopcionDAO.animalYaAdoptado(animalSel.getId())) {
                JOptionPane.showMessageDialog(frame,
                        animalSel.getNombre() + " ya tiene una adopción registrada.",
                        "Animal no disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate fechaLocal = dateChooserAdopcion.getDate()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Animal animalFresco = animalDAO.selectAnimalById(animalSel.getId());
            animalFresco.setEstado("Adoptado");
            animalDAO.updateAnimal(animalFresco);

            Animal animalRef = new Animal();
            animalRef.setId(animalFresco.getId());
            Cliente clienteRef = new Cliente();
            clienteRef.setId(clienteSel.getId());

            Adopcion ad = Adopcion.builder()
                    .fecha(fechaLocal)
                    .observaciones(txtObservacionesAdopcion.getText())
                    .cliente(clienteRef)
                    .animal(animalRef)
                    .build();
            if (!esValido(ad)) return;
            adopcionDAO.insertAdopcion(ad);
            mostrarAdopciones();
            mostrarAnimales();
            mostrarClientes();
            cargarAnimalesDisponibles();
            cargarClientesEnCombo();
            JOptionPane.showMessageDialog(frame,
                    "🎉 ¡" + animalSel.getNombre() + " ha encontrado hogar con "
                    + clienteSel.getNombre() + "!",
                    "Adopción registrada", JOptionPane.INFORMATION_MESSAGE);
            Adopcion adMostrar = new Adopcion();
            adMostrar.setFecha(fechaLocal);
            adMostrar.setObservaciones(txtObservacionesAdopcion.getText());
            adMostrar.setCliente(clienteSel);
            adMostrar.setAnimal(animalSel);
            mostrarFichaAdopcion(adMostrar);
            limpiarCamposAdopcion();
            comboAnimalAdopcion.setEnabled(true);
            lblFotoAdop.setIcon(null);
            txtMedsAdop.setText("");
        });

        // --- Acción Actualizar ---
        btnActualizar.addActionListener(e -> {
            if (txtIdAdopcion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona una adopción de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dateChooserAdopcion.getDate() == null) {
                JOptionPane.showMessageDialog(frame,
                        "La fecha no puede estar vacía.",
                        "Fecha requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtIdAdopcion.getText());
            Adopcion ad = adopcionDAO.selectAdopcionById(id);
            LocalDate fechaLocal = dateChooserAdopcion.getDate()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Animal animalSel   = (Animal) comboAnimalAdopcion.getSelectedItem();
            Cliente clienteSel = (Cliente) comboClienteAdopcion.getSelectedItem();

            Animal animalRef = new Animal();
            animalRef.setId(animalSel.getId());
            Cliente clienteRef = new Cliente();
            clienteRef.setId(clienteSel.getId());

            ad.setFecha(fechaLocal);
            ad.setObservaciones(txtObservacionesAdopcion.getText());
            ad.setCliente(clienteRef);
            ad.setAnimal(animalRef);
            if (!esValido(ad)) return;
            adopcionDAO.updateAdopcion(ad);
            mostrarAdopciones();
            mostrarClientes();
            cargarClientesEnCombo();
            JOptionPane.showMessageDialog(frame,
                    "Adopción actualizada correctamente. ✓",
                    "Adopción actualizada", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Borrar ---
        btnBorrar.addActionListener(e -> {
            if (txtIdAdopcion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Primero selecciona una adopción de la tabla.",
                        "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtIdAdopcion.getText());
            Adopcion ad = adopcionDAO.selectAdopcionById(id);
            int r = JOptionPane.showOptionDialog(frame,
                    "¿Cancelar la adopción de " + ad.getAnimal().getNombre() + "?\n"
                    + "El animal volverá a estar disponible.",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, new String[]{"Sí, cancelar adopción", "No"}, "No");
            if (r != 0) return;
            Animal animalFresco = animalDAO.selectAnimalById(ad.getAnimal().getId());
            animalFresco.setEstado("Disponible");
            animalDAO.updateAnimal(animalFresco);
            adopcionDAO.deleteAdopcion(id);
            mostrarAdopciones();
            mostrarAnimales();
            mostrarClientes();
            cargarAnimalesDisponibles();
            cargarClientesEnCombo();
            limpiarCamposAdopcion();
            lblFotoAdop.setIcon(null);
            txtMedsAdop.setText("");
            JOptionPane.showMessageDialog(frame,
                    animalFresco.getNombre() + " vuelve a estar disponible para adopción.",
                    "Adopción cancelada", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Acción Limpiar ---
        btnLimpiar.addActionListener(e -> {
            limpiarCamposAdopcion();
            cargarAnimalesDisponibles();
            cargarClientesEnCombo();
            lblFotoAdop.setIcon(null);
            txtMedsAdop.setText("");
            comboAnimalAdopcion.setEnabled(true);
            tablaAdopcion.clearSelection();
        });
        
        // --- Selección en tabla ---
        tablaAdopcion.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaAdopcion.getSelectedRow() != -1) {
                int id = Integer.parseInt(tablaAdopcion.getValueAt(
                        tablaAdopcion.getSelectedRow(), 0).toString());
                Adopcion ad = adopcionDAO.selectAdopcionById(id);
                cargarDatosAdopcion(ad);
                mostrarFichaAdopcion(ad);
               
                Animal a = animalDAO.selectAnimalByIdConMedicinas(ad.getAnimal().getId());
                lblFotoAdop.setIcon(a.getFoto() != null
                        ? bytesToImage(a.getFoto(), 150, 150) : null);
                StringBuilder sb = new StringBuilder();
                if (a.getMedicinas() != null && !a.getMedicinas().isEmpty())
                    for (Medicina m : a.getMedicinas())
                        sb.append("- ").append(m.getNombre())
                          .append(" (").append(m.getTipoMed()).append(")\n");
                txtMedsAdop.setText("<html>" + sb.toString().replace("\n", "<br>") + "</html>");
                comboAnimalAdopcion.setEnabled(false);
            }
        });
    
    }
}