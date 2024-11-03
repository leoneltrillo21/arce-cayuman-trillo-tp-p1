package juego;

import java.awt.Image;

import entorno.Entorno;

public class BolaFuego {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private double escala;
	private double velocidad;
	private int recorrido;
	private Image imagenDer, imagenIzq;
	private boolean direccion; //false:derecha, true:izquierda.
	private Entorno e;
	
	public BolaFuego(double x, double y, boolean direccion, Entorno ent) {
		this.x = x;
		this.y = y;
		this.escala = 0.2;
		this.velocidad = 4;
		this.imagenDer = entorno.Herramientas.cargarImagen("BFuegoDer.png");
		this.imagenIzq = entorno.Herramientas.cargarImagen("BFuegoIzq.png");
		this.direccion = direccion;
		this.ancho = imagenDer.getWidth(null)*this.escala;
		this.alto = imagenIzq.getHeight(null)*this.escala;
		this.e =ent;
	}
	
	void mover() {
		if(!direccion) {
			x = x + velocidad;
			recorrido++;
		} else {
			x = x - velocidad;
			recorrido++;
		}		
	}

	void mostrar() {
		if(direccion) {
			e.dibujarImagen(imagenIzq, x, y, 0, escala);
		}
		else {
			e.dibujarImagen(imagenDer, x, y, 0, escala);
		}
	}
	
	boolean estaFuera() {
		return x < -1 || x > e.ancho() + 1 || recorrido > 30;
	}
	
	double getBordeDer() {
		return x + (this.ancho/2) - 2; //Ajuste de escala.
	}
	
	double getBordeIzq() {
		return x - (this.ancho/2) + 2;
	}
	
	double getBordeSup() {
		return y - (this.alto/2);
	}
	
	double getBordeInf() {
		return y + (this.alto/2);
	}
}
