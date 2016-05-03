import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//GA��`�q�N���X
class kotai{
	//�������J�n����s�s
	private byte startCity = 00000000;
	//�s�s���Ƃ̗D�揇�ʁ@�D�揇�ʂ̍����s�s���ɑ�������B�@�D�揇�ʂ������ɂȂ�Ƃ��͓s�s�ԍ��̑������ɑ����B
	private ArrayList<Byte> cityPriority = new ArrayList<Byte>();
	//�]������
	private int record = 0;
	//�����N
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
	//�s�s�D�揇�ʁ@�����f�[�^�쐬 
	public void addCity(int count){
		for(int i = 0; i < count;i++){
			Random rnd = new Random();
			byte Priority = (byte)rnd.nextInt(256);
			//�����t��byte�^�͈̔͂�-128~127�̂���32�r�b�g����int�^�ɃL���X�g��A�r�b�g���Z�ɂ�艺��8�r�b�g�ȊO��0�ɁB
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

//�v���b�g�����s�s�p�N���X
class city{
	//�s�s�̍��W�Ǘ�
	private Point point;
	//���ʗp
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
	//�s�s����8�r�b�g�ŕ\���ł���ő�l255�܂łƂ���
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

	    //�E�B���h�E����
	    final JFrame frame = new JFrame();
	    frame.setResizable(false);
	    //�^�C�g���ݒ�
	    frame.setTitle("Salesman");
	    //�E�B���h�E�T�C�Y�ݒ�(�^�C�g����g���܂񂾃T�C�Y)
	    frame.setSize(670, 320);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	    //�R���e���g�y�C���̍쐬
	    JPanel cp = new JPanel();
	    //���C�A�E�g�͎蓮
	    cp.setLayout(null);
	    //�t���[���ɃR���e���g�y�C����o�^
	    frame.add(cp);
	    //�R���e���g�y�C���̓\��t���ʒu�E�傫����ݒ�
	    cp.setBounds(38,20,500,300);
	    
	    //�L�����o�X��z�u
	    cp.add(cvs);
	    cvs.setBackground(Color.white);
	    cvs.setBounds(0,0,500,300);
  	  
	    //�{�^���̔z�u
	    button1.setBounds(540, 170, 100, 40);
	    button2.setBounds(540, 220, 100, 40);
	    
	    cp.add(button1);
	    cp.add(button2);
	    
	    label1.setText("����:");
	    label1.setBounds(510,70,150,40);
	   
	    label2.setText("�ŗD�G��`�q:");
	    label2.setBounds(510,100,150,40);
	    
	    label3.setText("�i�����x:" + (300 - accelerate));
	    label3.setBounds(510,10,150,40);
	 
	    label4.setText("�ŏI�X�V:");
	    label4.setBounds(510,130,150,40);
	    
	    
	    cp.add(label1);
	    cp.add(label2);
	    cp.add(label3);
	    cp.add(label4);
		    
	    //�X���C�_�[�̐ݒu
	    slider.setBounds(500, 40, 150, 20);
	    cp.add(slider);    

	    //�E�B���h�E�\��
	    frame.setVisible(true);
	   
	    //�_�̃v���b�g
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
	    //�N���A
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
	    //���s
	    button2.addActionListener(
	    		new ActionListener(){
	    	        public void actionPerformed(ActionEvent event){
	    	        System.out.println("execution");
	    	        //�s�s�͂Q�ȏ�K�v
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
       	    label3.setText("�i�����x:" + (310 - accelerate));
            }
        });
	}
	//1���゠����20�̂̌̂𐶐�
	final static int KOTAISU = 20;
	//1���゠����10�΂̈�`�q�𓑑�
	final static int TOUTASU = 10;
	//public static void solve(){
	public void run(){
	//�̗p�z��
		ArrayList<kotai> kotaiBox = new ArrayList<kotai>();

		//==================�̂̐���==================//
		for(int i = 0; i < KOTAISU; i++){
			kotai kotai = new kotai();
			kotai.addCity(cityBox.size());
			kotai.setRndStart(cityBox.size());
			kotaiBox.add(kotai);
		}

		//�ˑR�ψ�
		int mutation = 50;
		//���㐔
		int sedai = 0;
		label1.setText("��[" + sedai + "]����-��`�q["+ "]");
		//���� 
		int maximumDistance = 9999999;
		//�ŏI�X�V
		int latestUpdate = 20;		
		//�L�^�X�V
		int recodeUpdate = 0;

		while(loopflg){
			//==================�̂̕]��==================//
			//������]�����ڂƂ��A�������Z����`�q�قǗD�ꂽ��`�q�Ƃ���B
			for(int i = 0; i < kotaiBox.size();i++){
				kotai kotai = kotaiBox.get(i);
				//�����J�n�ʒu
				int startCity = kotai.getStartCity() & 0xFF;
				//���݈ʒu
				int nowCity = startCity;
				
				ArrayList<Byte> priorityBox =  kotai.getCityPriority();
				//priorityBox�̔z��ԍ���city��tag�ƌ��т��Ă���B
				
				//priorityBox�̏��ʕt�����s��
				int[] rank = new int[priorityBox.size()];
				//������
				for(int j = 0; j < rank.length; j++){
					rank[j] = 1;
				}
				//�����N�t��
				for(int j = 1; j < rank.length;j++){
					for(int k = 0; k < j; k++){
						if ((priorityBox.get(k) & 0xFF) < (priorityBox.get(j) & 0xFF)){
							rank[k]++;
						}else if((priorityBox.get(k) & 0xFF) > (priorityBox.get(j) & 0xFF)){
							rank[j]++;
						}
					}
				}
				//���ʂ̍ő�l
				int maximum = 0;
				for(int j = 0; j < rank.length;j++){
					if(rank[j] >= maximum){
						maximum = rank[j];
					}
				}
				//�X�^�[�g�ʒu���ő�l+1�Ƃ��A�S�[���ʒu�ɂ���
				maximum++;
				rank[startCity] = maximum;
				   
				ArrayList<Point> currentPointBox = new ArrayList<Point>();
				ArrayList<Point> nextPointBox = new ArrayList<Point>();
				ArrayList<String> distanceBox = new ArrayList<String>();
				int count = 1;
				int totalDistance = 0;
				//���ʂ��Ƃɑ���
				for(int j = 1; j < maximum + 1;j++){
					for(int k = 0; k < rank.length;k++){
						if(rank[k] == j){
							//���̓s�s�̍��W���擾
							Point nextPoint  = cityBox.get(k).getPoint();
							//���݂̓s�s�̍��W���擾
							Point currentPoint = cityBox.get(nowCity).getPoint();
							
							//�������v��
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
				//�L�^�X�V
				if(maximumDistance > totalDistance){
					cvs.optionClear(cvs.getGraphics());
					cvs.drawline(currentPointBox, nextPointBox, distanceBox);
					maximumDistance = totalDistance;	
					recodeUpdate = sedai;
					latestUpdate = sedai;
					label2.setText("�ŗD�G��`�q:[" + maximumDistance + "]");
					label4.setText("�ŏI�X�V:��["+ recodeUpdate + "]����");
				}
				label1.setText("��[" + sedai + "]����-��`�q["+ i + "]");
				kotai.setRecord(totalDistance);
				//�|�[�Y�p
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
			//�ˑR�ψق̊������� 2% - 20%
			if(sedai > latestUpdate + 20){
				mutation--;
				if(mutation < 5){
					mutation = 5;
				}
				latestUpdate = sedai;
			}
			//==================�����N�t��==================//
			for(int i = 1; i < kotaiBox.size(); i++){
				for(int j = 0; j < i; j++){
					if(kotaiBox.get(i).getRecord() < kotaiBox.get(j).getRecord()){
						kotaiBox.get(j).setRank(kotaiBox.get(j).getRank()+1);
					}else{
						kotaiBox.get(i).setRank(kotaiBox.get(i).getRank()+1);
					}
				}
			}
			//==================��������===================//
		
			ArrayList<kotai> kotaiRemove = new ArrayList<kotai>();
			for(int i = 0; i < kotaiBox.size(); i++){
				if(kotaiBox.get(i).getRank() > TOUTASU){
					kotaiRemove.add(kotaiBox.get(i));
				}
			}
			for(int i = 0; i < kotaiRemove.size();i++){
				kotaiBox.remove(kotaiRemove.get(i));
			}					 
			//==================�����̐���===================//
			ArrayList<kotai> Kujibiki = new ArrayList<kotai>();
			for(int i = 0; i < kotaiBox.size(); i++){
				int Kuji = (KOTAISU/2) - kotaiBox.get(i).getRank() + 1;
				for(int j = 0; j < Kuji; j++){
					Kujibiki.add(kotaiBox.get(i));
				}
			}
		
			//==================�q���̍쐬===================//
		 	int count = 0;
			while(count < TOUTASU /2){
				//�����Őe�����߂�
				kotai oya1 = new kotai();
				kotai oya2 = new kotai();
				//�����N������
				oya1.setRank(0);
				oya2.setRank(0);
				
				//�e�������ɂȂ�Ȃ��悤�ɂ���		
				while(oya1.getRank() == oya2.getRank()){
					Random rnd1 = new Random();
					Random rnd2 = new Random();
					oya1 =	Kujibiki.get(rnd1.nextInt(Kujibiki.size()));
					oya2 =  Kujibiki.get(rnd2.nextInt(Kujibiki.size()));
				}
				
				//�I�΂ꂽ�e���m�Ŏq�������@��1��̌�����2�C�̎q�������܂��
				ArrayList<Byte>dna1 = oya1.getCityPriority();
				ArrayList<Byte>dna2 = oya2.getCityPriority();
				Byte startPoint1 = oya1.getStartCity();
				Byte startPoint2 = oya2.getStartCity();
				
				kotai kodomo1 = new kotai();
				kotai kodomo2 = new kotai();
			
				//�X�^�[�g�ʒu�̈�`�q�g�ݑւ�
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
			    //�e��`�q�̑g�ݍ��킹�Ŕ͈͊O�̒l���X�^�[�g�n�_�ƂȂ����ꍇ�A�J�n�n�_�������_���Ő����B
				if((byte)Integer.parseInt(startCity1,2) >= cityBox.size()){
				kodomo1.setRndStart(cityBox.size());	
				}
				if((byte)Integer.parseInt(startCity2,2) >= cityBox.size()){
					kodomo2.setRndStart(cityBox.size());	
				}
				    
				ArrayList<Byte>childDNA1 = new ArrayList<Byte>();
				ArrayList<Byte>childDNA2 = new ArrayList<Byte>();
				
				for(int i = 0; i < dna1.size();i++){
					//�D�揇�ʂ̈�`�q�g�ݑւ�
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
				
				//==================�ˑR�ψ�===================//
				//�f�t�H���g��2%�̊m���œˑR�ψق��s���悤�ɂ���B
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
				
			//�����N������
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
	//�������`��
		public void paint(int x1,int y1,int x2,int y2,String str){
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.str = str;
			isLine = false;
			repaint();
		}
	
		//����`��
		public void drawline(ArrayList<Point>currentPointBox,ArrayList<Point>nextPointBox,ArrayList<String>distanceBox){
			this.currentPointBox = currentPointBox;
			this.nextPointBox = nextPointBox;
			this.distanceBox = distanceBox;
			isLine = true;
			repaint();
		}
			
		
	public void paint(Graphics g){
		//�y���̐F��ς���
	    g.setColor(new Color(0,0,255));
	    //Graphics2D�ւ̃L���X�g
		Graphics2D g2 = (Graphics2D)g;
		BasicStroke stroke = new BasicStroke(10.0f); //���̑�����ύX
		if(isLine){
		 	stroke = new BasicStroke(5.0f); //���̑�����ύX
			g2.setStroke(stroke); //���̎�ނ�ݒ�
			for(int i = 0; i < currentPointBox.size();i++){
				//�y���̐F��ς���
			    g.setColor(new Color(0,255,0));
				g2.drawLine((int)currentPointBox.get(i).getX(),(int)currentPointBox.get(i).getY(),(int)nextPointBox.get(i).getX(),(int)nextPointBox.get(i).getY()); //�@g�@�̑���Ɂ@g2�@���g���_�ɒ���
				//�y���̐F��ς���
			    g.setColor(new Color(255,0,0));
				g2.drawString(distanceBox.get(i),((int)currentPointBox.get(i).getX() + (int)nextPointBox.get(i).getX())/2,((int)currentPointBox.get(i).getY() + (int)nextPointBox.get(i).getY())/2);	
			}
		}else{
			
			g2.setStroke(stroke); //���̎�ނ�ݒ�
			g2.drawLine(x1,y1,x2,y2); //�@g�@�̑���Ɂ@g2�@���g���_�ɒ���	
			if(str != ""){
				//������`���܂��B
				  g2.drawString(str,x1 + 5,y1 + 5);
				  str = "";
			}
		}
		
		if(!flg){
			clear(g);
			flg = true;
		}
		//�_��`�悷��
		//g.drawLine(x, y, x, y);
	}
	//�폜
	public void clear(Graphics g){
		g.clearRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
	}

	//�v���b�g�����_���c������ō폜
		public void optionClear(Graphics g){
			g.clearRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
			for(int i = 0; i < x1Box.size();i++){
				//�y���̐F��ς���
			    g.setColor(new Color(0,0,255));
			    //Graphics2D�ւ̃L���X�g
				Graphics2D g2 = (Graphics2D)g;
				BasicStroke stroke = new BasicStroke(10.0f); //���̑�����ύX
				
				g2.setStroke(stroke); //���̎�ނ�ݒ�
				g2.drawLine(x1Box.get(i),y1Box.get(i),x1Box.get(i),y1Box.get(i)); //�@g�@�̑���Ɂ@g2�@���g���_�ɒ���	
				//������`���܂��B
				g2.drawString(strBox.get(i),x1Box.get(i) + 5,y1Box.get(i) + 5);
				
			}
		
		}
	//update���\�b�h���������āC��ʃN���A��h��
	public void update(Graphics g){
		paint(g);
	}
}