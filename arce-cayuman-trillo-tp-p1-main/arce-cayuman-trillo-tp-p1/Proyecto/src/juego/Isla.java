package juego;

import java.awt.Image;

import entorno.Entorno;

public class Isla {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private Image imagen;
	private double escala;
	private Entorno e;
	
	public Isla(double x, double y, Entorno ent) {
		this.setX(x);
		this.setY(y);
		this.escala = 0.5;
		this.imagen = entorno.Herramientas.cargarImagen("IslaFlotante.png");
		this.e = ent;
		this.ancho = imagen.getWidth(null)*this.escala;
		this.setAlto(imagen.getHeight(null)*this.escala);
	}
	
	void mostrar() {
		e.dibujarImagen(imagen, x, getY(), 0, escala);
	}
	
	double getBordeDer() {
		return x + (this.ancho/2); //Ajuste de escala.
	}
	
	double getBordeIzq() {
		return x - (this.ancho/2);
	}
	
	double getBordeSup() {
		return getY() - (this.getAlto()/2);
	}
	
	double getBordeInf() {
		return getY() + (this.getAlto()/2);
	}

	double getY() {
		return y;
	}

	void setY(double y) {
		this.y = y;
	}

	double getX() {
		return x;
	}

	void setX(double x) {
		this.x = x;
	}

	double getAlto() {
		return alto;
	}

	void setAlto(double alto) {
		this.alto = alto;
	}
	
}
