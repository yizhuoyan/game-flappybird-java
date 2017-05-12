package com.chinasofti.bird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 下方滚动条（ground.png）
 * @author 映
 *
 */
public class Ground {
	private BufferedImage ground;
	private int x;//横坐标
	private int y;//纵坐标，标示了ground.png的高度，设置为400
	private int width;//ground.png图片宽度
	private int height;//ground.png图片高度
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Ground(int y) throws IOException{
		//定下滚动条图片的初始位置
		this.y = y;
		x = 0;
		ground = ImageIO.read(this.getClass().getResource("ground.png"));
		width = ground.getWidth();//获得宽度 497
		height = ground.getHeight();//获得高度 80	
	}
	
	public void step(){
		x--;
		//step2.1:不能让图片一直往右滚动
		if(x <= -(width - 360)){//360为大于320的一个数
			x = 0;
		}
	}
	
	//绘制滚动条
	public void paint(Graphics g) {
		g.drawImage(ground, x, y, null);//0,400		
	}
	
	public static void main(String[] args) throws IOException {
		new Ground(400);
	}
}
