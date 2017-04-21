package com.thf.ge.am.wbui.viewpart.datap;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class BuildStatusTableDataProvider implements IStructuredContentProvider {

	private static final long serialVersionUID = 8463431506157928295L;

	public BuildStatusTableDataProvider() {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List){
			Object[] rows = new Object[((List)inputElement).size()];
			return ((List)inputElement).toArray(rows);
		}
		return null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
