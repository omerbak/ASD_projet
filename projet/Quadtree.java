import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

public class Quadtree {
	
	private int x; //starting x value
	private int y; //starting y value
	private int size; // size of image
	private ArrayList<Quadtree> feuilles; // the list of feuilles
	private Color color;
	
	
	/*
	public Quadtree(Node r) {
		this.racine = r;
		
	}
	*/
	
	public Quadtree(int start_x, int start_y , int s) {
		this.x = start_x;
		this.y = start_y;
		this.size = s;
		
		this.feuilles = new ArrayList<Quadtree>(4);
		for(int i=0 ; i<4 ; i++) {
			this.feuilles.add(null);
		}
		
	}
	
	public Quadtree(ImagePNG image) {
		//at the start x = y = 0
		this.x = 0;
		this.y = 0;
		//all images treated in this project are squares so we take the height 
		this.size = image.height();
		this.feuilles = new ArrayList<Quadtree>(4);
		for(int i=0 ; i<4 ; i++) {
			this.feuilles.add(null);
		}
		
		splitImage(this,image);
		
	}
	
	private static void splitImage(Quadtree tree,ImagePNG image ) {
		
		if(tree.size >= 2) {
			//split
			int feuille_x;
			int feuille_y;
			int feuille_size = tree.size/2;
			
			//creating V1
			feuille_x = tree.x;
			feuille_y = tree.y;
			Quadtree V1 = new Quadtree(feuille_x, feuille_y, feuille_size);
			tree.feuilles.set(0, V1);
			
			//creating V2
			feuille_x = tree.x + feuille_size;
			feuille_y = tree.y;
			Quadtree V2 = new Quadtree(feuille_x, feuille_y, feuille_size);
			tree.feuilles.set(1, V2);
			
			//creating V3
			feuille_x = tree.x + feuille_size;
			feuille_y = tree.y + feuille_size;
			Quadtree V3 = new Quadtree(feuille_x, feuille_y, feuille_size);
			tree.feuilles.set(2, V3);
			
			//creating V4
			feuille_x = tree.x;
			feuille_y = tree.y + feuille_size;
			Quadtree V4 = new Quadtree(feuille_x, feuille_y, feuille_size);
			tree.feuilles.set(3, V4);
			
			//splitting the subtrees
			splitImage(V1,image);
			splitImage(V2,image);
			splitImage(V3,image);
			splitImage(V4,image);
			
			
			
		}else {
			//set color
			tree.color = image.getPixel(tree.x,tree.y);
			
		}
		
	}
	
	public void elagage() {
		
		if(this.feuilles.get(0).feuilles.get(0) != null) {
			for(int i =0; i<4; i++) {
				this.feuilles.get(i).elagage();
			}
		}else {
			boolean sameColor = true;
			Color tmpColor = this.feuilles.get(0).color;
			for(int i=1; i<4 ; i++) {
				if(this.feuilles.get(i).color.equals(tmpColor)) {
					
				}else {
					sameColor = false;
				}
				
			}
			if(sameColor) {
				this.color = tmpColor;
				for(int i=0; i<4; i++) {
					this.feuilles.set(i, null);
				}
			}
		}
	}
	
	/*
	 * @role: returns the medium of the color red of the 4 leafs of the quadtree
	 * @input: quadtree with 4 leafs each leaf has a colour
	 */
	private double mediumRed() {
		double sommeRed=0;
		for(int i=0; i<4; i++) {
			sommeRed+=this.feuilles.get(i).color.getRed();
		}
		
		return sommeRed/4;
	}
	
	/*
	 * @role: returns the medium of the color blue of the 4 leafs of the quadtree
	 * @input: quadtree with 4 leafs each leaf has a colour
	 */
	private double mediumBlue() {
		double sommeBlue=0;
		for(int i=0; i<4; i++) {
			sommeBlue+=this.feuilles.get(i).color.getBlue();
		}
		
		return sommeBlue/4;
	}
	
