package utils;

public class Tuple<T1 extends Object, T2 extends Object> {

	T1 left;
	T2 right;
	
	public Tuple(T1 left, T2 right) {
		super();
		this.left = left;
		this.right = right;
	}

	public T1 getLeft() {
		return left;
	}

	public void setLeft(T1 left) {
		this.left = left;
	}

	public T2 getRight() {
		return right;
	}

	public void setRight(T2 right) {
		this.right = right;
	}
	
	
	
}
