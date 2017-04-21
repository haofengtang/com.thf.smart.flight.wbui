/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
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

import org.eclipse.rap.addons.chart.Colors;
import org.eclipse.rap.addons.chart.basic.MapChart;
import org.eclipse.rap.addons.chart.basic.MapDataItem;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class MapSnippet extends AbstractEntryPoint {

  private MapChart mapChart;

  @PostConstruct
  public void createContents( Composite parent ) {
    parent.setLayout( new GridLayout() );
    createMapChart( parent );
  }

  private void createMapChart( Composite parent ) {
    mapChart = new WorldMapChart( parent, SWT.NONE );
    mapChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    //mapChart.setShowGraticule( true );
    mapChart.setScaleFactor( 3 );
    mapChart.setCenter( 23.3219, 42.6977 );
    mapChart.addListener( SWT.Selection, new Listener() {
      @Override
      public void handleEvent( Event event ) {
        System.out.println( "Selected country #" + event.index + "," + event.text );
      }
    } );
//    mapChart.setColors( Colors.CATEGORY_10 );
    mapChart.setItems( createItems() );
  }

  private static MapDataItem[] createItems() {
    return new MapDataItem[] {
      new MapDataItem( "100", "Bulgaria\nSofia", CATEGORY_10[ 0 ] ),
      new MapDataItem( "DEU", "Item 2", CATEGORY_10[ 1 ] ),
      new MapDataItem( "ES", "Item 3", CATEGORY_10[ 2 ] ),
      new MapDataItem( "AUT", "Item 4", CATEGORY_10[ 3 ] ),
      new MapDataItem( "ITA", "Item 5", CATEGORY_10[ 4 ] )
    };
  }

}
