import java.awt.Color;
import java.io.PrintWriter;
import java.util.*;
import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException  {
		
		////command line: java -jar imageCompress.jar <before.png> <numberFeuilles> <max degradation>

            ImagePNG png = new ImagePNG(args[0]);
            
            Quadtree tree = new Quadtree(png);
            
            ImagePNG delta = new ImagePNG(png);
            
            ImagePNG phi = new ImagePNG(png);
            
            
            
            /*
            //clearing the copy
            for (int x=0 ; x<copy.width(); x++)
            {
                for (int y=0 ; y<copy.height() ; y++)
                {
                	Color white = new Color(255,255,255);
                	copy.setPixel(x, y, white);

                }
            }
            */
            
            
            
            
            //*****************testing compressDelta *****************
            
            double degradation = Double.parseDouble(args[2]);
            
            System.out.println(tree.toString());
            System.out.println("compress");
            tree.compressDelta(degradation);
            System.out.println(tree.toString());
            
            tree.toImage(delta,"delta.png");
            
            System.out.println("done");
            
            
            
            
            
            
            
            //************************testing toImage**********************
            
            /*
            tree.toImage(delta,"toImage.png");
            
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
            
            int nbFeuilles = Integer.parseInt(args[1]);
            
            System.out.println(tree.toString());
            System.out.println("compressing using compressPhi");
            
            tree.compress_phi(nbFeuilles);
            
            System.out.println(tree.toString());
            tree.toImage(phi,"phi.png");
      

	}

}
