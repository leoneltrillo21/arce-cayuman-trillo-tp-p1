package juego;

import java.awt.Image;

import entorno.Entorno;

public class Gnomo {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private boolean direccion; //false:derecha, true:izq.
	private Image imagenDer;
	private Image imagenIzq;
	private Image imagenCayendo;
	private double escala;
	private double velocidad;
	private Entorno e;
	private boolean estaApoyado;
	
	public Gnomo(Entorno e) {
		this.setX(e.ancho() / 2 - 27);
		this.setY(41);
		this.e = e;
		this.setDireccion(false);
		this.escala = 0.9;
		this.velocidad = 0.3;
		this.imagenDer = entorno.Herramientas.cargarImagen("GnomoDer.png");
		this.imagenIzq = entorno.Herramientas.cargarImagen("GnomoIzq.png");
		this.imagenCayendo = entorno.Herramientas.cargarImagen("GnomoC.png");
		this.ancho = imagenDer.getWidth(null)*this.escala;
		this.alto = imagenDer.getHeight(null)*this.escala;
		this.setEstaApoyado(true);
	}
	
	void mover(boolean direccion) {
		if(isEstaApoyado()) {
			if(direccion) {
				setX(getX() - velocidad);
			}else {
				setX(getX() + velocidad);
			}
		}
	}
	
	void movVertical() {
		if(!this.isEstaApoyado()) {
			setY(getY() + 2);
		}
	}
	
	void mostrar() {
		if(isEstaApoyado()) {
			if(isDireccion()) {
				e.dibujarImagen(imagenIzq, getX(), getY(), 0, escala);
			} else {
				e.dibujarImagen(imagenDer, getX(), getY(), 0, escala);
			}
		} else {
			e.dibujarImagen(imagenCayendo, getX(), getY(), 0, escala);
		}
	}
	
	void cambiarDireccion() {
		this.setDireccion(Math.random() < 0.5);
	}
	
	boolean seCayo() {
		return this.getY() > e.alto();
	}
	
	double getBordeDer() {
		return getX() + (this.ancho/2);
	}
	
	double getBordeIzq() {
		return getX() - (this.ancho/2);
	}
	
	double getBordeSup() {
		return getY() - (this.alto/2);
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

	boolean isDireccion() {
		return direccion;
	}
	void setDireccion(boolean direccion) {
		this.direccion = direccion;
	}

	boolean isEstaApoyado() {
		return estaApoyado;
	}

	void setEstaApoyado(boolean estaApoyado) {
		this.estaApoyado = estaApoyado;
	}
}
