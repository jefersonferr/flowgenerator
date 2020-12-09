package org.flowgenerator.domain;
import java.util.*;
/**
 * classe que tem define um cluster para um  classe
 * que contem uma colecao de clusters de metodos  e 
 * uma colecao de clusters para as classes internas
 * @author Jeferson Ferreira
 */
public class ClusterClass extends ClusterGraph {
	
	// lista de metodos
    private LinkedList<ClusterMethod> metodos;
    private LinkedList<ClusterClass> inner_classes;
	/**
	 * constroi um cluster para uma determinada classe recebendo
	 * como parametro o seu rotulo, estilo e cor de borda
	 * @param label - rotulo do cluster 
	 * @param style - estilo da borda do cluster
	 * @param color - cor da borda do cluster
	 */
	public ClusterClass(String label, String style, String color) {
                  
		super(label, style, color);
if (true) {
		metodos = new LinkedList<ClusterMethod>();
		inner_classes = new LinkedList<ClusterClass>();
                 }
	}

	/**
	 * constroi um cluster para uma determinada classe recebendo
	 * somente o seu rotulo, o estilo e a cor da borda assume o
	 * valor padrao:  (style = "filled") e (color = "black") 
	 * @param label - rotulo do cluster
	 */
	public ClusterClass(String label) {
		super(label);
		metodos = new LinkedList<ClusterMethod>();
		inner_classes = new LinkedList<ClusterClass>();
	}
	/**
	 * retorna uma referencia da lista de metodos da classe
	 * para ser manipulada pela classe GraphGenerator
	 * @return LinkedList
	 */
	public LinkedList<ClusterMethod> getMetodos() {
		return metodos;
	}
	/**
	 * adiciona um metodo a lista de metodos da classe
	 * @param metodo - metodo
	 * @see ClusterMethod
	 */
	public void addMetodos (ClusterMethod metodo) {
		metodos.add(metodo);
	}

	/**
	 * retorna uma referencia da lista de classes internas da classe
	 * para ser manipulada pela classe GraphGenerator
	 * @return LinkedList
	 */
	public LinkedList<ClusterClass> getClasses() {
		return inner_classes;
	}

	/**
	 * adiciona uma classe interna a lista de classes
	 * @param inner_class - classe interna
	 */
	public void addClasses(ClusterClass inner_class) {
		inner_classes.add(inner_class);
	}
}
