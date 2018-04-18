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
	
	public void replace(TElement replace) {
		this.data = replace.getWert();
		this.hoehe = replace.getHoehe();
		this.left = replace.getLeft();
		this.right = replace.getRight();
	}

	@Override
	public TElement clone() throws CloneNotSupportedException {
		return (TElement)super.clone();
	}
	
}
