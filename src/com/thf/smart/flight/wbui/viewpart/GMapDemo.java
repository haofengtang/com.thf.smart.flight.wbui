/*******************************************************************************
 * Copyright (c) 2010 EclipseSource
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/

package com.thf.smart.flight.wbui.viewpart;

import static org.eclipse.rap.rwt.RWT.getResourceManager;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.eclipsesource.widgets.gmaps.GMap;
import com.eclipsesource.widgets.gmaps.LatLng;
import com.eclipsesource.widgets.gmaps.MapAdapter;

public class GMapDemo extends AbstractEntryPoint {

	static final private String INIT_CENTER = "33.0,5.0";
	static final private int INIT_ZOOM = 2;
	static final private int INIT_TYPE = GMap.TYPE_HYBRID;
	private GMap gmap = null;

	// @Override
	@PostConstruct
	protected void createContents(Composite parent) {
		SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		createMap(sash);
		Composite controls = new Composite(sash, SWT.BORDER);
		controls.setLayout(new GridLayout(1, true));
//		createCenterControl(parent.getDisplay(), controls);
		createZoomControl(controls);
		createMapTypeControl(controls);
		createAddressControl(parent.getDisplay(), controls);
//		createMarkerControl(parent.getDisplay(), controls);
		sash.setWeights(new int[] { 7, 2 });
	}

	protected void createMap(Composite parent) {
		gmap = new GMap(parent, SWT.NONE);
		gmap.setCenter(stringToLatLng(INIT_CENTER));
		gmap.setZoom(INIT_ZOOM);
		gmap.setType(INIT_TYPE);
	}

	private void createCenterControl(Display display, Composite parent) {
		new Label(parent, SWT.None).setText("Location:");
		final Text location = new Text(parent, SWT.BORDER);
		location.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		location.setText(INIT_CENTER);
		location.setFont(new Font(display, "Arial", 9, SWT.NORMAL));
		location.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				gmap.setCenter(stringToLatLng(location.getText()));
			}
		});
		gmap.addMapListener(new MapAdapter() {
			public void centerChanged() {
				location.setText(gmap.getCenter().toString());
			}
		});
	}

	private void createZoomControl(Composite controls) {
		new Label(controls, SWT.None).setText("Zoom:");
		final Spinner zoom = new Spinner(controls, SWT.NORMAL);
		zoom.setMaximum(20);
		zoom.setMinimum(0);
		zoom.setSelection(INIT_ZOOM);
		zoom.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				gmap.setZoom(Integer.parseInt(zoom.getText()));
			}
		});
		gmap.addMapListener(new MapAdapter() {
			public void zoomChanged() {
				zoom.setSelection(gmap.getZoom());
			};
		});
	}

	private void createMapTypeControl(Composite parent) {
		new Label(parent, SWT.None).setText("Type:");
		final Combo type = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		type.setItems(new String[] { "ROADMAP", "SATELLITE", "HYBRID", "TERRAIN" });
		type.setText("HYBRID");
		type.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = type.getSelectionIndex();
				if (index != -1) {
					gmap.setType(index);
				}
			}
		});
	}

	private void createAddressControl(Display display, Composite parent) {
		new Label(parent, SWT.None).setText("Address:");
		final Text addr = new Text(parent, SWT.BORDER);
		addr.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		addr.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				gmap.gotoAddress(addr.getText());
			}
		});
		addr.setFont(new Font(display, "Arial", 9, SWT.NORMAL));
		Button goToAddr = new Button(parent, SWT.PUSH);
		goToAddr.setText("go to");
		goToAddr.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gmap.gotoAddress(addr.getText());
			}
		});
		Button resolveAddr = new Button(parent, SWT.PUSH);
		resolveAddr.setText("resolve");
		resolveAddr.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gmap.resolveAddress();
			}
		});
		gmap.addMapListener(new MapAdapter() {
			public void addressResolved() {
				addr.setText(gmap.getAddress());
			}
		});
	}

	private void createMarkerControl(Display display, Composite controls) {
		final InputDialog markerDialog = new InputDialog(controls.getShell(), "Marker Name", "Enter Name", null, null);
		Button addMarker = new Button(controls, SWT.PUSH);
		addMarker.setText("add Marker");
		addMarker.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				markerDialog.open();
				String result = markerDialog.getValue();
				if (result != null && result.length() > 0) {
					gmap.addMarker(result);
				}
			}
		});
	}

	private LatLng stringToLatLng(final String input) {
		LatLng result = null;
		if (input != null) {
			String temp[] = input.split(",");
			if (temp.length == 2) {
				try {
					double lat = Double.parseDouble(temp[0]);
					double lon = Double.parseDouble(temp[1]);
					result = new LatLng(lat, lon);
				} catch (NumberFormatException ex) {
				}
			}
		}
		return result;
	}

	public void stop() {
	}
}
