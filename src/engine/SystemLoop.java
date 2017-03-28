package engine;

public class SystemLoop extends Thread implements Runnable {
	private Screen screen;
	private Engine engine;

	public SystemLoop(Engine engine) {
		this.engine = engine;
		this.screen = new Screen(engine);
	}

	public SystemLoop(Engine engine, Object object) {
		this.engine = engine;
	}

	private boolean running = false;

	private long lastFpsTime;

	@Override
	public void run() {
		running = true;
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		while (running) {
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double) OPTIMAL_TIME);
			lastFpsTime += updateLength;
			if (lastFpsTime >= 1000000000) {
				lastFpsTime = 0;
			}
			input();
			update(delta);
			render();
			try {
				Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
			} catch (Exception e) {
			}
		}

	}

	private void input() {
		if (screen == null)
			return;
		screen.input.poll();
		engine.input(screen.input, screen.renderObject);
	}

	private void render() {
		if (screen == null)
			return;
		screen.render();
	}

	private void update(double delta) {
		engine.update(delta);
	}

}
