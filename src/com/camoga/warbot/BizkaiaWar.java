package com.camoga.warbot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.conf.ConfigurationBuilder;

public class BizkaiaWar {
	
	String[] cities = new String[] {
			"","Gorliz","Lemoiz","Mungia","Bakio","Bermeo","Mundaka","Ibarrangelu","Elantxobe","Ea",
			"Ispaster","Lekeitio","Mendexa","Berriatua","Ondarroa","Barrika","Plentzia","Gatika","Jatabe","Meñaka",
			"Busturia","Gautegiz Arteaga","Ereño","Nabarniz","Gizaburuaga","Amoroto","Markina-Xemein","Etxebarria","Sopela","Urduliz",
			"Laukiz","Gamiz-Fika","Fruiz","Arrieta","Errigoiti","Murueta","Forua","Kortezubi","Arratzu","Aulesti",
			"Getxo","Berango","Erandio","Loiu","Derio","Zamudio","Lezama","Larrabetzu","Muxika","Gernika-Lumo",
			"Ajangiz","Mendata","Munitibar","Mallabia","Ermua","Leioa","Zierbena","Santurtzi","Portugalete","Sestao",
			"Sondika","Bilbo","Etxebarri","Galdakao","Zornotza","Iurreta","Garai","Berriz","Zaldibar","Muskiz",
			"Abanto","Ortuella","Trapagaran","Barakaldo","Alonsotegi","Arrigorriaga","Basauri","Zaratamo","Bedia","Lemoa",
			"Durango","Abadiño","Elorrio","Sopuerta","Galdames","Gueñes","Arrankudiaga","Ugao","Zeberio","Igorre",
			"Dima","Arantzazu","Mañaria","Atxondo","Arakaldo","Orozko","Arteaga","Areatza","Zeanuri","Ubide",
			"Otxandio","Lanestosa","Karrantza","Turtzioz","Artzentales","Balmaseda","Zalla","Gordexola","Izurtza","Urduña",
			"Sukarrieta","Morga","Ziortza-Bolibar"
	};
	
	int[][] frontiers = new int[][] {
		{},{2, 16},{1,4,16,17,18},{4,5,17,18,19,31,43,44,45},{2,3,5,18},{3,4,6,19,20,33},{5,7,20,110},{6,8,9,21,22,110},{7},{7,10,22},
		{9,11,12,22,23,24,25},{10,12},{10,11,13,25},{12,14,25,26},{13},{16,28,29},{1,2,15,17,29},{2,3,16,18,29,30,43},{2,3,4,17},{3,4,5,33,32,31},
		{5,33,34,35,21,6,110},{110,7,20,35,36,37,22},{7,9,10,21,37,23},{22,10,24,37,38,51,39},{10,23,25,39},{10,12,13,24,39,26},{25,13,39,27,53,112},{26},{15,29,40,41},{15,16,17,28,30,41,42},
		{29,17,42,43},{3,32,111,45,46,47},{19,31,33,111},{19,5,20,34,32,111},{33,35,36,20,49,48,111},{34,20,21,36},{34,35,21,37,49},{36,21,22,23,38,49},{49,50,37,23,51},{23,24,25,26,51,52,112},
		{28,41,42,55,58},{40,42,28,29},{41,40,55,59,73,60,61,43,30,29},{42,30,17,3,44,61,60},{43,3,45,61},{44,3,31,46,63,61,62},{45,31,47,63},{46,31,111,64,63},{111,34,49,50,51,52,67,65,64},{48,34,36,37,38,50},
		{49,48,51,38},{50,48,38,23,39,52},{51,39,67,48,112},{26,54,68,67,112},{68,53},{40,42,59,58},{57,69,70},{56,58,70,71},{57,55,40,59,71,72},{58,55,42,72,73},
		{42,43,61},{60,42,43,44,45,62,76,75,73,74},{61,45,63,76},{62,45,46,47,64,78,79,88,76,77},{63,47,48,111,65,80,79,90},{64,48,66,67,80,81},{65,67,81},{66,65,48,52,53,68,81,82,112},{67,53,54,82},{56,70,84,83},
		{56,57,69,71,84},{70,57,58,72,84},{71,58,59,73,84},{72,59,42,61,74,85,84},{73,61,75,86,85},{74,61,76,77,87,88,86},{75,77,61,62,63},{75,76,63,88},{63,79,89,88},{78,63,64,89,90},
		{64,90,65,81,108,92},{80,108,92,90,100,93,82,67,66,65},{81,68,67,93},{69,84,104,105,106},{69,70,71,72,73,85,83,106},{84,73,74,107,106,109},{74,75,87,88,94,95,109},{86,75,88},{87,86,75,77,63,78,89,91,96,95},{78,79,90,91,88},
		{89,79,64,80,92,81,100,99,98,96,91},{90,89,88,96},{90,80,108,81},{81,82},{86,95},{94,86,88,96,97,98,109},{95,88,91,90,97,98},{96,95,98},{95,97,90,99},{98,90,100},
		{99,90,81},{102},{101,103,104},{102,104},{102,103,105,83},{104,106,83},{105,83,107,85},{106,85,109},{80,81,92},{95,107,86,85},
		{6,7,20,21},{31,32,33,34,48,47}, {26,39,52,53,67}
	};
	
