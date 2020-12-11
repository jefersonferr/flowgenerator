package org.flowgenerator.domain;
import java.util.Stack;

/**
 * Define um cluster para um metodo de uma classe.
 * que contem um grafo de fluxo de controle associado
 * @author Jeferson Ferreira
 * @since 28/08/2005
 */
public class ClusterMethod extends ClusterGraph implements JavaParserConstants {

    // atributos
    private int node, node_ant, level, cc;
    // grafo de fluxo de controle do metodo
    private ControlFlowGraph graphNodes;
    // no auxiliar utilizado para a construcao do GFC
    private GraphNode node_aux;
    // pilhas
    private Stack<GraphNode> stackNodes, aux_stack;
    /**
     * Constroi um cluster para um determinado metodo.
     * recebendo como parametro o seu rotulo, estilo e cor de borda
     * @param label - String - rotulo do cluster 
	 * @param style - String - estilo da borda do cluster
	 * @param color - String - cor da borda do cluster
	 */
	public ClusterMethod(String label, String style, String color) {
		super(label, style, color);
		node = 0; 
		node_ant = node;
		level = 0;
		graphNodes = new ControlFlowGraph();
		stackNodes = new Stack<GraphNode>();
		aux_stack  = new Stack<GraphNode>();
	}
	
	/**
	 * Constroi um cluster para um determinado metodo recebendo
	 * somente o seu rotulo, o estilo e a cor da borda assume o
	 * valor padrao:  (style = "filled") e (color = "black") 
	 * @param label - String - rotulo do cluster
	 */
	public ClusterMethod(String label) {
		super(label);
		node = 0;
		node_ant = node;
		level = 0;
		graphNodes = new ControlFlowGraph();
		stackNodes = new Stack<GraphNode>();
		aux_stack  = new Stack<GraphNode>();
	}
	
