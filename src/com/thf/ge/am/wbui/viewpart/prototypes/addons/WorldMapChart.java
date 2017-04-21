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

import static org.eclipse.rap.rwt.RWT.getResourceManager;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.addons.chart.basic.MapChart;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.widgets.Composite;


public class WorldMapChart extends MapChart {

  public WorldMapChart( Composite parent, int style ) {
    super( parent, style, registerGeoData( "resources/world-110m.json" ) );
  }

  private static String registerGeoData( String path ) {
    ResourceManager resourceManager = getResourceManager();
    if( !resourceManager.isRegistered( path ) ) {
      try (InputStream inputStream = getResourceLoader().getResourceAsStream( path )) {
        resourceManager.register( path, inputStream );
      } catch( Exception exception ) {
        throw new RuntimeException( "Failed to register resource " + path, exception );
      }
    }
    return resourceManager.getLocation( path );
  }

  private static ResourceLoader getResourceLoader() {
    final ClassLoader classLoader = WorldMapChart.class.getClassLoader();
    return new ResourceLoader() {
      @Override
      public InputStream getResourceAsStream( String resourceName ) throws IOException {
        return classLoader.getResourceAsStream( resourceName );
      }
    };
  }

}
