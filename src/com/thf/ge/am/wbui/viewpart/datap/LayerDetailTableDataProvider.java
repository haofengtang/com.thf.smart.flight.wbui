package com.thf.ge.am.wbui.viewpart.datap;

import java.util.Hashtable;
import java.util.List;
import java.util.SortedSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.thf.ge.am.wbui.dm.DefLayer;
import com.thf.ge.am.wbui.dm.DefPartLayer;

public class LayerDetailTableDataProvider implements ITreeContentProvider {

	public LayerDetailTableDataProvider() {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Hashtable){
			return ((Hashtable)inputElement).values().toArray();
		}else if(inputElement instanceof List){
			return ((List)inputElement).toArray();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof List){
			List<DefLayer> layers = (List<DefLayer>)parentElement;
			return (layers.toArray());
		}else if(parentElement instanceof DefLayer){
			return ((DefLayer)parentElement).getParts().toArray();
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
			List<DefLayer> layers = (List<DefLayer>)element;
			return layers.size()>0;
		}else if(element instanceof DefLayer){
			SortedSet<DefPartLayer> parts = ((DefLayer)element).getParts();
			return parts!=null&&parts.size()>0;
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
