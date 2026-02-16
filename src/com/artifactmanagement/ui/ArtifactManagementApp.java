package com.artifactmanagement.ui;

import com.artifactmanagement.dao.ArtifactDAO;
import com.artifactmanagement.model.Artifact;
import com.artifactmanagement.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ArtifactManagementApp extends JFrame {

    private ArtifactDAO artifactDAO;
    private JPanel mainPanel;
    private JPanel artifactGridPanel;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;

    // UI Components
    private JTextField searchField;
    private JButton addButton;
    private JButton refreshButton;

    public ArtifactManagementApp() {
        artifactDAO = new ArtifactDAO();
        initializeUI();
        loadArtifacts();
    }

    private void initializeUI() {
        setTitle("Artifact Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create menu bar
        createMenuBar();

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        artifactGridPanel = new JPanel();
        artifactGridPanel.setLayout(new GridLayout(0, 4, 20, 20)); // 4 columns with gaps
        artifactGridPanel.setBackground(new Color(245, 245, 245));
        artifactGridPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        scrollPane = new JScrollPane(artifactGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        if (!DatabaseConnection.testConnection()) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed!\nPlease check your MySQL configuration.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Artifact menu
        JMenu artifactMenu = new JMenu("Artifact");
        JMenuItem addItem = new JMenuItem("Add New Artifact");
        addItem.addActionListener(e -> openAddArtifactDialog());
        artifactMenu.add(addItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(artifactMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Artifact Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(52, 73, 94));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addActionListener(e -> performSearch());

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        styleButton(searchButton, new Color(41, 128, 185));

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(searchButton);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(new Color(52, 73, 94));

        addButton = new JButton("Add New Artifact");
        addButton.addActionListener(e -> openAddArtifactDialog());
        styleButton(addButton, new Color(39, 174, 96));

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadArtifacts());
        styleButton(refreshButton, new Color(230, 126, 34));

        actionPanel.add(refreshButton);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(addButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        return headerPanel;
    }


    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    private void loadArtifacts() {
        artifactGridPanel.removeAll();

        List<Artifact> artifacts = artifactDAO.getAllArtifacts();

        if (artifacts.isEmpty()) {
            JLabel emptyLabel = new JLabel("<html><center>No artifacts found<br>Click 'Add New Artifact' to get started</center></html>");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            artifactGridPanel.add(emptyLabel);
        } else {
            for (Artifact artifact : artifacts) {
                JPanel artifactCard = createArtifactCard(artifact);
                artifactGridPanel.add(artifactCard);
            }
        }

        artifactGridPanel.revalidate();
        artifactGridPanel.repaint();
    }


    private JPanel createArtifactCard(Artifact artifact) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(250, 400));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(240, 240, 240));
        imagePanel.setPreferredSize(new Dimension(230, 250));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        ImageIcon thumbnailIcon = artifact.getThumbnailIcon(240, 220);
        if (thumbnailIcon != null) {
            imageLabel.setIcon(thumbnailIcon);
        } else {
            imageLabel.setText("No Image");
            imageLabel.setForeground(Color.GRAY);
        }

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(artifact.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoryLabel = new JLabel("Category: " + artifact.getCategory());
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(new Color(100, 100, 100));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel("<html>" + artifact.getShortDescription(50) + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descriptionLabel.setForeground(new Color(120, 120, 120));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descriptionLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = new JButton("View Details");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 10));
        viewButton.setPreferredSize(new Dimension(100, 25));
        viewButton.addActionListener(e -> openArtifactDetailDialog(artifact));
        styleButton(viewButton, new Color(52, 152, 219));

        buttonPanel.add(viewButton);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openArtifactDetailDialog(artifact);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(248, 248, 248));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private void openAddArtifactDialog() {
        AddEditArtifactDialog dialog = new AddEditArtifactDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isArtifactSaved()) {
            loadArtifacts(); // Refresh the grid
        }
    }

    private void openArtifactDetailDialog(Artifact artifact) {
        ArtifactDetailDialog dialog = new ArtifactDetailDialog(this, artifact);
        dialog.setVisible(true);
        if (dialog.isDataChanged()) {
            loadArtifacts(); // Refresh the grid if data was changed
        }
    }

    /**
     * Performs search based on the search field text
     */
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        artifactGridPanel.removeAll();

        List<Artifact> artifacts;
        if (searchTerm.isEmpty()) {
            artifacts = artifactDAO.getAllArtifacts();
        } else {
            artifacts = artifactDAO.searchArtifacts(searchTerm);
        }

        if (artifacts.isEmpty()) {
            JLabel emptyLabel = new JLabel("<html><center>No artifacts found matching '" + searchTerm + "'</center></html>");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            artifactGridPanel.add(emptyLabel);
        } else {
            for (Artifact artifact : artifacts) {
                JPanel artifactCard = createArtifactCard(artifact);
                artifactGridPanel.add(artifactCard);
            }
        }

        artifactGridPanel.revalidate();
        artifactGridPanel.repaint();
    }

    /**
     * Shows the about dialog
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Artifact Management System\nVersion 1.0\n\nDeveloped using Java Swing and MySQL",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ArtifactManagementApp().setVisible(true);
        });
    }
}
