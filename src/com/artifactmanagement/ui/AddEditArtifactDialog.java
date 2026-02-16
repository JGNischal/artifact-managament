package com.artifactmanagement.ui;

import com.artifactmanagement.dao.ArtifactDAO;
import com.artifactmanagement.model.Artifact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Dialog for adding new artifacts or editing existing ones
 */
public class AddEditArtifactDialog extends JDialog {

    private ArtifactDAO artifactDAO;
    private Artifact artifact;
    private boolean isEditMode;
    private boolean artifactSaved = false;

    // Form components
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField categoryField;
    private JTextField originField;
    private JTextField yearField;
    private JLabel imageLabel;
    private JButton imageButton;
    private JButton submitButton;
    private JButton resetButton;
    private JButton cancelButton;

    // Image data
    private byte[] imageData;
    private String imageName;

    /**
     * Constructor for adding new artifact
     */
    public AddEditArtifactDialog(JFrame parent, Artifact artifact) {
        super(parent, artifact == null ? "Add New Artifact" : "Edit Artifact", true);
        this.artifact = artifact;
        this.isEditMode = (artifact != null);
        this.artifactDAO = new ArtifactDAO();

        initializeUI();
        if (isEditMode) {
            populateFields();
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(isEditMode ? "Edit Artifact" : "Add New Artifact");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);

        JPanel formPanel = createFormPanel();

        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }


    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Artifact Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Artifact Name:*"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.3;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(descScrollPane, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        categoryField = new JTextField(20);
        categoryField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(categoryField, gbc);

        // Origin
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Origin:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        originField = new JTextField(20);
        originField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(originField, gbc);

        // Year Discovered
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Year Discovered:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        yearField = new JTextField(20);
        yearField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(yearField, gbc);

        // Image section
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Image:"), gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.4;
        JPanel imagePanel = createImagePanel();
        formPanel.add(imagePanel, gbc);

        return formPanel;
    }

//     * Creates the image panel with preview and upload button
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Artifact Image"));

        // Image preview
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        imageLabel.setBackground(new Color(250, 250, 250));
        imageLabel.setOpaque(true);
        imageLabel.setText("No Image Selected");
        imageLabel.setForeground(Color.GRAY);

        // Image button
        imageButton = new JButton("Choose Image");
        imageButton.setFont(new Font("Arial", Font.PLAIN, 12));
        imageButton.addActionListener(e -> chooseImage());
        styleButton(imageButton, new Color(52, 152, 219));

        JPanel buttonWrapper = new JPanel(new FlowLayout());
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(imageButton);

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(buttonWrapper, BorderLayout.SOUTH);

        return imagePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        styleButton(cancelButton, new Color(149, 165, 166));

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetForm());
        styleButton(resetButton, new Color(230, 126, 34));

        submitButton = new JButton(isEditMode ? "Update" : "Save");
        submitButton.addActionListener(e -> saveArtifact());
        styleButton(submitButton, new Color(39, 174, 96));

        buttonPanel.add(cancelButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(submitButton);

        return buttonPanel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void populateFields() {
        if (artifact != null) {
            nameField.setText(artifact.getName());
            descriptionArea.setText(artifact.getDescription());
            categoryField.setText(artifact.getCategory());
            originField.setText(artifact.getOrigin());
            yearField.setText(String.valueOf(artifact.getYearDiscovered()));

            if (artifact.getImageData() != null) {
                imageData = artifact.getImageData();
                imageName = artifact.getImageName();
                displayImage();
            }
        }
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif", "bmp"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Read image file into byte array
                FileInputStream fis = new FileInputStream(selectedFile);
                imageData = new byte[(int) selectedFile.length()];
                fis.read(imageData);
                fis.close();

                imageName = selectedFile.getName();
                displayImage();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error reading image file: " + e.getMessage(),
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage() {
        if (imageData != null) {
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image img = imageIcon.getImage();
            Image scaledImg = img.getScaledInstance(180, 130, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
            imageLabel.setText("");
        }
    }

    private void resetForm() {
        if (isEditMode) {
            populateFields();
        } else {
            nameField.setText("");
            descriptionArea.setText("");
            categoryField.setText("");
            originField.setText("");
            yearField.setText("");
            imageData = null;
            imageName = null;
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Selected");
        }
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Artifact name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        String yearText = yearField.getText().trim();
        if (!yearText.isEmpty()) {
            try {
                int year = Integer.parseInt(yearText);
                if (year < 0 || year > java.time.Year.now().getValue()) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid year.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    yearField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Year must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                yearField.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void saveArtifact() {
        if (!validateInput()) {
            return;
        }

        try {
            // Create or update artifact object
            if (artifact == null) {
                artifact = new Artifact();
            }

            artifact.setName(nameField.getText().trim());
            artifact.setDescription(descriptionArea.getText().trim());
            artifact.setCategory(categoryField.getText().trim());
            artifact.setOrigin(originField.getText().trim());

            String yearText = yearField.getText().trim();
            if (!yearText.isEmpty()) {
                artifact.setYearDiscovered(Integer.parseInt(yearText));
            } else {
                artifact.setYearDiscovered(0);
            }

            artifact.setImageData(imageData);
            artifact.setImageName(imageName);

            // Save to database
            boolean success;
            if (isEditMode) {
                success = artifactDAO.updateArtifact(artifact);
            } else {
                int newId = artifactDAO.insertArtifact(artifact);
                success = (newId > 0);
                if (success) {
                    artifact.setId(newId);
                }
            }

            if (success) {
                artifactSaved = true;
                JOptionPane.showMessageDialog(this,
                        "Artifact " + (isEditMode ? "updated" : "saved") + " successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to " + (isEditMode ? "update" : "save") + " artifact.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving artifact: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Returns whether the artifact was successfully saved
     */
    public boolean isArtifactSaved() {
        return artifactSaved;
    }

    /**
     * Returns the saved/updated artifact
     */
    public Artifact getArtifact() {
        return artifact;
    }
}
