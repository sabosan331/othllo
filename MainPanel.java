import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;

public class MainPanel extends JPanel implements ActionListener, MouseListener, Observer{
	
    static final int SIZE = 50;
	static final int W = SIZE * 8;
	static final int H = SIZE * 8;
	static final int WIND_W = 600;/*ウィンドウの幅*/   
	static final int WIND_H = 1000;/*ウィンドウの高さ*/

	
    //---------------------タイトル画面で使用------------------------------
	static final int WSIZE = 600;
	static final int HSIZE = 1000;	
    static final int set_W = 150;
    static final int set_H = 50;
    JButton start1;
    JButton start2;
    Font titlefont = new Font("Dialog", Font.BOLD, 100);
    Font buttonfont = new Font("MonoSpaced", Font.PLAIN, 25);
	int endflag=0;
	File file = new File("./history.csv");
    //---------------------------------------------------------------------
    
    //--------------------------設定画面で使用------------------------------
    JButton setend;
	JButton setplay1, setplay2, setplay3, setplay4, setplay5, setplay6;
	JButton setplay7, setplay8;
    Font setfont = new Font("MonoSpaced", Font.PLAIN, 50);
	Font powerfont = new Font("MonoSpaced", Font.PLAIN, 20);
	int playerdata1 = 1;//先手が人かCPUか
	int playerdata2 = 1;//後手が人かCPUか
	int cpupower = 1;//どちらかのCPUの強さ
    //----------------------------------------------------------------------

	//---------------------------ゲーム画面で使用----------------------------
	static final int O = 100;  /*盤面の左上の座標*/
	static final int SIZE_SMALL = 14;/*盤面のちっさい黒点*/ 
	static final int FONT_SIZE = 30;/*棋譜のフォントサイズ*/
	static final int FONT_BUTTON = 24;/*ボタンのフォントサイズ*/
	JButton button_return, button_hints, button_history, button_saving;

   	int[] x_saving = new int[100];/*直前のコマ保存用*/
	int[] y_saving = new int[100];/*直前のコマ保存用*/
	int index = 0;/*直前のコマ保存用*/
	int player[][];/*直前のPLAYERのコマ保存用*/
	int flag_hints = 0;/*ヒントボタンが押されたフラグ*/
	int flag_return = 0;/*待ったボタンのフラグ*/
	int th;/*turn保管用*/
	//----------------------------------------------------------------------
	
	//-------------------------結果画面で使用--------------------------------
	JButton end;
	JButton eresult;
	//----------------------------------------------------------------------

	//-------------------------画面切り替えで使用----------------------------
    int transition = 0;//画面切り替え
    int before_t = 0;  //前の画面を表すフラグ
	//----------------------------------------------------------------------
	
