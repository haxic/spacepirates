package engine;


public interface Engine {
	void update(double delta);

	void input(KeyboardInput input, RenderObject renderObject);

	void render(RenderObject r);

}
