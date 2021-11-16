/**
 * Provides the classes to add a Grid of points to Processing sketches
 * 
 * @author ktxo.art@gmail.com
 */
package ktxo.art.processing;

import java.util.Arrays;
import processing.core.*;

// ===================================================================================================

/**
 * Class implementing a Grid of Points. The grid is built using processing
 * points and lines.
 * 
 * Each point and line can be configured (weight, color) and moved.
 * 
 * 
 * Grid points are identified by coordinates:
 * 
 * <pre>
 * 0,0   0,1   0,2   0,3   ...   0,nx 
 * 1,0   1,1   1,2   1,3   ...   1,nx
 * ... 
 * ny,0  ny,1  ny,2  ny,3  ...   ny,nx
 * </pre>
 * 
 * Grid position is identified by Point(0,0).
 * 
 * Rows and columns are numbered,  rows from 0 to npointy and columns from 0 to
 * npointx.
 * 
 * <pre>
 *      C0    C1    C2    C3    ...   Cx
 * 
 * R0   0,0   0,1   0,2   0,3   ...   0,nx 
 * R1   1,0   1,1   1,2   1,3   ...   1,nx
 * ...  ... 
 * Ry   ny,0  ny,1  ny,2  ny,3  ...   ny,nx
 * </pre>
 *
 * 
 * 
 * @example SimpleGrid, SimpleGrid2, SimpleGrid3D, FaceGrid
 * @author ktxo.art@gmail.com
 */

public class Grid {
	/**
	 * Library Version
	 */
	public final static String VERSION = "##library.prettyVersion##";
	/**
	 * Default Point stroke cap
	 */
	public static final int POINT_CAP = PConstants.ROUND;	
	/**
	 * Default Point color
	 */
	public static final int POINT_COLOR = 255;
	/**
	 * Default Point Size
	 */
	public static final float POINT_SIZE = 4;
	/**
	 * Default Grid segments color
	 */
	public static final int GRID_COLOR = 120;
	/**
	 * Default Grid segments size
	 */
	public static final float GRID_WEIGHT = 0.5F;

	//private PVector position;
	private Point[] points;
	private boolean lockedPoints[];
	private boolean vibration_enable = false;
	private float vibration_value = 0;
	private int vibration_direction = GridConstants.VIBRATE_ALL;
	private float sizex = 0;
	private float sizey = 0;
	private float divx = 0;
	private float divy = 0;
	private int npointx = 0;
	private int npointy = 0;
	private int npoints = 0;
	private int grid_color = 0;
	private int point_color = Grid.POINT_COLOR;
	private int point_cap = Grid.POINT_CAP;
	private int[] segment_ax_color;
	private float[] segment_ax_weight;
	private int[] segment_ay_color;
	private float[] segment_ay_weight;
	private float point_size = Grid.POINT_SIZE;

	private float stroke_weight = Grid.GRID_WEIGHT;

	PApplet parent;
	

	// =========================================================

	/**
	 * Show library version and author
	 */
	private void welcome() {
		System.out.println("##library.name## v##library.prettyVersion## by ##author##");
	}

	// =========================================================
	/**
	 * Constructor
	 * 
	 * @param parent Parent sketch
	 */
	public Grid(PApplet parent) {
		this(parent, 10, 10, parent.width, parent.height);
		// position.set(0.5F * parent.width / 10, 0.5F * parent.height / 10);
	}

	// =========================================================
	private void setSegments() {
		int sx = (npointx - 1) * (npointy);
		int sy = (npointx) * (npointy - 1);

		segment_ax_color = new int[sx];
		Arrays.fill(segment_ax_color, parent.color(GRID_COLOR));
		segment_ax_weight = new float[sx];
		Arrays.fill(segment_ax_weight, GRID_WEIGHT);

		segment_ay_color = new int[sy];
		Arrays.fill(segment_ay_color, parent.color(GRID_COLOR));
		segment_ay_weight = new float[sy];
		Arrays.fill(segment_ay_weight, GRID_WEIGHT);
	}

	private void setSize() {
		PVector[] corners = getCorners();
		sizex = corners[GridConstants.UPPER_RIGHT_CORNER].x - corners[GridConstants.UPPER_LEFT_CORNER].x;
		sizey = corners[GridConstants.BOTTOM_RIGHT_CORNER].y - corners[GridConstants.UPPER_LEFT_CORNER].y;
	}

	// =========================================================
	/**
	 * Constructor Build a grid of size: len(rows[0]) * len(rows).
	 * 
	 * See example folder
	 * 
	 * @param parent Parent sketch
	 * @param rows   Array of Point (each Point in the grid). Grid row 'i' is
	 *               identified by rows[i]
	 */
	public Grid(PApplet parent, Point[][] rows) {
		this.parent = parent;
		this.grid_color = parent.color(255, 0, 0);
		this.point_color = parent.color(Grid.POINT_COLOR);
		this.npointx = rows[0].length;
		this.npointy = rows.length;
		this.npoints = npointx * npointy;
		this.sizex = -1;
		this.sizex = -1;
		this.divx = -1;
		this.divy = -1;
		//position = rows[0][0].getCenter();
		points = new Point[(npointx * npointy)];
		int id = 0;

		for (int i = 0; i < npointy; i++) {
			this.npointx = rows[i].length;
			for (int j = 0; j < npointx; j++) {
				points[id] = new Point(parent, rows[i][j].getCenter(), Grid.POINT_COLOR, Grid.POINT_SIZE);
				id++;
			}
		}
		lockedPoints = new boolean[npointx * npointy];
		Arrays.fill(lockedPoints, Boolean.FALSE);

		for (int i = 0; i < npoints; i++) {
			points[i].lock(false);
		}
		setSegments();
		setSize();
		welcome();
	}