    GameState state = new GameState();
	RandomCPU cpu = new RandomCPU();
	RandomCPU user = new RandomCPU();
	Font font_game_record = new Font("Arial", Font.PLAIN, FONT_SIZE);
	JLabel label = new JLabel();


	
	public MainPanel(){
		setPreferredSize(new Dimension(WSIZE, HSIZE));
		
		//タイトル画面のボタン-----------------------------------
		start1 = new JButton("始めから");
		start2 = new JButton("続きから");	
		start1.setFont(buttonfont);
		start1.setBounds(100, 500, set_W, set_H);
		start2.setFont(buttonfont);
		start2.setBounds(350, 500, set_W, set_H);
		start1.addActionListener(this);
		start2.addActionListener(this);
		//------------------------------------------------------
		
		//設定画面のボタン---------------------------------------
		setplay1 = new JButton("PLAYER");	
		setplay1.setFont(buttonfont);
		setplay1.setBounds(120, 270, set_W, set_H);
		setplay2 = new JButton("COM(弱)");	
		setplay2.setFont(buttonfont);
		setplay2.setBounds(120, 345, set_W, set_H);
		setplay3 = new JButton("COM(普通)");	
		setplay3.setFont(buttonfont);
		setplay3.setBounds(100, 420, set_W+20, set_H);
		setplay4 = new JButton("COM(強)");	
		setplay4.setFont(buttonfont);
		setplay4.setBounds(120, 495, set_W, set_H);
		setplay5 = new JButton("PLAYER");	
		setplay5.setFont(buttonfont);
		setplay5.setBounds(320, 270, set_W, set_H);
		setplay6 = new JButton("COM(弱)");	
		setplay6.setFont(buttonfont);
		setplay6.setBounds(320, 345, set_W, set_H);
		setplay7 = new JButton("COM(普通)");	
		setplay7.setFont(buttonfont);
		setplay7.setBounds(320, 420, set_W+20, set_H);
		setplay8 = new JButton("COM(強)");	
		setplay8.setFont(buttonfont);
		setplay8.setBounds(320, 495, set_W, set_H);
		
		setend = new JButton("スタート");	
		setend.setFont(buttonfont);
		setend.setBounds(220, 700, set_W, set_H);
		
		setplay1.addActionListener(this);
		setplay2.addActionListener(this);
		setplay3.addActionListener(this);
		setplay4.addActionListener(this);
		setplay5.addActionListener(this);
		setplay6.addActionListener(this);
		setplay7.addActionListener(this);
		setplay8.addActionListener(this);
		setend.addActionListener(this);
		//------------------------------------------------------

		//----------------------------------ゲーム画面のボタン-------------
		//待ったボタン		
		button_return = new JButton("待った");
		button_return.setFont(new Font("Arial", Font.PLAIN, FONT_BUTTON));
		button_return.setBounds(100, 650, 180, SIZE);
		button_return.addActionListener(this);
		button_return.setActionCommand("待った");
		
		//ヒントボタン
		button_hints = new JButton("ヒント");
		button_hints.setFont(new Font("Arial", Font.PLAIN, FONT_BUTTON));
		button_hints.setBounds(320, 650, 180, SIZE);
		button_hints.addActionListener(this);
		button_hints.setActionCommand("ヒント");
		
		//履歴表示ボタン
		button_history = new JButton("履歴表示");
		button_history.setFont(new Font("Arial", Font.PLAIN, FONT_BUTTON));
		button_history.setBounds(100, 740, 400, SIZE);
		button_history.addActionListener(this);
		button_history.setActionCommand("履歴表示");
			
		//保存して終了ボタン
		button_saving = new JButton("保存して終了");
		button_saving.setFont(new Font("Arial", Font.PLAIN, FONT_BUTTON));
		button_saving.setBounds(100, 840, 400, SIZE);
		button_saving.addActionListener(this);
		button_saving.setActionCommand("保存して終了");
			



		//------------------------------------------------------------------
		
		//------------------------------------終了画面のボタン
		//		end = new JButton("タイトル");
		//		eresult = new JButton("履歴表示");
		//		end.setFont(buttonfont);
		//		end.setBounds(100, 500, set_W, set_H);	
		//		eresult.setFont(buttonfont);
		//		eresult.setBounds(350, 500, set_W, set_H);
		//		end.addActionListener(this);
		//		eresult.addActionListener(this);
		//---------------------------------------------------

		
		addMouseListener(this);
		state.addObserver(this);
	}
    
