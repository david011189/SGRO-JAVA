package pe.edu.barlen.sgro.util;

import java.security.SecureRandom;

// RF-03: genera tokens aleatorios seguros para la recuperación de contraseña
public final class TokenUtil {

    private static final SecureRandom RNG = new SecureRandom();

    private TokenUtil() {}

    // Devuelve un token de 32 bytes (64 caracteres hexadecimales)
    public static String generar() {
        return hex(32);
    }

    // RF-30: código legible de reserva, p. ej. RES-A1B2C3D4
    public static String codigoReserva() {
        return "RES-" + hex(4).toUpperCase();
    }

    private static String hex(int numBytes) {
        byte[] bytes = new byte[numBytes];
        RNG.nextBytes(bytes);
        StringBuilder sb = new StringBuilder(numBytes * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
