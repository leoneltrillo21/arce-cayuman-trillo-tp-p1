package juego;

import java.awt.Image;

import entorno.Entorno;

public class Caballero {
	private double y;
	private double x;
	private double alto;
	private double ancho;
	private boolean direccion; //false: derecha, true: izquierda.
	private Image imagenIzq;
	private Image imagenDer;
	private double escala;
	private Entorno e;
	private boolean estaApoyado;
	private boolean estaSaltando;
	private int contadorSalto;
	
	public Caballero(double y, double x, Entorno ent) {
		this.x = x;
		this.y = y;
		this.direccion = false;
		this.escala = 0.17;
		this.imagenDer = entorno.Herramientas.cargarImagen("ImagePersonajeDer.png");
		this.imagenIzq = entorno.Herramientas.cargarImagen("ImagePersonajeIzq.png");
		this.e = ent;
		this.ancho = imagenDer.getWidth(null)*this.escala;
		this.alto = imagenIzq.getHeight(null)*this.escala;
		this.estaApoyado = false;
		this.estaSaltando = false;
		this.contadorSalto = 0;
	}
	
	void movVertical() {
		if(!this.isEstaApoyado() && !this.isEstaSaltando()) {
			setY(getY() + 2);
		}
		if(this.isEstaSaltando() && !this.isEstaApoyado()) {
			setY(getY() - 3);
			contadorSalto++;
		}
		if(getBordeSup() < 0) {
			cancelarSalto();
		}
		if(contadorSalto==40) {
			setEstaSaltando(false);
			contadorSalto = 0;
		}
	}
	
	void mostrar() {
		if(isDireccion()) {
			e.dibujarImagen(imagenIzq, getX(), getY(), 0, escala);
		}
		else {
			e.dibujarImagen(imagenDer, getX(), getY(), 0, escala);
		}
	}
	
	void mover(double y) {
		this.setX(this.getX() + y);
		if(getBordeDer() > this.e.ancho()) {
			this.setX(e.ancho()-(this.ancho/2));
		}
		if(getBordeIzq() < 0) {
			this.setX(this.ancho/2);
		}
		if(y >= 0) {
			this.setDireccion(false);
		}
		else {
			this.setDireccion(true);
		}
	}
	
	void saltar() {
		this.setEstaApoyado(false);
		this.setEstaSaltando(true);
	}

	void cancelarSalto() {
		this.setEstaSaltando(false);
		contadorSalto = 0;
	}
	
	boolean seCayo() {
		return getY() > e.alto();
	}
	
	boolean puedeRescatar() {
		return getBordeInf() > 250; //375 fila 3 de islas
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

	boolean isEstaSaltando() {
		return estaSaltando;
	}

	void setEstaSaltando(boolean estaSaltando) {
		this.estaSaltando = estaSaltando;
	}
}
