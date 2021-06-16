package ktxo.art.processing;

import processing.core.*;



/**
 * Simple Point to build {@link ktxo.art.processing.Grid} 
 * 
 * @author ktxo.art@gmail.com
 *
 */
public class Point {
	PApplet parent;

	static final int VIBRATE = 2;

	private PVector center = new PVector(0, 0);
	private PVector centerori = new PVector(0, 0);

	float point_size = 4F;
	int point_color = 0;
	private boolean locked = false;
	private boolean hidden = false;

	// =========================================================
	/**
	 * Constructor 
	 * 
	 * @param parent Parent sketch
	 * @param x Point x-coordinates
	 * @param y Point y-coordinates
	 */
	public Point(PApplet parent, float x, float y) {
		this(parent, new PVector(x, y));
	}

	/**
	 * Constructor
	 * 
	 * @param parent Parent sketch
	 * @param pos Point coordinates
	 */
	public Point(PApplet parent, PVector pos) {
		this.parent = parent;
		// parent.registerMethod("dispose", this);
		center.set(pos);
		centerori.set(pos);
	}

	/**
	 * Constructor 
	 * 
	 * @param parent Parent sketch
	 * @param pos Point coordinates
	 * @param color Point color
	 * @param size Point size
	 */
	public Point(PApplet parent, PVector pos, int color, float size) {
		this(parent, pos);
		point_color = color;
		point_size = size;
	}
	// =========================================================

	/**
	 *
	 */
	public Point clone() {
		Point p = new Point(parent, center, point_color, point_size);
		p.locked = locked;
		p.hidden = hidden;
		return p;
	}
	// =========================================================
	/**
	 * Move point (change point center coordinates) 
	 * 
	 * @param pos Point coordinates
	 */
	public void move(PVector pos) {
		// center.add(pos.sub(center));
		center.set(pos);
		centerori.set(center);
	}

	/**
	 * Change point center coordinates
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	public void move(float x, float y) {
		center.add(x, y);
		centerori.set(center);
	}

	/**
	 * Change point x-coordinates
	 * 
	 * @param x X-coordinate
	 */
	public void moveX(float x) {
		center.x += x;
		move(center.add(x, 0));
	}
	/**
	 * Change point y-coordinates
	 * 
	 * @param y Y-coordinate
	 */
	public void moveY(float y) {
		move(center.add(0, y));
	}

	// =========================================================
	/**
	 * Draw the point 
	 */
	public void draw() {
		float s = point_size;
		int c = point_color;

		if (!hidden) {
			parent.stroke(point_color);
			parent.strokeWeight(point_size);
			parent.point(center.x, center.y);
			parent.stroke(c);
			parent.strokeWeight(s);
		}
	}

	// =========================================================
	private int sigNum(float val) {
		if (val > 0) {
			return 1;
		} else if (val < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	// =========================================================
	/**
	 * Add vibration to point, vibration is calculated using {@link PApplet#noise(float)} 
	 * 
	 * @param vibration Amount of vibration, vibration is added using noise()
	 *                  function
	 * @param direction Vibration direction, noise id added to x-coordinate
	 *                  {@link GridConstants#VIBRATE_X}, x-coordinate {@link GridConstants#VIBRATE_Y}
	 *                  both coordinates {@link GridConstants#VIBRATE_ALL}
	 */
	public void vibrate(float vibration, int direction) {
		// point_size= random(0.5, 1);

		float n = parent.noise(centerori.x * vibration, centerori.y * vibration);
		center = centerori.copy();
		switch (direction) {
		case GridConstants.VIBRATE_X:
			center.add(new PVector(sigNum(parent.random(-1, 1)) * n, 0));
			break;
		case GridConstants.VIBRATE_Y:
			center.add(new PVector(0, sigNum(parent.random(-1, 1)) * n));
			break;
		case GridConstants.VIBRATE_ALL:
			center.add(new PVector(sigNum(parent.random(-1, 1)) * n, sigNum(parent.random(-1, 1)) * n));
			break;
		default:
			center.add(new PVector(sigNum(parent.random(-1, 1)) * n, sigNum(parent.random(-1, 1)) * n));
			break;
		}
	}



	// =========================================================
	/**
	 * Lock this point (avoid vibration)
	 * 
	 * @param lock Enable/disable lock 
	 */
	public void lock(boolean lock) {
		this.locked = lock;
	}

	// =========================================================
	
	/**
	 * Check if  point is locked
	 * 
	 * @return Lock status
	 */
	public boolean isLocked() {
		return locked;
	}

	// =========================================================
	/**
	 * Get point coordinates
	 * 
	 * @return Point coordinates
	 */
	public PVector getCenter() {
		return center;
	}

	/**
	 * Set point coordinates
	 * 
	 * @param center Point coordinates
	 */
	public void setCenter(PVector center) {
		this.center = center;
	}

	// =========================================================
	/**
	 * Set point color
	 * 
	 * @param color Point color
	 */	
	public void setColor(int color) {
		point_color = color;
	}

	/**
	 * Get point color
	 * 
	 * @return Point color
	 */
	public int getColor() {
		return point_color;
	}

	// =========================================================
	/**
	 * Set point size
	 * 
	 * @param size Point size
	 */	
	public void setSize(float size) {
		point_size = size;
	}
	/**
	 * Get point size
	 * 
	 * @return Point size
	 */	
	public float getSize() {
		return point_size;
	}

	// =========================================================
	/**
	 * Check if this point is hidden
	 * 
	 * @return Hidden value
	 */	
	public boolean isHidden() {
		return hidden;
	}

	// =========================================================
	/**
	 * Hide/Unhide this point
	 * 
	 * @param hide Point hidden value
	 */
	public void hide(boolean hide) {
		this.hidden = hide;
	}
	/**
	 * Show information for this point
	 * 
	 */
	public void dump() {
		System.out.println(
				String.format("x=%.2f y=%.2f weight=%.2f color=%d", center.x, center.y, point_size, point_color));
	}

}
