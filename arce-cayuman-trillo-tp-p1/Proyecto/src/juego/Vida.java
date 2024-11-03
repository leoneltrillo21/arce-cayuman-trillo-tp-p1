package juego;

import java.awt.Image;

import entorno.Entorno;

public class Vida {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private Image imagen;
	private double escala;
	private Entorno e;
	
	public Vida(double x, double y, Entorno ent) {
		this.x = x;
		this.y = y;
		this.escala = 0.1;
		this.imagen = entorno.Herramientas.cargarImagen("Corazon.png");
		this.e = ent;
		this.ancho = imagen.getWidth(null)*this.escala;
		this.alto = imagen.getHeight(null)*this.escala;
	}
	
	void mostrar() {
		e.dibujarImagen(imagen, x, y, 0, escala);
	}
	
	double getBordeDer() {
		return x + (this.ancho/2);
	}
	
	double getBordeIzq() {
		return x - (this.ancho/2);
	}
	
	double getBordeSup() {
		return y - (this.alto/2);
	}
	
	double getBordeInf() {
		return y + (this.alto/2);
	}
	
}
