package engine;


import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class KeyboardInput implements KeyListener, MouseListener, MouseMotionListener {
	private static final int KB_KEY_COUNT = 256;
	private boolean[] KB_currentKeys = null;
	private KeyState[] KB_keys = null;
	private static final int M_KEY_COUNT = 256;
	private boolean[] M_currentKeys = null;
	private KeyState[] M_keys = null;
	private V2 mousePos = new V2(0, 0);
	private V2 mousePosFromCenter = new V2(0, 0);
	private V2 mouseVel = new V2(0, 0);
	private V2 mouseMoved = new V2(0, 0);
	private JFrame frame;

	private enum KeyState {
		RELEASED, // Not down
		PRESSED, // Down, but not the first time
		ONCE // Down for the first time
	}

	public KeyboardInput() {
		KB_currentKeys = new boolean[KB_KEY_COUNT];
		KB_keys = new KeyState[KB_KEY_COUNT];
		for (int i = 0; i < KB_KEY_COUNT; ++i) {
			KB_keys[i] = KeyState.RELEASED;
		}

		M_currentKeys = new boolean[M_KEY_COUNT];
		M_keys = new KeyState[M_KEY_COUNT];
		for (int i = 0; i < M_KEY_COUNT; ++i) {
			M_keys[i] = KeyState.RELEASED;
		}
	}
	
	public V2 getMousePosition() {
		return mousePos.clone();
	}

	public void add(JFrame frame) {
		this.frame = frame;
	}

	public synchronized void poll() {
		for (int i = 0; i < KB_KEY_COUNT; ++i) {
			if (KB_currentKeys[i]) {
				if (KB_keys[i] == KeyState.RELEASED) {
					KB_keys[i] = KeyState.ONCE;
				} else
					KB_keys[i] = KeyState.PRESSED;
			} else {
				KB_keys[i] = KeyState.RELEASED;
			}
		}
		for (int i = 0; i < M_KEY_COUNT; ++i) {
			if (M_currentKeys[i]) {
				if (M_keys[i] == KeyState.RELEASED) {
					M_keys[i] = KeyState.ONCE;
				} else
					M_keys[i] = KeyState.PRESSED;
			} else {
				M_keys[i] = KeyState.RELEASED;
			}
		}
		mouseMoved = mouseVel.clone().mul(10);
		mouseVel = new V2(0, 0);
	}

	public boolean kb_keyDown(int keyCode) {
		return KB_keys[keyCode] == KeyState.ONCE || KB_keys[keyCode] == KeyState.PRESSED;
	}

	public boolean kb_keyDownOnce(int keyCode) {
		return KB_keys[keyCode] == KeyState.ONCE;
	}

	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KB_KEY_COUNT) {
			KB_currentKeys[keyCode] = true;
		}
	}

	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KB_KEY_COUNT) {
			KB_currentKeys[keyCode] = false;
		}
	}

	public boolean m_keyDown(int keyCode) {
		return M_keys[keyCode] == KeyState.ONCE || M_keys[keyCode] == KeyState.PRESSED;
	}

	public boolean m_keyDownOnce(int keyCode) {
		// System.out.println(M_keys[keyCode]);
		return M_keys[keyCode] == KeyState.ONCE;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public synchronized double getMouseXVel() {
		return mouseMoved.x;

	}

	public synchronized double getMouseYVel() {
		return mouseMoved.y;

	}

	public synchronized void mousePressed(MouseEvent e) {
		int keyCode = e.getButton();
		if (keyCode >= 0 && keyCode < M_KEY_COUNT) {
			M_currentKeys[keyCode] = true;
		}
	}

	public synchronized void mouseReleased(MouseEvent e) {
		int keyCode = e.getButton();
		if (keyCode >= 0 && keyCode < M_KEY_COUNT) {
			M_currentKeys[keyCode] = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMove(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMove(e);
	}

	private void mouseMove(MouseEvent e) {
		Insets insets = frame.getInsets();
		mousePos = new V2(e.getX()-insets.left, e.getY()-insets.top);
		mousePosFromCenter = new V2(e.getX()-frame.getWidth()/2, e.getY()-frame.getHeight()/2);
		Point locOnScreen = frame.getLocationOnScreen();
		int middleX = locOnScreen.x + (frame.getWidth() / 2);
		int middleY = locOnScreen.y + (frame.getHeight() / 2);
		V2 center = new V2(middleX, middleY);
		mouseVel = mouseVel.add(mousePos.sub(center));
		// System.out.println(mousePos + " " + mouseVel);
	}

	public V2 getMousePositionFromCenter() {
		return mousePosFromCenter.clone();
	}

}