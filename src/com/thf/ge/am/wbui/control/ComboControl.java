package com.thf.ge.am.wbui.control;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboControl {

	@Inject
	MApplication app;
	@Inject 
	EPartService partService;
	@Inject
	EModelService modelService;
	
	@PostConstruct
	public void createGui(final Composite parent) {
		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.add("Map View", 0);
		combo.select(0);
		
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				MPerspective element = (MPerspective) modelService.find("com.thf.ge.am.wbui.perspective.meltpooldataanalytics", app);
				partService.switchPerspective(element);
//				if(((Combo)e.getSource()).getSelectionIndex()==0){
//					MPerspective element = (MPerspective) modelService.find("com.thf.ge.am.wbui.perspective.meltpoolmonitor", app);
//					partService.switchPerspective(element);
//				}else if(((Combo)e.getSource()).getSelectionIndex()==1){
//						MPerspective element = (MPerspective) modelService.find("com.thf.ge.am.wbui.perspective.meltpooldataanalytics", app);
//						partService.switchPerspective(element);
//				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}
}