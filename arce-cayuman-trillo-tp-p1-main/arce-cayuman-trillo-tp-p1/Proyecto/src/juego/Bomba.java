package juego;

import java.awt.Image;

import entorno.Entorno;

public class Bomba {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private double escala;
	private double velocidad;
	private Image imagen;
	private boolean direccion; //false:derecha, true:izquierda.
	private Entorno e;
	
	public Bomba(double x, double y, boolean direccion, Entorno ent) {
		this.x = x;
		this.y = y;
		this.escala = 0.03;
		this.velocidad = 2;
		this.imagen = entorno.Herramientas.cargarImagen("Bomba.png");
		this.direccion = direccion;
		this.ancho = imagen.getWidth(null)*this.escala;
		this.alto = imagen.getHeight(null)*this.escala;
		this.e =ent;
	}
	
	void mover() {
		if(!direccion) {
			x = x + velocidad;
		} else {
			x = x - velocidad;
		}		
	}

	void mostrar() {
		e.dibujarImagen(imagen, x, y, 0, escala);
	}
	
	boolean estaFuera() {
		return x < -1 || x > e.ancho() +1;
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
