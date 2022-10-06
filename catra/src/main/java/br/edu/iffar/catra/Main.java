package br.edu.iffar.catra;

import java.lang.reflect.InvocationTargetException;

import br.edu.iffar.catra.control.PrincipalControl;
import br.edu.iffar.catra.dao.Factory;
import br.edu.iffar.catra.forms.FormCatra;
import br.edu.iffar.catra.forms.Splat;
import br.edu.iffar.catra.util.CamCreatorThread;
import br.edu.iffar.catra.util.Log;

/**
 *
 * @author darlan
 */
public class Main extends Thread {

    public static void main(String args[]) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Log.init();
//    	
		Splat splat = Splat.getinstance();
        new Thread(splat).start();
        splat.setProgress(10);

        CamCreatorThread creator = new CamCreatorThread();
        creator.start();

        Thread banco = new Thread(new Factory());
        banco.start();
        splat.setProgress(20);

        try {
            banco.join();
            splat.setProgress(60);
            splat.buscaRefeicoes();

            creator.join();
            splat.setProgress(80);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        
        FormCatra f = FormCatra.getInstance();

        splat.setProgress(90);
        
        f.initCam(creator);
        
        f.init();
        
        PrincipalControl.getPrincipalControl().setFormCatra(f);

        splat.setProgress(100);
		splat.btnVisible();

    }
}
