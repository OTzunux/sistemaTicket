package Main;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final Map<String, User> users = new HashMap<>();

    static {
        users.put("admin", new User("admin", "admin123", "ADMINISTRADOR"));
        users.put("tecnico", new User("tecnico", "tec123", "TECNICO"));
        users.put("usuario", new User("usuario", "user123", "USUARIO"));
    }

    public static User authenticate(String username, String password) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        User user = users.get(username);

        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        return user;
    }
}