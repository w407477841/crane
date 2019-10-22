package com.xywg.iot.netty.model;
	
/**
 *
 * 坐标bean
 *
 * @author hjy
 */
public class Coordinate {

	/**
	 * 横坐标
	 */
	private double x;

	/**
	 * 纵坐标
	 */
	private double y;
		
		
	@Override
	public String toString() {
		return "坐标为 [x=" + x + ", y=" + y + "]";
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

}
