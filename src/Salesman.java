import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//GA遺伝子クラス
class kotai{
	//走査を開始する都市
	private byte startCity = 00000000;
	//都市ごとの優先順位　優先順位の高い都市順に走査する。　優先順位が同じになるときは都市番号の早い順に走査。
	private ArrayList<Byte> cityPriority = new ArrayList<Byte>();
	//評価項目
	private int record = 0;
	//ランク
	private int rank = 1;
	
	public byte getStartCity() {
		return startCity;
	}
	public void setStartCity(byte startCity) {
		this.startCity = startCity;
	}
	public void setRndStart(int cityCount){
		Random rnd = new Random();
		startCity = (byte)rnd.nextInt(cityCount);
	}
	public ArrayList<Byte> getCityPriority() {
		return cityPriority;
	}
	public void setCityPriority(ArrayList<Byte> cityPriority) {
		this.cityPriority = cityPriority;
	}
	//都市優先順位　初期データ作成 
	public void addCity(int count){
		for(int i = 0; i < count;i++){
			Random rnd = new Random();
			byte Priority = (byte)rnd.nextInt(256);
			//符号付きbyte型の範囲は-128~127のため32ビット長のint型にキャスト後、ビット演算により下位8ビット以外を0に。
			int intValue = Priority & 0xFF;
		    String binaryNumber = Integer.toBinaryString(intValue);			    
		    System.out.println(intValue + "[" + binaryNumber + "]");
			cityPriority.add(Priority);
		}
	}
	public int getRecord() {
		return record;
	}
	public void setRecord(int record) {
		this.record = record;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}

//プロットした都市用クラス
class city{
	//都市の座標管理
	private Point point;
	//判別用
	private int tag;
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
}

public class Salesman extends Thread{
	//都市数を8ビットで表現できる最大値255個までとする
	static final int MAX_CITY = 256; 
	static int count = 0;
	static ArrayList<city> cityBox = new ArrayList<city>();
	static final plotCanvas cvs = new plotCanvas();
    static Boolean flg = true;
    static Boolean loopflg = true;
    static Boolean pause = false;
    static Boolean firstClick = true;
	static  JLabel label1 = new JLabel();
    static  JLabel label2 = new JLabel();
    static  JLabel label3 = new JLabel();   
    static  JLabel label4 = new JLabel();   
    static JButton button1 = new JButton("DELETE");
    static JButton button2 = new JButton("EXECUTION");
    static JSlider slider = new JSlider(10,300,100);
	static int accelerate = 100;
	public static void main(String[]args){

	    //ウィンドウ生成
	    final JFrame frame = new JFrame();
	    frame.setResizable(false);
	    //タイトル設定
	    frame.setTitle("Salesman");
	    //ウィンドウサイズ設定(タイトルや枠も含んだサイズ)
	    frame.setSize(670, 320);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	    //コンテントペインの作成
	    JPanel cp = new JPanel();
	    //レイアウトは手動
	    cp.setLayout(null);
	    //フレームにコンテントペインを登録
	    frame.add(cp);
	    //コンテントペインの貼り付け位置・大きさを設定
	    cp.setBounds(38,20,500,300);
	    
	    //キャンバスを配置
	    cp.add(cvs);
	    cvs.setBackground(Color.white);
	    cvs.setBounds(0,0,500,300);
  	  
	    //ボタンの配置
	    button1.setBounds(540, 170, 100, 40);
	    button2.setBounds(540, 220, 100, 40);
	    
	    cp.add(button1);
	    cp.add(button2);
	    
	    label1.setText("世代:");
	    label1.setBounds(510,70,150,40);
	   
	    label2.setText("最優秀遺伝子:");
	    label2.setBounds(510,100,150,40);
	    
	    label3.setText("進化速度:" + (300 - accelerate));
	    label3.setBounds(510,10,150,40);
	 
	    label4.setText("最終更新:");
	    label4.setBounds(510,130,150,40);
	    
	    
	    cp.add(label1);
	    cp.add(label2);
	    cp.add(label3);
	    cp.add(label4);
		    
	    //スライダーの設置
	    slider.setBounds(500, 40, 150, 20);
	    cp.add(slider);    

	    //ウィンドウ表示
	    frame.setVisible(true);
	   
	    //点のプロット
	    cvs.addMouseListener(
	    	      new MouseAdapter(){
	    	        public void mouseClicked(MouseEvent  event){
	    	        if(!firstClick){
	    	        	return;
	    	        }
	    	        	System.out.println("(" + event.getX() +","+ event.getY() + ")");
	    	        	count++;
	    	        	if(count > MAX_CITY){
	    	        	 	 JLabel label = new JLabel("");
		    	        	 JOptionPane.showMessageDialog(label, "The default maximum number of cities is 256", "alert", 
		    	        			  JOptionPane.ERROR_MESSAGE);
	    	        		return;
	    	        	}
	    	        	
	    	        	cvs.paint(event.getX(), event.getY(),String.valueOf(count));
	    	        	city city = new city();
	    	        	Point point = new Point(event.getX(), event.getY());
	    	        	city.setPoint(point);
	    	        	city.setTag(count - 1);
	    	        	cityBox.add(city);
	    	        }
	    	      }
	    		);
	    //クリア
	    button1.addActionListener(
	    	      new ActionListener(){
	    	        public void actionPerformed(ActionEvent event){
	    	        	if(!flg){
	    	        		return;
	    	        	}
	    	        	loopflg = false;
	    	        	pause = false;
	    	        	flg = true;
	    	        	firstClick = true;
	    	           System.out.println("delete");
		    	        label1.setText("");
		    		    label2.setText("");
		    		    count = 0;
		    	        cvs.clear(cvs.getGraphics());
		    	     	cvs.init();
				    	cityBox = new ArrayList<city>();
	    	        }
	    	      }
	    		);
	    //実行
	    button2.addActionListener(
	    		new ActionListener(){
	    	        public void actionPerformed(ActionEvent event){
	    	        System.out.println("execution");
	    	        //都市は２つ以上必要
	    	        if(cityBox.size() < 2){
	    	        	 JLabel label = new JLabel("");
	    	        	 JOptionPane.showMessageDialog(label, "You need at least two citys.", "alert", 
	    	        			  JOptionPane.ERROR_MESSAGE);
	    	        	return;
	    	        }
	    	     //   solve();
	    	      Salesman salesman = new Salesman();

	    	        if(flg){
	    	        	flg = false;
	    	        	pause = false;
	    	        	button2.setText("PAUSE");
	    	        	if(firstClick){
	    	        		salesman.start();
	    	        		loopflg =true;
	    	        		firstClick = false;
	    	        	}
	    	        }else{
	    	        	button2.setText("EXECUTION");
	    	        	pause = true;
	    	        	flg = true;
	    	        }
	    	      }
	    		}
	    	);
	    slider.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
               accelerate = slider.getValue();
       	    label3.setText("進化速度:" + (310 - accelerate));
            }
        });
	}
	//1世代あたり20体の個体を生成
	final static int KOTAISU = 20;
	//1世代あたり10対の遺伝子を淘汰
	final static int TOUTASU = 10;
	//public static void solve(){
	public void run(){
	//個体用配列
		ArrayList<kotai> kotaiBox = new ArrayList<kotai>();

		//==================個体の生成==================//
		for(int i = 0; i < KOTAISU; i++){
			kotai kotai = new kotai();
			kotai.addCity(cityBox.size());
			kotai.setRndStart(cityBox.size());
			kotaiBox.add(kotai);
		}

		//突然変異
		int mutation = 50;
		//世代数
		int sedai = 0;
		label1.setText("第[" + sedai + "]世代-遺伝子["+ "]");
		//距離 
		int maximumDistance = 9999999;
		//最終更新
		int latestUpdate = 20;		
		//記録更新
		int recodeUpdate = 0;

		while(loopflg){
			//==================個体の評価==================//
			//距離を評価項目とし、距離が短い遺伝子ほど優れた遺伝子とする。
			for(int i = 0; i < kotaiBox.size();i++){
				kotai kotai = kotaiBox.get(i);
				//走査開始位置
				int startCity = kotai.getStartCity() & 0xFF;
				//現在位置
				int nowCity = startCity;
				
				ArrayList<Byte> priorityBox =  kotai.getCityPriority();
				//priorityBoxの配列番号がcityのtagと結びついている。
				
				//priorityBoxの順位付けを行う
				int[] rank = new int[priorityBox.size()];
				//初期化
				for(int j = 0; j < rank.length; j++){
					rank[j] = 1;
				}
				//ランク付け
				for(int j = 1; j < rank.length;j++){
					for(int k = 0; k < j; k++){
						if ((priorityBox.get(k) & 0xFF) < (priorityBox.get(j) & 0xFF)){
							rank[k]++;
						}else if((priorityBox.get(k) & 0xFF) > (priorityBox.get(j) & 0xFF)){
							rank[j]++;
						}
					}
				}
				//順位の最大値
				int maximum = 0;
				for(int j = 0; j < rank.length;j++){
					if(rank[j] >= maximum){
						maximum = rank[j];
					}
				}
				//スタート位置を最大値+1とし、ゴール位置にする
				maximum++;
				rank[startCity] = maximum;
				   
				ArrayList<Point> currentPointBox = new ArrayList<Point>();
				ArrayList<Point> nextPointBox = new ArrayList<Point>();
				ArrayList<String> distanceBox = new ArrayList<String>();
				int count = 1;
				int totalDistance = 0;
				//順位ごとに走査
				for(int j = 1; j < maximum + 1;j++){
					for(int k = 0; k < rank.length;k++){
						if(rank[k] == j){
							//次の都市の座標を取得
							Point nextPoint  = cityBox.get(k).getPoint();
							//現在の都市の座標を取得
							Point currentPoint = cityBox.get(nowCity).getPoint();
							
							//距離を計測
							int distance = (int) Math.sqrt(Math.pow((nextPoint.getX() - currentPoint.getX()), 2) + Math.pow((nextPoint.getY() - currentPoint.getY()), 2));
								totalDistance += distance;
								
								 currentPointBox.add(currentPoint);
								 nextPointBox.add(nextPoint);
								 distanceBox.add("No." + count + "[" + String.valueOf(distance) + "]");
								 
								 count++;
								 nowCity = k;
							}
						}
					}
				//記録更新
				if(maximumDistance > totalDistance){
					cvs.optionClear(cvs.getGraphics());
					cvs.drawline(currentPointBox, nextPointBox, distanceBox);
					maximumDistance = totalDistance;	
					recodeUpdate = sedai;
					latestUpdate = sedai;
					label2.setText("最優秀遺伝子:[" + maximumDistance + "]");
					label4.setText("最終更新:第["+ recodeUpdate + "]世代");
				}
				label1.setText("第[" + sedai + "]世代-遺伝子["+ i + "]");
				kotai.setRecord(totalDistance);
				//ポーズ用
				while(pause){
					try{
					Thread.sleep(100);
					}catch(InterruptedException e){					
					}
				}
				
				try{
				Thread.sleep(accelerate);
				}catch(InterruptedException e){
				}
			}
			//突然変異の割合調整 2% - 20%
			if(sedai > latestUpdate + 20){
				mutation--;
				if(mutation < 5){
					mutation = 5;
				}
				latestUpdate = sedai;
			}
			//==================ランク付け==================//
			for(int i = 1; i < kotaiBox.size(); i++){
				for(int j = 0; j < i; j++){
					if(kotaiBox.get(i).getRecord() < kotaiBox.get(j).getRecord()){
						kotaiBox.get(j).setRank(kotaiBox.get(j).getRank()+1);
					}else{
						kotaiBox.get(i).setRank(kotaiBox.get(i).getRank()+1);
					}
				}
			}
			//==================淘汰する===================//
		
			ArrayList<kotai> kotaiRemove = new ArrayList<kotai>();
			for(int i = 0; i < kotaiBox.size(); i++){
				if(kotaiBox.get(i).getRank() > TOUTASU){
					kotaiRemove.add(kotaiBox.get(i));
				}
			}
			for(int i = 0; i < kotaiRemove.size();i++){
				kotaiBox.remove(kotaiRemove.get(i));
			}					 
			//==================くじの生成===================//
			ArrayList<kotai> Kujibiki = new ArrayList<kotai>();
			for(int i = 0; i < kotaiBox.size(); i++){
				int Kuji = (KOTAISU/2) - kotaiBox.get(i).getRank() + 1;
				for(int j = 0; j < Kuji; j++){
					Kujibiki.add(kotaiBox.get(i));
				}
			}
		
			//==================子供の作成===================//
		 	int count = 0;
			while(count < TOUTASU /2){
				//乱数で親を決める
				kotai oya1 = new kotai();
				kotai oya2 = new kotai();
				//ランク初期化
				oya1.setRank(0);
				oya2.setRank(0);
				
				//親が同じにならないようにする		
				while(oya1.getRank() == oya2.getRank()){
					Random rnd1 = new Random();
					Random rnd2 = new Random();
					oya1 =	Kujibiki.get(rnd1.nextInt(Kujibiki.size()));
					oya2 =  Kujibiki.get(rnd2.nextInt(Kujibiki.size()));
				}
				
				//選ばれた親同士で子供を作る　※1回の結婚で2匹の子供が生まれる
				ArrayList<Byte>dna1 = oya1.getCityPriority();
				ArrayList<Byte>dna2 = oya2.getCityPriority();
				Byte startPoint1 = oya1.getStartCity();
				Byte startPoint2 = oya2.getStartCity();
				
				kotai kodomo1 = new kotai();
				kotai kodomo2 = new kotai();
			
				//スタート位置の遺伝子組み替え
				int intValue1 = startPoint1 & 0xFF;
				int intValue2 = startPoint2 & 0xFF;
			
				String startCity1 = new String();
				String startCity2 = new String();
				
				for(int i = 0; i < 8; i ++ ){
					Random rnd1 = new Random();
					if(rnd1.nextInt(2) == 0){
					startCity1 +=  (byte) ((intValue1 >> i) & 0x01);
					startCity2 +=  (byte) ((intValue2 >> i) & 0x01);
					}else{
					startCity2 +=  (byte) ((intValue1 >> i) & 0x01);
					startCity1 +=  (byte) ((intValue2 >> i) & 0x01);
					}
				}
				StringBuffer sb1 = new StringBuffer(startCity1);
			    startCity1 = sb1.reverse().toString();
			    StringBuffer sb2 = new StringBuffer(startCity2);
			    startCity2 = sb2.reverse().toString();
				kodomo1.setStartCity((byte)Integer.parseInt(startCity1,2));
				kodomo2.setStartCity((byte)Integer.parseInt(startCity2,2));
			    //親遺伝子の組み合わせで範囲外の値がスタート地点となった場合、開始地点をランダムで生成。
				if((byte)Integer.parseInt(startCity1,2) >= cityBox.size()){
				kodomo1.setRndStart(cityBox.size());	
				}
				if((byte)Integer.parseInt(startCity2,2) >= cityBox.size()){
					kodomo2.setRndStart(cityBox.size());	
				}
				    
				ArrayList<Byte>childDNA1 = new ArrayList<Byte>();
				ArrayList<Byte>childDNA2 = new ArrayList<Byte>();
				
				for(int i = 0; i < dna1.size();i++){
					//優先順位の遺伝子組み替え
					Byte priority1_gene = dna1.get(i);
					Byte priority2_gene = dna2.get(i);
					
					int Value1 = priority1_gene & 0xFF;
					int Value2 = priority2_gene & 0xFF;
				
					String priority1 = new String();
					String priority2 = new String();
				
					for(int j = 0; j < 8; j ++ ){
						Random rnd1 = new Random();
						if(rnd1.nextInt(2) == 0){
							priority1 +=  (byte) ((Value1 >> j) & 0x01);
							priority2 +=  (byte) ((Value2 >> j) & 0x01);
						}else{
							priority2 +=  (byte) ((Value1 >> j) & 0x01);
							priority1 +=  (byte) ((Value2 >> j) & 0x01);
						}
					}
					StringBuffer stringbf1 = new StringBuffer(priority1);
					priority1 = stringbf1.reverse().toString();
				    StringBuffer stringbf2 = new StringBuffer(priority2);
				    priority2 = stringbf2.reverse().toString();
				        
					childDNA1.add((byte)Integer.parseInt(priority1,2));
					childDNA2.add((byte)Integer.parseInt(priority2,2));
		
				}
				kodomo1.setCityPriority(childDNA1);
				kodomo2.setCityPriority(childDNA2);
				
				//==================突然変異===================//
				//デフォルトで2%の確率で突然変異を行うようにする。
				for(int i = 0; i < childDNA1.size();i++){
					Random rnd1 = new Random();
					Random rnd2 = new Random();	
					if(rnd1.nextInt(100) % mutation == 0){
						Byte priority1_gene = childDNA1.get(i);
						Random random1 = new Random();
						int num1 = random1.nextInt(8);		
						switch (num1){
						case 0:
							priority1_gene = (byte) (priority1_gene | 0x01);
						case 1:
							priority1_gene = (byte) (priority1_gene | 0x02);
						case 2:
							priority1_gene = (byte) (priority1_gene | 0x04);
						case 3:
							priority1_gene = (byte) (priority1_gene | 0x08);
						case 4:
							priority1_gene = (byte) (priority1_gene | 0x10);
						case 5:
							priority1_gene = (byte) (priority1_gene | 0x20);
						case 6:
							priority1_gene = (byte) (priority1_gene | 0x40);
						case 7:
							priority1_gene = (byte) (priority1_gene | 0x80);
						}
					}
					if(rnd2.nextInt(100) % mutation == 0){
						Byte priority2_gene = childDNA2.get(i);
						Random random2 = new Random();
						int num2 = random2.nextInt(8);		
						switch (num2){
						case 0:
							priority2_gene = (byte) (priority2_gene | 0x01);
						case 1:
							priority2_gene = (byte) (priority2_gene | 0x02);
						case 2:
							priority2_gene = (byte) (priority2_gene | 0x04);
						case 3:
							priority2_gene = (byte) (priority2_gene | 0x08);
						case 4:
							priority2_gene = (byte) (priority2_gene | 0x10);
						case 5:
							priority2_gene = (byte) (priority2_gene | 0x20);
						case 6:
							priority2_gene = (byte) (priority2_gene | 0x40);
						case 7:
							priority2_gene = (byte) (priority2_gene | 0x80);
						}
					}
				}
				kotaiBox.add(kodomo1);
				kotaiBox.add(kodomo2);
			count++;
			}
				
			//ランク初期化
			for(int i = 0; i < kotaiBox.size();i++){
				kotaiBox.get(i).setRank(1);
				kotaiBox.get(i).setRecord(0);
			}		
		sedai++;
		}
	}	
}