	// =========================================================
	/**
	 * Constructor
	 * 
	 * @param parent Parent sketch
	 * @param x      X coordinates for grid points
	 * @param y      Y coordinates for grid points
	 */
	public Grid(PApplet parent, float x[], float y[]) {
		this.parent = parent;
		// parent.registerMethod("dispose", this);
		this.npointx = x.length;
		this.npointy = y.length;
		this.npoints = npointx * npointy;
		this.sizex = -1;
		this.sizex = -1;
		this.divx = -1;
		this.divy = -1;
		// position = new PVector(parent.width / 2 - sizex / 2, parent.height / 2 -
		// sizey / 2);
		this.points = new Point[(npointx * npointy)];
		int id = 0;
		for (int i = 0; i < npointy; i++) {
			for (int j = 0; j < npointx; j++) {
				points[id] = new Point(parent, new PVector(x[j], y[i]), point_color, 10);
				id++;
			}
		}
		lockedPoints = new boolean[npointx * npointy];
		Arrays.fill(lockedPoints, Boolean.FALSE);

		for (int i = 0; i < npoints; i++) {
			points[i].lock(false);
		}
		setSegments();
		setSize();
		welcome();
	}

	// =========================================================
	/**
	 * Constructor 
	 * 
	 * Build a grid with npointx*xpointy {@link Point} distributed along
	 * sizex and sizey
	 * 
	 * See example folder
	 * 
	 * @param parent  Parent sketch
	 * @param npointx Number of Points along x-axis
	 * @param npointy Number of Points along y-axis
	 * @param sizex   Grid x-length
	 * @param sizey   Grid y-length
	 */
	public Grid(PApplet parent, int npointx, int npointy, float sizex, float sizey) {
		this.parent = parent;
		this.npointx = npointx;
		this.npointy = npointy;
		this.npoints = npointx * npointy;
		this.sizex = sizex;
		this.sizey = sizey;
		this.divx = sizex / (npointx - 1);
		this.divy = sizey / (npointy - 1);

		// position = new PVector(parent.width / 2 - sizex / 2, parent.height / 2 -
		// sizey / 2);

		points = new Point[(npointx * npointy)];

		int id = 0;
		for (int i = 0; i < npointy; i++) {
			for (int j = 0; j < npointx; j++) {
				points[id] = new Point(parent, new PVector(j * divx, i * divy), point_color, 10F);
				id++;
			}
		}
		lockedPoints = new boolean[npointx * npointy];
		Arrays.fill(lockedPoints, Boolean.FALSE);

		for (int i = 0; i < npoints; i++) {
			points[i].lock(false);
		}
		setSegments();
		setSize();
		welcome();
	}

	// =========================================================
	/**
	 * Constructor Build a grid with npointx*xpointy {@link Point} distributed along screen
	 * width and height
	 * 
	 * See example folder
	 * 
	 * @param parent  Parent sketch
	 * @param npointx Number of Points along x-axis
	 * @param npointy Number of Points along y-axis
	 */
	public Grid(PApplet parent, int npointx, int npointy) {
		this(parent, npointx, npointy, parent.width, parent.height);
	}

	// =========================================================

	/**
	 * Clone grid
	 * 
	 * @return A clone of current grid
	 */
	public Grid clone() {
		Grid g = new Grid(parent, npointx, npointy, sizex, sizey);
		g.grid_color = grid_color;

		g.point_color = point_color;
		g.point_size = point_size;
		// g.position = position.copy();
		g.lockedPoints = Arrays.copyOf(lockedPoints, lockedPoints.length);
		g.vibration_enable = vibration_enable;
		g.vibration_value = vibration_value;

		for (int i = 0; i < npoints; i++) {
			g.points[i] = points[i].clone();
		}
		g.segment_ax_color = Arrays.copyOf(segment_ax_color, segment_ax_color.length);
		g.segment_ax_weight = Arrays.copyOf(segment_ax_weight, segment_ax_weight.length);
		g.segment_ay_color = Arrays.copyOf(segment_ay_color, segment_ay_color.length);
		g.segment_ay_weight = Arrays.copyOf(segment_ay_weight, segment_ay_weight.length);
		return g;
	}

	// =========================================================
	/**
	 * Move Grid to a new location: moving point with coordinate (0,0) to (point.x,
	 * point.y) and adjusting all points
	 * 
	 * @param point New location (x,y)
	 */
	public void move(PVector point) {

		PVector d = point.sub(points[0].getCenter());
		for (Point p : points) {
			p.move(p.getCenter().add(d));
		}
		setSize();
	}

	/**
	 * Move Grid to a new location: moving point with coordinate (0,0) to (x, y) and
	 * adjusting all points
	 * 
	 * @param x X location
	 * @param y Y location
	 */
	public void move(float x, float y) {
		float dx = x - points[0].getCenter().x;
		float dy = y - points[0].getCenter().y;
		for (Point point : points) {
			point.move(PVector.add(point.getCenter(), new PVector(dx, dy)));
		}
		setSize();
	}

	// =========================================================
	/**
	 * Move a grid point identified by coord to newpos
	 * 
	 * coord.x (from 0 to npointx-1) coord.y (from 0 to npointy-1)
	 * 
	 * @param coord  Point coordinates to move
	 * @param newpos New location of point [width, height]
	 */
	public void movePoint(PVector coord, PVector newpos) {
		// newpos.sub(getPosition());
		// PVector n = PVector.sub(newpos,getPosition());
		points[getIndexFromCoordinates(coord)].move(newpos);
		setSize();
	}

