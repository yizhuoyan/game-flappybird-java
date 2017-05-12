package com.yizhuoyan.bird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 * 初始世界，包含背景图片，开始图片，游戏结束图片
 * @author 映
 *
 */
public class World extends JPanel{
	private BufferedImage background;
	private BufferedImage startImg;
	
	//step2:增加了一个ground
	private Ground ground;
	
	//step3:增加两组柱子
	private Column column1;
	private Column column2;
	
	//step3:增加start标志位
	private boolean start;
	
	//step4:增加bird
	private Bird bird;
	
	//step4.4:添加游戏结束标志位
	private boolean gameover;
	private BufferedImage end;
	
	//step5:添加得分记录
	int score;
	
	/**
	 * 构造方法
	 * @throws IOException 
	 */
	public World() throws IOException{
		//加载三张图片
		background = ImageIO.read(this.getClass().getResource("bg.png"));
		startImg = ImageIO.read(getClass().getResource("start.png"));
		//step2:初始化ground
		init();
	}
	
	/**
	 * 初始化成员变量
	 * @throws IOException 
	 */
	private void init() throws IOException{
		//step2:初始化ground
		ground = new Ground(400);//设置滚动条图片的初始纵坐标为400
		//step3:初始化column1，column2
		column1 = new Column(320 + 100);//step3.3
		column2 = new Column(320 + 100 + 180);//step3.3
		
		//step4:初始化bird
		bird = new Bird(140,225);
		
		//step4.4
		start = false;
		gameover = false;
		
		//step5
		score = 0;
	} 
	
	/**
	 * 开始动作
	 * @throws InterruptedException 
	 */
	public void action() throws InterruptedException{
		
		//step3:添加监听事件
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				
				//step4.4:如果游戏结束，重新初始化
				if(gameover){
					try {
						init();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return;
				}
				start = true;
				
				//step4.2:每点击一次鼠标，小鸟当前速度vt重新变为v0 20
				bird.flappy();
			}
			
		});
		
		//step2:循环重绘图形
		while(true){
			//step3.1：start=true才移动柱子
			if(start){
				column1.step();
				column2.step();
				//step4.1小鸟更换图片
				bird.step();
				
				//step4.4:如果碰撞，改变标志位状态
				if(bird.hit(column1,column2,ground)){
					AudioPlayWave audioPlayWave = new AudioPlayWave("de.wav");
					audioPlayWave.start();
					start = false;
					gameover = true;
				}
				
				//step5:判断得分
				if(bird.pass(column1, column2)){
					score++;
				}
			}
			ground.step();//ground的横坐标--
			repaint();
			Thread.sleep(1000/60);//每隔1/60秒重绘一次
		}
	}
	
	/**
	 * 重写父类paint方法，重绘图形
	 */
	@Override
	public void paint(Graphics g) {
		//绘制背景图片
		g.drawImage(background, 0, 0, null);
		//绘制滚动条
		ground.paint(g);
		
		//step3:绘制柱子,只有start=false的情况下才绘制开始画面
		column1.paint(g);
		column2.paint(g);
		
		//step4:绘制小鸟
		bird.paint(g);
		
		//step5:绘制分数
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 30);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString("得分：" + score , 30, 50);
		
		//step4.4:如果结束，绘制结束画面
		if(gameover){
			try {
				end = ImageIO.read(getClass().getResource("gameover.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(end,0,0,null);
			return;
		}
		
		if (!start) {
			//绘制开始图片
			g.drawImage(startImg, 0, 0, null);
		}
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame frame = new JFrame("FlappyBird");
		World world = new World();
		frame.add(world);
		frame.setSize(320, 480);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);//设置位置
		frame.setVisible(true);
		world.action();
		
	}
}
