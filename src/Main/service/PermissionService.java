package Main.service; // O Main

import Main.dao.RolePermissionDAO;
import Main.model.Permission;
import Main.dao.RoleDAO;

import java.sql.SQLException;
import java.util.List;

public class PermissionService {
    private RolePermissionDAO permissionDAO;

    public PermissionService() {
        this.permissionDAO = new RolePermissionDAO();
    }

    public List<Permission> getAllPermissions() throws SQLException {
        return permissionDAO.getAllPermissions();
    }

    public void createPermission(Permission permission) throws SQLException, IllegalArgumentException {
        // Validaciones de negocio para el permiso
        if (permission.getName() == null || permission.getName().trim().isEmpty() || permission.getName().trim().length() < 3 || permission.getName().trim().length() > 50) { [cite: 6]
            throw new IllegalArgumentException("El nombre del permiso debe contener entre 3 y 50 caracteres y no puede estar vacío.");
        }
        permissionDAO.createPermission(permission);
    }

    // No hay requisitos para editar/eliminar permisos directamente en el CU, pero se podría añadir
    // public void updatePermission(Permission permission) throws SQLException { ... }
    // public void deletePermission(int permissionId) throws SQLException { ... }
}