    public void paintComponent(Graphics g){
		
		
		if(transition==0){//タイトル画面
			if(before_t==2){
				remove(button_return);
				remove(button_hints);			
				remove(button_history);
				remove(button_saving);				
				before_t=0;
			}
			//背景
			g.setColor(Color.PINK);
			g.fillRect(0, 0, WSIZE, HSIZE);				
			//文字列
			g.setFont(titlefont);
			g.setColor(Color.BLACK);				
			g.drawString("Othello", 100, 200);
			//パネル初期化
			setLayout(null);
			
			add(start1);
			add(start2);		
		}
		
		
		else if(transition==1){//設定画面
			
			//前パネルのボタン消去
			if(before_t==0){
				remove(start1);remove(start2);
				before_t=1;
			}				
			//背景
			g.setColor(Color.PINK);
			g.fillRect(0, 0, WSIZE, HSIZE);
			g.setColor(Color.WHITE);
			g.fillRect(10, 200, WSIZE-20, 60);
			
			//文字列
			g.setFont(setfont);
			g.setColor(Color.RED);			
			g.drawString("VS", WSIZE/2-30, 250);
			g.setColor(Color.BLUE);						
			g.drawString("プレーヤー選択", WSIZE/4-30, 80);				
			g.setColor(Color.BLACK);
			g.drawString("先手", WSIZE/2-170, 180);
			g.drawString("後手", WSIZE/2+70, 180);
			g.setColor(Color.BLUE);
			g.setFont(powerfont);
			g.drawString("※COM対COMは選択不可", WSIZE/4+30, 600);
			
			//ボタン配置
			add(setend);				
			add(setplay1);add(setplay2);add(setplay3);add(setplay4);
			add(setplay5);add(setplay6);add(setplay7);add(setplay8);
			
			//強さ表示
			if(playerdata1 != 0||playerdata2 != 0){
				g.setColor(Color.BLACK);
				g.setFont(setfont);
				if(playerdata1==1)g.drawString("PLAYER", WSIZE/4-70, 250);
				else if(cpupower==1) g.drawString("COM(弱)", WSIZE/4-80, 250);
				else if(cpupower==2) g.drawString("COM(普通)", WSIZE/4-130, 250);
				else if(cpupower==3) g.drawString("COM(強)", WSIZE/4-80, 250);				
				if(playerdata2==1)  g.drawString("PLAYER", WSIZE/4+190, 250);				
				else if(cpupower==1) g.drawString("COM(弱)", WSIZE/4+190, 250);
				else if(cpupower==2) g.drawString("COM(普通)", WSIZE/4+190, 250);			  
				else if(cpupower==3) g.drawString("COM(強)", WSIZE/4+190, 250);				
			}				
		}
		
		
		else if(transition==2){//ゲーム画面			
			if(before_t==0){		
				remove(start1);remove(start2);
				before_t=2;
			}else if(before_t==1){
				remove(setend);
				remove(setplay1);remove(setplay2);remove(setplay3);remove(setplay4);
				remove(setplay5);remove(setplay6);remove(setplay7);remove(setplay8);			
				before_t=2;
			}
			
			//-------------------------------ゲーム画面デザイン---------

			//ウィンドウ
			g.setColor(Color.PINK);
			g.fillRect(0, 0, WIND_W, WIND_H);
			
			//盤面背景
			g.setColor(new Color(0,204, 102, 200));
			g.fillRect(O, O, W, H);
			
			for(int i=0; i<8; i++){
				for(int j=0; j<8; j++){
					if( (i+j)%2 == 0){
						g.setColor(new Color(0, 102, 0, 100));
						g.fillRect(O+SIZE*i, O+SIZE*j, SIZE, SIZE);
					}
				}
			}

			//盤面の線
			Graphics2D g2 = (Graphics2D)g;
			g.setColor(Color.BLACK);
			BasicStroke superwideStroke = new BasicStroke(8.0f);//太い線
			g2.setStroke(superwideStroke);
			g2.draw(new Rectangle.Double(100,100,400,400));//外枠
			BasicStroke wideStroke = new BasicStroke(1.5f);//細い線
			g2.setStroke(wideStroke);
			for(int i=0; i<9; i++){
				g.drawLine(O, O+i*SIZE, O+W, O+i*SIZE);
				g.drawLine(O+i*SIZE, O, O+i*SIZE, O+H);
			}
			g.fillOval(200-SIZE_SMALL/2, 200-SIZE_SMALL/2, SIZE_SMALL, SIZE_SMALL);
			g.fillOval(200-SIZE_SMALL/2, 400-SIZE_SMALL/2, SIZE_SMALL, SIZE_SMALL);
			g.fillOval(400-SIZE_SMALL/2, 200-SIZE_SMALL/2, SIZE_SMALL, SIZE_SMALL);
			g.fillOval(400-SIZE_SMALL/2, 400-SIZE_SMALL/2, SIZE_SMALL, SIZE_SMALL);
			
			//棋譜
			char alp = 'a';
			g.setFont(font_game_record);
			for(int i=1 ; i<9; i++){
				g.drawString(""+i, 70, 85+i*SIZE);
				g.drawString(""+alp++, 65+i*SIZE, 90);
			}
			
			
			//駒
			int best[] = user.decide(state, 2);//COM強さ選択
			
			if(flag_hints == 1){
				for(int y=0; y<8; y++){
					for(int x=0; x<8; x++){
						if(best[0] != -1){
							g.setColor(Color.YELLOW);
							g.fillOval(O+best[0]*SIZE, O+best[1]*SIZE, SIZE, SIZE);
						}
					}
				}
				flag_hints = 0;
			}
			
			for(int y=0; y<8; y++){
				for(int x=0; x<8; x++){
					//黒表示
					if(state.data[x][y] == 1){
						g.setColor(Color.BLACK);
						g.fillOval(O+x*SIZE, O+y*SIZE, SIZE, SIZE);
					}
					//白表示
					else if(state.data[x][y] == -1){
						g.setColor(Color.WHITE);
						g.fillOval(O+x*SIZE, O+y*SIZE, SIZE, SIZE);
					}
					//配置可能箇所表示
					else if(state.canReverse(x, y) == true){
						g.setColor(Color.RED);
						g.fillOval((O+20)+x*SIZE, (O+20)+y*SIZE, SIZE/5, SIZE/5);
					}
					//直近の駒表示
					if( x_saving[0] != 0){
						BasicStroke normalwideStroke = new BasicStroke(5.0f);//太い線
						g2.setStroke(normalwideStroke);
						for( int count=0; count<index; count++){
							if( count == index- 1){
								g.setColor(Color.BLUE);
								//	g.fillOval(O+x_saving[count]*SIZE, O+y_saving[count]*SIZE, SIZE*1/2, SIZE*1/2);
								g2.draw(new Rectangle.Double(O+x_saving[count]*SIZE, O+y_saving[count]*SIZE , SIZE, SIZE));
							}
						}
					}
					g2.setStroke(wideStroke);
				}
			}
			
			//対局状況
			g.setColor(Color.BLACK);
			g.drawString("" + state.black, 200, 590);
			g.drawString("" + state.white, 370, 590);
			g.fillOval(100, 540, SIZE*3/2, SIZE*3/2);
			g.setColor(Color.WHITE);
			g.fillOval(425, 540, SIZE*3/2, SIZE*3/2);
			
			add(button_return);
			add(button_hints);			
			add(button_history);
			add(button_saving);
			
			//データ表示
			g.setColor(Color.PINK);
			g.fillRect(0, 0, 600, 60);
			g.setColor(Color.BLACK);
			if(state.turn+1 < 61 ){
				if(state.player == 1)
					g.drawString((state.turn) + "手目　　黒の番", 180, 50);
				else
					g.drawString((state.turn) + "手目　　白の番", 180, 50);
			}else
				g.drawString("ゲーム終了", 230, 50);
		
			//------------------------------------------------------------------------------
			
	    }
		
    }
    
