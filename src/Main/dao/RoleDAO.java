package Main.dao;

import Main.model.Role;
import Main.model.Permission;
import Main.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    // Necesitamos una instancia de RolePermissionDAO para cargar los permisos
    private RolePermissionDAO rolePermissionDAO;

    public RoleDAO() {
        this.rolePermissionDAO = new RolePermissionDAO();
    }

    public int createRole(Role role) throws SQLException {
        String SQL = "INSERT INTO roles(name, description) VALUES(?, ?)";
        int id = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, role.getName());
            pstmt.setString(2, role.getDescription());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                        role.setId(id); // Asigna el ID generado al objeto Role
                        // Si el rol tiene permisos ya definidos en el objeto, asignarlos a la BD
                        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                            for (Permission perm : role.getPermissions()) {
                                rolePermissionDAO.assignPermissionToRole(id, perm.getId());
                            }
                        }
                    }
                }
            }
        }
        return id;
    }

    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String SQL = "SELECT r.id, r.name, r.description FROM roles r";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                role.setDescription(rs.getString("description"));
                // Cargar los permisos para cada rol
                role.setPermissions(rolePermissionDAO.getPermissionsForRole(role.getId()));
                roles.add(role);
            }
        }
        return roles;
    }

    public Role getRoleById(int id) throws SQLException {
        String SQL = "SELECT id, name, description FROM roles WHERE id = ?";
        Role role = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setName(rs.getString("name"));
                    role.setDescription(rs.getString("description"));
                    // Cargar los permisos para este rol
                    role.setPermissions(rolePermissionDAO.getPermissionsForRole(role.getId()));
                }
            }
        }
        return role;
    }

    public void updateRole(Role role) throws SQLException {
        String SQL = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, role.getName());
            pstmt.setString(2, role.getDescription());
            pstmt.setInt(3, role.getId());
            pstmt.executeUpdate();

            // Gestionar la actualización de permisos:
            // 1. Eliminar todos los permisos existentes para el rol
            rolePermissionDAO.removeAllPermissionsFromRole(role.getId());
            // 2. Insertar los permisos actuales del objeto Role
            if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                for (Permission perm : role.getPermissions()) {
                    rolePermissionDAO.assignPermissionToRole(role.getId(), perm.getId());
                }
            }
        }
    }

    public void deleteRole(int id) throws SQLException {
        // Antes de eliminar el rol, elimina sus asignaciones de permisos
        rolePermissionDAO.removeAllPermissionsFromRole(id);
        String SQL = "DELETE FROM roles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Nuevo método para verificar si un rol tiene usuarios asociados
    public boolean hasUsersAssociated(int roleId) throws SQLException {
        String SQL = "SELECT COUNT(*) FROM users WHERE role_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, roleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}