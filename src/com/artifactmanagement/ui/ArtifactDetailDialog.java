package com.artifactmanagement.ui;

import com.artifactmanagement.dao.ArtifactDAO;
import com.artifactmanagement.model.Artifact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// * Dialog for displaying full artifact details with edit and delete options
public class ArtifactDetailDialog extends JDialog {

    private ArtifactDAO artifactDAO;
    private Artifact artifact;
    private boolean dataChanged = false;

    // UI Components
    private JLabel imageLabel;
    private JLabel nameLabel;
    private JTextArea descriptionArea;
    private JLabel categoryLabel;
    private JLabel originLabel;
    private JLabel yearLabel;
    private JLabel createdDateLabel;
    private JLabel modifiedDateLabel;

    private JButton editButton;
    private JButton deleteButton;
    private JButton closeButton;

    public ArtifactDetailDialog(JFrame parent, Artifact artifact) {
        super(parent, "Artifact Details", true);
        this.artifact = artifact;
        this.artifactDAO = new ArtifactDAO();

        initializeUI();
        populateData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(700, 600);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createHeaderPanel();

        JPanel contentPanel = createContentPanel();

        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Artifact Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Left panel for image
        JPanel imagePanel = createImagePanel();

        // Right panel for details
        JPanel detailsPanel = createDetailsPanel();

        contentPanel.add(imagePanel, BorderLayout.WEST);
        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Artifact Image",
                0, 0,
                new Font("Arial", Font.BOLD, 14)));
        imagePanel.setPreferredSize(new Dimension(300, 400));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(280, 350));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        imageLabel.setBackground(new Color(250, 250, 250));
        imageLabel.setOpaque(true);

        JPanel imageWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imageWrapper.setBackground(Color.WHITE);
        imageWrapper.add(imageLabel);

        imagePanel.add(imageWrapper, BorderLayout.CENTER);

        return imagePanel;
    }


    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Artifact Information",
                0, 0,
                new Font("Arial", Font.BOLD, 14)));

        JPanel namePanel = createDetailRow("Name:", Color.BLACK, 18, true);
        nameLabel = (JLabel) namePanel.getComponent(1);
        detailsPanel.add(namePanel);
        detailsPanel.add(Box.createVerticalStrut(15));

        JPanel categoryPanel = createDetailRow("Category:", new Color(52, 152, 219), 14, false);
        categoryLabel = (JLabel) categoryPanel.getComponent(1);
        detailsPanel.add(categoryPanel);
        detailsPanel.add(Box.createVerticalStrut(10));

        JPanel originPanel = createDetailRow("Origin:", new Color(39, 174, 96), 14, false);
        originLabel = (JLabel) originPanel.getComponent(1);
        detailsPanel.add(originPanel);
        detailsPanel.add(Box.createVerticalStrut(10));

        JPanel yearPanel = createDetailRow("Year Discovered:", new Color(230, 126, 34), 14, false);
        yearLabel = (JLabel) yearPanel.getComponent(1);
        detailsPanel.add(yearPanel);
        detailsPanel.add(Box.createVerticalStrut(15));

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(Color.WHITE);
        descPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descTitleLabel = new JLabel("Description:");
        descTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descTitleLabel.setForeground(new Color(85, 85, 85));

        descriptionArea = new JTextArea(6, 30);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(new Color(248, 248, 248));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        descPanel.add(descTitleLabel, BorderLayout.NORTH);
        descPanel.add(Box.createVerticalStrut(5), BorderLayout.CENTER);
        descPanel.add(descScrollPane, BorderLayout.SOUTH);

        detailsPanel.add(descPanel);
        detailsPanel.add(Box.createVerticalStrut(15));

        JPanel timestampPanel = createTimestampPanel();
        detailsPanel.add(timestampPanel);

        detailsPanel.add(Box.createVerticalGlue());

        return detailsPanel;
    }

    private JPanel createDetailRow(String labelText, Color valueColor, int fontSize, boolean isBold) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(85, 85, 85));
        label.setPreferredSize(new Dimension(125, 20));

        JLabel valueLabel = new JLabel();
        valueLabel.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, fontSize));
        valueLabel.setForeground(valueColor);

        rowPanel.add(label);
        rowPanel.add(valueLabel);

        return rowPanel;
    }

    private JPanel createTimestampPanel() {
        JPanel timestampPanel = new JPanel();
        timestampPanel.setLayout(new BoxLayout(timestampPanel, BoxLayout.Y_AXIS));
        timestampPanel.setBackground(Color.WHITE);
        timestampPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timestampPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Record Information"));

        // Created date
        JPanel createdPanel = createDetailRow("Created:", new Color(120, 120, 120), 12, false);
        createdDateLabel = (JLabel) createdPanel.getComponent(1);
        timestampPanel.add(createdPanel);

        // Modified date
        JPanel modifiedPanel = createDetailRow("Modified:", new Color(120, 120, 120), 12, false);
        modifiedDateLabel = (JLabel) modifiedPanel.getComponent(1);
        timestampPanel.add(modifiedPanel);

        return timestampPanel;
    }

    /**
     * Creates the button panel
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        styleButton(closeButton, new Color(149, 165, 166));

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteArtifact());
        styleButton(deleteButton, new Color(231, 76, 60));

        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editArtifact());
        styleButton(editButton, new Color(52, 152, 219));

        buttonPanel.add(closeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        return buttonPanel;
    }

    /**
     * Styles a button with custom appearance
     */
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    /**
     * Populates the dialog with artifact data
     */
    private void populateData() {
        if (artifact != null) {
            nameLabel.setText(artifact.getName());
            categoryLabel.setText(artifact.getCategory() != null ? artifact.getCategory() : "Not specified");
            originLabel.setText(artifact.getOrigin() != null ? artifact.getOrigin() : "Unknown");

            if (artifact.getYearDiscovered() > 0) {
                yearLabel.setText(String.valueOf(artifact.getYearDiscovered()));
            } else {
                yearLabel.setText("Not specified");
            }

            descriptionArea.setText(artifact.getDescription() != null ? artifact.getDescription() : "No description available");

            // Display image
            ImageIcon imageIcon = artifact.getImageIcon();
            if (imageIcon != null) {
                Image img = imageIcon.getImage();
                Image scaledImg = img.getScaledInstance(260, 320, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
                imageLabel.setText("");
            } else {
                imageLabel.setText("No Image Available");
                imageLabel.setForeground(Color.GRAY);
                imageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            }

            // Display timestamps
            if (artifact.getCreatedDate() != null) {
                createdDateLabel.setText(artifact.getCreatedDate().toString());
            } else {
                createdDateLabel.setText("Unknown");
            }

            if (artifact.getModifiedDate() != null) {
                modifiedDateLabel.setText(artifact.getModifiedDate().toString());
            } else {
                modifiedDateLabel.setText("Unknown");
            }
        }
    }

    /**
     * Opens the edit dialog
     */
    private void editArtifact() {
        AddEditArtifactDialog editDialog = new AddEditArtifactDialog((JFrame) getParent(), artifact);
        editDialog.setVisible(true);

        if (editDialog.isArtifactSaved()) {
            // Refresh the artifact data
            artifact = artifactDAO.getArtifactById(artifact.getId());
            populateData();
            dataChanged = true;
        }
    }

    /**
     * Deletes the artifact
     */
    private void deleteArtifact() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this artifact?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            boolean success = artifactDAO.deleteArtifact(artifact.getId());

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Artifact deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dataChanged = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete artifact.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Returns whether data was changed (edited or deleted)
     */
    public boolean isDataChanged() {
        return dataChanged;
    }
}