    public void update(Observable o, Object arg){
		repaint();
    }
    
    public void mousePressed(MouseEvent e){
		
		int x = e.getX() - O;
		int y = e.getY() - O;
		int flag_end = 0;//ゲーム状態管理
		
		x /= SIZE;
		y /= SIZE;
	
		if(transition==2){//ゲーム画面の時のみ使用する
			System.out.println("黒前：" + state.turn );
		
			if( state.put(x,y) == false ){
				System.out.println("そこには置けません");
			}
			//else{
			
			//駒を置く
			state.put(x,y);
			System.out.println("黒後：" + state.turn);
			//履歴書き込み
			state.writeHistory(true);
			
			x_saving[index] = x;
			y_saving[index] = y;
			index++;
			//終了判定
			if(state.checkEnd() == true){
					flag_end = 1;
					JOptionPane.showMessageDialog(this, "End!");
					th = state.turn;
					new ResultDialog();
					repaint();
			}
			
			//パスチェック
			else if( state.checkPass() == true ){
				state.player *= -1;
				if(state.player == 1)
					JOptionPane.showMessageDialog(this, "Pass! Next turn is black");
				else if(state.player ==-1)
					JOptionPane.showMessageDialog(this, "Pass! Next turn is white");
			}
			//	}
			
			//人VS人の場合はここを実行しない(COM動作)
			if(playerdata1 == 2 || playerdata2 == 2){
				//CPUのターン		   
				if(state.player == cpu.color){
					int action[] = cpu.decide(state, cpupower-1);//COM強さ選択

					x_saving[index] = action[0];
					y_saving[index] = action[1];
					index++;
					
					if(action[0] != -1){
						state.put(action[0], action[1]);
						//履歴書き込み
						state.writeHistory(true);
					}
					
					//終了判定
					if(flag_end == 1){//既にプレイヤーのターンで終了しているときは判定しない
						flag_end = 0;
					}
					else if(state.checkEnd() == true ){	
						JOptionPane.showMessageDialog(this, "End!");
						th = state.turn;
						new ResultDialog();
						repaint();
					}
					
					//パスチェック
					else if( state.checkPass() == true ){
						state.player *= -1;
						if(state.player == 1)
							JOptionPane.showMessageDialog(this, "Pass! Next turn is black");
						else if(state.player ==-1)
							JOptionPane.showMessageDialog(this, "Pass! Next turn is white");
					}
				}				
			}
		
		}//transition==2
	}//mousePressed
	
	
	
