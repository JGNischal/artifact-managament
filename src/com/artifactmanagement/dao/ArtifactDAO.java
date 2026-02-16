package com.artifactmanagement.dao;

import com.artifactmanagement.model.Artifact;
import com.artifactmanagement.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ArtifactDAO {
    public List<Artifact> getAllArtifacts() {
        List<Artifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM artifacts ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Artifact artifact = mapResultSetToArtifact(rs);
                artifacts.add(artifact);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving artifacts: " + e.getMessage());
            e.printStackTrace();
        }

        return artifacts;
    }

    public Artifact getArtifactById(int id) {
        String sql = "SELECT * FROM artifacts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToArtifact(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving artifact by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public int insertArtifact(Artifact artifact) {
        String sql = "INSERT INTO artifacts (name, description, category, origin, year_discovered, image_data, image_name) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, artifact.getName());
            stmt.setString(2, artifact.getDescription());
            stmt.setString(3, artifact.getCategory());
            stmt.setString(4, artifact.getOrigin());
            stmt.setInt(5, artifact.getYearDiscovered());

            if (artifact.getImageData() != null) {
                stmt.setBytes(6, artifact.getImageData());
                stmt.setString(7, artifact.getImageName());
            } else {
                stmt.setNull(6, Types.LONGVARBINARY);
                stmt.setNull(7, Types.VARCHAR);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting artifact: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Updates an existing artifact in the database
     * @param artifact Artifact to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateArtifact(Artifact artifact) {
        String sql = "UPDATE artifacts SET name = ?, description = ?, category = ?, origin = ?, year_discovered = ?, image_data = ?, image_name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artifact.getName());
            stmt.setString(2, artifact.getDescription());
            stmt.setString(3, artifact.getCategory());
            stmt.setString(4, artifact.getOrigin());
            stmt.setInt(5, artifact.getYearDiscovered());

            if (artifact.getImageData() != null) {
                stmt.setBytes(6, artifact.getImageData());
                stmt.setString(7, artifact.getImageName());
            } else {
                stmt.setNull(6, Types.LONGVARBINARY);
                stmt.setNull(7, Types.VARCHAR);
            }

            stmt.setInt(8, artifact.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating artifact: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes an artifact from the database
     * @param id Artifact ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteArtifact(int id) {
        String sql = "DELETE FROM artifacts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting artifact: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Searches artifacts by name or category
     * @param searchTerm Search term
     * @return List of matching artifacts
     */
    public List<Artifact> searchArtifacts(String searchTerm) {
        List<Artifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM artifacts WHERE name LIKE ? OR category LIKE ? ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Artifact artifact = mapResultSetToArtifact(rs);
                artifacts.add(artifact);
            }

        } catch (SQLException e) {
            System.err.println("Error searching artifacts: " + e.getMessage());
            e.printStackTrace();
        }

        return artifacts;
    }

//     * Maps a ResultSet row to an Artifact object

    private Artifact mapResultSetToArtifact(ResultSet rs) throws SQLException {
        Artifact artifact = new Artifact();
        artifact.setId(rs.getInt("id"));
        artifact.setName(rs.getString("name"));
        artifact.setDescription(rs.getString("description"));
        artifact.setCategory(rs.getString("category"));
        artifact.setOrigin(rs.getString("origin"));
        artifact.setYearDiscovered(rs.getInt("year_discovered"));
        artifact.setImageData(rs.getBytes("image_data"));
        artifact.setImageName(rs.getString("image_name"));
        artifact.setCreatedDate(rs.getTimestamp("created_date"));
        artifact.setModifiedDate(rs.getTimestamp("modified_date"));

        return artifact;
    }
}
