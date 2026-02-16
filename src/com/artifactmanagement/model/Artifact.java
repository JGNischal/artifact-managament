package com.artifactmanagement.model;

import javax.swing.ImageIcon;
import java.sql.Timestamp;

public class Artifact {

    private int id;
    private String name;
    private String description;
    private String category;
    private String origin;
    private int yearDiscovered;
    private byte[] imageData;
    private String imageName;
    private Timestamp createdDate;
    private Timestamp modifiedDate;

    // Default constructor
    public Artifact() {}

    // Constructor with essential fields
    public Artifact(String name, String description, String category, String origin, int yearDiscovered) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.origin = origin;
        this.yearDiscovered = yearDiscovered;
    }

    // Full constructor
    public Artifact(int id, String name, String description, String category, String origin,
                    int yearDiscovered, byte[] imageData, String imageName,
                    Timestamp createdDate, Timestamp modifiedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.origin = origin;
        this.yearDiscovered = yearDiscovered;
        this.imageData = imageData;
        this.imageName = imageName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public int getYearDiscovered() { return yearDiscovered; }
    public void setYearDiscovered(int yearDiscovered) { this.yearDiscovered = yearDiscovered; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    public Timestamp getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(Timestamp modifiedDate) { this.modifiedDate = modifiedDate; }

//     * Returns a shortened description for display in cards
    public String getShortDescription(int maxLength) {
        if (description == null) return "";
        if (description.length() <= maxLength) return description;
        return description.substring(0, maxLength - 3) + "...";
    }

//     * Creates an ImageIcon from the stored image data

    public ImageIcon getImageIcon() {
        if (imageData != null && imageData.length > 0) {
            return new ImageIcon(imageData);
        }
        return null;
    }


    public ImageIcon getThumbnailIcon(int width, int height) {
        ImageIcon originalIcon = getImageIcon();
        if (originalIcon != null) {
            java.awt.Image img = originalIcon.getImage();
            java.awt.Image scaledImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", origin='" + origin + '\'' +
                ", yearDiscovered=" + yearDiscovered +
                '}';
    }
}
