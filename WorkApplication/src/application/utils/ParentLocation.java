package application.utils;

public class ParentLocation {

	private static ParentLocation INSTANCE;
	private double x, y;

	private ParentLocation() {

	}

	public static ParentLocation getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ParentLocation();
		}
		return INSTANCE;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

}
