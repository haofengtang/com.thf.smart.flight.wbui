package com.thf.ge.am.wbui.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ImageUtils {

	static private ImageUtils _self;
	
	private RGB[] rgbs;

	public ImageUtils() {
		rgbs = new RGB[4];
		rgbs[0] = new RGB(0xff, 0x00, 0x00);
//		rgbs[1] = new RGB(0xd3, 0xff, 0xe3);
		rgbs[1] = new RGB(0x80, 0x80, 0xff);
		rgbs[2] = new RGB(0xf0, 0xf0, 0xf0);
		rgbs[3] = new RGB(0xB0, 0xB0, 0xB0);
	}

	static public ImageUtils getInstance(){
		if(_self==null) _self=new ImageUtils();
		return _self;
	}
	
    public Image generateBarImgae(String statusLastIdxString){
    	int barWidth = 200;
    	int barHeight = 20;
    	int totalLayer = 1400;
    	String[] statusLastIdxStrs = statusLastIdxString.split("_");
    	if(statusLastIdxStrs.length>=3)
    		totalLayer = Integer.parseInt(statusLastIdxStrs[2]);
    	int lastLayerPixel = Integer.parseInt(statusLastIdxStrs[1])*barWidth/totalLayer;

    	PaletteData pltData = new PaletteData(rgbs);
    	ImageData imgData = new ImageData(barWidth, barHeight, 8, pltData);
    	for(int i=0;i<barWidth;i++){
    		for(int j=0;j<barHeight;j++){
        		if(i<lastLayerPixel){
        	    	imgData.setPixel(i, j, 1);
        		}else{
        			if(j==0||j==(barHeight-1)||i==(barWidth-1))
        				imgData.setPixel(i, j, 3);
        			else
        				imgData.setPixel(i, j, 2);
        		}
    		}
    	}
    	String[] anomalyLayers = statusLastIdxStrs[0].split(",");
    	for(String al:anomalyLayers){
    		if(al!=null&&!("".equals(al.trim()))){
        		int alInt = Integer.parseInt(al) * barWidth / totalLayer;
        		for(int j=0;j<barHeight;j++){
        	    	imgData.setPixel(alInt, j, 0);
        		}
    		}
    	}
    	return new Image(Display.getDefault(), imgData);
    }
    
//    public Image getImage(String file) {
//        // assume that the current class is called View.java
//            Bundle bundle = FrameworkUtil.getBundle(View.class);
//            URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
//            ImageDescriptor image = ImageDescriptor.createFromURL(url);
//            return image.createImage();
//
//    }

    public Image overlayImage(Image targetImg, int alertLevel){
//    	RGB red = new RGB(255, 0, 0);
		ImageData imgData = targetImg.getImageData();
		int radias = (int)(((double)Math.min(imgData.width, imgData.height)*0.8)/2.0);
		int centerX = imgData.width/2;
		int centerY = imgData.height/2;
		switch (alertLevel){
		case 1:
			break;
		case 2:
			for(int i=0;i<imgData.width;i++){
				for(int j=0;j<imgData.height;j++){
					if((i-centerX)*(i-centerX)+(j-centerY)*(j-centerY)<=radias*radias)
						imgData.setPixel(i, j, 0x00ffff);
				}
			}
		case 3:
			for(int i=0;i<imgData.width;i++){
				for(int j=0;j<imgData.height;j++){
					if((i-centerX)*(i-centerX)+(j-centerY)*(j-centerY)<=radias*radias)
						imgData.setPixel(i, j, 0xff0000);
				}
			}
			break;
		default:
			;
		}
		return new Image(Display.getDefault(), imgData);
    }

    public void sound(){
		AudioInputStream audioInputStream;
		try {
			AudioListener listener = new AudioListener();
			audioInputStream = AudioSystem.getAudioInputStream(new File("/sounds/buzz.wav"));
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			try {
				clip.start();
				listener.waitUntilDone();
			} finally {
				clip.close();
			}
			audioInputStream.close();
		} catch (Exception ex) {
		} finally {
		}
    }


	class AudioListener implements LineListener {
		private boolean done = false;
		@Override
		public synchronized void update(LineEvent event) {
			Type eventType = event.getType();
			if (eventType == Type.STOP || eventType == Type.CLOSE) {
				done = true;
				notifyAll();
			}
		}
		public synchronized void waitUntilDone() throws InterruptedException {
			while (!done) {
				wait();
			}
		}
	}

	public BufferedImage convertToAWT(ImageData data) {
		ColorModel colorModel = null;
		PaletteData palette = data.palette;
		if (palette.isDirect) {
			colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
			BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					RGB rgb = palette.getRGB(pixel);
					bufferedImage.setRGB(x, y,  rgb.red << 16 | rgb.green << 8 | rgb.blue);
				}
			}
			return bufferedImage;
		} else {
			RGB[] rgbs = palette.getRGBs();
			byte[] red = new byte[rgbs.length];
			byte[] green = new byte[rgbs.length];
			byte[] blue = new byte[rgbs.length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				red[i] = (byte)rgb.red;
				green[i] = (byte)rgb.green;
				blue[i] = (byte)rgb.blue;
			}
			if (data.transparentPixel != -1) {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
			} else {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
			}
			BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					pixelArray[0] = pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}

	public ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int rgb = bufferedImage.getRGB(x, y);
					int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
					data.setPixel(x, y, pixel);
					if (colorModel.hasAlpha()) {
						data.setAlpha(x, y, (rgb >> 24) & 0xFF);
					}
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
}
