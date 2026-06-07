# 🏨 SGRO — Sistema de Gestión de Reservas Online

Sistema web para automatizar las reservas del hospedaje **"Barlen"**, ubicado en Anco Huallo, Chincheros, Apurímac. Permite a los clientes cotizar, reservar y pagar habitaciones en línea, y al personal gestionar el hospedaje desde un panel administrativo.

---

## 🚀 Funcionalidades principales

- **Cotización:** Consulta de disponibilidad y tarifas según fechas y número de huéspedes.
- **Carrito de reservas:** Selección y revisión de habitaciones antes de confirmar.
- **Pago en línea:** Procesamiento seguro de transacciones con generación de comprobante.
- **Asignación automática:** Vinculación de habitación al cliente tras confirmar el pago.
- **Panel administrativo:** Gestión de habitaciones, tarifas, reservas e informes.
- **Recepción:** Check-in, check-out y reservas presenciales (walk-in).

---

## 🛠️ Tecnologías utilizadas

| Capa              | Tecnología              |
|-------------------|-------------------------|
| Back-end          | Java (Servlets)         |
| Front-end         | HTML, CSS, JavaScript   |
| Base de datos     | MySQL                   |
| Servidor          | Apache Tomcat           |
| IDE               | Eclipse                 |
| Control de versiones | Git / GitHub         |

---

## ⚙️ Requisitos para ejecutar el proyecto

- JDK 17 o superior
- Apache Tomcat 10
- MySQL 8.0
- Eclipse IDE for Enterprise Java Developers

---

## 🗂️ Estructura del proyecto

```
SGRO-JAVA/
├── SGRO/               # Proyecto principal (Java Web)
│   ├── src/            # Código fuente Java (servlets, modelos, DAOs)
│   ├── WebContent/     # Archivos HTML, CSS, JS y JSP
│   └── WEB-INF/        # Configuración del servidor (web.xml)
├── screenshots/        # Capturas de pantalla del sistema
└── datos de acceso al sistema.docx  # Credenciales de acceso
```

---

## 📦 Instalación y ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/david011189/SGRO-JAVA.git
   ```
2. Importa el proyecto en Eclipse:
   `File → Import → Existing Projects into Workspace`
3. Crea la base de datos en MySQL:
   ```sql
   CREATE DATABASE sgro;
   ```
4. Configura las credenciales de BD en el archivo de conexión.
5. Despliega en Apache Tomcat y accede en:
   ```
   http://localhost:8080/SGRO
   ```