	/**
	 * Move a grid point identified by coord (x1,y1) to newpos (x2,y2) in screen
	 * 
	 * @param x1 X-Coordinate (from 0 to npointx-1)
	 * @param y1 Y-Coordinate (from 0 to npointy-1)
	 * @param x2 X location in screen
	 * @param y2 Y location in screen
	 */
	public void movePoint(int x1, int y1, float x2, float y2) {
		points[getIndexFromCoordinates(x1, y1)].move(x2, y2);
		setSize();
	}

	/**
	 * Move a grid point identified by coord to newpos  in screen
	 * incrementing coordinates according to factor.
	 * 
	 * @param coord  Point coordinates to move
	 * @param newpos New location of point [width, height]
	 * @param factor Move point this factor
	 */
	public void movePoint(PVector coord, PVector newpos, float factor) {
		PVector s = getPosFromCoordinates((int)coord.x, (int)coord.y);

		PVector dir = PVector.sub(newpos, s).normalize();
		dir.mult(factor);
		PVector aux = PVector.add(s, dir);
		movePoint(coord, aux);
	}

	/**
	 * Move a grid point identified by coord (x1,y1) to newpos (x2,y2) in screen
	 * incrementing coordinates according to factor.
	 * 
	 * @param x1     X-Coordinate (from 0 to npointx-1)
	 * @param y1     Y-Coordinate (from 0 to npointy-1)
	 * @param x2     X location in screen
	 * @param y2     Y location in screen
	 * @param factor Move point this factor
	 */
	public void movePoint(int x1, int y1, float x2, float y2, float factor) {
		PVector s = getPosFromCoordinates(x1, y1);
		PVector d = new PVector(x2, y2);
		PVector dir = PVector.sub(d, s).normalize();
		dir.mult(factor);
		PVector aux = PVector.add(s, dir);
		movePoint(new PVector(x1, y1), aux);
	}

	// =========================================================
	/**
	 * Return grid corners
	 * 
	 * @return Grid corner: [upper-left, upper-right, lower-left, lower-right]
	 */
	public PVector[] getCorners() {
		PVector corners[] = new PVector[4];
		corners[0] = points[0].getCenter().copy();
		corners[1] = points[npointx - 1].getCenter().copy();
		corners[2] = points[npointx * (npointy - 1)].getCenter().copy();
		corners[3] = points[npoints - 1].getCenter().copy();

		return corners;
	}

	// =========================================================
	/**
	 * Fill grid space with a color
	 * 
	 * @param color Color, e.g.: color(255,0)
	 */
	public void fillGrid(int color) {
		for (int i = 0; i < npointy - 1; i++) {
			for (int j = 0; j < npointx - 1; j++) {
				fillBlock(new PVector(j, i), color);
			}
		}
	}

