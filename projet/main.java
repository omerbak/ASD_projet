import java.awt.Color;
import java.io.PrintWriter;
import java.util.*;

public class main {

	public static void main(String[] args) {
		
		try {
            if (args.length!=1) throw new IllegalArgumentException("Preciser le nom (et chemin) d'un unique fichier png en paramatre");

            ImagePNG png = new ImagePNG(args[0]);
            
            Quadtree tree = new Quadtree(png);
            
            ImagePNG copy = new ImagePNG(png);
            
            //clearing the copy
            for (int x=0 ; x<copy.width(); x++)
            {
                for (int y=0 ; y<copy.height() ; y++)
                {
                	Color white = new Color(255,255,255);
                	copy.setPixel(x, y, white);

                }
            }
            
            
            //******************testing elagage *********************
            
            /*
            System.out.println(tree.toString());
            
            System.out.println("compress");
            tree.elagage();
            System.out.println(tree.toString());
            
            tree.toImage(copy,"elagage.png");
            */
            
            
            
            //*****************testing compressDelta *****************
            /*
            System.out.println(tree.toString());
            System.out.println("compress");
            tree.compressDelta(70);
            System.out.println(tree.toString());
            
            tree.toImage(copy,"delta.png");
            
            System.out.println("done");
            */
            
            
            
            
            
            
            //************************testing toImage**********************
            
            /*
            tree.toImage(copy,"toImage.png");
            
            System.out.println("done");
            */
            
            
            
            //*********************testing toString***********************
            /*
            System.out.println(tree.toString());
            
            try (PrintWriter out = new PrintWriter("toString.txt")) {
                out.println(tree.toString());
            }
            */
            
            
            
            
            //******************testing compressPhi*********************
            
            
            System.out.println(tree.toString());
            System.out.println("compressing using compressPhi");
            
            tree.compress_phi(2096);
            
            
            System.out.println(tree.toString());
            tree.toImage(copy,"phi.png");
            try (PrintWriter out = new PrintWriter("toString.txt")) {
                out.println(tree.toString());
            }
            
            
            
          
            
            
            
            
            

            
           
            
        
		}catch (Exception e) {
            e.printStackTrace();
        }

	}

}