	int[][] regionid = new int[][] {
		{},{17}, {13}, {65}, {9}, {5}, {33}, {61}, {45}, {85}, 
		{117}, {105}, {133}, {201}, {213}, {49}, {53}, {93}, {37}, {81}, 
		{69}, {89}, {129}, {185}, {181}, {157}, {321}, {629}, {77}, {97}, 
		{137}, {197}, {189}, {121}, {145}, {125}, {169}, {153}, {241}, {225},
		{101}, {109}, {165}, {177}, {229}, {265}, {557}, {589}, {325}, {233}, 
		{257}, {305}, {601}, {389}, {425}, {193}, {113}, {173}, {237}, {269}, 
		{529}, {577}, {333}, {337}, {625}, {409}, {417}, {397}, {465}, {149}, 
		{205}, {249}, {297}, {541}, {357}, {365}, {369}, {393}, {405}, {401}, 
		{457}, {461}, {481}, {329}, {533}, {349}, {421}, {441}, {445}, {437}, 
		{453}, {473}, {485}, {497}, {477}, {493}, {489}, {509}, {505}, {521}, 
		{513}, {385}, {617}, {317}, {597}, {413}, {377}, {429}, {469}, {517}, 
		{73}, {253}, {900}};

	int[][] borderid = new int[][] {
		{}, {19}, {15}, {67}, {11}, {7}, {35}, {63}, {47}, {87}, 
		{119}, {107}, {135}, {203}, {215}, {51}, {55}, {95}, {39}, {83}, 
		{71}, {91}, {131}, {187}, {183}, {159}, {323}, {631}, {79}, {99}, 
		{139}, {199}, {191}, {123}, {147}, {127}, {171}, {155}, {243}, {227}, 
		{103}, {111}, {167}, {179}, {231}, {267}, {559}, {591}, {327}, {235}, 
		{259}, {307}, {603}, {391}, {427}, {195}, {115}, {175}, {239}, {271}, 
		{531}, {579}, {335}, {339}, {627}, {411}, {419}, {399}, {467}, {151}, 
		{207}, {251}, {299}, {543}, {359}, {14543}, {371}, {395}, {407}, {403}, 
		{459}, {463}, {483}, {331}, {535}, {351}, {423}, {443}, {447}, {439}, 
		{455}, {475}, {487}, {499}, {479}, {495}, {491}, {511}, {507}, {523}, 
		{515}, {387}, {619}, {319}, {599}, {415}, {379}, {431}, {471}, {519}, 
		{75}, {255}, {901}};
	
