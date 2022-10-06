package br.edu.iffar.catra.util;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@SuppressWarnings("rawtypes")
public class CamCaptureThread extends Thread{

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 6441489157408381878L;

   // private Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    
	private CallBack call;

    //public CamThread(FormCatra call) {
    public CamCaptureThread(CamCreatorThread creator) {
        super();
        this.webcam = creator.getWebCam();
    }
    
	public void registerCamBack(CallBack call) {
    	this.call = call;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void run() {

        do {
            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
//                    System.out.println("nao tem qr ai");
                    continue;
                }
            }

            if (this.call != null && result != null && !result.getText().equals(this.call.getOfBack())) {
            	
            	try {
            		java.awt.Toolkit.getDefaultToolkit().beep();
				} catch (Throwable e) {
					e.printStackTrace();
				}           	
            	
            	try {
            		this.call.callBack(result.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
                
                System.out.println("achei");
            }

        } while (true);
    }
}
