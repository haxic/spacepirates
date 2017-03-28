package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Screen extends JPanel {
	Engine engine;
	Dimension dim = new Dimension(800, 800);
	KeyboardInput input;
	JFrame frame;
	private int SCALE_WIDTH = 1;
	private int SCALE_HEIGHT = 1;
	RenderObject renderObject;

	public Screen(Engine engine) {
		this.engine = engine;
		frame = new JFrame("Derp");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(50, 50);
		frame.setResizable(true);
		setPreferredSize(dim);
		frame.getContentPane().add(this);
		frame.pack();
		input = new KeyboardInput();
		input.add(frame);
		frame.addMouseMotionListener(input);
		frame.addMouseListener(input);
		frame.addKeyListener(input);
		dim = getDimension();
		screenSize = new V3();
		screenSizeHalf = new V3();
		renderObject = new RenderObject(g, screenSize, screenSizeHalf);
		setBackground(Color.black);
		setVisible(true);
		frame.setVisible(true);
	}

	public Dimension getDimension() {
		return (Dimension) dim.clone();
	}

	Graphics2D g; // graphic object
	int screenWidth; // screen width
	int screenHeight; // screen height
	int screenWidthHalf; // screen width half
	int screenHeightHalf; // screen height half
	V3 screenSize;
	V3 screenSizeHalf;

	private Image mImage;

	public void updateRenderObject() {
		Dimension d = getSize();
		checkOffscreenImage();
		
		screenWidth = d.width;
		screenHeight = d.height;
		
		screenSize.x = screenWidth;
		screenSize.y = screenHeight;
		
		screenWidthHalf = screenWidth / 2;
		screenHeightHalf = screenHeight / 2;
		
		screenSizeHalf.x = screenWidthHalf;
		screenSizeHalf.y = screenHeightHalf;
	}

	public void paint(Graphics g) {
		updateRenderObject();
		renderObject.g = (Graphics2D) mImage.getGraphics();
		Graphics offG = mImage.getGraphics();
		offG.setColor(getBackground());
		offG.fillRect(0, 0, screenWidth, screenHeight);
		engine.render(renderObject);
		g.drawImage(mImage, 0, 0, screenWidth * SCALE_WIDTH, screenHeight * SCALE_HEIGHT, null);
	}

	private void checkOffscreenImage() {
		Dimension d = getSize();
		if (mImage == null || mImage.getWidth(null) != d.width || mImage.getHeight(null) != d.height) {
			mImage = createImage(d.width, d.height);
		}
	}

	public void render() {
		repaint();
	}

}
