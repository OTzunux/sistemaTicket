package Main.dao;

import Main.model.Permission;
import Main.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolePermissionDAO {

    public void assignPermissionToRole(int roleId, int permissionId) throws SQLException {
        String SQL = "INSERT INTO role_permissions(role_id, permission_id) VALUES(?, ?) ON CONFLICT (role_id, permission_id) DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, roleId);
            pstmt.setInt(2, permissionId);
            pstmt.executeUpdate();
        }
    }

    public void removePermissionFromRole(int roleId, int permissionId) throws SQLException {
        String SQL = "DELETE FROM role_permissions WHERE role_id = ? AND permission_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, roleId);
            pstmt.setInt(2, permissionId);
            pstmt.executeUpdate();
        }
    }

    // Nuevo método: Elimina todos los permisos asociados a un rol
    public void removeAllPermissionsFromRole(int roleId) throws SQLException {
        String SQL = "DELETE FROM role_permissions WHERE role_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, roleId);
            pstmt.executeUpdate();
        }
    }


    public List<Permission> getPermissionsForRole(int roleId) throws SQLException {
        List<Permission> permissions = new ArrayList<>();
        String SQL = "SELECT p.id, p.name, p.description FROM permissions p " +
                     "JOIN role_permissions rp ON p.id = rp.permission_id WHERE rp.role_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, roleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Permission permission = new Permission();
                    permission.setId(rs.getInt("id"));
                    permission.setName(rs.getString("name"));
                    permission.setDescription(rs.getString("description"));
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }
}