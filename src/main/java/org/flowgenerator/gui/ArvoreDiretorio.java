/**
 * ArvoreDiretorio
 * Responsavel pela criacao do painel e da arvore de arquivos do diretorio corrente
 * 
 * @author Jeferson Ferreira
 * @since  26/11/2005
 */
package org.flowgenerator.gui;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.flowgenerator.controller.FileData;

public class ArvoreDiretorio extends JPanel {
	private static final long serialVersionUID = 1L;
	private   String currDir;
    protected JTree  tree;
    protected DefaultMutableTreeNode rootNode, nodeAux;
    protected DefaultTreeModel treeModel;

    public ArvoreDiretorio(String title) {
        super(new GridLayout(1,0));
        rootNode = new DefaultMutableTreeNode(FlowGenerator.mlp.getString("mycomputer"));
    	File discos[] = File.listRoots();
    	for (int i=0; i<discos.length; i++) {
    	   currDir = setDirectory(discos[i].toString());
    	   if (discos[i].exists()) {
    	      FileData fd = new FileData( currDir, currDir, FlowGenerator.mlp.getString("disk"));
    		  nodeAux = new DefaultMutableTreeNode(fd);
        	  createTreeNodes(nodeAux, currDir);
    		  rootNode.add(nodeAux );
    	   }
    	}
        tree = new JTree( rootNode );
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        ToolTipManager.sharedInstance().registerComponent(tree);
        tree.addMouseListener( new whenMouseClicked()   );
        tree.setCellRenderer(  new ArvoreCellRenderer() );
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    private String setDirectory(String dir) {
        dir = dir.replace('\\','/');
        if ( dir.charAt( dir.length()-1 ) != '/' )
        	dir += "/";
        return dir;
    }

    private void createTreeNodes(DefaultMutableTreeNode Node, String title) {
        DefaultMutableTreeNode node = null;
        List<FileData> data = showDirectory(title);
        Collections.sort(data);
        for (int i = 0; i < data.size(); i++ ) {
            FileData fd = (FileData) (data.get(i) );
            if ((fd.getFileType()==FlowGenerator.mlp.getString("file")&& fd.getFileName().contains(".java"))||fd.getFileType()==FlowGenerator.mlp.getString("directory")) {;
               node = new  DefaultMutableTreeNode( fd );
               Node.add(node);
            }
        }
    }
    
    private List<FileData> showDirectory(String path) {
        List<FileData> vetor = new ArrayList<FileData>();
        File   file  = new File( path );
        File[] dir = file.listFiles();
        int lenDir = dir.length;
        for ( int i = 0; i < lenDir; i++ ) {
            if ( dir[i].isDirectory() || dir[i].isFile() ) {
                FileData fd = new  FileData( dir[i]);
                vetor.add(fd);
            }
        }
        return vetor;
    }
    
    private void updateArvore( TreePath selPath  ) {
        DefaultMutableTreeNode currentNode = null;
        String path = null;
        if (selPath == null) {
            currentNode = rootNode;
        } else {
            currentNode = (DefaultMutableTreeNode)
                          (selPath.getLastPathComponent());
        }

        Object obj = currentNode.getUserObject();
        if ( (obj) instanceof FileData) {
             FileData  fd = (FileData)(obj);
             if (fd.getFileType().charAt(0)==FlowGenerator.mlp.getString("file").charAt(0)) {
                 return;
             }
             if (fd.getFileType().charAt(0)==FlowGenerator.mlp.getString("disk").charAt(0)) {
                 return;
             }
             path = fd.getFileFullPath();
             List<FileData>  data = showDirectory(path);
             if (data.size() == 0) {
                 return;
             }
     //        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
             currentNode.removeAllChildren();
             createTreeNodes(currentNode, path );
     //        model.reload();
             tree.expandPath(selPath);
        } else {
               path = setDirectory(obj.toString());
        }
       tree.expandPath(selPath);
    }
    
	/**
	 * Classe responsavel pelo tratamento dos eventos do mouse
	 * @author Antonio Azevedo
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    private class whenMouseClicked extends MouseAdapter {
         public void mousePressed(MouseEvent e) {
            int selRow = tree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if( selRow != -1 ) {
                if(e.getClickCount() == 1) {
                    onMouseClick(selRow, selPath);
                } else if (e.getClickCount() == 2) {
                    onMouseDoubleClick(selRow, selPath);
                }
            }
         }
    }
    
	/**
	 * Classe responsavel pelo tratamento do click simples
	 * @author Antonio Azevedo
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    private void onMouseClick(int row, TreePath selPath ) {
    	updateArvore( selPath );
    }
	/**
	 * Classe responsavel pelo tratamento do duplo click
	 * @author Antonio Azevedo / Jeferson Ferreira
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    private void onMouseDoubleClick(int row, TreePath selPath ) {
    	DefaultMutableTreeNode parentNode = null;
        if (selPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode) (selPath.getLastPathComponent());
        }
        Object obj = parentNode.getUserObject();
        if ( (obj) instanceof FileData) {
             FileData  fd = (FileData)(obj);
             if (fd.getFileType().charAt(0)==FlowGenerator.mlp.getString("file").charAt(0)) {
				FlowGenerator.addTabedPane(new File(fd.getFileFullPath()));
			} 
        }
    }
}
