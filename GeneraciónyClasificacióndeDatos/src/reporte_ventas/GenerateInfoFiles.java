package reporte_ventas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * La clase GenerateInfoFiles se utiliza para 
 * generar archivos de texto con información de productos y vendedores.
 * Contiene métodos para inicializar y escribir dicha información en archivos separados.
 */
public class GenerateInfoFiles {

	/**
	 * Array de cadenas que contiene los nombres de varios productos.
	 * Estos nombres representan un inventario de artículos comunes en una ferretería.
	 */
	private String[] nombresProductos = {
			"Sillas", "Mesas", "Bancos", "Taladro", "Guantes", "Tornillos", "Clavos", "Prensa",
			"Alicates", "Navaja", "Martillo", "Carretilla", "Termo", "Aislante", "Pintura",
			"Secante", "Disolvente", "Tiner", "Lijas", "Bateria", "Plastico", "Tuercas",
			"Soldadora", "Lubricante", "Tubo", "Barilla", "Cemento", "Grava", "Arena",
			"Brocha", "Rodillo", "Pala", "Cincel", "Cobador"
	};

	/**
	 * Array de cadenas que contiene los nombres de los vendedores.
	 */
	private String[] nombresVendedores = {
			"Juan", "Maria", "Pedro", "Luisa"
	};

	/**
	 * Array de cadenas que contiene los apellidos de los vendedores.
	 */
	private String[] apellidosVendedores = {
			"Perez", "Gonzalez", "Rodriguez", "Martinez"
	};

	/**
	 * Mapa que asocia cada producto con un identificador único.
	 * Se utiliza para mantener un registro rápido y eficiente de los productos.
	 */
	private Map<String, Integer> productosIdMap = new HashMap<>();

	/**
	 * Mapa que asocia cada producto con su precio.
	 * Permite un acceso rápido a los precios de los productos para procesos de venta o inventario.
	 */
	private Map<String, Integer> productosPrecioMap = new HashMap<>();

	/**
	 * Mapa que asocia el nombre completo de los vendedores con un identificador único.
	 * Facilita la gestión de información relacionada con los vendedores.
	 */
	private Map<String, Integer> vendedoresMap = new HashMap<>();

	/**
	 * Constructor de la clase GenerateInfoFiles.
	 * Inicializa los mapas de productos y vendedores mediante métodos específicos.
	 */
	public GenerateInfoFiles() {
		inicializarProductos();
		inicializarVendedores();
	}

	/**
	 * Inicializa los mapas de identificadores y precios de los productos.
	 * Asigna un ID y un precio a cada producto utilizando una base y un incremento definidos.
	 */
	private void inicializarProductos() {
		// ID inicial para el primer producto
		int idInicial = 125;
		// Incremento para los IDs de los siguientes productos
		int incrementoId = 53;
		// Precio base para el primer producto
		int precioBase = 10650;
		// Incremento para los precios de los siguientes productos
		int incrementoPrecio = 24300;

		// Itera sobre el array de nombres de productos
		for (int i = 0; i < nombresProductos.length; i++) {
			// Obtiene el nombre del producto actual
			String nombreProducto = nombresProductos[i];
			// Asigna un ID único al producto actual basado en el ID inicial y el incremento
			productosIdMap.put(nombreProducto, idInicial + (i * incrementoId));
			// Asigna un precio al producto actual basado en el precio base y el incremento
			productosPrecioMap.put(nombreProducto, precioBase + (i * incrementoPrecio));
		}
	}

	/**
	 * Inicializa el mapa de vendedores con sus respectivas cédulas.
	 * Asigna una cédula única a cada vendedor utilizando una cédula base y un incremento definido.
	 */
	private void inicializarVendedores() {

		// Incremento específico para las cédulas de los vendedores
		int incrementoCedula = 256788188;
		// Cédula inicial para el primer vendedor
		int cedulaActual = 32698401;

		// Itera sobre los arrays de nombres y apellidos de los vendedores
		for (int i = 0; i < nombresVendedores.length; i++) {
			// Construye el nombre completo del vendedor actual
			String nombreCompleto = nombresVendedores[i] + " " + apellidosVendedores[i];
			// Asocia el nombre completo del vendedor con su cédula en el mapa
			vendedoresMap.put(nombreCompleto, cedulaActual);
			// Incrementa la cédula para el siguiente vendedor
			cedulaActual += incrementoCedula; // Incrementar la cédula para el siguiente vendedor
		}
	}

