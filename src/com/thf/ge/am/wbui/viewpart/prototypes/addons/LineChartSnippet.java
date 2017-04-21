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

import org.eclipse.rap.addons.chart.basic.DataGroup;
import org.eclipse.rap.addons.chart.basic.DataItem;
import org.eclipse.rap.addons.chart.basic.LineChart;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class LineChartSnippet extends AbstractEntryPoint {

  private LineChart lineChart;

  @PostConstruct
  public void createContents( Composite parent ) {
    parent.setLayout( new GridLayout() );
    createLineChart( parent );
    createUpdateButton( parent );
    update();
  }

  private void createLineChart( Composite parent ) {
    lineChart = new LineChart( parent, SWT.NONE );
    lineChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    lineChart.setXAxisLabel( "Time" );
    lineChart.setYAxisLabel( "Radiation" );
    lineChart.setYAxisFormat( "d" );
    lineChart.addListener( SWT.Selection, new Listener() {
      @Override
      public void handleEvent( Event event ) {
        System.out.println( "Selected line item #" + event.index + ", point #" + event.detail );
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
    lineChart.setItems( createItems() );
  }

  private static DataGroup[] createItems() {
    return new DataGroup[] {
      new DataGroup( createRandomPoints(), "Series 1", CATEGORY_10[ 0 ] ),
      new DataGroup( createRandomPoints(), "Series 2", CATEGORY_10[ 1 ] )
    };
  }

  private static DataItem[] createRandomPoints() {
    DataItem[] values = new DataItem[100];
    for( int i = 0; i < values.length; i++ ) {
      values[i] = new DataItem( Math.random() * 100 );
    }
    return values;
  }

}
