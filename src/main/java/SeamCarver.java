import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

	private final Picture picture;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		checkForNull(picture);
		this.picture = new Picture(picture);
	}

	// current picture
	public Picture picture() {
		return picture;
	}

	// width of current picture
	public int width() {
		return picture.width();
	}

	// height of current picture
	public int height() {
		return picture.height();
	}

	private void checkRange(int x, int y) {
		if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) throw new IllegalArgumentException();
	}

	private void checkForNull(Object... parameters) {
		for (Object parameter : parameters) {
			if (parameter == null) throw new IllegalArgumentException();
		}
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		checkRange(x, y);
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {

	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {

	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		checkForNull(seam);
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		checkForNull(seam);

	}

	//  unit testing (optional)
//	public static void main(String[] args){
//
//	}

}