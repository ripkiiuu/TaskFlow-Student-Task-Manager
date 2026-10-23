
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class TaskFlow {

    private static final Color BG = new Color(245, 247, 251);
    private static final Color PRIMARY = new Color(55, 90, 180);
    private static final Color TEXT = new Color(35, 45, 65);
    private static final Color BORDER = new Color(225, 230, 240);
    private static final Color GREEN = new Color(35, 145, 100);
    private static final Color RED = new Color(190, 65, 65);

    private static DefaultTableModel model;
    private static JTable table;
    private static TableRowSorter<DefaultTableModel> sorter;

    private static JTextField taskField;
    private static JComboBox<String> subjectBox;
    private static JComboBox<String> priorityBox;
    private static JComboBox<String> filterBox;

    private static JLabel totalLabel;
    private static JLabel pendingLabel;
    private static JLabel completedLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskFlow::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("TaskFlow | Student Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 760);
        frame.setMinimumSize(new Dimension(850, 620));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(
                22, 30, 22, 30));

        JLabel title = new JLabel("TaskFlow");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setForeground(PRIMARY);

        JLabel subtitle = new JLabel(
                "Kelola tugas kuliahmu dengan lebih teratur");
        subtitle.setForeground(Color.GRAY);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        header.add(titlePanel, BorderLayout.WEST);

        // STATISTICS
        JPanel stats = new JPanel(new GridBagLayout());
        stats.setOpaque(false);

        totalLabel = new JLabel("Total: 0");
        pendingLabel = new JLabel("Belum selesai: 0");
        completedLabel = new JLabel("Selesai: 0");

        styleStat(totalLabel, PRIMARY);
        styleStat(pendingLabel, RED);
        styleStat(completedLabel, GREEN);

        GridBagConstraints sc = new GridBagConstraints();
        sc.insets = new Insets(0, 6, 0, 6);
        sc.gridx = 0;
        stats.add(totalLabel, sc);
        sc.gridx = 1;
        stats.add(pendingLabel, sc);
        sc.gridx = 2;
        stats.add(completedLabel, sc);

        header.add(stats, BorderLayout.EAST);

        // INPUT FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        taskField = new JTextField();
        taskField.setPreferredSize(new Dimension(180, 38));
        taskField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        subjectBox = new JComboBox<>(new String[]{
            "Pemrograman Berorientasi Objek",
            "Pemrograman Web",
            "Basis Data",
            "Kecerdasan Buatan",
            "Lainnya"
        });

        priorityBox = new JComboBox<>(new String[]{
            "Rendah", "Sedang", "Tinggi"
        });

        JButton addButton = createButton("Tambah Tugas", PRIMARY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Nama Tugas"), gbc);

        gbc.gridx = 1;
        form.add(new JLabel("Mata Kuliah"), gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Prioritas"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(taskField, gbc);

        gbc.gridx = 1;
        form.add(subjectBox, gbc);

        gbc.gridx = 2;
        form.add(priorityBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        form.add(addButton, gbc);

        // TABLE MODEL
        model = new DefaultTableModel(
                new String[]{
                    "Nama Tugas", "Mata Kuliah",
                    "Prioritas", "Status"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setSelectionBackground(new Color(220, 230, 255));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(
                new Color(235, 240, 250));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));

        // ACTION BUTTONS
        JButton completeButton = createButton(
                "Tandai Selesai", GREEN);
        JButton deleteButton = createButton(
                "Hapus Tugas", RED);

        JPanel actions = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(completeButton);
        actions.add(deleteButton);

        // FILTER
        filterBox = new JComboBox<>(new String[]{
            "Semua Tugas", "Belum selesai", "Selesai"
        });
        filterBox.setPreferredSize(new Dimension(170, 34));

        JPanel filterPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter status:"));
        filterPanel.add(filterBox);

        JPanel tableToolbar = new JPanel(new BorderLayout());
        tableToolbar.setOpaque(false);
        tableToolbar.add(actions, BorderLayout.WEST);
        tableToolbar.add(filterPanel, BorderLayout.EAST);

        JLabel sectionTitle = new JLabel("Daftar Tugas");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 19));
        sectionTitle.setForeground(TEXT);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
        tablePanel.setOpaque(false);
        tablePanel.add(sectionTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(tableToolbar, BorderLayout.SOUTH);

        // MAIN CONTENT
        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(
                22, 30, 22, 30));
        content.add(form, BorderLayout.NORTH);
        content.add(tablePanel, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "TaskFlow | Student Task Manager",
                SwingConstants.CENTER);
        footer.setForeground(Color.GRAY);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // EVENT HANDLING
        Runnable addTask = () -> {
            String name = taskField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Nama tugas tidak boleh kosong!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                taskField.requestFocusInWindow();
                return;
            }

            model.addRow(new Object[]{
                name,
                subjectBox.getSelectedItem(),
                priorityBox.getSelectedItem(),
                "Belum selesai"
            });

            taskField.setText("");
            taskField.requestFocusInWindow();
            updateStats();
        };

        // Interface 1: ActionListener
        addButton.addActionListener(e -> addTask.run());

        // Interface 2: KeyListener
        taskField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Tidak ada aksi saat karakter diketik.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask.run();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Tidak ada aksi saat tombol dilepas.
            }
        });

        completeButton.addActionListener(e -> {
            int viewRow = table.getSelectedRow();

            if (viewRow == -1) {
                showSelectWarning(frame);
                return;
            }

            int row = table.convertRowIndexToModel(viewRow);
            String status = model.getValueAt(row, 3).toString();

            if (status.equals("Selesai")) {
                model.setValueAt("Belum selesai", row, 3);
            } else {
                model.setValueAt("Selesai", row, 3);
            }

            updateStats();
        });

        deleteButton.addActionListener(e -> {
            int viewRow = table.getSelectedRow();

            if (viewRow == -1) {
                showSelectWarning(frame);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Yakin ingin menghapus tugas yang dipilih?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int row = table.convertRowIndexToModel(viewRow);
                model.removeRow(row);
                updateStats();
            }
        });

        filterBox.addActionListener(e -> {
            String filter = (String) filterBox.getSelectedItem();

            if ("Belum selesai".equals(filter)) {
                sorter.setRowFilter(RowFilter.regexFilter(
                        "^Belum selesai$", 3));
            } else if ("Selesai".equals(filter)) {
                sorter.setRowFilter(RowFilter.regexFilter(
                        "^Selesai$", 3));
            } else {
                sorter.setRowFilter(null);
            }
        });

        frame.add(header, BorderLayout.NORTH);
        frame.add(content, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        updateStats();
        frame.setVisible(true);
    }

    private static JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 36));
        return button;
    }

    private static void styleStat(JLabel label, Color color) {
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(color);
    }

    private static void updateStats() {
        int total = model.getRowCount();
        int completed = 0;

        for (int i = 0; i < total; i++) {
            if ("Selesai".equals(model.getValueAt(i, 3))) {
                completed++;
            }
        }

        totalLabel.setText("Total: " + total);
        pendingLabel.setText(
                "Belum selesai: " + (total - completed));
        completedLabel.setText("Selesai: " + completed);
    }

    private static void showSelectWarning(JFrame frame) {
        JOptionPane.showMessageDialog(
                frame,
                "Pilih satu tugas dari tabel terlebih dahulu.",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
