/**
 * ArvoreCellRenderer 
 * Acidona filhos ao no selecionado
 * 
 * @author Jeferson Ferreira
 * @since  30/10/2005
 */

package org.flowgenerator.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.flowgenerator.controller.FileData;

public class ArvoreCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	private   Icon homeIcon   =  new MyIcon("home.gif").getIcone();
	private   Icon diskIcon   =  new MyIcon("disk.jpeg").getIcone();
	private   Icon openIcon   =  new MyIcon("open.gif").getIcone();
	private   Icon dirIcon    =  new MyIcon("dir.gif").getIcone();
	private   Icon fileIcon   =  new MyIcon("file.gif").getIcone();
	
	public ArvoreCellRenderer() {
		super();
	}
	public Component getTreeCellRendererComponent
		( JTree tree, 
          Object value,
		  boolean selection, 
		  boolean expanded,
		  boolean leaf, 
		  int row, 
		  boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selection, expanded, leaf, row, hasFocus);	
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;	
		setIconAndToolTip(node.getUserObject(), tree, expanded);
		return this;
	}
	
	/**
	 * setIconAndToolTip
	 * checa o tipo do objeto e altera para o icone e tooltip apropriados
	 * @return void
	 * @exception
	 */
	private void setIconAndToolTip(Object obj, JTree tree, boolean expanded) {
		if (obj instanceof String) {
			String str = (String)(obj);
			setIcon(homeIcon);
			setToolTipText( str );
		} else if (obj instanceof FileData) {
			FileData  fd = (FileData)(obj);
			if ( fd.getFileType().charAt(0)==FlowGenerator.mlp.getString("file").charAt(0) ) {
				setIcon(fileIcon);
			} else if ( fd.getFileType().charAt(0)==FlowGenerator.mlp.getString("disk").charAt(0) ) {			
				setIcon(diskIcon);
		    } else {
				if ( expanded  )
					setIcon(openIcon);
				else
					setIcon(dirIcon);
			}
			setText( fd.getFileName() );
			setToolTipText(fd.getInfoString());
		}
	}
}
