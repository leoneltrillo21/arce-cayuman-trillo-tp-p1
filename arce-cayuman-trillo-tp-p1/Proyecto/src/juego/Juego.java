package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	private Entorno entorno;
	private Caballero pep;
	private Isla[] islas;
	private Tortuga[] tortugas;
	private Gnomo[] gnomos;
	private Point[] coordenadas;
	private Vida[] vidas;
	private Bomba[] bombas;
	private BolaFuego bola;
	private Image casaGnomo, fondo, nubes, cuadro, corazonVidas, corazonGris, bandera;
	private int perdidos, maxPerdidos, salvados, objetivoSalvados, enemigosEliminados, cantVidas;
	private boolean jugando;
	private boolean[] ocupados;
	
	Juego() {
		
		// Inicializa el objeto entorno
		
		this.entorno = new Entorno(this, "Proyecto para TP", 810, 600);
		this.fondo = Herramientas.cargarImagen("Fondo.png");
		this.nubes = Herramientas.cargarImagen("Nubes.png");
		this.cuadro = Herramientas.cargarImagen("Cuadro.png");
		this.casaGnomo = Herramientas.cargarImagen("CasaGnomo.png");
		this.corazonVidas = Herramientas.cargarImagen("CorazonVidas.png");
		this.corazonGris = Herramientas.cargarImagen("CorazonVidasB.png");
		this.bandera = Herramientas.cargarImagen("BanderaSpawn.png");
		this.coordenadas = new Point[12];
		this.ocupados = new boolean[12];
		this.islas = new Isla[15];
		this.tortugas = new Tortuga[5];
		this.bombas = new Bomba[10]; // Nos aseguramos que cada tortuga pueda lanzar otra bomba si es que la primera no se destruyó.
		this.gnomos = new Gnomo[4];
		this.objetivoSalvados = 8;
		this.maxPerdidos = 8;
		this.jugando = true;
		this.vidas = new Vida[3];
		
		// Inicializar lo que haga falta para el juego:
		//Personaje:
		crearCaballero();
		//Islas:
		crearIslas();
		//Tortuga:
		crearCoordenadas();
		crearTortugas();
		//Gnomo:
		crearGnomos();
		
		this.entorno.iniciar();
	}


	public void tick(){
	
		dibujarFondo(); 
		mostrarEstadisticas(); //Estadísticas en tiempo real.
		
		if (objetivoCumplido()) {
			juegoGanado();
		} else if (!jugando || objetivoPerdido()) {
			juegoPerdido();
		}		
		
		if (jugando) {
			dibujarObjetos(); //Dibuja gnomos, tortugas, islas, etc.
			estadoDeCaballero(); //Estado de caballero(acciones), si este muere, finaliza el juego.
			estadoDeGnomos(); //Estado de cada gnomo(movimientos y colisiones).	
			estadoBombas(); 
			estadoDeTortugas(); //Estado de cada tortuga y reinicio(movimientos y colisiones).
			estadoBolaFuego();
		}
	}
	

	//CREAR OBJETOS:
	private void crearCaballero() {
		this.pep = new Caballero(500, entorno.ancho()/2, entorno);
	}
	private void crearIslas() {
	    int k = 0; // Indice de cada isla en el array.
	    int niveles = 5; // Cantidad de pisos de las islas.
	    int espacioVertical = 110; // Espacio vertical entre islas
	    int anchoPantalla = entorno.ancho();
	    for (int i = 1; i <= niveles; i++) {
	        int desplazamiento = anchoPantalla / (i + 1);
	        for (int j = 1; j <= i; j++) {
	            // Cálculo de la posición X para simetría
	            int x = desplazamiento * j + (j - 1) * 38 - (i * 15);
	            // Cálculo de la posición Y para simetría.
	            int y = i * espacioVertical;
	            islas[k] = new Isla(x, y, entorno);
	            k++;
	        }
	    }
	}		
	private void crearTortuga(int indice) {
		double random = Math.random() * 12; // Número random entre 0 y 12.
		boolean condicion= false;
		while(!condicion) {
			// Si la posición está desocupada, y, además, el número es distinto a la isla del caballero.
			if(ocupados[(int) random] == false && (int)random != 9) {  
				ocupados[(int) random] = true; // Ahora esa posición ya no se puede usar.
				// Se crea una tortuga con x coordenadas.
				tortugas[indice] = new Tortuga(coordenadas[(int) random].x, coordenadas[(int) random].y, entorno, (int) random); 
				condicion = true; // Fin del bucle.
			} else { // Caso contrario se vuelve a generar un nuevo número.
				random = Math.random() * 12;
			}
		}
	}
	private void crearTortugas() {
		for(int i = 0; i < tortugas.length; i++) {
			crearTortuga(i);
			}
		}
	private void crearCoordenadas() {
		int indi = 0;
	    for (int i = 3; i <= 5; i++) { // Crea coordenadas entre la tercer y última fila de islas.
	        int desplazamiento = entorno.ancho() / (i + 1);
	        for (int j = 1; j <= i; j++) {
	            // Cálculo de la posición X para simetría
	            int x = desplazamiento * j + (j - 1) * 38 - (i * 15);
	            // Cálculo de la posición Y para simetría.
	            int y = i * 90;
	            coordenadas[indi] = new Point(x, y);
	            indi++;
	        }
	    }
	}
	private void crearGnomo(int i) {
		gnomos[i] = new Gnomo(entorno);
		gnomos[i].setX(gnomos[i].getX() + i*5); // Separamos a los gnomos modificando su eje x.
	}
	private void crearGnomos() {
		for(int i = 0; i < gnomos.length; i++) {
			crearGnomo(i);
		}
	}
	private void crearBola() {
		bola = new BolaFuego(pep.getX(), pep.getY(), pep.isDireccion(), entorno);	
	}	
	private void crearCorazon(double x, double y) {
		boolean v = false;
		int i = 0;
		while(!v || i == 3) { // Recorre el array de vidas hasta que encuentra una que sea nula.
			if(vidas[i] == null) {
				vidas[i] = new Vida(x, y, entorno);
				v = true;
			}
			i++;
		}
	}
	private void crearBomba(Tortuga tortuga) {
		boolean v = false;
		int i = 0;
		while(!v) { // Recorre el array de vidas hasta que encuentra una que sea nula.
			if(bombas[i] == null) {
				// Se crea una bomba con la dirección y coordenada de la tortuga.
				bombas[i] = new Bomba(tortuga.getX(), tortuga.getY(), tortuga.isDireccion(), entorno);
				v = true;
			}
			i++;
		}
	}
	
	//MOSTRAR OBJETOS Y ESTADÍSTICAS:
	private void dibujarFondo() {
		entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0, 1);
	}
	private void dibujarObjetos() {
		entorno.dibujarImagen(bandera, entorno.ancho()/2 + 10, 510, 0, 0.025);
		
		for(Vida v : vidas) {
			if(v != null) {
				v.mostrar();
			}
		}
		
		pep.mostrar();
		
		if(bola != null) {
			bola.mostrar();
		}
		
		for(Isla i: islas) {
			i.mostrar();
		}
		
		for(Tortuga t : tortugas) {
			if(t != null) {
				t.mostrar();
			}
		}
		for(Bomba b : bombas) {
			if(b != null) {
				b.mostrar();
			}
		}
		for(Gnomo g : gnomos) {
			if(g != null) {
				g.mostrar();
			}
		}

		entorno.dibujarImagen(casaGnomo, entorno.ancho()/2 - 15, 57, 0, 0.18);
		entorno.dibujarImagen(nubes, entorno.ancho()/2, 620, 0, 0.58);
	}	
	private String tiempo() {
		int cantSegundos = entorno.tiempo()/1000; // Pasamos de milésimas a segundos.
		int minutos = cantSegundos / 60;
		int segundos = cantSegundos % 60; 
		return "Tiempo: " + minutos + ":" + segundos; // Retornamos un string de minutos y segundos.
	}
	private void dibujarVidas() {
		for(int i = 0; i < 3; i++) {
			entorno.dibujarImagen(corazonGris, 25 + 28*i, 19, 0, 0.1); // Muestra los corazones "vacíos" en pantalla.
		}
		for(int i = 0; i < cantVidas; i++) {
			entorno.dibujarImagen(corazonVidas, 25 + 28*i, 19, 0, 0.1); // Dibuja corazones arriba de los anteriores.
		}
	}
	private void mostrarEstadisticas() {
		// Muestra valores actuales en pantalla.
		dibujarVidas();
		entorno.cambiarFont("Rockwell", 16, Color.BLACK);
		entorno.escribirTexto(tiempo(), 130, 20);		
		entorno.escribirTexto("Eliminados: " + enemigosEliminados, 247, 20);
		entorno.escribirTexto("Perdidos: " + perdidos, 463, 20);
		entorno.escribirTexto("Salvados: " + salvados + "/" + objetivoSalvados, 575, 20);
	}

	//ESTADOS DE OBJETOS:
	//Caballero:
	private void estadoDeCaballero() {
		colisionCorazon();
		chequearTeclas();
		movVerticalCaballero();
		pisandoIslaPep();
		caballeroPerdio();
	}
	private void chequearTeclas() {
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && !tocoPared()) {
			pep.mover(2);
			if(tocoPared()) pep.setX(pep.getX()- 2);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !tocoPared()) {
			pep.mover(-2);
			if(tocoPared()) pep.setX(pep.getX() + 2);
		}
		if(entorno.sePresiono(entorno.TECLA_ARRIBA) && pep.isEstaApoyado()) {
			pep.saltar();
		}
		if(entorno.sePresiono('c') && bola == null && pep.isEstaApoyado()) {
			crearBola();
		}

	}
	private void caballeroPerdio() { 
		if(colisionPyT() || pep.seCayo() || colisionBombaPep()) {
			cantVidas--;
			crearCaballero(); // Se vuelve a generar el personaje.
			if(cantVidas < 0) {
				jugando = false;
				pep = null;
			}
		}
	}
	private void movVerticalCaballero() {
		pep.movVertical();
		if(tocoTecho()) pep.cancelarSalto();
	}
	// Tortugas:
	private void tortugasEliminadas() {
		for(int i = 0; i < tortugas.length; i++) {
			if(colisionBolaYTortuga(tortugas[i])) {
				int aux = tortugas[i].getIndice();
				double x= tortugas[i].getX();
				double y = tortugas[i].getY();
				bola = null;
				ocupados[aux] = false;	
				tortugas[i] = null;
				enemigosEliminados++;
				// Se crea un nuevo corazon si, por un lado, la cantidad de enemigos eliminados es impar,
				// y, por otro lado, si la cantidad de vidas actuales es menor a la cantidad de vidas máximas que puede tener el caballero 
				if(enemigosEliminados % 2 == 1 && cantVidas < vidas.length) crearCorazon(x, y + 6); //Ajuste de coordenada y.
			}
		}		
	}	
	private void estadoDeTortugas() {
		for(int i = 0; i < tortugas.length; i++) {	
			if(tortugas[i] != null) {
				movimientos(tortugas[i]);
			}
		}
		tortugasEliminadas();
		reinicioTortugas();
	}	
	private void moverTortuga(Tortuga tortuga, Isla isla) {
		if(tortuga.isDireccion()) { // Dirección = false : derecha.
			tortuga.setX(tortuga.getX() - tortuga.getVelocidad());
			if(tortuga.estaAfuera()) tortuga.cambiarDireccion(); // Si llega al borde de la ventana solo cambia de dirección.
			else if(tortuga.getX() < isla.getBordeIzq()) { // Si llega al borde de la isla lanza una bomba y cambia de dirección.
				crearBomba(tortuga);
				tortuga.cambiarDireccion();
			}
		}
		else {
			tortuga.setX(tortuga.getX() + tortuga.getVelocidad()); // Se dirige hacia la izq.
			if(tortuga.estaAfuera()) tortuga.cambiarDireccion(); // Si llega al borde de la ventana solo cambia de dirección.				
			else if(tortuga.getX() > isla.getBordeDer()){ // Si llega al borde izq. de la isla lanza una bomba y cambia de dirección.
				crearBomba(tortuga);
				tortuga.cambiarDireccion();
			}
		}
		
	}
	private void movimientos(Tortuga tortuga) {
		tortuga.movVertical();
		int r = pisandoIslaTortuga(tortuga); // Se guarda el número de isla.
		if(r >= 0) {
			moverTortuga(tortuga, islas[r]); // Se mueve dentro de la isla.
		}
	}
	private void reinicioTortugas() {
		if(gnomosEnAltura()) { //LLamamos al método para evitar que los gnomos colisiones instantáneamente con las nuevas tortugas.
			for(int i = 0; i < tortugas.length; i++) {
				if(tortugas[i] == null) {
					crearTortuga(i);
				}
			}
		}
	}
	// Gnomos:
	private void estadoDeGnomos() {
		for(int i = 0; i < gnomos.length; i++) {
			if(gnomos[i] != null) {
				movimientos(gnomos[i]); // Si el gnomo existe se revisan sus movimientos(ej: si está cayendo, si logró pisar una isla, etc.) 
				// Posibles casos en los que el gnomo se debe eliminar de pantalla.
				if(gnomoPerdido(gnomos[i])) {
					gnomos[i] = null;
					perdidos++;
				}else if(gnomoGanado(gnomos[i])){
					gnomos[i] = null;
					salvados++;
					}
			} else {
				crearGnomo(i); // En caso de que no exista se crea uno nuevo en la casa de los gnomos.
			}
		}		
	}
	private void movimientos(Gnomo gnomo) {
		gnomo.movVertical();
		// El gnomo cambia de dirección aleatoriamente hasta que llegue a una isla.
		if(pisandoIslaGnomo(gnomo)) {
			gnomo.mover(gnomo.isDireccion());
		} else {
			gnomo.cambiarDireccion();
		}
	}
	private boolean gnomoPerdido(Gnomo gnomo) {
		return gnomo.seCayo() || colisionGnomoYTortuga(gnomo) || colisionBomba(gnomo); // Lo que suceda primero.
	}
	private boolean gnomoGanado(Gnomo gnomo) {
		return colisionPepYGnomo(pep, gnomo) && pep.puedeRescatar();
	}
	private boolean gnomosEnAltura() {
		// Método para conocer si todos los gnomos se encuentran en la primera o segunda fila de islas.
		boolean aux = true;
		for(int i = 0; i < gnomos.length; i++) {
			if(gnomos[i].getY() > 240) {
				aux = false;
			}
		}
		return aux;
	}
	// Bola de fuego y bombas:
	private void estadoBolaFuego() {
		if(bola != null) {
			bola.mover();
			if(bola.estaFuera()) bola = null;
		}
	}
	private void estadoBombas() {
		for(int i = 0; i < bombas.length; i++) {
			if(bombas[i] != null) {
				bombas[i].mover();
				if(colisionBolaYBomba(bombas[i])) {
					bombas[i] = null;
					bola = null;
				}
				if(bombas[i] != null && bombas[i].estaFuera()) bombas[i] = null;
			}
		}
	}


	///RESULTADOS FINALES:
	private boolean objetivoPerdido() {
		if(perdidos >= maxPerdidos) { // Si la cantidad de gnomos perdidos es mayor o igual a la cantidad máxima permitida.
			jugando = false;
			pep = null;
			return true;
		}
		return false;
	}
	private boolean objetivoCumplido() {
		if(salvados == objetivoSalvados) { // Si se cumple la cantidad definida como objetivo.
			return true;
		}
		return false;
	}	
	private void escribirResultado(String resultado) {
		// Escribe en pantalla el resultado final con un efecto tipo 3D.
		entorno.dibujarImagen(cuadro, entorno.ancho()/2, 290, 0, 0.5);
		entorno.cambiarFont("Rockwell", 34, Color.BLACK);
		entorno.escribirTexto(resultado, 275, 218);
		entorno.cambiarFont("Rockwell", 34, Color.YELLOW);
		entorno.escribirTexto(resultado, 278, 215);
	}
	private void juegoGanado() {
		jugando = false;
		escribirResultado("¡ G A N A S T E !");
		estadisticasFinal();
		if (entorno.estaPresionada('r')) reiniciarJuego();		
	}
	private void juegoPerdido() {
		escribirResultado("¡ P E R D I S T E !");
		estadisticasFinal();
		if (entorno.estaPresionada('r')) reiniciarJuego();
	}
	private void estadisticasFinal() { 
		entorno.cambiarFont("Rockwell", 20, Color.BLACK);
		// Muestra en pantalla los resultados pasando los valores enteros a string.
		entorno.escribirTexto(enemigosEliminados + "", 493, 256);
		entorno.escribirTexto(salvados + "", 475, 312);
		entorno.escribirTexto(perdidos + "", 475, 367);
		if (entorno.estaPresionada('r')) reiniciarJuego();
	}
	
	//COLISIONES:
	private boolean pisandoIslaPep(Isla isla) {
		return (Math.abs(pep.getBordeInf() - (isla.getBordeSup()-0.5)) < 1) &&
				(pep.getBordeDer() > isla.getBordeIzq()) && (pep.getBordeIzq() < isla.getBordeDer());
	}	
	private boolean pisandoIslaPep() {
		for(Isla isla : islas) {
			if(pisandoIslaPep(isla) && !pep.isEstaSaltando()) {
				pep.setEstaApoyado(true);
				return true;
			}
		}
		pep.setEstaApoyado(false);
		return false;
	}
	private boolean tocoTecho(Caballero pep, Isla isla) {
		return ((Math.abs(pep.getBordeSup()- isla.getBordeInf()) < 2)
				&& (pep.getBordeDer() > (isla.getBordeIzq()))
				&& (pep.getBordeIzq() < (isla.getBordeDer())));
	}
	private boolean tocoPared(Caballero pep, Isla isla) {
		return Math.abs(pep.getY() - isla.getY()) < isla.getAlto() && ((Math.abs(pep.getBordeDer() - isla.getBordeIzq()) < 2)
				|| (Math.abs(pep.getBordeIzq() - isla.getBordeDer()) < 2));
	}
	private boolean tocoPared() {
		for(Isla isla : islas){
			if(tocoPared(pep, isla)){
				return true;
			}
		}
		return false;
	}
	private boolean tocoTecho() {
		for(Isla isla : islas) {
			if(tocoTecho(pep, isla)) {
				return true;
			}
		}
		return false;
	}	
	private boolean pisandoIslaTortuga(Tortuga tortuga, Isla isla) {
		return (Math.abs(tortuga.getBordeInf() - isla.getBordeSup()) < 1) &&
				tortuga.getBordeDer() > isla.getBordeIzq() && tortuga.getBordeIzq() < isla.getBordeDer();
	}
	private int pisandoIslaTortuga(Tortuga tortuga) {
		for(int i = 0; i < islas.length; i++) {
			if(pisandoIslaTortuga(tortuga, islas[i])) {
				tortuga.setEstaApoyado(true);
				return i;
			}
		}
		return -1;
	}
	private boolean pisandoIslaGnomo(Gnomo gnomo, Isla isla) {
		return (Math.abs(gnomo.getBordeInf() - isla.getBordeSup()) < 1) &&
				(gnomo.getBordeDer() > isla.getBordeIzq()) && (gnomo.getBordeIzq() < isla.getBordeDer());
	}	
	private boolean pisandoIslaGnomo(Gnomo gnomo) {
		for(Isla isla : islas) {
			if(pisandoIslaGnomo(gnomo, isla)) {
				gnomo.setEstaApoyado(true);
				return true;
			}
		}
		gnomo.setEstaApoyado(false);
		return false;
	}
	private boolean colisionPyT(Caballero pep, Tortuga tortuga) {
		return (tortuga.getBordeIzq() < pep.getBordeDer() && tortuga.getBordeDer() > pep.getBordeIzq() &&
				tortuga.getBordeSup() < pep.getBordeInf() && tortuga.getBordeInf() > pep.getBordeSup());
		}
	private boolean colisionPyT() {
		for(Tortuga tortuga : tortugas) {
			if(tortuga != null) {
				if(colisionPyT(pep, tortuga)) {
					return true;
				}
			}
		}
		return false;
	}
	private boolean colisionPepYGnomo(Caballero pep, Gnomo gnomo) {
		return (gnomo.getBordeIzq() < pep.getBordeDer() && gnomo.getBordeDer() > pep.getBordeIzq() &&
				gnomo.getBordeSup() < pep.getBordeInf() && gnomo.getBordeInf() > pep.getBordeSup());
		}
	private boolean colisionGnomoYTortuga(Gnomo gnomo, Tortuga tortuga) {
		return (tortuga.getBordeIzq() < gnomo.getBordeDer() && tortuga.getBordeDer() > gnomo.getBordeIzq() &&
				tortuga.getBordeSup() < gnomo.getBordeInf() && tortuga.getBordeInf() > gnomo.getBordeSup());
		}
	private boolean colisionGnomoYTortuga(Gnomo gnomo) {
		for(Tortuga tortuga : tortugas) {
			if(tortuga != null) {
				if(colisionGnomoYTortuga(gnomo, tortuga)) {
					return true;
				}
			}
		}
		return false;
	}	
	private boolean colisionBolaYTortuga(Tortuga tortuga) {
		if(bola != null && tortuga != null) {
			return (tortuga.getBordeIzq() < bola.getBordeDer() && tortuga.getBordeDer() > bola.getBordeIzq() &&
					tortuga.getBordeSup() < bola.getBordeInf() && tortuga.getBordeInf() > bola.getBordeSup());
		}
		return false;
	}
	private boolean colisionBolaYBomba(Bomba bomba) {
		if(bola != null) {
			return bomba.getBordeIzq() < bola.getBordeDer() && bomba.getBordeDer() > bola.getBordeIzq() &&
					bomba.getBordeSup() < bola.getBordeInf() && bomba.getBordeInf() > bola.getBordeSup();
		}
		return false;
	}
	private void colisionCorazon() {
		for(int i = 0; i < vidas.length; i++) {
			if(colisionCorazon(vidas[i])) {
				vidas[i] = null;
				cantVidas++;
			}
		}
	}
	private boolean colisionCorazon(Vida vida) {
		if(vida != null) {
			return vida.getBordeIzq() < pep.getBordeDer() && vida.getBordeDer() > pep.getBordeIzq() &&
					vida.getBordeSup() < pep.getBordeInf() && vida.getBordeInf() > pep.getBordeSup();
		}
		return false;
	}
	private boolean colisionBombaPep(Bomba bomba) {
		return bomba.getBordeIzq() < pep.getBordeDer() && bomba.getBordeDer() > pep.getBordeIzq() &&
				bomba.getBordeSup() < pep.getBordeInf() && bomba.getBordeInf() > pep.getBordeSup();
	}
	private boolean colisionBombaPep() {
		for(int i = 0; i < bombas.length; i++) {
			if(bombas[i] != null && colisionBombaPep(bombas[i])) {
				bombas[i] = null;
				return true;
			}	
		}
		return false;
	}
	private boolean colisionBomba(Gnomo gnomo, Bomba bomba) {
		return (bomba.getBordeIzq() < gnomo.getBordeDer() && bomba.getBordeDer() > gnomo.getBordeIzq() &&
				bomba.getBordeSup() < gnomo.getBordeInf() && bomba.getBordeInf() > gnomo.getBordeSup());
	}
	private boolean colisionBomba(Gnomo gnomo) {
		for(int i = 0; i < bombas.length; i++) {
			if(bombas[i] != null) {
				if(colisionBomba(gnomo, bombas[i])) {
					bombas[i] = null;
					return true;
				}
			}
		}
		return false;
	}	
	//REINICIO:
	private void reiniciarJuego() {
		//Reestablece valores.
		reiniciarValores();
		crearCaballero();
		crearTortugas();
		crearGnomos();
	}	
	private void reiniciarValores() {
		for(int i = 0; i < bombas.length; i++) {
			bombas[i] = null;
		}
		for(int i = 0; i< ocupados.length; i++) {
			ocupados[i] = false;
		}
		for(int i = 0; i< vidas.length; i++) {
			vidas[i] = null;
		}
		this.jugando = true;
		this.perdidos = 0;
		this.salvados = 0;
		this.enemigosEliminados = 0;
		this.cantVidas = 0;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
