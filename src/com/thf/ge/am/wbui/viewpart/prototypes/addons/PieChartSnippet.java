/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package com.thf.ge.am.wbui.viewpart.prototypes.addons;


import static org.eclipse.rap.addons.chart.Colors.CATEGORY_10;

import javax.annotation.PostConstruct;

import org.eclipse.rap.addons.chart.basic.DataItem;
import org.eclipse.rap.addons.chart.basic.PieChart;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class PieChartSnippet extends AbstractEntryPoint {

  private PieChart pieChart;

  @PostConstruct
  public void createContents( Composite parent ) {
    parent.setLayout( new GridLayout() );
    createPieChart( parent );
    createUpdateButton( parent );
    update();
  }

  private void createPieChart( Composite parent ) {
    pieChart = new PieChart( parent, SWT.NONE );
    pieChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    pieChart.addListener( SWT.Selection, new Listener() {
      @Override
      public void handleEvent( Event event ) {
        System.out.println( "Selected pie item #" + event.index );
      }
    } );
  }

  private void createUpdateButton( Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Change data" );
    button.addListener( SWT.Selection, new Listener() {
      @Override
      public void handleEvent( Event event ) {
        update();
      }
    } );
  }

  private void update() {
    pieChart.setItems( createItems() );
  }

  private static DataItem[] createItems() {
    return new DataItem[] {
      new DataItem( Math.random() * 100, "Item 1", CATEGORY_10[ 0 ] ),
      new DataItem( Math.random() * 100, "Item 2", CATEGORY_10[ 1 ] ),
      new DataItem( Math.random() * 100, "Item 3", CATEGORY_10[ 2 ] ),
      new DataItem( Math.random() * 100, "Item 4", CATEGORY_10[ 3 ] ),
      new DataItem( Math.random() * 100, "Item 5", CATEGORY_10[ 4 ] )
    };
  }

}