	/**
	 * Crea un archivo de texto con la información de los productos.
	 * El archivo generado contiene los IDs, nombres y precios de los productos.
	 * @throws IOException Si ocurre un error de entrada/salida al escribir el archivo.
	 */
	public void createProductsFile() throws IOException {

		// Utiliza try-with-resources para asegurar que el BufferedWriter se cierre después de su uso
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("productos.txt"))) {
			// Escribe la cabecera del archivo
			writer.write("ID;Nombre;Precio\n\n");

			// Itera sobre el array de nombres de productos
			for (String nombreProducto : nombresProductos) {

				// Obtiene el ID y el precio del producto actual del mapa
				Integer idProducto = productosIdMap.get(nombreProducto);
				Integer precio = productosPrecioMap.get(nombreProducto);

				if (idProducto != null && precio != null) {
	                // Escribe la información del producto en el archivo, separada por punto y coma
	                writer.write(idProducto + ";" + nombreProducto + ";" + precio + "\n");
	            } else {
	                System.err.println("Error: No se encontró información para el producto '" + nombreProducto + "'.");
	            }
	        }
	        System.out.println("Archivo 'productos.txt' creado exitosamente.");
	    } catch (IOException e) {
	        System.err.println("Error al crear el archivo 'productos.txt': " + e.getMessage());
	    }
	}

	/**
	 * Crea un archivo de texto con la información de los vendedores.
	 * El archivo generado contiene los nombres y las cédulas de los vendedores.
	 * @throws IOException Si ocurre un error de entrada/salida al escribir el archivo.
	 */
	public void createSalesManInfoFile() throws IOException {

		// Utiliza try-with-resources para asegurar que el BufferedWriter se cierre después de su uso
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendedores.txt"))) {

			// Escribe la cabecera del archivo
			writer.write("Nombre/Apellido;Cédula\n\n");

			// Itera sobre el conjunto de entradas del mapa de vendedores
			for (Map.Entry<String, Integer> entry : vendedoresMap.entrySet()) {

				// Escribe la información del vendedor en el archivo, separada por punto y coma
				writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
			}
			System.out.println("Archivo 'vendedores.txt' creado exitosamente.");
	    } catch (IOException e) {
	        // Mensaje de error en caso de que ocurra una excepción
	        System.err.println("Ocurrió un error al crear el archivo 'vendedores.txt': " + e.getMessage());
	    }
	}
		
	

	/**
	 * Este método genera archivos de reporte de ventas para cada vendedor listado en el mapa de vendedores.
	 * Cada archivo contiene una lista de productos vendidos, la cantidad de cada producto y la cédula del vendedor.
	 * Los archivos se almacenan en una carpeta denominada "ventas".
	 * Si se encuentra con algún error durante la creación de los archivos, el método maneja la excepción y proporciona mensajes de error detallados.
	 */
	public void createSalesReportFiles() throws IOException {
	   
	    Random random = new Random(); // Generador de números aleatorios para simular cantidades de productos vendidos.	 
	    int maxProductosVendidos = 10; // Número máximo de productos que se pueden vender, utilizado para generar el reporte.	   
	    String nombreCarpeta = "ventas"; // Nombre de la carpeta donde se almacenarán los archivos de reporte.	   
	    File carpeta = new File(nombreCarpeta); // Creación de un objeto File para la carpeta 'ventas'.
	    // Si la carpeta no existe, se crea.
	    if (!carpeta.exists()) {
	        carpeta.mkdir();
	    }

	    // Iteración sobre cada vendedor en el mapa de vendedores.
	    for (String vendedor : vendedoresMap.keySet()) {
	    	// Construcción del nombre del archivo de reporte para el vendedor actual.
	        String nombreArchivo = nombreCarpeta + File.separator + vendedor + "_ventas.txt"; 	      
	        BufferedWriter writer = null;// Inicialización del BufferedWriter como null para poder cerrarlo en el bloque finally.
	        try {
	            // Creación del BufferedWriter para escribir en el archivo de reporte.
	            writer = new BufferedWriter(new FileWriter(nombreArchivo)); 	           
	            Integer cedulaVendedor = vendedoresMap.get(vendedor); // Obtención de la cédula del vendedor del mapa de vendedores.	           
	            writer.write(vendedor + ";" + cedulaVendedor + "\n\n"); // Escritura del nombre y cédula del vendedor en el archivo.	            
	            writer.write("id;producto;cantidad" + "\n\n"); // Escritura de la cabecera de los detalles de venta en el archivo.
	            // Generación y escritura de las ventas de productos aleatorios en el archivo.
	            for (int i = 0; i < maxProductosVendidos; i++) {
	                // Selección aleatoria de un producto vendido.
	                String productoVendido = nombresProductos[random.nextInt(nombresProductos.length)];	              
	                int idProducto = productosIdMap.get(productoVendido); // Obtención del ID del producto del mapa de IDs de productos.	            
	                int cantidadVendida = 1 + random.nextInt(10);// Generación aleatoria de la cantidad vendida.
	                // Escritura de los detalles del producto vendido en el archivo.
	                writer.write(idProducto + ";" + productoVendido + ";" + cantidadVendida + "\n");
	            }
	            // Mensaje de confirmación de la creación exitosa del archivo de reporte.
	            System.out.println("Archivo '" + vendedor + "_ventas' creado exitosamente.");
	        } catch (IOException e) {
	            // Manejo de excepciones de entrada/salida con mensaje de error.
	            System.err.println("Error al crear el archivo '" + vendedor + "_ventas: " + e.getMessage());
	        } finally {
	            // Intento de cerrar el BufferedWriter si fue abierto.
	            try {
	                if (writer != null) {
	                    writer.close();
	                }
	            } catch (IOException e) {
	                // Manejo de excepciones al cerrar el BufferedWriter con mensaje de error.
	                System.err.println("Ocurrió un error al cerrar el archivo de ventas para el vendedor " + vendedor + ": " + e.getMessage());
	            }
	        }
	    }
	}
	
	
	
	/**
	 * Punto de entrada principal del programa.
	 * Crea instancias y ejecuta métodos para generar archivos de productos, vendedores y reportes de ventas.
	 */
	public static void main(String[] args) {

		// Crea una instancia de la clase GenerateInfoFiles
		GenerateInfoFiles generator = new GenerateInfoFiles();
		try {

			// Intenta crear los archivos de productos y vendedores
			generator.createProductsFile(); // Genera el archivo de productos
			generator.createSalesManInfoFile(); // Genera el archivo de información de vendedores
			generator.createSalesReportFiles(); // Genera los reportes de venta
		} catch (IOException e) {
			// Captura y muestra las excepciones de entrada/salida en la consola			
			e.printStackTrace();
		}
	}
}