	/*
	 * @role: returns the medium of the color green of the 4 leafs of the quadtree
	 * @input: quadtree with 4 leafs each leaf has a colour
	 */
	private double mediumgGreen() {
		double sommeGreen=0;
		for(int i=0; i<4; i++) {
			sommeGreen+=this.feuilles.get(i).color.getGreen();
		}
		
		return sommeGreen/4;
	}
	
	/**
	 * 
	 * @param redM
	 * @param blueM
	 * @param greenM
	 * @return the colour difference of the leaf that contains colors
	 */
	private double colorDifference(double redM,double blueM, double greenM) {
		double red = this.color.getRed();
		double blue = this.color.getBlue();
		double green = this.color.getGreen();
		
		//(Ri - Rm)2
		double diffRed = (red-redM)*(red-redM);
		
		//(Vi - Vm)2
		double diffGreen = (green-greenM)*(green-greenM);
		
		//(Bi - Bm)2
		double diffBlue = (blue - blueM)*(blue - blueM);
		
		return Math.sqrt((diffRed+diffGreen+diffBlue)/3);
	}
	
	/*
	 * @role: returns the max color difference of the four leafs of the quadtree
	 */
	private double max_colorDifference() {
		
		double redM = this.mediumRed();
		double greenM = this.mediumgGreen();
		double blueM = this.mediumBlue();
		
		double V1_colorDifference = this.feuilles.get(0).colorDifference(redM, blueM, greenM);
		double V2_colorDifference = this.feuilles.get(1).colorDifference(redM, blueM, greenM);
		double V3_colorDifference = this.feuilles.get(2).colorDifference(redM, blueM, greenM);
		double V4_colorDifference = this.feuilles.get(3).colorDifference(redM, blueM, greenM);
		
		return Math.max(Math.max(V1_colorDifference, V2_colorDifference), Math.max(V3_colorDifference, V4_colorDifference));
	}
	
	public void compressDelta(double maxDegradation) {
		
		if(this.color == null) {
			
			if(this.feuilles.get(0).color != null && this.feuilles.get(1).color != null && this.feuilles.get(2).color != null && this.feuilles.get(3).color != null) {
				
				double maxColorDiff = this.max_colorDifference();
				
				
				if(maxColorDiff <= maxDegradation) {
					double redM = this.mediumRed();
					double greenM = this.mediumgGreen();
					double blueM = this.mediumBlue();
					Color moyen = new Color((int)redM,(int)greenM,(int)blueM);
					this.color = moyen;
					
					for(int i=0; i<4; i++) {
						this.feuilles.set(i, null);
					}
				
				}
				
			}else {
				for(int i =0; i<4; i++) {
					this.feuilles.get(i).compressDelta(maxDegradation);
				}
				
				if(this.feuilles.get(0).color != null && this.feuilles.get(1).color != null && this.feuilles.get(2).color != null && this.feuilles.get(3).color != null) {
					this.compressDelta(maxDegradation);
				}
				
			}
				
					
		}
		
		
	}
	
	private boolean isLeaf() {
		
		return (this.feuilles.get(0)==null) && (this.feuilles.get(1)==null) && (this.feuilles.get(2)==null) && (this.feuilles.get(3)==null);
	}
	
	public void setPixels(ImagePNG image) {
		
		if(this.isLeaf()) {
			for(int i = x ; i < size+x ; i++) {
                for(int j = y ; j < size+y ;j++) {
                	image.setPixel(i, j, this.color);
                }
			}
			
		}else {
			if(this.feuilles.get(0) !=null) {
				this.feuilles.get(0).setPixels(image);
			}
			if(this.feuilles.get(1) !=null) {
				this.feuilles.get(1).setPixels(image);
			}
			if(this.feuilles.get(2) !=null) {
				this.feuilles.get(2).setPixels(image);
			}
			if(this.feuilles.get(3) !=null) {
				this.feuilles.get(3).setPixels(image);
			}
		}
		
		
		
		
		
		
	}
	
