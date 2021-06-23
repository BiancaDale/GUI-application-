package textcollage;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;





/**
 * A panel that contains a large drawing area where strings
 * can be drawn.  The strings are represented by objects of
 * type DrawTextItem.  An input box under the panel allows
 * the user to specify what string will be drawn when the
 * user clicks on the drawing area.
 * 
 * For my assignment this week I created a text transformation Panel.  The panel has a combo box where the user can 
		 * choose from different text transformation options. 
		 * The transformation options include transparency, magnification, the font style and the rotation angle. 
		 */

public class DrawTextPanel extends JPanel  {


	// As it now stands, this class can only show one string at at
	// a time!  The data for that string is in the DrawTextItem object
	// named theString.  (If it's null, nothing is shown.  This
	// variable should be replaced by a variable of type
	// ArrayList<DrawStringItem> that can store multiple items.





	private ArrayList<DrawTextItem> theString = new ArrayList<DrawTextItem>();  
	//ArrayList to hold DrawTextItem 


	private Color currentTextColor = Color.BLACK;  // Color applied to new strings.

	private Canvas canvas;  // the drawing area.
	private JTextField input;  // where the user inputs the string that will be added to the canvas
	private SimpleFileChooser fileChooser;  // for letting the user select files
	private JMenuBar menuBar; // a menu bar with command that affect this panel
	private MenuHandler menuHandler; // a listener that responds whenever the user selects a menu command
	private JMenuItem undoMenuItem;  // the "Remove Item" command from the edit menu
	private JPanel transformOptions; // Container for multiple combo boxes to adjust text transformation options



	/**
	 * An object of type Canvas is used for the drawing area.
	 * The canvas simply displays all the DrawTextItems that
	 * are stored in the ArrayList, strings.
	 */
	private class Canvas extends JPanel {

		Canvas() {
			//	strings = new ArrayList<stringData>();
			setPreferredSize( new Dimension(800,600) );
			setBackground(Color.LIGHT_GRAY);
			setFont( new Font( "Serif", Font.BOLD, 30 ));


		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			if (theString != null) {
				for (DrawTextItem item : theString) {
					item.draw(g);
				}																										
			}

		}}