	public void mouseClicked(MouseEvent e){
		
    }
    public void mouseReleased(MouseEvent e){
		
    }
    public void mouseEntered(MouseEvent e){
	
    }
    public void mouseExited(MouseEvent e){
		
    }
    
    public void actionPerformed(ActionEvent e){//ボタン動作
		int i;
		String cmd = e.getActionCommand();


		//------------盤面処理と履歴表示--------------------------------
		if (cmd.equals("待った")){
			System.out.println("待った！");

			if(state.turn == 0){
				System.out.println("初期状態です、UNDOできません");
			}
			else{
				if( playerdata1 ==1 && playerdata2 == 1 ){//対人の場合はUNDO1
					state.undo(1);
					x_saving[index] = 0;
					y_saving[index] = 0;
					index -= 1;
				}
				//対COMの場合はUNDO2
				else{
					state.undo(2);
					x_saving[index] = 0;
					y_saving[index] = 0;
					index -= 2;
				}
				//indexが0以下にならないようにする
				if(index < 0)
					index = 0;
			}
		}
		if (cmd.equals("ヒント")){
			System.out.println("ヒント");
			flag_hints = 1;
			repaint();
		}
		if (cmd.equals("履歴表示")){
			System.out.println("履歴表示");
			th = state.turn;
			new HistoryDialog();
		}
		if (cmd.equals("保存して終了")){
			System.out.println("保存して終了します");
			Component c = (Component)e.getSource();
			Window w = SwingUtilities.getWindowAncestor(c);
			w.dispose();
			System.exit(0);
		}
		//-----------------------------------------------------------------


		//----------------------------------タイトルと設定画面------------------------
		if(e.getSource() == start1){
			transition=1;
			state.writeHistory(false);
			System.out.println("新しくゲームを始めます。");
		}//ボタン（始めから）
		else if(e.getSource() == start2){
			if (file.exists()) {				
				transition=2;
				state.readHistory();
				System.out.println("ゲームを再開します。");
			}
			else{
				System.out.println("前回のデータはありません。");
			}
		}//ボタン（続きから）
		else if(e.getSource() == setend){
			transition=2;

			if(playerdata1==2){
                //ゲームスタート初期値
                state.data[3][3] = -1;
                state.data[3][4] = 1;
                state.data[4][3] = 1;
                state.data[4][4] = 1;
                state.data[5][4] = 1;        
                state.turn = 1;
                state.player = -1;
                state.black = 4;
                state.white = 1;
                state.writeHistory(true);
                cpu.color = 1;
            }
		}//ボタン（スタート）
		else if(e.getSource() == setplay1){
			playerdata1=1;
		}//ボタン（プレイヤー）
		else if(e.getSource() == setplay2){
			if(playerdata2==1)
				{playerdata1=2; cpupower=1;}
		}//ボタン（COM（弱））
		else if(e.getSource() == setplay3){
			if(playerdata2==1)
				{playerdata1=2; cpupower=2;}
		}//ボタン（COM（普通））
		else if(e.getSource() == setplay4){
			if(playerdata2==1)
				{playerdata1=2; cpupower=3;}
		}//ボタン（COM（強））
		else if(e.getSource() == setplay5){
			playerdata2=1;
		}//ボタン（プレイヤー）
		else if(e.getSource() == setplay6){
			if(playerdata1==1)
				{playerdata2=2; cpupower=1;}
		}//ボタン（COM（弱））
		else if(e.getSource() == setplay7){
			if(playerdata1==1)
				{playerdata2=2; cpupower=2;}
		}//ボタン（COM（普通））
		else if(e.getSource() == setplay8){
			if(playerdata1==1)
				{playerdata2=2; cpupower=3;}
		}//ボタン（COM（強））
		//----------------------------------------------------------------
		
		/*		else if(e.getSource() == end){
			transition=0;
			}*/
		/*
		else if(e.getSource() ==eresult){
			transition=3;
			}*/
		
		repaint();
    }