	String[] meses = new String[] {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
	String[] semanas = new String[] {"Primera semana de ", "Segunda semana de ", "Tercera semana de ", "Cuarta semana de "};
	
	Random randcolors = new Random(27);
	long seed = 13857442;
	Random rand = new Random(seed);
	
	HashMap<Integer, ArrayList<Integer>> possessions = new HashMap<Integer, ArrayList<Integer>>();
	
	Element draft = null;
//	Element stripedfill = null;
	SVGDocument doc = null;
	NodeList paths = null;
	
	Twitter tw = null;
	
	HashMap<Integer,Integer> svgregions = new HashMap<>();
	HashMap<Integer,Integer> svgborders = new HashMap<>();
	
	int iteration = 0;
	int randsteps = 0;
	
	public void start(boolean started) throws Exception {
		readSVG();
		setupTwitter();

		if(!started) {
			for(int i = 1; i < cities.length; i++) {
				possessions.put(i,new ArrayList<Integer>());
				possessions.get(i).add(i);
			}
		}
		
		if(possessions.size() == 1) System.exit(0);
		
		//Avanzamos el rand para que tenga la misma seed que el anterior
		for(int i = 0; i < randsteps; i++) {
			rand.nextInt();
		}
		
		//Obtencion de las ids del mapa
		for(int k = 0; k < paths.getLength(); k++) {
			if(!paths.item(k).getNodeName().equals("path")) continue;
			Element path = ((Element)paths.item(k));
			if(path.getAttribute("fill").equalsIgnoreCase("#d3ffbe")) svgregions.put(Integer.parseInt(path.getAttribute("id").substring(4)), k);
			else if(path.getAttribute("fill").equalsIgnoreCase("none")) svgborders.put(Integer.parseInt(path.getAttribute("id").substring(4)), k);
		}
		
		genCityColors();
		setConqueredCitiesColor();
//		JFrame frame = new JFrame();
//		frame.setSize(800,600);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(true);
//		frame.setVisible(true);
//		Panel panel = new Panel();
//		frame.add(panel);
//		svgToImage();
//		Scanner sc = new Scanner(System.in);
		while(possessions.size() > 1) {
//			setConqueredCitiesColor();
			int randcity = rand.nextInt(cities.length-1)+1;
			int attacking = getCityOwner(randcity);
			if(randcity!=attacking && rand.nextInt(30)==0) {
				indep(randcity, attacking);
				randsteps += 1;
			} else {
				int[] neighbours = getNeighbours(attacking);
				int attacked = neighbours[rand.nextInt(neighbours.length)];
				conquer(attacking, attacked);
				randsteps += 2;
			}
//			setConqueredCitiesColor();
//			unhighlight();
			iteration++;
//			sc.nextLine();
			if(possessions.size() > 1) return;
			System.out.println(iteration);
		}
		
		for(int i : possessions.keySet()) {
			highlightEmpireBorder(i, "#00ff00", "2px");
			status(cities[i].toUpperCase() + " HA GANADO LA GUERRA", svgToImage());
		}
	}
	
	public void conquer(int attacking, int attacked) throws Exception {
		String status = "";
		if(iteration == 0) status += "LA GUERRA HA COMENZADO\n";
		
		status += semanas[iteration%4] + meses[(iteration/4)%12] + " de " + (2020+iteration/48)+"\n";
//		
		highlightCityArea(attacked);
		
		int conqueredOwner = getCityOwner(attacked);
		possessions.get(conqueredOwner).remove((Integer)attacked);
		highlightEmpireBorder(conqueredOwner, "#0000ff", "3px");
		highlightEmpireBorder(attacking, "#00ff00", "3px");	
		possessions.get(attacking).add(attacked);
		highlightCityBorder(attacked, "#ff0000", "3px");
		svgToImage();

		if(conqueredOwner != attacked) {
			status += cities[attacking] + " conquista " + cities[attacked] + " previamente ocupado por " + cities[conqueredOwner] +"\n";
		} else {
			status += cities[attacking] + " conquista " + cities[attacked] + "\n";
		}
		if(possessions.get(conqueredOwner).size()==0) {
			possessions.remove((Integer)conqueredOwner);
			status += cities[conqueredOwner] + " ha sido derrotado\n";
			status += (possessions.size() == 1 ? "Queda ":"Quedan ") + possessions.size() + (possessions.size() == 1 ? " pueblo":" pueblos");
		}
		
		status(status, image);
	}
	
	public void indep(int city, int owner) throws Exception {
		String status = semanas[iteration%4] + meses[(iteration/4)%12] + " de " + (2020+iteration/48)+"\n";
		status += cities[city] + " se ha independizado de " + cities[owner] + "\n";
		int citiesleft = possessions.size();
		possessions.get(owner).remove((Integer) city);
		if(possessions.get(city) == null) possessions.put(city, new ArrayList<Integer>());
		possessions.get(city).add(city);
		changeCityColor(city, cityColors[city]);
		if(possessions.get(owner).size() == 0) {
			possessions.remove(owner);
			status += cities[owner] + " ha sido derrotado";
		} else highlightEmpireBorder(owner, "#ff0000", "3px");
		if(possessions.size() != citiesleft) {
			status += (possessions.size() == 1 ? "Queda ":"Quedan ") + possessions.size() + (possessions.size() == 1 ? " pueblo":" pueblos");
		}
		highlightCityBorder(city, "#00ff00", "3.5px");
		svgToImage();
		status(status, image);
	}
	
	public void unhighlight() {
		while(draft.hasChildNodes()) {
			draft.removeChild(draft.getFirstChild());
		}
	}

	public int[] cityColors = new int[cities.length];
	
	public void genCityColors() {
		for(int city = 1; city < cities.length; city++) {
			cityColors[city] = randcolors.nextInt(0x1000000);
			changeCityColor(city, cityColors[city]);
		}
	}
	
	public void setConqueredCitiesColor() {
		for(int city : possessions.keySet()) 
			for(int city2 : possessions.get(city)) 
				changeCityColor(city2, cityColors[city]);
	}
	
	public void changeCityColor(int city, int color) {
		for(int i : regionid[city]) {
			Element path = ((Element) paths.item(svgregions.get(i)));
			path.setAttribute("fill", "#"+String.format("0x%06X",color).substring(2));
		}	
	}
	
	public void highlightEmpireBorder(int city, String color, String width) {
		for(int possession : possessions.get(city)) {
			for(int region : borderid[possession]) {
				Element path = ((Element)paths.item(svgborders.get(region)).cloneNode(true));
				path.setAttribute("stroke", color); 
				path.setAttribute("stroke-width", width);
				draft.appendChild(path);
			}			
		}
	}
	
	public void highlightCityBorder(int city, String color, String width) {
		for(int region : borderid[city]) {
			Element path = ((Element)paths.item(svgborders.get(region)).cloneNode(true));
			path.setAttribute("stroke", color); 
			path.setAttribute("stroke-width", width);
			draft.appendChild(path);
		}
	}
	
	public void highlightCityArea(int city) {
		for(int region : regionid[city]) {
			Element path = ((Element)paths.item(svgregions.get(region)).cloneNode(true));
			Element path2 = ((Element)paths.item(svgregions.get(region)).cloneNode(true));
			path2.setAttribute("fill", "#"+String.format("0x%06X",cityColors[getCityOwner(city)]).substring(2));
			path.setAttribute("fill","url(#diagonalHatch)");
			draft.appendChild(path2);
			draft.appendChild(path);
		}
	}
	
	public int[] getNeighbours(int empire) {
		ArrayList<Integer> neighbours = new ArrayList<>();
		for(int possession : possessions.get(empire)) {
			for(int city : frontiers[possession]) {
				if(possessions.get(empire).contains(city)) continue;
				if(neighbours.contains(city)) continue;
				neighbours.add(city);
			}
		}
		
		return neighbours.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public int getCityOwner(int city) {
		for(int i : possessions.keySet()) {
			if(possessions.get(i).contains(city)) return i;
		}
		return -1;
	}
	
	public void status(String text, BufferedImage image) throws Exception {
		System.out.println(text);
//		if(0==0) return;
		StatusUpdate status = new StatusUpdate(text);
		if(image != null) {
			File file = new File("C:/Users/usuario/Desktop/JAVA/warbot/photo"+(iteration+1)+".png");
			ImageIO.write(image, "png", file);
			UploadedMedia media = tw.uploadMedia(file);
			status.setMediaIds(media.getMediaId());
		} else {
			throw new Exception("Image null");
		}
		tw.updateStatus(status);			
	}
	
	public void renderFrontiers(int empire) {
		for(int possession : possessions.get(empire)) {
			for(int city : frontiers[possession]) {
				if(possessions.get(empire).contains(city)) continue;
				for(int region : regionid[city]) {
					int p = svgregions.get(region);
					Element path = (Element) paths.item(p);
					path.setAttribute("fill", "#ffff00");
					paths.item(p);
				}
			}			
		}
	}
	
	public Integer[] getPossessions(int city) {
		return possessions.get(city).toArray(new Integer[] {});
	}
	
	public int getCityIndex(String city) {
		for(int i = 0; i < cities.length; i++) {
			if(cities[i].equals(city)) return i;
		}
		
		return -1;
	}
	
	public void readSVG() throws IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		doc = f.createSVGDocument(BizkaiaWar.class.getResource("/mapa.svg").toString());
		Element svgRoot = doc.getDocumentElement();
		NodeList nodes = svgRoot.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("g") && paths == null) {
				paths = nodes.item(i).getChildNodes();
			}
		}
				
		draft = (Element) svgRoot.getChildNodes().item(svgRoot.getChildNodes().getLength()-2);
	}
	
	class Panel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0,0, null);
			g.dispose();
			repaint();
		}
		
		public void render() throws Exception {
			svgToImage();
		}
	};
	
	BufferedImage image;
	public BufferedImage svgToImage() throws Exception {
		TranscodingHints hints = new TranscodingHints();
		hints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
		hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
		hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
		
		TranscoderInput input = new TranscoderInput(doc);
		ImageTranscoder t = new ImageTranscoder() {
			public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
				image = img;		
			}
			
			public BufferedImage createImage(int w, int h) {
				return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			}
		};
		t.setTranscodingHints(hints);
		t.transcode(input, null);
	
		return image;
	}
	
	public void setupTwitter() throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("")
		.setOAuthConsumerSecret("")
		.setOAuthAccessToken("")
		.setOAuthAccessTokenSecret("");

		tw = new TwitterFactory(cb.build()).getInstance();
	}
}