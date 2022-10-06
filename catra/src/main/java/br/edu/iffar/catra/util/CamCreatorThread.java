package br.edu.iffar.catra.util;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class CamCreatorThread extends Thread{

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 6441489157408381878L;

   // private Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel panel = null;

    //public CamThread(FormCatra call) {
    public CamCreatorThread() {
        super();

        	
    	
//        this.call=call;
//        Dimension size = WebcamResolution.VGA.getSize();
        
//        this.call.setWebCamPanel(panel);
    }
    
    public Webcam getWebCam() {
    	return webcam;
    }
    
    public WebcamPanel getPanel() {
    	return this.panel;
    }

    @Override
    public void run() {

	    if(Webcam.getWebcams().isEmpty()) {
	    	System.out.println("Web Cam não foi encontrada");
	    	return;
	    }
	    webcam = Webcam.getWebcams().get(0);
	    webcam.setViewSize(WebcamResolution.QVGA.getSize());
	
	    panel = new WebcamPanel(webcam);
	    panel.setPreferredSize(WebcamResolution.QVGA.getSize());
	    panel.setFPSDisplayed(true);
    }
}
