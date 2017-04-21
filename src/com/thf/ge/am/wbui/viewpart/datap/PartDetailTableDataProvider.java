package com.thf.ge.am.wbui.viewpart.datap;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.thf.ge.am.wbui.dm.DefPart;
import com.thf.ge.am.wbui.dm.DefPartLayer;

public class PartDetailTableDataProvider implements ITreeContentProvider {

	public PartDetailTableDataProvider() {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Hashtable){
			SortedSet<DefPart> partsSorted = new TreeSet<DefPart>(new Comparator<DefPart>(){
				public int compare(DefPart o1, DefPart o2) {
					return o1.getPartIndex() - o2.getPartIndex();
				}
			});
			partsSorted.addAll(((Hashtable)inputElement).values());
			return partsSorted.toArray();
		}else if(inputElement instanceof List){
			return ((List)inputElement).toArray();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof DefPart){
			SortedSet<DefPartLayer> partsSorted = new TreeSet<DefPartLayer>(new Comparator<DefPartLayer>(){
				public int compare(DefPartLayer o1, DefPartLayer o2) {
					return o2.getLayerIndex() - o1.getLayerIndex();
				}
			});
			partsSorted.addAll(((DefPart)parentElement).getLayers());
			return (partsSorted.toArray());
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof List){
			List<DefPart> parts = (List<DefPart>)element;
			return parts.size()>0;
		}else if(element instanceof DefPart){
			List<DefPartLayer> players = ((DefPart)element).getLayers();
			return players!=null&&players.size()>0;
		}
		return false;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