	/**
	 * Fill with a color a block identified by coord (upper left point) coord.x
	 * (from 0 to npointx-1) coord.y (from 0 to npointy-1)
	 * 
	 * @param coord Block coordinates (upper left point)
	 * @param color Color, e.g.: color(255,0)
	 */
	public void fillBlock(PVector coord, int color) {
		if (coord.x >= 0 && coord.x < (npointx - 1) && coord.y >= 0 && coord.y < (npointy - 1)) {
			PVector p0 = getPosFromCoordinates((int) coord.x, (int) coord.y);
			PVector p1 = getPosFromCoordinates((int) coord.x + 1, (int) coord.y);
			PVector p2 = getPosFromCoordinates((int) coord.x + 1, (int) coord.y + 1);
			PVector p3 = getPosFromCoordinates((int) coord.x, (int) coord.y + 1);
			parent.fill(color);
			parent.quad(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
			parent.noFill();
		}
	}

	/**
	 * Fill with a color a row
	 * 
	 * @param row   Row number (from 0 to npointy-1)
	 * @param color Color, e.g.: color(255,0)
	 */
	public void fillRow(int row, int color) {
		if (row < (npointy - 1)) {
			parent.fill(color);
			for (int i = 0; i < npointx - 1; i++) {
				PVector p0 = getPosFromCoordinates(i, row);
				PVector p1 = getPosFromCoordinates(i + 1, row);
				PVector p2 = getPosFromCoordinates(i + 1, row + 1);
				PVector p3 = getPosFromCoordinates(i, row + 1);
				parent.quad(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
			}
			parent.noFill();
		}
	}

	/**
	 * Fill with a color a column
	 * 
	 * @param col   Column number (from 0 to npointx-1)
	 * @param color Color, e.g.: color(255,0)
	 */
	public void fillColumn(int col, int color) {
		if (col < (npointx - 1)) {
			parent.fill(color);
			for (int i = 0; i < npointy - 1; i++) {
				PVector p0 = getPosFromCoordinates(col, i);
				PVector p1 = getPosFromCoordinates(col + 1, i);
				PVector p2 = getPosFromCoordinates(col + 1, i + 1);
				PVector p3 = getPosFromCoordinates(col, i + 1);
				parent.quad(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
			}
			parent.noFill();
		}
	}

	// =========================================================
	/**
	 * Move row, incrementing y-coordinate
	 * 
	 * @param row   Row number (from 0 to npointy-1)
	 * @param value Add this value to all y-coordinates in row
	 * @param on    Move row on direction horizontal
	 *              ({@link ktxo.art.processing.GridConstants#GRID_AX}) or vertical
	 *              ({@link ktxo.art.processing.GridConstants#GRID_AY})
	 */
	public void moveRow(int row, float value, int on) {
		for (int i = 0; i < npointx; i++) {
			if (on == GridConstants.GRID_AX) {
				points[getIndexFromCoordinates(i, row)].moveX(value);
			} else if (on == GridConstants.GRID_AY) {
				points[getIndexFromCoordinates(i, row)].moveY(value);
			}
		}
	}

	/**
	 * Move each row point in direction to another point incrementing coordinates
	 * according to factor.
	 * 
	 * 
	 * @param row               Row number (from 0 to npointy-1)
	 * @param destinationPoints Array containing the final location. Number of
	 *                          points must be equal to number of points in the row
	 * @param factor            Move point this factor
	 */
	public void moveRowTo(int row, PVector[] destinationPoints, float factor) {
		PVector[] opoints = getPointsOfRow(row);
		assert opoints.length == destinationPoints.length
				: String.format("Number of points '%d' must be equal to numbber of row points '%d'", opoints.length,
						destinationPoints.length);

		for (int i = 0; i < destinationPoints.length; i++) {
			PVector dir = PVector.sub(destinationPoints[i], opoints[i]).normalize();
			dir.mult(factor);
			PVector aux = PVector.add(opoints[i], dir);
			movePoint(new PVector(i, row), aux);
			//
		}
	}

	/**
	 * Move column, incrementing x-coordinate
	 * 
	 * @param col   Column number (from 0 to npointx-1)
	 * @param value Add this value to all x-coordinates in column
	 * @param on    Move column on direction horizontal
	 *              ({@link ktxo.art.processing.GridConstants#GRID_AX}}) or vertical
	 *              ({@link ktxo.art.processing.GridConstants#GRID_AY})
	 */
	public void moveColumn(int col, float value, int on) {
		for (int i = 0; i < npointy; i++) {
			if (on == 0) {
				points[getIndexFromCoordinates(col, i)].moveX(value);
			} else if (on == 1) {
				points[getIndexFromCoordinates(col, i)].moveY(value);
			}
		}
	}

	/**
	 * Move each column point in direction to another point incrementing coordinates
	 * according to factor.
	 * 
	 * 
	 * @param col               Column number (from 0 to npointx-1)
	 * @param destinationPoints Array containing the final location. Number of
	 *                          points must be equal to number of points in the
	 *                          column
	 * @param factor            Move point this factor
	 */
	public void moveColumnTo(int col, PVector[] destinationPoints, float factor) {
		PVector[] opoints = getPointsOfColumn(col);
		assert opoints.length == destinationPoints.length
				: String.format("Number of points '%d' must be equal to number of column points '%d'", opoints.length,
						destinationPoints.length);

		for (int i = 0; i < destinationPoints.length; i++) {
			PVector dir = PVector.sub(destinationPoints[i], opoints[i]).normalize();
			dir.mult(factor);
			PVector aux = PVector.add(opoints[i], dir);
			movePoint(new PVector(col, i), aux);
		}
	}

	// =========================================================
	/**
	 * Get Grid position (upper left point)
	 * 
	 * @return Coordinate of upper left point
	 */
	public PVector getPosition() {
		return points[0].getCenter().copy();
	}

	// =========================================================
	/**
	 * Return the position in the screen of a point in the grid
	 * 
	 * @param xCoord X-coordinate of point (from 0 to npointx-1)
	 * @param yCoord Y-coordinate of point (from 0 to npointy-1)
	 * 
	 * @return Coordinate of point
	 */
	public PVector getPosFromCoordinates(int xCoord, int yCoord) {
		assert xCoord < npointx || yCoord < npointy
				: String.format("Invalid coordinates, values must be: xCoord < %d (npointx) and yCoord < %d (npointy)",
						npointx, npointy);
		return points[xCoord + yCoord * npointx].getCenter();
	}

	/**
	 * Helper function to locate a point in internal array {@link #points}
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * 
	 * @return Point index
	 */
	private int getIndexFromCoordinates(int x, int y) {
		return (x + y * npointx);
	}

	/**
	 * Helper function to locate a point in internal array {@link #points}
	 * 
	 * @param point Point coordinates
	 * 
	 * @return Point index
	 */
	private int getIndexFromCoordinates(PVector point) {
		return (int) (point.x + point.y * npointx);
	}

	// =========================================================
	/**
	 * Return coordinates for point number id in array {@link #points}
	 * 
	 * @param id Point number (0 to npoints)
	 * 
	 * @return Point coordinates
	 */
	private PVector getCoordinatesFromIndex(int idx) {
		return this.points[idx].getCenter();
	}

	// =========================================================
	private void drawSegment(Point p0, Point p1) {
		if (p0.isHidden() == true || p1.isHidden() == true) {
			return;
		}
		PVector start = p0.getCenter();
		PVector end = p1.getCenter();
		parent.line(start.x, start.y, end.x, end.y);
	}

	// =========================================================
	private void drawPoint(Point p) {
		if (p.isHidden() == true) {
			return;
		}
		p.draw();
	}

	// =========================================================

	/**
	 * Get internal index for a point
	 * 
	 * @param axis Get index from internal x-segments or y-segments, Valid values:
	 *             {@link ktxo.art.processing.GridConstants#GRID_AX}} or
	 *             {@link ktxo.art.processing.GridConstants#GRID_AY}
	 * @param x    Point x-coordinate
	 * @param y    Point y-coordinate
	 * 
	 * @return Point index
	 */
	private int getSegmentIndex(int axis, int x, int y) {
		if (axis == GridConstants.GRID_AX) {
			return (y * (npointx - 1) + x);
		} else {
			return (x * (npointy - 1) + y);
		}
	}

	// =========================================================
	public void render(boolean enableTranslate) {
		if (vibration_enable == true) {
			for (int i = 0; i < this.points.length; i++) {
				if (lockedPoints[i] == false) {
					this.points[i].vibrate(vibration_value, vibration_direction);
				}
			}
		}
		if (enableTranslate) {
			PVector[] C = getCorners();
			System.out.println((C[1].x - C[0].x) / 2 + "--" + (C[2].y - C[1].y) / 2);
			parent.translate((C[1].x - C[0].x) / 2, (C[2].y - C[1].y) / 2);

		}
		int id = 0;

		for (int i = 0; i < npointy; i++) {
			for (int j = 0; j < npointx - 1; j++) {
				// Horizontal
				id = getSegmentIndex(GridConstants.GRID_AX, j, i);
				parent.strokeWeight(segment_ax_weight[id]);
				parent.stroke(segment_ax_color[id]);
				drawSegment(this.points[i * npointx + j], this.points[i * npointx + j + 1]);
				if (i < npointy - 1) {
					// Vertical
					id = getSegmentIndex(GridConstants.GRID_AY, j, i);
					parent.strokeWeight(segment_ay_weight[id]);
					parent.stroke(segment_ay_color[id]);
					drawSegment(this.points[i * npointx + j], this.points[(i + 1) * npointx + j]);
				}
				// drawPoint(this.points[i * npointx + j]);
			}
			// drawPoint(this.points[i * npointx + npointx - 1]);
			if (i < npointy - 1) {
				// Last vertical
				id = getSegmentIndex(GridConstants.GRID_AY, npointx - 1, i);
				parent.strokeWeight(segment_ay_weight[id]);
				parent.stroke(segment_ay_color[id]);
				drawSegment(this.points[i * npointx + (npointx - 1)], this.points[(i + 1) * npointx + (npointx - 1)]);
			}
		}
		// Draw point over lines
		for (Point point : points) {
			drawPoint(point);
		}
	}

	/**
	 * Show grid
	 * 
	 */
	public void render() {
		if (vibration_enable == true) {
			for (int i = 0; i < this.points.length; i++) {
				if (lockedPoints[i] == false) {
					this.points[i].vibrate(vibration_value, vibration_direction);
				}
			}
		}
		int id = 0;

		for (int i = 0; i < npointy; i++) {
			for (int j = 0; j < npointx - 1; j++) {
				// Horizontal
				id = getSegmentIndex(GridConstants.GRID_AX, j, i);
				parent.strokeWeight(segment_ax_weight[id]);
				parent.stroke(segment_ax_color[id]);
				drawSegment(this.points[i * npointx + j], points[i * npointx + j + 1]);
				if (i < npointy - 1) {
					// Vertical
					id = getSegmentIndex(GridConstants.GRID_AY, j, i);
					parent.strokeWeight(segment_ay_weight[id]);
					parent.stroke(segment_ay_color[id]);
					drawSegment(this.points[i * npointx + j], this.points[(i + 1) * npointx + j]);
				}
				// drawPoint(this.points[i * npointx + j]);
			}
			// drawPoint(this.points[i * npointx + npointx - 1]);
			if (i < npointy - 1) {
				// Last vertical
				id = getSegmentIndex(GridConstants.GRID_AY, npointx - 1, i);
				parent.strokeWeight(segment_ay_weight[id]);
				parent.stroke(segment_ay_color[id]);
				drawSegment(this.points[i * npointx + (npointx - 1)], this.points[(i + 1) * npointx + (npointx - 1)]);
			}
		}
		// Draw point over lines
		for (Point point : points) {
			drawPoint(point);
		}

	}

	// =========================================================
	/**
	 * Hide/Unhide a point see {@link ktxo.art.processing.Point#hide}
	 * 
	 * @param point Point coordinates to move
	 * @param hide  Hide/no hide this point
	 */
	public void hidePoint(PVector point, boolean hide) {
		this.points[(int) (point.x + point.y * npointy)].hide(hide);
	}

	// =========================================================
	/**
	 * Lock a point (avoid vibration) {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param point Point coordinates
	 */
	public void lockPoint(PVector point) {
		lockedPoints[(int) (point.x + point.y * npointx)] = true;
	}

	/**
	 * Lock a list of points (avoid vibration)
	 * {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param points Points to lock
	 */
	public void lockPoints(PVector points[]) {
		for (PVector p : points) {
			lockedPoints[(int) (p.x + p.y * npointy)] = true;
		}
	}

	/**
	 * Unlock a point (avoid vibration) {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param point Point coordinates
	 */
	public void unLockPoint(PVector point) {
		lockedPoints[(int) (point.x + point.y * npointx)] = false;
	}

	/**
	 * unLock a list of points (avoid vibration)
	 * {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param points Points to lock
	 */
	public void unLockPoints(PVector points[]) {
		for (PVector p : points) {
			lockedPoints[(int) (p.x + p.y * npointy)] = false;
		}
	}

	// =========================================================
	/**
	 * Lock row points (avoid vibration) {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param row Row number, valid values from 0 to npointy-1
	 */
	public void lockRow(int row) {
		for (int i = 0; i < npointx; i++) {
			lockedPoints[(i + row * npointx)] = true;
		}
	}

	/**
	 * Unlock column points (avoid vibration) {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param col Row number, valid values from 0 to npointy-1
	 */
	public void lockColumn(int col) {
		for (int i = 0; i < npointy; i++) {
			lockedPoints[(col + npointx * i)] = true;
		}
	}

	/**
	 * Lock row points (avoid vibration) {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param row Row number, valid values from 0 to npointy-1
	 */
	public void UnLockRow(int row) {
		for (int i = 0; i < npointx; i++) {
			lockedPoints[(i + row * npointx)] = false;
		}
	}

	/**
	 * Unlock column points (avoid vibration) {@link ktxo.art.processing.Point#lock}
	 * 
	 * @param col Row number, valid values from 0 to npointy-1
	 */
	public void UnLockColumn(int col) {
		for (int i = 0; i < npointy; i++) {
			lockedPoints[(col + npointx * i)] = false;
		}
	}

	// =========================================================
	/**
	 * Lock corners grid (avoid vibration)
	 * 
	 */
	public void lockCorners() {
		lockPoint(new PVector(0, 0));
		lockPoint(new PVector(npointx - 1, 0));
		lockPoint(new PVector(npointx - 1, npointy - 1));
		lockPoint(new PVector(0, npointy - 1));
	}

	// =========================================================
	/**
	 * Enable/disable vibration in grid points
	 * 
	 * @param enable    Enable/disable vibration
	 * @param value     Amount of vibration, see
	 *                  {@link ktxo.art.processing.Point#vibrate}
	 * @param direction Vibration direction, noise id added to x-coordinate
	 *                  {@link GridConstants#VIBRATE_X}, x-coordinate
	 *                  {@link GridConstants#VIBRATE_Y} both coordinates
	 *                  {@link GridConstants#VIBRATE_ALL}
	 */
	public void vibrate(boolean enable, float value, int direction) {
		vibration_enable = enable;
		vibration_value = value;
		vibration_direction = direction;
	}

	/**
	 * Enable/disable vibration in one point
	 * 
	 * @param point     Point coordinates
	 * @param enable    enable Enable/disable vibration
	 * @param value     Amount of vibration, see
	 *                  {@link ktxo.art.processing.Point#vibrate}
	 * @param direction Vibration direction, noise id added to x-coordinate
	 *                  {@link GridConstants#VIBRATE_X}, x-coordinate
	 *                  {@link GridConstants#VIBRATE_Y} both coordinates
	 *                  {@link GridConstants#VIBRATE_ALL}
	 */
	public void vibrate(PVector point, boolean enable, float value, int direction) {
		vibration_enable = enable;
		vibration_value = value;
		vibration_direction = direction;
	}

	/**
	 * Enable/disable vibration
	 * 
	 * @param status Vibration status
	 */
	public void vibrate(boolean status) {
		vibration_enable = status;
	}

	// =========================================================
	/**
	 * Shrink grid dimentions
	 * 
	 * @param factor Shrink number
	 * @param on     Segment direction
	 *               ({@link ktxo.art.processing.GridConstants#GRID_AX}) or vertical
	 *               ({@link ktxo.art.processing.GridConstants#GRID_AY})
	 */
	public void shrink(float factor, int on) {
		if (on == GridConstants.GRID_AX || on == GridConstants.GRID_ALL) {
			for (int j = 0; j < npointy; j++) {
				for (int i = 1; i < npointx; i++) {
					points[getIndexFromCoordinates(i, j)].getCenter().x -= factor * i;
				}
			}
		}
		if (on == GridConstants.GRID_AY || on == GridConstants.GRID_ALL) {
			for (int i = 0; i < npointx; i++) {
				for (int j = 1; j < npointy; j++) {
					points[getIndexFromCoordinates(i, j)].getCenter().y -= factor * j;
				}
			}
		}
	}

	/**
	 * Expand grid dimentions
	 * 
	 * @param factor Expanding number
	 * @param on     Segment direction
	 *               ({@link ktxo.art.processing.GridConstants#GRID_AX}) or vertical
	 *               ({@link ktxo.art.processing.GridConstants#GRID_AY})
	 */
	public void expand(float factor, int on) {
		if (on == GridConstants.GRID_AX || on == GridConstants.GRID_ALL) {
			for (int j = 0; j < npointy; j++) {
				for (int i = 1; i < npointx; i++) {
					points[getIndexFromCoordinates(i, j)].getCenter().x += factor * i;
				}
			}
		}
		if (on == GridConstants.GRID_AY || on == GridConstants.GRID_ALL) {
			for (int i = 0; i < npointx; i++) {
				for (int j = 1; j < npointy; j++) {
					points[getIndexFromCoordinates(i, j)].getCenter().y += factor * j;
				}
			}
		}
	}

	// =========================================================
	/*
	 * public void arc(PVector p1, PVector p2) { float s = stroke_weight; int c =
	 * grid_color; parent.stroke(parent.color(255, 0, 0)); parent.strokeWeight(1);
	 * PVector P1 = getPosFromCoordinates((int) p1.x, (int) p1.y); PVector P2 =
	 * getPosFromCoordinates((int) p2.x, (int) p2.x); parent.curve(10, 40, P1.x,
	 * P1.y, P2.x, P2.y, 60, 120); parent.stroke(c); parent.strokeWeight(s); }
	 */
	// =========================================================

	/**
	 * Set Grid weight
	 * 
	 * @param weight Weight
	 */
	public void setGridWeight(float weight) {
		setGridInternalWeight(weight);
		setGridContourWeight(weight);
	}

	/**
	 * Set horizontal/vertical segment weight
	 * 
	 * @param point  Segment coordinate (upper-left point)
	 * @param weight Weight
	 * @param on     Segment direction
	 *               {@link ktxo.art.processing.GridConstants#GRID_AX} or vertical
	 *               {@link ktxo.art.processing.GridConstants#GRID_AY}
	 */
	public void setSegmentWeight(PVector point, float weight, int on) {
		if (on == GridConstants.GRID_AX) {
			segment_ax_weight[(int) (npointy * point.x + point.x)] = weight;
		} else if (on == GridConstants.GRID_AY) {
			segment_ay_weight[(int) (npointy * point.x + point.x)] = weight;
		} else {
			segment_ax_weight[(int) (npointy * point.x + point.x)] = weight;
			segment_ay_weight[(int) (npointy * point.x + point.x)] = weight;
		}
	}

	/**
	 * Set grid contour weight
	 * 
	 * @param weight Grid lines weight
	 */
	public void setGridContourWeight(float weight) {
		for (int i = 0; i < npointx - 1; i++) {
			segment_ax_weight[i] = weight;
			segment_ax_weight[segment_ax_weight.length - 1 - i] = weight;
		}
		for (int i = 0; i < npointy - 1; i++) {
			segment_ay_weight[i] = weight;
			segment_ay_weight[segment_ay_weight.length - 1 - i] = weight;
		}
	}

	/**
	 * Set color for one column
	 * 
	 * @param col   Column number (from 0 to npointx-1)
	 * @param color Contour color
	 * 
	 */
	public void setGridColorColumn(int col, int color) {
		int id = 0;
		for (int j = 0; j < npointy - 1; j++) {
			id = getSegmentIndex(GridConstants.GRID_AY, col, j);
			segment_ay_color[id] = color;
		}
	}

	/**
	 * Set color for one row
	 * 
	 * @param row   Row number (from 0 to npointy-1)
	 * @param color Contour color
	 * 
	 */
	public void setGridColorfRow(int row, int color) {
		int id = 0;
		for (int i = 0; i < npointx - 1; i++) {
			id = getSegmentIndex(GridConstants.GRID_AX, i, row);
			segment_ax_color[id] = color;
		}
	}

	/**
	 * Set horizontal/vertical segment color
	 * 
	 * @param point Segment coordinate (upper-left point)
	 * @param color Color
	 * @param on    Segment direction
	 *              ({@link ktxo.art.processing.GridConstants#GRID_AX}) or vertical
	 *              ({@link ktxo.art.processing.GridConstants#GRID_AY})
	 */
	public void setSegmentColor(PVector point, int color, int on) {
		if (on == GridConstants.GRID_AX) {
			segment_ax_color[(int) (npointy * point.x + point.x)] = color;
		} else if (on == GridConstants.GRID_AY) {
			segment_ay_color[(int) (npointy * point.x + point.x)] = color;
		} else {
			segment_ax_color[(int) (npointy * point.x + point.x)] = color;
			segment_ay_color[(int) (npointy * point.x + point.x)] = color;
		}
	}

	/**
	 * Set weight for all internal segments (no contour)
	 * 
	 * @param weight Grid lines weight
	 */
	public void setGridInternalWeight(float weight) {
		int id = 0;
		// Horizontal
		for (int i = 1; i < npointy - 1; i++) {
			for (int j = 0; j < npointx - 1; j++) {
				id = getSegmentIndex(GridConstants.GRID_AX, j, i);
				segment_ax_weight[id] = weight;
			}
		}
		// Vertical
		for (int i = 0; i < npointy - 1; i++) {
			for (int j = 1; j < npointx - 1; j++) {
				id = getSegmentIndex(GridConstants.GRID_AY, j, i);
				segment_ay_weight[id] = weight;
			}
		}
	}

	/**
	 * Set weight for one column
	 * 
	 * @param col    Column number (from 0 to npointx-1)
	 * @param weight Grid lines weight
	 * 
	 */
	public void setGridWeightColumn(int col, float weight) {
		int id = 0;
		for (int j = 0; j < npointy - 1; j++) {
			id = getSegmentIndex(GridConstants.GRID_AY, col, j);
			segment_ay_weight[id] = weight;

		}
	}

	/**
	 * Set weight for one row
	 * 
	 * @param row    Row number (from 0 to npointy-1)
	 * @param weight Grid lines weight
	 * 
	 */
	public void setGridWeightRow(int row, float weight) {
		int id = 0;
		for (int i = 0; i < npointx - 1; i++) {
			id = getSegmentIndex(GridConstants.GRID_AX, i, row);
			segment_ax_weight[id] = weight;

		}
	}

	/**
	 * Set color for all internal segments (no contour)
	 * 
	 * @param color Segment color
	 */
	public void setGridInternalColor(int color) {
		int id = 0;
		// Horizontal
		for (int i = 1; i < npointy - 1; i++) {
			for (int j = 0; j < npointx - 1; j++) {
				id = getSegmentIndex(GridConstants.GRID_AX, j, i);
				segment_ax_color[id] = color;
			}
		}
		// Vertical
		for (int i = 0; i < npointy - 1; i++) {
			for (int j = 1; j < npointx - 1; j++) {
				id = getSegmentIndex(GridConstants.GRID_AY, j, i);
				segment_ay_color[id] = color;
			}
		}
	}

	/**
	 * Set grid contour color
	 * 
	 * @param color Contour color
	 */
	public void setGridContourColor(int color) {
		for (int i = 0; i < npointx - 1; i++) {
			segment_ax_color[i] = color;
			segment_ax_color[segment_ax_color.length - 1 - i] = color;
		}
		for (int i = 0; i < npointy - 1; i++) {
			segment_ay_color[i] = color;
			segment_ay_color[segment_ay_color.length - 1 - i] = color;
		}
	}

	// =========================================================
	/**
	 * Set Grid color (all segments)
	 * 
	 * @param color Color
	 */
	public void setGridColor(int color) {
		setGridInternalColor(color);
		setGridContourColor(color);
	}
	// =========================================================
	/**
	 * Set grid points stroke cap
	 * 
	 * @param cap Points stroke cap. See strokeCap() in {@link processing.core.PApplet} (either SQUARE, PROJECT, or ROUND)
	 */
	public void setPointCap(int cap) {
		point_cap = cap;
		for (int i = 0; i < npoints; i++) {
			points[i].setCap(point_cap);
		}
	}

	/**
	 * Set stroke cap of one point
	 * 
	 * @param point Point coordinates
	 * @param cap Stroke cap. See strokeCap() in {@link processing.core.PApplet} (either SQUARE, PROJECT, or ROUND)
	 */
	public void setPointCap(PVector point, int cap) {
		this.points[getIndexFromCoordinates(point)].setCap(cap);
	}
	// =========================================================
	/**
	 * Set grid points color
	 * 
	 * @param color Point color
	 */
	public void setPointColor(int color) {
		point_color = color;
		for (int i = 0; i < npoints; i++) {
			points[i].setColor(point_color);
		}
	}

	/**
	 * Set color of one point
	 * 
	 * @param point Point coordinates
	 * @param color Point color
	 */
	public void setPointColor(PVector point, int color) {
		this.points[getIndexFromCoordinates(point)].setColor(color);
	}

	/**
	 * Set grid points size
	 * 
	 * @param size Point size
	 */
	public void setPoinSize(float size) {
		point_size = size;
		for (int i = 0; i < npoints; i++) {
			points[i].setSize(point_size);
		}
	}

	/**
	 * Set size of one point
	 * 
	 * @param point Point coordinates
	 * @param size  Point size
	 */
	public void setPoinSize(PVector point, float size) {
		this.points[getIndexFromCoordinates(point)].setSize(size);
	}

	// =========================================================
	/**
	 * Get a list of all grid points
	 * 
	 * @return Grid points
	 */
	public PVector[] getPoints() {
		PVector p[] = new PVector[npoints];

		for (int i = 0; i < npoints; i++) {
			p[i] = points[i].getCenter().copy();
		}
		return p;
	}

	/**
	 * Get a list of all points from one column
	 * 
	 * @param col Column number (from 0 to npointx-1)
	 * 
	 * @return Columns points
	 */
	public PVector[] getPointsOfColumn(int col) {
		PVector p[] = new PVector[npointy];

		for (int i = 0; i < npointy; i++) {
			p[i] = getPosFromCoordinates(col, i).copy();
		}
		return p;
	}

	/**
	 * Get a list of all points from one row
	 * 
	 * @param row Row number (from 0 to npointy-1)
	 * 
	 * @return Row points
	 */
	public PVector[] getPointsOfRow(int row) {
		PVector p[] = new PVector[npointx];

		for (int i = 0; i < npointy; i++) {
			p[i] = getPosFromCoordinates(i, row).copy();
		}
		return p;
	}

	/**
	 * Get number of columns
	 * 
	 * @return Number of columns
	 */
	public int getNumCols() {
		return npointx;
	}

	/**
	 * Get number of rows
	 * 
	 * @return Number of rows
	 */
	public int getNumRows() {
		return npointy;
	}

	/**
	 * Get number of points
	 * 
	 * @return Number of points
	 */
	public int getNumOfPoints() {
		return npointx * npointy;
	}

	/**
	 * Get grid size, grid size is calculated using corners:
	 * 
	 * <pre>
	 * sizex = upper_right_corner.x - upper_left_corner.x
	 * sizey = bottom_right_corner.y - upper_left_corner.y
	 * </pre>
	 * 
	 * See {@link ktxo.art.processing.GridConstants#UPPER_LEFT_CORNER},
	 * {@link ktxo.art.processing.GridConstants#UPPER_RIGHT_CORNER},{@link ktxo.art.processing.GridConstants#BOTTOM_LEFT_CORNER},{@link ktxo.art.processing.GridConstants#BOTTOM_RIGHT_CORNER}
	 * 
	 * @return Grid size: [sizex, sizey]
	 */
	public float[] getSize() {
		float[] s = { sizex, sizey };
		return s;
	}

	/**
	 * Get max x-coordinate
	 * 
	 * @return Max x-coordinate
	 */
	public float getMaxX() {
		float max = 0;
		for (int i = 0; i < npointy; i++) {
			if (this.points[getIndexFromCoordinates(npointx - 1, i)].getCenter().x > max) {
				max = this.points[getIndexFromCoordinates(npointx - 1, i)].getCenter().x;
			}
		}
		return max;
	}

	/**
	 * Get max y-coordinate
	 * 
	 * @return Max y-coordinate
	 */
	public float getMaxY() {
		float max = 0;
		for (int i = 0; i < npointx; i++) {
			if (this.points[getIndexFromCoordinates(i, npointy - 1)].getCenter().y > max) {
				max = this.points[getIndexFromCoordinates(i, npointy - 1)].getCenter().y;
			}
		}
		return max;
	}

	// =========================================================
	/**
	 * Show information from grid
	 * 
	 * @param includePoint List points coordinates
	 */
	public void dump(boolean includePoint) {
		StringBuffer sbf = new StringBuffer("position=");
		sbf.append("(" + getPosition().x + "," + getPosition().y + ")");
		sbf.append(" sizex/divx=" + sizex + "/" + divx + " sizey/divy=" + sizey + "/" + divy);
		sbf.append(" npointx=" + npointx);
		sbf.append(" npointy=" + npointy);
		sbf.append(" npoints=" + npoints);
		sbf.append(" corners=");
		for (PVector c : getCorners()) {
			sbf.append("(" + c.x + "," + c.y + ") ");
		}
		sbf.append(" maxXY=(" + getMaxX() + "," + getMaxY() + ") ");
		if (includePoint) {
			sbf.append("\n");
			for (int i = 0; i < this.points.length; i++) {
				sbf.append(i + " : (" + this.points[i].getCenter().x + "," + this.points[i].getCenter().y + ")\n");
			}
		}
		System.out.println(sbf.toString());
	}

	/**
	 * Get version of the Library.
	 * 
	 * @return String Library version
	 */
	public static String version() {
		return VERSION;
	}
}
