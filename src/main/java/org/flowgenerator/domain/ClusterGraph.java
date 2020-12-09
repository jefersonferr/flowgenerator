package org.flowgenerator.domain;

import org.flowgenerator.gui.FlowGenerator;

/**
 * classe abstrata  que tem  como  objetivo  definir  objetos  que
 * possuem o comportamento de um cluster dentro  da  representacao
 * grafica proposta pelo FlowGenerator para uma determinada classe 
 * @author Jeferson Ferreira
 */
public abstract class ClusterGraph{
	// controla a geracao dos labels dos clusters
    static private int nextCluster;
	private int numberCluster = 1;
	private String labelCluster;
	private String styleCluster;
	private String colorCluster;
	
	/**
	 * construtor que recebe somente o rotulo do cluster e gera uma
	 * instancia com estilo e cor da borda padrao
	 * @param label String - rotulo do subgrafo  
	 */
	public ClusterGraph(String label) {
	   setNumberCluster(this.getNextCluster());
	   setLabelCluster(label);
	   setStyleCluster("none");
	   setColorCluster("gray");
	}
	
	/**
	 * construtor que recebe o rotulo, o estilo e a cor do cluster
	 * e gera uma instancia com estilo e cor da borda informados
	 * @param label String- rotulo do cluster
	 * @param style String - determina o estilo da borda
	 * @param color String- determina a cor da borda  
	 */
	public ClusterGraph(String label, String style, String color){
	   setNumberCluster(this.getNextCluster());
	   setLabelCluster(label);
	   setStyleCluster(style);
	   setColorCluster(color);
	}
	
	/**
	 * retorna o proximo numero de cluster
	 * @return int 
	 */
	private int getNextCluster() {
		return nextCluster++;
	}
	
	/**
	 * retorna a cor da borda do cluster
	 * @return String
	 */
	public String getColorCluster() {
		return colorCluster;
	}
	
	/**
	 * altera a cor da borda do cluster
	 * @param colorCluster - cor da borda
	 */
	private void setColorCluster(String colorCluster) {
		this.colorCluster = colorCluster;
	}
	
	/**
	 * retorna o rotulo do cluster
	 * @return String
	 */
	public String getLabelCluster() {
		return labelCluster;
	};
	
	public String getNameFileCluster() {
		return (((labelCluster.replace(FlowGenerator.mlp.getString("method")+" ","")).replace(FlowGenerator.mlp.getString("constructor")+" ","")).replace("()",""));
	};
	/**
	 * altera o rotulo do cluster
	 * @param labelCluster
	 */
	private void setLabelCluster(String labelCluster) {
		this.labelCluster = labelCluster;
	}
	
	/**
	 * retorna uma string concatenando a palavra cluster ao seu numero
	 * esta string sera utilizada para geracao  do  arquivo  .dot  que
	 * ira servir de base para a representacao grafica do cluster 
	 * @return String - nome do cluster
	 */
	public String getNumberCluster() {
		return "cluster_"+numberCluster;
	}
	
	/**
	 * altera o numero do cluster
	 * @param numberCluster - numero do cluster
	 */
	private void setNumberCluster(int numberCluster) {
		this.numberCluster = numberCluster;
	}
	
	/**
	 * retorna o estilo da borda do cluster
	 * @return String
	 */
	public String getStyleCluster() {
		return styleCluster;
	}
	
	/**
	 * altera o estilo da borta do cluster
	 * @param styleCluster - estilo da borda
	 */
	private void setStyleCluster(String styleCluster) {
		this.styleCluster = styleCluster;
	}
   
}