	/**
	 * An object of type MenuHandler is registered as the ActionListener
	 * for all the commands in the menu bar.  The MenuHandler object
	 * simply calls doMenuCommand() when the user selects a command
	 * from the menu.
	 */
	private class MenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			doMenuCommand( evt.getActionCommand());
		}
	}

	/**
	 * Creates a DrawTextPanel.  The panel has a large drawing area and
	 * a text input box where the user can specify a string.  When the
	 * user clicks the drawing area, the string is added to the drawing
	 * area at the point where the user clicked.
	 */
	public DrawTextPanel() {

		fileChooser = new SimpleFileChooser();
		undoMenuItem = new JMenuItem("Remove Item");
		undoMenuItem.setEnabled(false);
		menuHandler = new MenuHandler();
		setLayout(new BorderLayout(3,3));
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Text to add: "));
		input = new JTextField("Hello World!", 40);
		bottom.add(input);
		add(bottom, BorderLayout.SOUTH);
		
		/**
		 * Creates a text transformation Panel.  The panel has a combo box where the user can 
		 * choose from different text transformation options. 
		 * The transformation options include transparency, magnification, the font style and the rotation angle 
		 */

		// creates a new panel for the text transformation options 
		transformOptions = new JPanel();
		transformOptions.setLayout(new BoxLayout(transformOptions, BoxLayout.Y_AXIS));
		JPanel titleBar = new JPanel();
		titleBar.setMaximumSize(new Dimension(200, 75));
		JLabel fontTitle = new JLabel("Transform Text");
		fontTitle.setFont(new Font("Serif", Font.BOLD, 24));
		titleBar.add(fontTitle);
		transformOptions.add(titleBar);	

		// Used to center the text in a combo box
		DefaultListCellRenderer center = new DefaultListCellRenderer();
		center.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

		// adds the magnify size options and action listener to the combo box 
		JComboBox<String>magnifyStyles = new JComboBox<String>();
		magnifyStyles.addItem("X2");
		magnifyStyles.addItem("X3");
		magnifyStyles.addItem("X4");
		magnifyStyles.addItem("X5");
		magnifyStyles.setPreferredSize(new Dimension(100, 25));
		magnifyStyles.setRenderer(center);
		magnifyStyles.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String s = (String) magnifyStyles.getSelectedItem(); //get the selected item

				if ("X2".equals(s)) {
					for (DrawTextItem string: theString){
						string.setMagnification(2);   // set string magnification X2
					}

				} else if ("X3".equals(s)) {
					for (DrawTextItem string: theString){
						string.setMagnification(3);  // set string magnification X3
					}}
				else if ("X4".equals(s)) {
					for (DrawTextItem string: theString){
						string.setMagnification(4);   // set string magnification X4
					}
				} else if ("X5".equals(s)) {
					for (DrawTextItem string: theString){
						string.setMagnification(5);  // set string magnification X5
					}
				}
			}});

		// adds the magnify size option to the combo box  
		JPanel magnifySizeSelection = new JPanel();
		magnifySizeSelection.add(new JLabel("Magnify Size"));
		magnifySizeSelection.add(magnifyStyles);
		magnifySizeSelection.setMaximumSize(new Dimension(150, 50));
		transformOptions.add(magnifySizeSelection);
		add(transformOptions, BorderLayout.EAST);

		// adds the transparency percentage options and action listener to the combo box 
		JComboBox<String>transparencyStyles = new JComboBox<String>();
		transparencyStyles.addItem("30%");
		transparencyStyles.addItem("60%");
		transparencyStyles.addItem("80%");
		transparencyStyles.addItem("95%");
		transparencyStyles.setPreferredSize(new Dimension(100, 25));
		transparencyStyles.setRenderer(center);
		transparencyStyles.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String s = (String) transparencyStyles.getSelectedItem(); //get the selected item

				if ("30%".equals(s)) {
					for (DrawTextItem string: theString){
						string.setTextTransparency(0.3);  // set string transparency to 30%
					}

				} else if ("60%".equals(s)) {
					for (DrawTextItem string: theString){
						string.setTextTransparency(0.6);  // set string transparency to 60%
					}}
				else if ("80%".equals(s)) {
					for (DrawTextItem string: theString){
						string.setTextTransparency(0.8);   // set string transparency to 80%
					}
				} else if ("95%".equals(s)) {
					for (DrawTextItem string: theString){
						string.setTextTransparency(0.95);  // set string transparency to 95%
					}
				}
			}});
		// adds the transparency option to the combo box 
		JPanel transparencySelection = new JPanel();
		transparencySelection.add(new JLabel("Transparency"));
		transparencySelection.add(transparencyStyles);
		transparencySelection.setMaximumSize(new Dimension(150, 50));
		transformOptions.add(transparencySelection);
		add(transformOptions, BorderLayout.EAST);

		// adds the rotation angle options and action listener to the combo box 
		JComboBox<String> rotationAngle = new JComboBox<String>();
		rotationAngle.addItem("30°");
		rotationAngle.addItem("45°");
		rotationAngle.addItem("60°");	
		rotationAngle.addItem("90°");
		rotationAngle.setPreferredSize(new Dimension(100, 25));
		rotationAngle.setRenderer(center);
		rotationAngle.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String s = (String) rotationAngle.getSelectedItem();//get the selected item

				if ("30°".equals(s)) {
					for (DrawTextItem string: theString){
						string.setRotationAngle(30);   // set string rotation angle to 30°
					}

				} else if ("45°".equals(s)) {
					for (DrawTextItem string: theString){
						string.setRotationAngle(45);   // set string rotation angle to 45°
					}}
				else if ("60°".equals(s)) {
					for (DrawTextItem string: theString){
						string.setRotationAngle(60);   // set string rotation angle to 60°
					}
				}
				else if ("90°".equals(s)) {
					for (DrawTextItem string: theString){
						string.setRotationAngle(90);   // set string rotation angle to 90°
					}
				}

			}});


		// adds the rotation angle option to the combo box 
		JPanel rotationAngleSelection = new JPanel();
		rotationAngleSelection.add(new JLabel("Rotation Angle"));
		rotationAngleSelection.add(rotationAngle);
		rotationAngleSelection.setMaximumSize(new Dimension(150, 50));
		transformOptions.add(rotationAngleSelection);
		add(transformOptions, BorderLayout.EAST);

		// adds the font options and action listener to the combo box 
		JComboBox<String> fontOption = new JComboBox<String>();
		fontOption.addItem("Serif Bold");	
		fontOption.addItem("SansSerif Bold");	
		fontOption.addItem("Monospaced");	
		fontOption.addItem("Dialog");	
		fontOption.addItem("DialogInput");
		fontOption.setPreferredSize(new Dimension(100, 25));	
		fontOption.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String s = (String) fontOption.getSelectedItem();//get the selected item			

				Font font1 = new Font("Serif", Font.BOLD, 24);
				Font font2 = new Font("SansSerif", Font.BOLD, 24);
				Font font3 = new Font("Monospaced", Font.PLAIN, 24);
				Font font4 = new Font("Dialog", Font.PLAIN, 24);
				Font font5 = new Font("DialogInput", Font.ITALIC, 24);
				// Set the font to be one of the five
				if ("Serif Bold".equals(s)) {
					for (DrawTextItem string: theString)
						string.setFont(font1);			            
				} else if("SansSerif Bold".equals(s)) {
					for (DrawTextItem string: theString)
						string.setFont(font2);			            	
				} else if("Monospaced".equals(s)) {
					for (DrawTextItem string: theString)
						string.setFont(font3);			          
				} else if("Dialog".equals(s)) {
					for (DrawTextItem string: theString)
						string.setFont(font4);			            
				} else if ("DialogInput".equals(s)){
					for (DrawTextItem string: theString)
						string.setFont(font5);			            	
				} 
			}});

		// adds the change font option to the combo box 
		JPanel fontSelection = new JPanel();
		fontSelection.add(new JLabel("Change Font"));
		fontSelection.add(fontOption);
		fontSelection.setMaximumSize(new Dimension(150, 50));
		transformOptions.add(fontSelection);
		add(transformOptions, BorderLayout.EAST); 


		canvas.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doMousePress( e );
			}
		} );
	}


	/**
	 * This method is called when the user clicks the drawing area.
	 * A new string is added to the drawing area.  The center of
	 * the string is at the point where the user clicked.
	 * @param e the mouse event that was generated when the user clicked
	 */
	public void doMousePress( MouseEvent e ) {
		String text = input.getText().trim();
		if (text.length() == 0) {
			input.setText("Hello World!");
			text = "Hello World!";
		}
		DrawTextItem s = new DrawTextItem( text, e.getX(), e.getY() );
		s.setTextColor(currentTextColor);  // Default is null, meaning default color of the canvas (black).
		s.setFont( new Font( "Serif", Font.ITALIC + Font.BOLD, 12 )); 

		theString.add(s);  // add to the ArrayList
		undoMenuItem.setEnabled(true);
		canvas.repaint();
	}


	/**
	 * Returns a menu bar containing commands that affect this panel.  The menu
	 * bar is meant to appear in the same window that contains this panel.
	 */
	public JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();

			String commandKey; // for making keyboard accelerators for menu commands
			if (System.getProperty("mrj.version") == null)
				commandKey = "control ";  // command key for non-Mac OS
			else
				commandKey = "meta ";  // command key for Mac OS

			JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);

			JMenuItem saveItem = new JMenuItem("Save...");
			saveItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "N"));
			saveItem.addActionListener(menuHandler);
			fileMenu.add(saveItem);

			JMenuItem openItem = new JMenuItem("Open...");
			openItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "O"));
			openItem.addActionListener(menuHandler);
			fileMenu.add(openItem);
			fileMenu.addSeparator();

			JMenuItem saveImageItem = new JMenuItem("Save Image...");
			saveImageItem.addActionListener(menuHandler);
			fileMenu.add(saveImageItem);


			JMenu editMenu = new JMenu("Edit");
			menuBar.add(editMenu);
			undoMenuItem.addActionListener(menuHandler); // undoItem was created in the constructor
			undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "Z"));
			editMenu.add(undoMenuItem);
			editMenu.addSeparator();

			JMenuItem clearItem = new JMenuItem("Clear");
			clearItem.addActionListener(menuHandler);
			editMenu.add(clearItem);

			JMenu optionsMenu = new JMenu("Options");
			menuBar.add(optionsMenu);

			JMenuItem colorItem = new JMenuItem("Set Text Color...");
			colorItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "T"));
			colorItem.addActionListener(menuHandler);
			optionsMenu.add(colorItem);

			JMenuItem bgColorItem = new JMenuItem("Set Background Color...");
			bgColorItem.addActionListener(menuHandler);
			optionsMenu.add(bgColorItem);

		}
		return menuBar;
	}

	/**
	 * Carry out one of the commands from the menu bar.
	 * @param command the text of the menu command.
	 */
	private void doMenuCommand(String command) {


		if (command.equals("Save...")) { // save all the string info to a file
			saveFile();
		}
		else if (command.equals("Open...")) { // read a previously saved file, and reconstruct the list of strings
			openFile();
		}

		else if (command.equals("Clear")) {  // remove all strings
			theString.clear();   // clear the list
			undoMenuItem.setEnabled(false);
			canvas.repaint();
		}


		else if (command.equals("Remove Item")) { // remove the most recently added string
			DrawTextItem temp = theString.get(theString.size()-1);//get the last item
			theString.remove(temp);   // remove the last item
			if(theString.size() == 0)
				undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Set Text Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Text Color", currentTextColor);
			if (c != null)
				currentTextColor = c;
		}
		else if (command.equals("Set Background Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Background Color", canvas.getBackground());
			if (c != null) {
				canvas.setBackground(c);
				canvas.repaint();
			}
		}
		else if (command.equals("Save Image...")) {  // save a PNG image of the drawing area
			File imageFile = fileChooser.getOutputFile(this, "Select Image File Name", "textimage.png");
			if (imageFile == null)
				return;
			try {
				// Because the image is not available, I will make a new BufferedImage and
				// draw the same data to the BufferedImage as is shown in the panel.
				// A BufferedImage is an image that is stored in memory, not on the screen.
				// There is a convenient method for writing a BufferedImage to a file.
				BufferedImage image = new BufferedImage(canvas.getWidth(),canvas.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setFont(canvas.getFont());
				canvas.paintComponent(g);  // draws the canvas onto the BufferedImage, not the screen!
				boolean ok = ImageIO.write(image, "PNG", imageFile); // write to the file
				if (ok == false)
					throw new Exception("PNG format not supported (this shouldn't happen!).");
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this, 
						"Sorry, an error occurred while trying to save the image:\n" + e);
			}
		}
	}

	/**
	 * Save the user's collage to a file in human-readable text format.
	 * Files created by this method can be read back into the program
	 * using the saveFile() method.
	 */
	private void saveFile() {
		SimpleFileChooser fileChooser = new SimpleFileChooser();
		File outputFile = fileChooser.getOutputFile();
		try {
			PrintWriter printer = new PrintWriter (new FileWriter(outputFile));
			Color background = canvas.getBackground();//background in the first line
			printer.println(background.getRGB());
			for (DrawTextItem s: theString){
				printer.println(s.getString()); // print the text 
				printer.println(s.getX());		// print the x-coordinate of text
				printer.println(s.getY());		// print the y-coordinate of text
				Color textcolor = s.getTextColor();
				printer.println(textcolor.getRGB());//print the color code of the text
				Font textFont = s.getFont();
				String fontName = textFont.getName();
				int fontStyle = textFont.getStyle();
				int fontSize = textFont.getSize();
				printer.println(fontName + " " + fontStyle + " " + fontSize); // print the font name, style and size 
				printer.println(s.getMagnification());	// print the magnification of text
				printer.println(s.getRotationAngle());	// print the rotation angle of text
				printer.println(s.getTextTransparency()); // print the transparency of text
			}
			printer.close();// close the print writer
		}
		catch (Exception e) {
			// show error in a message box
			JOptionPane.showMessageDialog(this, "Error while saving file!!\n" +e);
		}
	}

	/**
	 *  Reads the contents of the file saved from the method saveFile() 
	 *  back into the program and reconstructs the picture represented by the contents of the file
	 *  @param files opened must be created by the saveFile() method otherwise and exception will be thrown
	 */
	private void openFile() {

		SimpleFileChooser fileChooser = new SimpleFileChooser();
		File inputFile = fileChooser.getInputFile();
		if (inputFile == null) {
			return;
		}
		try {
			theString.clear();
			canvas.repaint();	//remove current contents on the canvas while opening file
			Scanner scanner = new Scanner(inputFile);	//new scanner to read the file
			Color background = Color.decode(scanner.next());	//decode the RGB number into the background color
			canvas.setBackground(background);	//set the background color
			scanner.nextLine();
			while (scanner.hasNext()) {
				String text = scanner.nextLine();
				DrawTextItem s = new DrawTextItem(text);
				s.setX(Integer.parseInt(scanner.next())); // set the x-value
				scanner.nextLine();
				s.setY(Integer.parseInt(scanner.next())); // set the y-value
				scanner.nextLine();
				Color textcol = Color.decode(scanner.next());//decode the RGB number into the text color
				s.setTextColor(textcol);//set the text color
				String fontName = scanner.next();
				int fontStyle = scanner.nextInt();
				int fontSize = scanner.nextInt();
				Font newFont = new Font(fontName, fontStyle, fontSize);					
				s.setFont(newFont); // set the font name, style and size 
				s.setMagnification(Double.parseDouble(scanner.next())); // set the magnification of text
				s.setRotationAngle(Double.parseDouble(scanner.next()));	// set the rotation angle of text
				s.setTextTransparency(Double.parseDouble(scanner.next()));	// set the transparency of text
				scanner.nextLine();
				theString.add(s);
			}
			scanner.close();	//close the scanner
			canvas.repaint(); 	// make the new list of strings 
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this,"Error while opening: " +e);
		}
	}
}