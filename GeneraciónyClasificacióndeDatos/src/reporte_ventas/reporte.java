package reporte_ventas;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class reporte {
    private static final String PRODUCTOS_FILE = "productos.txt";
    private static final String VENDEDORES_FILE = "vendedores.txt";
    private static final String REPORTE_VENTAS_FILE = "reporte_ventas.txt";

    public static void main(String[] args) {
        Map<Integer, Integer> preciosProductos = cargarPreciosProductos();
        Map<String, Integer> ventasTotales = calcularVentasTotales(preciosProductos);
        escribirReporteVentas(ventasTotales);
    }

    private static Map<Integer, Integer> cargarPreciosProductos() {
        Map<Integer, Integer> preciosProductos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTOS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] partes = line.split(";");
                    if (partes.length == 3 && esNumerico(partes[0]) && esNumerico(partes[2])) {
                        int idProducto = Integer.parseInt(partes[0]);
                        int precio = Integer.parseInt(partes[2]);
                        preciosProductos.put(idProducto, precio);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preciosProductos;
    }

    private static Map<String, Integer> calcularVentasTotales(Map<Integer, Integer> preciosProductos) {
        Map<String, Integer> ventasTotales = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(VENDEDORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] partes = line.split(";");
                    if (partes.length == 2 && esNumerico(partes[1])) {
                        String vendedor = partes[0];
                        int totalVentas = leerVentasVendedor(vendedor, preciosProductos);
                        ventasTotales.put(vendedor, totalVentas);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ventasTotales;
    }

    private static int leerVentasVendedor(String vendedor, Map<Integer, Integer> preciosProductos) {
        int totalVentas = 0;
        String archivoVentas = vendedor.replace(" ", " ") + "_ventas.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoVentas))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] partes = line.split(";");
                    if (partes.length == 3 && esNumerico(partes[0]) && esNumerico(partes[2])) {
                        int idProducto = Integer.parseInt(partes[0]);
                        int cantidadVendida = Integer.parseInt(partes[2]);
                        int precioProducto = preciosProductos.getOrDefault(idProducto, 0);
                        totalVentas += cantidadVendida * precioProducto;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalVentas;
    }
    

    private static void escribirReporteVentas(Map<String, Integer> ventasTotales) {
        List<Map.Entry<String, Integer>> listaOrdenada = ventasTotales.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());                   
             
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPORTE_VENTAS_FILE))) {
        	writer.write("Nombre/Apellido;Total ventas\n\n");
            for (Map.Entry<String, Integer> entry : listaOrdenada) {
            
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
            
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean esNumerico(String cadena) {
        if (cadena == null) {
            return false;
        }
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}