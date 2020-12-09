/**
 * Estrutura de dados que armazena as informacoes do grafo
 * @author Jeferson Ferreira
 * @since  03/08/2005
 */
package org.flowgenerator.domain;
import java.util.*;
public class ControlFlowGraph {
	
	// armazena a lista de nos do grafo
	private LinkedList<GraphNode> nodes;

	public ControlFlowGraph() {
		nodes = new LinkedList<GraphNode>();
	}
	
	// insere um no no grafo
	public void insertNode(int number, int previous, int level, int command) {
		GraphNode newNode;
		newNode = new GraphNode(number, previous, level, command);
		nodes.add(newNode);
	}
	
	// insere um arco entre dois nos
	public void joinGraphNodes(int origin, int destination) {
		// selecionar o objeto no grafo
		GraphNode aux = (GraphNode) nodes.get(origin-1);
		GraphNode aux2 =(GraphNode) nodes.get(destination-1);
		// inserir o arco no noh source
		aux.insertArc((aux2.getLabelNode()).intValue());
	}
	
	// inativa um no do grafo
	public void inactiveGraphNode(int number) {
		// selecionar o objeto no grafo
		GraphNode aux = (GraphNode) nodes.get(number-1);
		// inativar o noh
		aux.inactive();
	}
	public GraphNode getNode(int number) {
		return (GraphNode) nodes.get(number-1);                
	}
	//      retorna a lista dos nos
	public LinkedList<GraphNode> getNodes() {
		return nodes;
	}
	// remove um no do grafo
	public boolean removeNode(GraphNode node) {
		return nodes.remove(node);
	}
}
