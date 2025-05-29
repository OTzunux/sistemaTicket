package Main.service; // Sugiero un nuevo paquete para servicios, o Main

import Main.dao.RoleDAO;
import Main.dao.RolePermissionDAO; // Puede que necesitemos para obtener permisos
import Main.model.Role;
import Main.model.Permission;

import java.sql.SQLException;
import java.util.List;

public class RoleService {
    private RoleDAO roleDAO;
    private RolePermissionDAO permissionDAO; // Para obtener todos los permisos disponibles si es necesario

    public RoleService() {
        this.roleDAO = new RoleDAO();
        this.permissionDAO = new RolePermissionDAO(); // Inicializar si vas a usarlo
    }

    public List<Role> getAllRoles() throws SQLException {
        return roleDAO.getAllRoles();
    }

    public Role getRoleById(int id) throws SQLException {
        return roleDAO.getRoleById(id);
    }

    public void createRole(Role role) throws SQLException, IllegalArgumentException {
        // Validaciones de negocio para el rol
        if (role.getName() == null || role.getName().trim().isEmpty() || role.getName().trim().length() < 3 || role.getName().trim().length() > 50) { 
            throw new IllegalArgumentException("El nombre del rol debe contener entre 3 y 50 caracteres y no puede estar vacío.");
        }
        // Puedes añadir más validaciones si es necesario
        roleDAO.createRole(role);
    }

    public void updateRole(Role role) throws SQLException, IllegalArgumentException {
        // Validaciones de negocio para el rol
        if (role.getName() == null || role.getName().trim().isEmpty() || role.getName().trim().length() < 3 || role.getName().trim().length() > 50) { 
            throw new IllegalArgumentException("El nombre del rol debe contener entre 3 y 50 caracteres y no puede estar vacío.");
        }
        if (role.getId() <= 0) {
            throw new IllegalArgumentException("ID de rol inválido para actualizar.");
        }
        roleDAO.updateRole(role);
    }

    public void deleteRole(int roleId) throws SQLException, IllegalArgumentException {
        // Flujo Alterno 1: Intento de eliminar un rol con usuarios asociados [cite: 17]
        if (roleDAO.hasUsersAssociated(roleId)) { 
            throw new IllegalArgumentException("No se puede eliminar el rol. Hay usuarios asignados a este rol. Reasigne los usuarios antes de eliminar."); 
        }
        roleDAO.deleteRole(roleId);
    }

    public List<Permission> getAllPermissions() throws SQLException {
        return permissionDAO.getAllPermissions();
    }
    // Puedes añadir métodos para asignar/desasignar permisos si los DAOs correspondientes los llaman
    // o si el controlador los necesita directamente.
}