package reporte_ventas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * La clase GenerateInfoFiles se utiliza para generar archivos de texto con información de productos y vendedores.
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
		}
	}

	/**
	 * Genera archivos de reporte de ventas para cada vendedor.
	 * Cada archivo contiene la cédula del vendedor y una lista de productos vendidos con sus cantidades.
	 * @throws IOException Si ocurre un error de entrada/salida al escribir los archivos.
	 */
	public void createSalesReportFiles() throws IOException {

		// Instancia de Random para generar números aleatorios
		Random random = new Random();

		// Máximo número de productos vendidos que se reportarán
		int maxProductosVendidos = 10;

		// Itera sobre el conjunto de claves del mapa de vendedores
		for (String vendedor : vendedoresMap.keySet()) {

			// Crea o sobrescribe el archivo de ventas del vendedor actual
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(vendedor + "_ventas.txt"))) {

				// Obtiene la cédula del vendedor del mapa
				Integer cedulaVendedor = vendedoresMap.get(vendedor);

				// Escribe la información del vendedor en el archivo
				writer.write(vendedor + ";" + cedulaVendedor + "\n\n"); 

				// Genera un reporte de ventas con productos aleatorios y sus cantidades
				for (int i = 0; i < maxProductosVendidos; i++) {

					// Selecciona un producto aleatorio de la lista de productos
					String productoVendido = nombresProductos[random.nextInt(nombresProductos.length)];

					// Obtiene el ID del producto del mapa
					int idProducto = productosIdMap.get(productoVendido); 

					// Genera una cantidad vendida aleatoria entre 1 y 10
					int cantidadVendida = 1 + random.nextInt(10); 

					// Escribe la información del producto vendido en el archivo

					writer.write(idProducto + ";" + productoVendido + ";" + cantidadVendida + "\n");
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