class plotCanvas extends Canvas{
private static final long serialVersionUID = 1L;
	Thread thread = null;
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private String str = "";
	static Boolean flg = false;
	static Boolean isLine = false;
	static ArrayList<Point>currentPointBox = new ArrayList<Point>();
	static ArrayList<Point>nextPointBox = new ArrayList<Point>();
	static ArrayList<String>distanceBox = new ArrayList<String>();
	static ArrayList<Integer>x1Box = new ArrayList<Integer>();
	static ArrayList<Integer>y1Box = new ArrayList<Integer>();
	static ArrayList<String>strBox = new ArrayList<String>();
	
	public void init(){
		currentPointBox = new ArrayList<Point>();
		nextPointBox = new ArrayList<Point>();
		distanceBox = new ArrayList<String>();
		x1Box = new ArrayList<Integer>();
		y1Box = new ArrayList<Integer>();
		strBox = new ArrayList<String>();
	}
	public void paint(int x1,int y1,String str){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1;
		this.y2 = y1;
		this.str = str + "("+ x1  + ","+ y1 +")";
		x1Box.add(x1);
		y1Box.add(y1);
		strBox.add(this.str);
		isLine = false;
		repaint();
	}
	//文字列を描画
		public void paint(int x1,int y1,int x2,int y2,String str){
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.str = str;
			isLine = false;
			repaint();
		}
	
