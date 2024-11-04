package juego;

import java.awt.Image;

import entorno.Entorno;

public class Tortuga {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private boolean direccion; //false:derecha, true:izq.
	private Image imagenDer;
	private Image imagenIzq;
	private double escala;
	private double velocidad;
	private Entorno e;
	private boolean estaApoyado;
	private int indice;
	
	public Tortuga(double x, double y, Entorno e, int indice) {
		this.indice = indice;
		this.x = x;
		this.y = y;
		this.e = e;
		this.direccion = false;
		this.escala = 0.03;
		this.velocidad = 0.5;
		this.imagenDer = entorno.Herramientas.cargarImagen("TortugaDer.png");
		this.imagenIzq = entorno.Herramientas.cargarImagen("TortugaIzq.png");
		this.setAncho(imagenDer.getWidth(null)*this.escala);
		this.alto = imagenDer.getHeight(null)*this.escala;
		this.estaApoyado = false;
	}

	void movVertical() {
		if(!this.isEstaApoyado()) {
			setY(getY() + 2);
		}
	}
	
	double getVelocidad() {
		return velocidad;
	}

	void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	void mostrar() {
		if(isDireccion()) {
			e.dibujarImagen(imagenIzq, getX(), getY(), 0, escala);
		}
		else {
			e.dibujarImagen(imagenDer, getX(), getY(), 0, escala);
		}
	}
	
	int getIndice() {
		return indice;
	}

	void setIndice(int indice) {
		this.indice = indice;
	}

	boolean isEstaApoyado() {
		return estaApoyado;
	}

	void setEstaApoyado(boolean estaApoyado) {
		this.estaApoyado = estaApoyado;
	}

	boolean isDireccion() {
		return direccion;
	}

	void cambiarDireccion() {
		this.direccion = !this.direccion;
	}
	
	boolean estaAfuera() {
		return getBordeDer() > e.ancho() || getBordeIzq() < 0;
	}
	double getBordeDer() {
		return getX() + (this.getAncho()/2) - 2; //Ajuste de escala.
	}
	
	double getBordeIzq() {
		return getX() - (this.getAncho()/2) + 2;
	}
	
	double getBordeSup() {
		return getY() - (this.alto/2) + 3;
	}
	
	double getBordeInf() {
		return getY() + (this.alto/2);
	}

	double getX() {
		return x;
	}

	void setX(double x) {
		this.x = x;
	}

	double getY() {
		return y;
	}

	void setY(double y) {
		this.y = y;
	}

	double getAncho() {
		return ancho;
	}

	void setAncho(double ancho) {
		this.ancho = ancho;
	}
	
}