	//-------------------------履歴表示クラス-----------------------------------------
	class HistoryDialog extends JDialog implements ActionListener{
		JButton pre = new JButton("前の手");
		JButton next = new JButton("次の手");
		JButton cancel = new JButton("ゲームに戻る");
		int nt = state.turn;//ゲームのターン保持
		int tmp_index = index;//ゲームの直近駒保持
		int tmp_x = x_saving[index];//ゲームの直近駒保持
		int tmp_y = y_saving[index];//ゲームの直近駒保持

		HistoryDialog(){
			setSize(300, 75);
			setLayout(new FlowLayout());
			setModal(true);
			JPanel panel1 = new JPanel();
			panel1.add(pre);
			panel1.add(next);
			panel1.add(cancel);			
			add(panel1);
			pre.addActionListener(this);
			next.addActionListener(this);
			cancel.addActionListener(this);
			setVisible(true);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == pre && th > 0){
				th -= 1;
				state.turn = th;

				index -= 1;
				
				if(index < 0)
					index = 0;

				state.checkHistory(th);
			}
			if(e.getSource() == next && th < nt){
				th += 1;
				state.turn = th;
				index += 1;
				state.checkHistory(th);
			}
			if(e.getSource() == cancel){
				System.out.println("ゲームに戻る");
				x_saving[index] = tmp_x;
				y_saving[index] = tmp_y;
				index = tmp_index;
				state.readHistory();
				setVisible(false);
			}
		}
	}
	//-------------------------------------------------------------------

		//-------------------------結果表示クラス-----------------------------------------
	class ResultDialog extends JDialog implements ActionListener{
		JButton pre = new JButton("前の手");
		JButton next = new JButton("次の手");
		JButton cancel = new JButton("タイトルに戻る");
		int nt = state.turn;
		int tmp_index = index;//ゲームの直近駒保持
		int tmp_x = x_saving[index];//ゲームの直近駒保持
		int tmp_y = y_saving[index];//ゲームの直近駒保持
		
		ResultDialog(){
			setSize(300, 75);
			setLayout(new FlowLayout());
			setModal(true);
			JPanel panel1 = new JPanel();
			panel1.add(pre);
			panel1.add(next);
			panel1.add(cancel);			
			add(panel1);
			pre.addActionListener(this);
			next.addActionListener(this);
			cancel.addActionListener(this);
			setVisible(true);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == pre && th > 0){
				th -= 1;
				state.turn = th;
				
				index -= 1;
			
				if(index < 0)
					index = 0;

				state.checkHistory(th);
			}
			if(e.getSource() == next && th < nt){
				th += 1;
				state.turn = th;
				index += 1;
				state.checkHistory(th);
			}
			if(e.getSource() == cancel){
				System.out.println("タイトルに戻る");
				transition=0;
				state.deleteHistory();
				setVisible(false);
				//init

				for(int i=0; i<100; i++){
					x_saving[i] = 0;
					y_saving[i] = 0;
				}
				
				index = 0;
				state.data = new int[8][8];
				state.data[3][3] = -1;
				state.data[3][4] = 1;
				state.data[4][3] = 1;
				state.data[4][4] = -1;				
				state.turn = 0;
				state.player = 1;
				state.black = 2;
				state.white = 2;
			}
			repaint();
		}
	}
	//-------------------------------------------------------------------
	
}
