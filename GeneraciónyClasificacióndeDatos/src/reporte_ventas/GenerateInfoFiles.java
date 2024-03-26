package reporte_ventas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GenerateInfoFiles {

    private String[] nombresProductos = {
        "Sillas", "Mesas", "Bancos", "Taladro", "Guantes", "Tornillos", "Clavos", "Prensa",
        "Alicates", "Navaja", "Martillo", "Carretilla", "Termo", "Aislante", "Pintura",
        "Secante", "Disolvente", "Tiner", "Lijas", "Bateria", "Plastico", "Tuercas",
        "Soldadora", "Lubricante", "Tubo", "Barilla", "Cemento", "Grava", "Arena",
        "Brocha", "Rodillo", "Pala", "Cincel", "Cobador"
    };
    private String[] nombresVendedores = {
        "Juan", "Maria", "Pedro", "Luisa"
    };
    private String[] apellidosVendedores = {
        "Perez", "Gonzalez", "Rodriguez", "Martinez"
    };

    // Mapas para almacenar IDs únicos y precios para productos
    private Map<String, Integer> productosIdMap = new HashMap<>();
    private Map<String, Integer> productosPrecioMap = new HashMap<>();
    // Mapa para almacenar nombres y números de cédula de vendedores
    private Map<String, Integer> vendedoresMap = new HashMap<>();

    public GenerateInfoFiles() {
        inicializarProductos();
        inicializarVendedores();
    }

    private void inicializarProductos() {
        int idInicial = 125; // ID inicial para el primer producto
        int incrementoId = 53; // Incremento para los siguientes IDs
        int precioBase = 10650; // Precio base para el primer producto
        int incrementoPrecio = 24300; // Incremento para los siguientes precios

        for (int i = 0; i < nombresProductos.length; i++) {
            String nombreProducto = nombresProductos[i];
            productosIdMap.put(nombreProducto, idInicial + (i * incrementoId));
            productosPrecioMap.put(nombreProducto, precioBase + (i * incrementoPrecio));
        }
    }

    private void inicializarVendedores() {
        int incrementoCedula = 256788188; // Incremento específico para las cédulas
        int cedulaActual = 32698401; // Cédula inicial para el primer vendedor

        for (int i = 0; i < nombresVendedores.length; i++) {
            String nombreCompleto = nombresVendedores[i] + " " + apellidosVendedores[i];
            vendedoresMap.put(nombreCompleto, cedulaActual);
            cedulaActual += incrementoCedula; // Incrementar la cédula para el siguiente vendedor
        }
    }

    public void createProductsFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productos.txt"))) {
            writer.write("ID;Nombre;Precio\n\n");
            for (String nombreProducto : nombresProductos) {
                int idProducto = productosIdMap.get(nombreProducto);
                int precio = productosPrecioMap.get(nombreProducto);
                writer.write(idProducto + ";" + nombreProducto + ";" + precio + "\n");
            }
        }
    }

    public void createSalesManInfoFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendedores.txt"))) {
            writer.write("Nombre/Apellido;Cédula\n\n");
            for (Map.Entry<String, Integer> entry : vendedoresMap.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
            }
        }
    }
    
    public void createSalesReportFiles() throws IOException {
        Random random = new Random();
        int maxProductosVendidos = 10; // Máximo número de productos vendidos por reporte

        for (String vendedor : vendedoresMap.keySet()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(vendedor + "_ventas.txt"))) {
                Integer cedulaVendedor = vendedoresMap.get(vendedor); // Obtenemos la cédula del vendedor
                writer.write(vendedor + ";" + cedulaVendedor + "\n\n"); // Información del vendedor
                for (int i = 0; i < maxProductosVendidos; i++) {
                    String productoVendido = nombresProductos[random.nextInt(nombresProductos.length)];
                    int idProducto = productosIdMap.get(productoVendido); // Obtenemos el ID del producto
                    int cantidadVendida = 1 + random.nextInt(10); // Cantidad vendida entre 1 y 10
                    writer.write(idProducto + ";" + productoVendido + ";" + cantidadVendida + "\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        GenerateInfoFiles generator = new GenerateInfoFiles();
        try {
            generator.createProductsFile();
            generator.createSalesManInfoFile();
            generator.createSalesReportFiles(); // Generar los reportes de venta
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}