		//線を描画
		public void drawline(ArrayList<Point>currentPointBox,ArrayList<Point>nextPointBox,ArrayList<String>distanceBox){
			this.currentPointBox = currentPointBox;
			this.nextPointBox = nextPointBox;
			this.distanceBox = distanceBox;
			isLine = true;
			repaint();
		}
			
		
	public void paint(Graphics g){
		//ペンの色を変える
	    g.setColor(new Color(0,0,255));
	    //Graphics2Dへのキャスト
		Graphics2D g2 = (Graphics2D)g;
		BasicStroke stroke = new BasicStroke(10.0f); //線の太さを変更
		if(isLine){
		 	stroke = new BasicStroke(5.0f); //線の太さを変更
			g2.setStroke(stroke); //線の種類を設定
			for(int i = 0; i < currentPointBox.size();i++){
				//ペンの色を変える
			    g.setColor(new Color(0,255,0));
				g2.drawLine((int)currentPointBox.get(i).getX(),(int)currentPointBox.get(i).getY(),(int)nextPointBox.get(i).getX(),(int)nextPointBox.get(i).getY()); //　g　の代わりに　g2　を使う点に注意
				//ペンの色を変える
			    g.setColor(new Color(255,0,0));
				g2.drawString(distanceBox.get(i),((int)currentPointBox.get(i).getX() + (int)nextPointBox.get(i).getX())/2,((int)currentPointBox.get(i).getY() + (int)nextPointBox.get(i).getY())/2);	
			}
		}else{
			
			g2.setStroke(stroke); //線の種類を設定
			g2.drawLine(x1,y1,x2,y2); //　g　の代わりに　g2　を使う点に注意	
			if(str != ""){
				//文字を描きます。
				  g2.drawString(str,x1 + 5,y1 + 5);
				  str = "";
			}
		}
		
		if(!flg){
			clear(g);
			flg = true;
		}
		//点を描画する
		//g.drawLine(x, y, x, y);
	}
	//削除
	public void clear(Graphics g){
		g.clearRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
	}

	//プロットした点を残した上で削除
		public void optionClear(Graphics g){
			g.clearRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
			for(int i = 0; i < x1Box.size();i++){
				//ペンの色を変える
			    g.setColor(new Color(0,0,255));
			    //Graphics2Dへのキャスト
				Graphics2D g2 = (Graphics2D)g;
				BasicStroke stroke = new BasicStroke(10.0f); //線の太さを変更
				
				g2.setStroke(stroke); //線の種類を設定
				g2.drawLine(x1Box.get(i),y1Box.get(i),x1Box.get(i),y1Box.get(i)); //　g　の代わりに　g2　を使う点に注意	
				//文字を描きます。
				g2.drawString(strBox.get(i),x1Box.get(i) + 5,y1Box.get(i) + 5);
				
			}
		
		}
	//updateメソッドを乗っ取って，画面クリアを防ぐ
	public void update(Graphics g){
		paint(g);
	}
}