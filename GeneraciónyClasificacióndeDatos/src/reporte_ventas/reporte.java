package reporte_ventas;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase principal que genera un reporte de ventas.
 */
public class reporte {
	
    // Nombres de archivos constantes para productos, vendedores y reporte de ventas.
    private static final String PRODUCTOS_FILE = "productos.txt";
    private static final String VENDEDORES_FILE = "vendedores.txt";
    private static final String REPORTE_VENTAS_FILE = "reporte_ventas.txt";

    /**
     * Método principal que se ejecuta al iniciar el programa.
     */
    public static void main(String[] args) {
        // Carga los precios de los productos desde un archivo.
        Map<Integer, Integer> preciosProductos = cargarPreciosProductos();
        // Calcula las ventas totales por vendedor.
        Map<String, Integer> ventasTotales = calcularVentasTotales(preciosProductos);
        // Escribe el reporte de ventas en un archivo.
        escribirReporteVentas(ventasTotales);
    }

    /**
     * Carga los precios de los productos desde un archivo de texto.
     * @return Un mapa con los IDs de los productos y sus precios correspondientes.
     */
    private static Map<Integer, Integer> cargarPreciosProductos() {
        // Inicializa un mapa para almacenar los precios de los productos.
        Map<Integer, Integer> preciosProductos = new HashMap<>();
        // Intenta leer el archivo de productos.
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTOS_FILE))) {
            String line;
            // Lee el archivo línea por línea.
            while ((line = reader.readLine()) != null) {
                // Verifica que la línea no esté vacía.
                if (!line.trim().isEmpty()) {
                    // Divide la línea por el separador punto y coma.
                    String[] partes = line.split(";");
                    // Verifica que la línea tenga 3 partes y que el ID y el precio sean numéricos.
                    if (partes.length == 3 && esNumerico(partes[0]) && esNumerico(partes[2])) {
                        // Parsea el ID y el precio del producto.
                        int idProducto = Integer.parseInt(partes[0]);
                        int precio = Integer.parseInt(partes[2]);
                        // Agrega el ID y el precio al mapa de precios de productos.
                        preciosProductos.put(idProducto, precio);
                    }
                }
            }
        } catch (IOException e) {
            // Imprime el error si no se puede leer el archivo.
            e.printStackTrace();
        }
        // Retorna el mapa de precios de productos.
        return preciosProductos;
    }


    /**
     * Calcula las ventas totales de cada vendedor basándose en los precios de los productos.
     * @param preciosProductos Un mapa que contiene los precios de los productos con su ID como clave.
     * @return Un mapa con el nombre de cada vendedor y el total de sus ventas.
     */
    private static Map<String, Integer> calcularVentasTotales(Map<Integer, Integer> preciosProductos) {
        // Inicializa un mapa para almacenar el total de ventas por vendedor.
        Map<String, Integer> ventasTotales = new HashMap<>();
        // Intenta leer el archivo de vendedores.
        try (BufferedReader reader = new BufferedReader(new FileReader(VENDEDORES_FILE))) {
            String line;
            // Lee el archivo línea por línea.
            while ((line = reader.readLine()) != null) {
                // Verifica que la línea no esté vacía.
                if (!line.trim().isEmpty()) {
                    // Divide la línea por el separador punto y coma.
                    String[] partes = line.split(";");
                    // Verifica que la línea tenga 2 partes y que la cédula sea numérica.
                    if (partes.length == 2 && esNumerico(partes[1])) {
                        // Obtiene el nombre del vendedor.
                        String vendedor = partes[0];
                        // Calcula el total de ventas para el vendedor.
                        int totalVentas = leerVentasVendedor(vendedor, preciosProductos);
                        // Agrega el total de ventas al mapa de ventas totales.
                        ventasTotales.put(vendedor, totalVentas);
                    }
                }
            }
        } catch (IOException e) {
            // Imprime el error si no se puede leer el archivo.
            e.printStackTrace();
        }
        // Retorna el mapa de ventas totales.
        return ventasTotales;
    }

    /**
     * Lee las ventas de un vendedor específico y calcula el total de ventas.
     * @param vendedor El nombre del vendedor.
     * @param preciosProductos Un mapa que contiene los precios de los productos con su ID como clave.
     * @return El total de ventas del vendedor.
     */
    private static int leerVentasVendedor(String vendedor, Map<Integer, Integer> preciosProductos) {
        // Inicializa el total de ventas a 0.
        int totalVentas = 0;
        // Define el nombre de la carpeta donde se encuentran los archivos de ventas.
        String nombreCarpeta = "ventas";
        // Construye la ruta del archivo de ventas para el vendedor.
        String archivoVentas = nombreCarpeta + File.separator + vendedor.replace(" ", " ") + "_ventas.txt";
        // Intenta leer el archivo de ventas del vendedor.
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoVentas))) {
            String line;
            // Lee el archivo línea por línea.
            while ((line = reader.readLine()) != null) {
                // Verifica que la línea no esté vacía.
                if (!line.trim().isEmpty()) {
                    // Divide la línea por el separador punto y coma.
                    String[] partes = line.split(";");
                    // Verifica que la línea tenga 3 partes y que el ID y la cantidad vendida sean numéricos.
                    if (partes.length == 3 && esNumerico(partes[0]) && esNumerico(partes[2])) {
                        // Parsea el ID del producto y la cantidad vendida.
                        int idProducto = Integer.parseInt(partes[0]);
                        int cantidadVendida = Integer.parseInt(partes[2]);
                        // Obtiene el precio del producto del mapa de precios.
                        int precioProducto = preciosProductos.getOrDefault(idProducto, 0);
                        // Calcula el total de ventas y lo acumula.
                        totalVentas += cantidadVendida * precioProducto;
                    }
                }
            }
        } catch (IOException e) {
            // Imprime un mensaje de error si no se puede leer el archivo de ventas.
            System.err.println("Error al leer el archivo de ventas para el vendedor " + vendedor + ": " + e.getMessage());
        }
        // Retorna el total de ventas del vendedor.
        return totalVentas;
    }
    

    /**
     * Escribe un reporte de ventas en un archivo de texto.
     * @param ventasTotales Un mapa que contiene el nombre de cada vendedor y el total de sus ventas.
     */
    private static void escribirReporteVentas(Map<String, Integer> ventasTotales) {
        // Ordena las entradas del mapa de ventas totales en orden descendente por valor (total de ventas).
        List<Map.Entry<String, Integer>> listaOrdenada = ventasTotales.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Intenta escribir en el archivo de reporte de ventas.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPORTE_VENTAS_FILE))) {
            // Escribe la cabecera del archivo.
            writer.write("Nombre/Apellido;Total ventas\n\n");
            // Itera sobre la lista ordenada y escribe cada entrada en el archivo.
            for (Map.Entry<String, Integer> entry : listaOrdenada) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine(); // Añade un salto de línea después de cada entrada.
            }
            System.out.println("Archivo 'reporte_ventas.txt' creado exitosamente.");
        } catch (IOException e) {
            // Captura y muestra los errores de entrada/salida en la consola.
            System.err.println("Error al escribir el archivo de reporte de ventas: " + e.getMessage());
        }
    }

    /**
     * Determina si una cadena de texto es numérica.
     * @param cadena La cadena de texto a evaluar.
     * @return true si la cadena es numérica, false en caso contrario.
     */
    private static boolean esNumerico(String cadena) {
        // Verifica si la cadena es nula.
        if (cadena == null) {
            return false;
        }
        try {
            // Intenta convertir la cadena a un entero.
            Integer.parseInt(cadena);
            return true; // Retorna verdadero si la conversión es exitosa.
        } catch (NumberFormatException nfe) {
            return false; // Retorna falso si ocurre un error en la conversión.
        }
    }
  }