	/**
	 * Recebe o comando enviado pela classe GraphGenerator 
	 * e faz o tratamento conforme o comando para gerar o 
	 * grafo de fluxo de controle
	 * @param c - int - numero do comando 
	 * @see GraphGenerator
	 */
	public void processCommand(int c) {
		// comandos BEGIN, WHILE, DO, SWITCH e FOR
		if (c==BEGIN||c==IF||c==WHILE||c==DO||c==SWITCH||c==FOR) {
			node_ant = node;
			node++;
			level++;
			graphNodes.insertNode( node, node_ant, level, c);
			if (node_ant != 0) {
				graphNodes.joinGraphNodes(node_ant, node);
			}
            //	modificado para tratar o bug(30-04)
			if (c==SWITCH||c==IF) {
				node_aux = (GraphNode)stackNodes.pop();
				if (node_aux.getCommand()!=WHILE || node_aux.getCommand()!=FOR) {
					// altera o status do noh para inativo
					node_aux.inactive();
				}
				stackNodes.push(node_aux);
			}
			// incluido para tratar o bug(30-04)
			if (c==WHILE) {
				node_aux = (GraphNode)stackNodes.pop();
				if (node_aux.getCommand()==THEN||node_aux.getCommand()==ELSE) {
					// altera o status do noh para inativo
					node_aux.inactive();
				}
				stackNodes.push(node_aux);
			}
			stackNodes.push(graphNodes.getNode(node));
			if (c==SWITCH) {
				level++;
			}
			// inserido para resolver bug(30-04)
			if (c==IF) {
				c = THEN;
				node_ant = node;
				node++;
				level++;
				graphNodes.insertNode( node, node_ant, level, c);
				graphNodes.joinGraphNodes(node_ant, node);
				stackNodes.push(graphNodes.getNode(node));	
			}
			// comando ELSE  
		} else if (c==ELSE) {
			// encontra o if
			do {
				node_aux  = (GraphNode)stackNodes.pop();
				aux_stack.push(node_aux);
			} while(node_aux.getCommand()!=IF);
			
			// pega o no que originou o if e seta como anterior
			// comentado codigo abaixo em virturde do bug(30-04)
			//node_aux  = (GraphNode)stackNodes.pop();
			//aux_stack.push(node_aux);
			node_ant = (node_aux.getNumber()).intValue();
		    if (node_aux.getStatus()=='E') {
		    	if (node_aux.getStatusAnt()=='A') {
		    	    node_aux.active();
		    	} else if(node_aux.getStatusAnt()=='I') {
		    	    node_aux.inactive();
		    	}
		    }
			
			// empilha os nohs novamente
			do {
				if (!aux_stack.empty()) {
					node_aux  = (GraphNode)aux_stack.pop();
					stackNodes.push(node_aux);
				}
			} while(!aux_stack.empty());
			
			node++;
			graphNodes.insertNode( node, node_ant, level, c);
			if (node_ant != 0) {
				graphNodes.joinGraphNodes(node_ant, node);
			}
			stackNodes.push(graphNodes.getNode(node));
			
			// comandos ENDIF e ENDCASE
		} else if (c==ENDIF||c==ENDCASE) {
			int i=0;
			node++;
			level--;
		//	if (c==ENDCASE) {
				level--;
		//	}
			graphNodes.insertNode( node, node_ant, level, c);
			do {
				node_aux = (GraphNode)stackNodes.pop();
				
				// a validacao do numero de arcos impede que de um noh parta mais de 2 arcos
				if (node_aux.getLevel()>level) {
					if ((node_aux.numArcs()<2)&&(node_aux.getStatus()=='A')||node_aux.getStatus()=='E') {
						graphNodes.joinGraphNodes((node_aux.getNumber()).intValue(), node);
						if (node_aux.getStatus()=='E') {
							// usa apenas uma vez a excecao
							if (node_aux.getStatusAnt()=='A') {
								node_aux.active();
							} else {
								node_aux.inactive();
							}
						}
						i++;
					}
				} else {
					if ((node_aux.getCommand()!=WHILE)||(node_aux.getCommand()!=FOR||node_aux.getCommand()==DO||node_aux.getCommand()==IF)) {
						stackNodes.push(node_aux);
					}
					if (i<2||node_aux.getStatus()=='E') {
						if (node_aux.numArcs()<2||node_aux.getStatus()=='E') {
							if (node_aux.getStatus()=='E') {
								// usa apenas uma vez a excecao
								if (node_aux.getStatusAnt()=='A') {
									node_aux.active();
								} else {
									node_aux.inactive();
								}
							}
							graphNodes.joinGraphNodes((node_aux.getNumber()).intValue(), node);
						}
					}
					stackNodes.push(graphNodes.getNode(node));
				}
				
			} while((!stackNodes.empty())&&(node_aux.getLevel()>level));
			
			// comando LOOP
		} else if (c==LOOP) {
			node_ant = node;
			node++;
			graphNodes.insertNode( node, node_ant, level, c);
			graphNodes.joinGraphNodes(node_ant, node);
			do {
				node_aux = (GraphNode)stackNodes.pop();
			} while (node_aux.getCommand()!=WHILE);
			graphNodes.joinGraphNodes(node, (node_aux.getNumber()).intValue());
			stackNodes.push(graphNodes.getNode(node)); //ALTEREI para acertar a tabela dos comandos
			node_ant = (node_aux.getNumber()).intValue();
			node++;
			level--;
			graphNodes.insertNode( node, node_ant, level, c);
			graphNodes.joinGraphNodes(node_ant, node);
			node_aux = (GraphNode)stackNodes.pop();
			if (node_aux.getCommand()!=WHILE) {
				// altera o status do noh par inativo
				node_aux.inactive();
			}
			stackNodes.push(node_aux);
			stackNodes.push(graphNodes.getNode(node));
			
			// comando ENDFOR   
		} else if (c==ENDFOR) {
			node_ant = node;
			node++;
			graphNodes.insertNode( node, node_ant, level, c);
			graphNodes.joinGraphNodes(node_ant, node);
			do {
				node_aux = (GraphNode)stackNodes.pop();
			} while (node_aux.getCommand()!=FOR);
			graphNodes.joinGraphNodes(node,(node_aux.getNumber()).intValue());
			node_ant = (node_aux.getNumber()).intValue();
			node++;
			level--;
			graphNodes.insertNode( node, node_ant, level, c);
			graphNodes.joinGraphNodes(node_ant, node);
			node_aux = (GraphNode)stackNodes.pop();
			if (node_aux.getCommand()!=FOR) {
				// altera o status do noh para inativo
				node_aux.inactive();
			}
			stackNodes.push(node_aux);
			stackNodes.push(graphNodes.getNode(node));
			
			// comando ENDDO
		} else if (c==ENDDO) {
			node_ant = node;
			node++;
			graphNodes.insertNode( node, node_ant, level, c);
			graphNodes.joinGraphNodes(node_ant, node);
			do {
				node_aux = (GraphNode)stackNodes.pop();
			} while (node_aux.getCommand()!= DO);
			graphNodes.joinGraphNodes(node,(node_aux.getNumber()).intValue());
			node_ant = node;
			node++;
			level--;
			graphNodes.insertNode( node, node_ant, level, c);
			graphNodes.joinGraphNodes(node_ant, node);
			node_aux = (GraphNode)stackNodes.pop();
			if (node_aux.getCommand()!=DO) {
				// altera o status do noh para inativo
				node_aux.inactive();
			}
			stackNodes.push(node_aux);
			stackNodes.push(graphNodes.getNode(node));
			
			// comandos CASE e DEFAULT   
		} else if (c==CASE||c==_DEFAULT) {
			
			// encontra o switch
			do {
				node_aux  = (GraphNode)stackNodes.pop();
				aux_stack.push(node_aux);
			} while(node_aux.getCommand()!=SWITCH);
			node_ant = (node_aux.getNumber()).intValue();
			
			// empilha os nohs novamente
			do {
				if (!aux_stack.empty()) {
					node_aux  = (GraphNode)aux_stack.pop();
					stackNodes.push(node_aux);
				}
			} while(!aux_stack.empty());
			node++;
			graphNodes.insertNode( node, node_ant, level, c);
			if (node_ant != 0) {
				graphNodes.joinGraphNodes(node_ant, node);
			}
			stackNodes.push(graphNodes.getNode(node));
		}
	} 
	public void addInformation(String c, String expr, int line){
		node_aux = (GraphNode)stackNodes.pop();
		if (c=="(loop)") {
			aux_stack.push(node_aux);
			node_aux = (GraphNode)stackNodes.pop();
		}
		// inserido para tratar o bug(30-04)
		if (c=="if") {
			node_aux.setDescCommand("(then)");
			node_aux.setExpression(null);
			node_aux.setLineCommand(null);
			aux_stack.push(node_aux);
			node_aux = (GraphNode)stackNodes.pop();
		}
		node_aux.setDescCommand(c);
		node_aux.setExpression(expr);
		node_aux.setLineCommand(line);
		stackNodes.push(node_aux);
		//modificad para tratar o bug(20-04)
		if (c=="(loop)"||c=="if") {
			node_aux = (GraphNode)aux_stack.pop();
			stackNodes.push(node_aux);
		}
	}
	/**
	 * retorna uma referencia do grafo de fluxo de controle do metodo
	 * para ser manipulado externamente
	 * @return ControlFlowGraph
	 * @see ControlFlowGraph
	 */
	public ControlFlowGraph getGfc() {
		return graphNodes;
	}

	public int getCc() {
		return cc;
	}

	public void setCc(int cc) {
		this.cc = cc;
	}
}