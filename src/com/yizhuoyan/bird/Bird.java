package com.yizhuoyan.bird;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bird {
	private int x;
	private int y;
	private BufferedImage bird;
	
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

	//step4.1
	private BufferedImage[] birds;//存放多张小鸟图片
	private int index;//图片序号计数
	
	//step4.2
	private int g;//重力加速度
	private double t;//计算间隔时间（秒）
	private double v0;//初始速度（像素/秒）
	private double vt;//当前时刻速度
	private double s;//运动距离
	
	//step4.3
	private double angle;//鸟的飞行角度
	
	//step4.4
	private int size = 26;
	
	public Bird(int x , int y) throws IOException{
		this.x = x;
		this.y = y;
		birds = new BufferedImage[3];
		for(int i = 0;i < 3; i++){
			birds[i] = ImageIO.read(getClass().getResource(i + ".png"));
		}
		bird = birds[0];
		
		//step4.2
		this.g = 4; //重力加速度
		this.t = 0.25; //每次计算的间隔时间
		this.v0 = 20; //初始上抛速度
		BufferedImage img=null;
		
	}
	
	/**
	 * 绘制小鸟
	 * @param g
	 */
	public synchronized void paint(Graphics g){
		//g.drawImage(bird, x, y, null);
		
		//step4.3:旋转坐标系
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(angle, this.x, this.y);
		//以x,y 为中心绘制图片
		int x = this.x-bird.getWidth()/2;
		int y = this.y-bird.getHeight()/2;
		g.drawImage(bird, x, y, null);
		//旋转回来 
		g2d.rotate(-angle, this.x, this.y);
		
	}
	
	/**
	 * step4.1:改变小鸟姿态
	 * 
	 * step4.2
	 * 竖直上抛位移计算
	 * (1) 上抛速度计算 Vt=Vo-gt  
	 * (2) 上抛距离计算 S=Vot-1/2gt^2
	 */
	public void step(){
		//step4.2
		//Vt1 是本次
		double vt1 = vt;
		//计算垂直上抛运动, 经过时间t以后的速度, 
		double v = vt1 - g*t;
		//作为下次计算的初始速度
		vt = v;
		//计算垂直上抛运动的运行距离
		s = vt1*t - 0.5 * g * t * t;
		//将计算的距离 换算为 y坐标的变化
		y = y - (int)s;
		
		//step4.3
		//计算仰角
		angle = -Math.atan(s/15);//横坐标移动速度60像素/秒，t=0.25秒，60*0.25=15
		
		//更换小鸟图片
		index ++;
		bird = birds[index/8%3];//00000000,11111111,22222222,00000000...
		
		
	}
	
	/**
	 * step4.2
	 */
	public void flappy(){
		AudioPlayWave audioPlayWave = new AudioPlayWave("fei.wav");
		audioPlayWave.start();
		//每次小鸟上抛跳跃, 就是将小鸟在当前点重新以初始速度 V0 向上抛
		System.out.println();
		vt = v0;
	}
	
	//step4.4碰撞检测
	/** 判断鸟是否与柱子和地发生碰撞 */
	public boolean hit(Column column1, Column column2, Ground ground) {
		//碰到地面
		if(y-size/2 >= ground.getY() || y-size/2 <= 0){
			return true;
		}
		//碰到柱子
		return hit(column1) || hit(column2);
	}
	/** 检查当前鸟是否碰到柱子 */
	public boolean hit(Column col){
		//如果鸟碰到柱子: 鸟的中心点x坐标在 柱子宽度 + 鸟的一半
		if( x >col.getX()-col.getWidth()/2-size/2 && x<col.getX()+col.getWidth()/2+size/2){
			if(y>col.getY()-col.getGap()/2+size/2  && y<col.getY()+col.getGap()/2-size/2 ){
			
				return false;
				
			}
			return true;
		}
		return false;
	}
	
	//step5
	/** 判断鸟是否通过柱子 */
	public boolean pass(Column col1, Column col2) {
	
		return col1.getX()==x || col2.getX()==x;
		
	}
}
