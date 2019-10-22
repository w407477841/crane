package com.xywg.iot.netty.model;

/**
 *
 * 封装的圆bean
 *
 * @author hjy
 */
public class Round {

	/**
	 *  圆心横坐标
	 */
	private double x;
	/**
	 * 圆心纵坐标
	 */
	private double y;
	/**
	 * 半径
	 */
	private double r;
	
	public Round(double x, double y, double r) {
		super();
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getR() {
		return r;
	}
	
	public void setR(double r) {
		this.r = r;
	}
	
}
