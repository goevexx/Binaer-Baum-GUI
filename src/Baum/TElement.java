package Baum;

public class TElement {
	private int data;
	private int hoehe;
	private TElement left;
	private TElement right;

	public TElement() {
		super();
		this.hoehe = 0;
	}

	public TElement(int hoehe) {
		super();
		this.hoehe = hoehe;
	}

	public int getHoehe() {
		return hoehe;
	}

	public void setHoehe(int hoehe) {
		this.hoehe = hoehe;
	}

	public int getWert() {
		return data;
	}

	public void setWert(int wert) {
		this.data = wert;
	}

	public TElement getLeft() {
		return left;
	}

	public void setLeft(TElement left) {
		this.left = left;
	}

	public TElement getRight() {
		return right;
	}

	public void setRight(TElement right) {
		this.right = right;
	}

	
}
