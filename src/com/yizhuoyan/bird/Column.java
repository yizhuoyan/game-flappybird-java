package com.yizhuoyan.bird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Column {
	//图片缓存
	private BufferedImage column;
	//以上下两根柱子的中间作为原点定位
	private int x;
	private int y;
	private int width;
	private int height;
	
	//step3.4
	private Random random;
	
	//step4.4上下柱间的空间高度
	private int gap = 109;
	
	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param x 确定其出现的横坐标
	 * @throws IOException
	 */
	public Column(int x) throws IOException{
		column = ImageIO.read(getClass().getResource("column.png"));
		width = column.getWidth();
		height = column.getHeight();
		random = new Random();
		this.x = x;
		
		//step3.4
		//y = 240-350;//游戏高度的一半减柱子图片长度的一半，让上下柱子中间的空出现在正中间
		y = 140 + random.nextInt(140);
	}
	
	/**
	 * step3.1:柱子向右移动
	 */
	public void step(){
		x--;
		if(x <= -width){//-58,柱子图片的宽度
			x = 320;//step3.3
			//step3.4
			y = 140 + random.nextInt(140);
		}
	}
	
	/**
	 * 绘图方法，让柱子出现在界面上
	 * @param g
	 */
	public void paint(Graphics g){
		//step3.4
		g.drawImage(column, x-width/2, y-height/2, null);
	}
}