	public void toImage(ImagePNG image, String name) {
		
		this.setPixels(image);
		
		try {
			image.save(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void printQuad() {
		if(this.feuilles.get(1) !=null) {
			this.feuilles.get(0).printQuad(); 
			this.feuilles.get(1).printQuad();
			this.feuilles.get(2).printQuad(); 
			this.feuilles.get(3).printQuad(); 
			
		}else {
			System.out.println("color " + this.color.toString());
		}
	}
	
	public static void printList(ArrayList<Double> list) {
		
		for(int i=0; i<list.size();i++) {
			
			System.out.println(list.get(i));
		}
		
		
	}
	
    // Function (static) that converts a color to an hexadecimal code
    public static String colorToHex(Color col) {
        return Integer.toHexString(col.getRGB()).substring(2);
    }
    
	
	public String toString() {
		if(this.isLeaf()) {
			return colorToHex(this.color);
		}else {
			return "( " +  ((this.feuilles.get(0) == null) ? "" : this.feuilles.get(0).toString() +  " ") + ((this.feuilles.get(1) == null) ? "" : this.feuilles.get(1).toString() +  " ")
					 + ((this.feuilles.get(2) == null) ? "" : this.feuilles.get(2).toString() +  " ") + ((this.feuilles.get(3) == null) ? "" : this.feuilles.get(3).toString() +  " " ) + " )";
			
		}
				
		
		/*
		if(this.feuilles.get(0) == null) { 
			return colorToHex(this.color);

		}else {
			return "( " + this.feuilles.get(0).toString() + " " + this.feuilles.get(1).toString() 
					+ " " + this.feuilles.get(2).toString() + " " + this.feuilles.get(3).toString() + " )";
		}
		*/
		
	}
	
	/**
	 * role: returns the sum of all color differences of all leafs of a quad tree,
	 * 			the leafs could contains colors or could be quad trees
	 * @return : the sum of all color differences
	 */
	public double max_colorDifference_rec() {
		if(this.color == null) {
			if(this.feuilles.get(0).color != null) {
				return this.max_colorDifference();
			}else {
				return Math.max(Math.max(this.feuilles.get(0).max_colorDifference_rec(), this.feuilles.get(1).max_colorDifference_rec()),
						Math.max(this.feuilles.get(2).max_colorDifference_rec(), this.feuilles.get(3).max_colorDifference_rec()));
			}
		}else {
			return 0;
		}
		
	}
	
	
	public void compressPhi(int nbFeuilles) {
		
		if( nbFeuilles == 0) {
			
			for(int i=0 ; i<4 ; i++) {
				this.feuilles.set(i, null);
			}
		}else {
			
			
			
			//calculating the max colour difference of each feuillles
			double maxV1 = this.feuilles.get(0).max_colorDifference_rec();
			double maxV2 = this.feuilles.get(1).max_colorDifference_rec();
			double maxV3 = this.feuilles.get(2).max_colorDifference_rec();
			double maxV4 = this.feuilles.get(3).max_colorDifference_rec();
			
			//creating a list of the max colour difference of each feuillles
			ArrayList<Double> maxColors = new ArrayList<Double>(4);
			maxColors.add(maxV1);
			maxColors.add(maxV2);
			maxColors.add(maxV3);
			maxColors.add(maxV4);
			
			//crating a list of index that link each maxcolor with the correct index
			ArrayList<Integer> index = new ArrayList<Integer>(4);
			index.add(0);
			index.add(1);
			index.add(2);
			index.add(3);
			
			
			double tmpMaxColor;
			int tmpind;
			for(int i=0 ; i<3 ; i++) {
				
				for(int j=i+1; j<4;j++) {
					
					if(maxColors.get(j) < maxColors.get(i)) {
						tmpMaxColor =  maxColors.get(i);
						maxColors.set(i,maxColors.get(j));
						maxColors.set(j,tmpMaxColor);
						
						//making the same changes in the list index
						tmpind =  index.get(i);
						index.set(i,index.get(j));
						index.set(j,tmpind);
						
					}
				}
			}
			
			
			
			if(nbFeuilles <=4) {
				
				
				for(int i=0; i<nbFeuilles ; i++) {
					this.feuilles.get(index.get(i)).replaceParMoyen();
				}
				
				
				
				for(int j=nbFeuilles;j<4;j++) {
					this.feuilles.set(index.get(j), null);
				}
				
			}else {
				
				 
				int nbNoeud = nbFeuilles/4;
				
				if(nbNoeud > 4) {
					nbNoeud =4;
				}
				
				
				// if the result of the division 1.5 we lose one feuille
				int feuillePerNoeud = (nbFeuilles - (4-nbNoeud))/nbNoeud;
				
				for(int i=0; i<4-nbNoeud ; i++) {
					this.feuilles.get(index.get(i)).replaceParMoyen();
				}
				
				for(int j=4-nbNoeud;j<4;j++) {
					this.feuilles.get(index.get(j)).compressPhi(feuillePerNoeud);
				}
			}
			
			
			
		}
	}
	
	public void compress_phi(int nbFeuilles) {
		if(this.color ==null) {
			if(nbFeuilles >= 16) {
				
				for(int i=0; i<4 ; i++) {
					this.feuilles.get(i).compress_phi(nbFeuilles/4);
				}
				
			}else if(nbFeuilles < 4) {
				this.replaceParMoyen();
				
			}else {
				
				//calculating the max colour difference of each feuillles
				double maxV1 = this.feuilles.get(0).max_colorDifference_rec();
				double maxV2 = this.feuilles.get(1).max_colorDifference_rec();
				double maxV3 = this.feuilles.get(2).max_colorDifference_rec();
				double maxV4 = this.feuilles.get(3).max_colorDifference_rec();
				
				//creating a list of the max colour difference of each feuillles
				ArrayList<Double> maxColors = new ArrayList<Double>(4);
				maxColors.add(maxV1);
				maxColors.add(maxV2);
				maxColors.add(maxV3);
				maxColors.add(maxV4);
				
				//crating a list of index that link each maxcolor with the correct index
				ArrayList<Integer> index = new ArrayList<Integer>(4);
				index.add(0);
				index.add(1);
				index.add(2);
				index.add(3);
				
				
				double tmpMaxColor;
				int tmpind;
				for(int i=0 ; i<3 ; i++) {
					
					for(int j=i+1; j<4;j++) {
						
						if(maxColors.get(j) < maxColors.get(i)) {
							tmpMaxColor =  maxColors.get(i);
							maxColors.set(i,maxColors.get(j));
							maxColors.set(j,tmpMaxColor);
							
							//making the same changes in the list index
							tmpind =  index.get(i);
							index.set(i,index.get(j));
							index.set(j,tmpind);
							
						}
					}
				}
					
					 
					int nbNoeud = nbFeuilles/4;
					
					for(int i=0; i<4-nbNoeud ; i++) {
						this.feuilles.get(index.get(i)).replaceParMoyen();
					}
					
					for(int j=4-nbNoeud;j<4;j++) {
						this.feuilles.get(index.get(j)).compress_phi(4);
					}
				
				
			}
		}
	}
	
	public void replaceParMoyen() {
		if(this.color == null) {
			if(this.feuilles.get(0).color != null) {
				// all subtrees are leafs
				double redM = this.mediumRed();
				double greenM = this.mediumgGreen();
				double blueM = this.mediumBlue();
				Color moyen = new Color((int)redM,(int)greenM,(int)blueM);
				this.color = moyen;
				
				for(int i=0; i<4; i++) {
					this.feuilles.set(i, null);
				}
				
				
			}else {
				for(int i=0; i<4; i++) {
					this.feuilles.get(i).replaceParMoyen();
				}
				
				this.replaceParMoyen();
				
			}
		}
	}
	
	

}
