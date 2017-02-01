import java.util.*;

class Rev_Status{
	// 開放度判定
	public int[] pos ={-1,-1}; // 最善手	
	public int openval = 0; // 配置
	
	public Rev_Status(){

		int[] tmp = {-1,-1};
		pos = tmp;
		openval = -1;
	}
}

public class RandomCPU {

	int color;	//BLACK or WHITE
	Opening open = new Opening();

	  int  WT1[][]=        //OSERO ウエイトテーブル
    { { 30 ,-12, 0,  -1,   -1, 0,-12,30 },
      {-12,-15, -3, -3,  -3, -3,-15,-12 },
      {  0 , -3,  0,  -1,   -1,  0, -3, 0 },
      { -1, -3, -1,  -1,   -1, -1, -3,  -1 },
      { -1, -3, -1,  -1,   -1, -1, -3,  -1 },
      {  0, -3,  0, -1,  -1,  0, -3, 0 },
      {-12,-15, -3, -3,  -3, -3,-15,-12 },
      { 30,-12, 0,  -1,   -1, 0,-12,30 },
    };
	
	public RandomCPU(){
		color = -1;
	}

	void Tedomari(GameState state){

		int[] sumicnt = new int[4];
		// Area0
		for(int y=0; y<=1; y++){
			for(int x=0; x<=1; x++){
				if(state.data[x][y] != 0)
					sumicnt[0]++;
			}
		}
		//Area1
		for(int y=6; y<=7; y++){
			for(int x=0; x<=1; x++){
				if(state.data[x][y] != 0)
					sumicnt[1]++;
			}
		}
		//Area2
		for(int y=0; y<=1; y++){
			for(int x=6; x<=7; x++){
				if(state.data[x][y] != 0)
					sumicnt[2]++;
			}
		}
		//Area3
		for(int y=6; y<=7; y++){
			for(int x=6; x<=7; x++){
				if(state.data[x][y] != 0)
					sumicnt[3]++;
			}
		}
		
		if(sumicnt[0] >= 3 ){
			WT1[0][0] = 50;
			WT1[0][1] = 50;
			WT1[1][0] = 50;
			WT1[1][1] = 50;
		}
		if(sumicnt[1] >= 3 ){
			WT1[0][6] = 50;
			WT1[0][7] = 50;
			WT1[1][6] = 50;
			WT1[1][7] = 50;
		}
		if(sumicnt[2] >= 3 ){
			WT1[6][0] = 50;
			WT1[7][0] = 50;
			WT1[6][1] = 50;
			WT1[7][1] = 50;
		}
		if(sumicnt[3] >= 3 ){
			WT1[6][6] = 50;
			WT1[6][7] = 50;
			WT1[7][6] = 50;
			WT1[7][7] = 50;
		}
	   		
		/*	System.out.printf("---------WT1----------%n");
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				System.out.printf("%3d",WT1[x][y]);
			}
			System.out.printf("%n");
		}
		*/
	}
	
	int[] decide(GameState state,int cpulevel){
		
		ArrayList<int[]> array = new ArrayList<int[]>();

		//盤面の空マスを置けるかチェック
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){				
				//すでに駒があるときはパス
				if(state.data[x][y] != 0)
					continue;				
				//置けるマスのとき、候補として記憶
				if(state.canReverse(x, y) == true){
					int pos[] = {x,y};
					array.add(pos);
				}
			}
		}
		//おける場所がない場合
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}

		// 定石判定
		if(	cpulevel == 2 && state.turn <= 10 ){
			int opening[] = Check_Opening(state, state.data); // 定石で求めた位置
			if(opening != null ) return opening;
		}

		Tedomari(state); //手どまりでスミの重み変更
		
		int index = 0;
		
		//////////////////////////////////////////////
		//0:ランダムで打ち手を選択
		//1:盤の重みの最大となる打ち手を選択
		//2:開放度理論
		//////////////////////////////////////////////

		if(cpulevel == 0){
			//ランダム選択
			Random rnd = new Random();
			index = rnd.nextInt(array.size());
			return array.get(index);
		}else if(cpulevel == 1){
			//打ち込み可能場所の先頭の重み保存(max探索の為)
			int postmp[] = array.get(0);
			int x = postmp[0]; int y = postmp[1];
			int max = WT1[x][y];
			//最大重み探索
			for(int i=0;i<array.size();i++){
				int pos[] = array.get(i);
				x = pos[0]; y = pos[1];
				if(max <= WT1[x][y] ){
					max = WT1[x][y]; //重みの最大値更新
					index = i; //重み最大のインデックス更新
				}
			}
			return array.get(index);
		}else if(cpulevel == 3){
 
			///////////////１手後の盤面作成///////////////////////
			GameState[] nextstate = new GameState[array.size()];
			for(int i=0;i<array.size();i++){
				nextstate[i] = new GameState();
			}
			for(int ban=0;ban<array.size();ban++){
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++){
						nextstate[ban].data[i][j] = state.data[i][j];
					}
				}
			}
			//次の盤面状態
			//		System.out.printf("次の盤面の可能性%n");
			for(int ban=0;ban<array.size();ban++){
				int pos[] = array.get(ban);
				int x = pos[0]; int y = pos[1];
				nextstate[ban].player *= -1; //プレーヤー反転
				nextstate[ban].put(x,y); //候補手を置く
				//		System.out.printf("-------%d-------%n",ban);
				for(int j=0;j<8;j++){
					for(int i=0;i<8;i++){
						//	System.out.printf("%d,",nextstate[ban].data[i][j]);
					}
					//		System.out.printf("%n");
				}
			}

			///////////////次の手で反転するコマ/////////////////////
			Rev_Status[][] ReList = new Rev_Status[array.size()][18];
			for(int j=0;j<18;j++){
				for(int i=0;i<array.size();i++){
					ReList[i][j] = new Rev_Status();
 				}
			}

			//反転したコマの位置保存
			for(int ban=0;ban<array.size();ban++){
				//	System.out.printf("-------%d-------%n",ban);
				int revcnt=0;
				for(int y=0; y<8; y++){
					for(int x=0; x<8; x++){
						//反転した場所を保存
						int put[] = array.get(ban); //置いた場所
						int tx=put[0]; int ty=put[1];
						//反転してかつ置いた場所以外の場所保存
						if(nextstate[ban].data[x][y] != state.data[x][y] && (x!=put[0] || y!=put[1]) ){
							int pos[] = {x,y};
							ReList[ban][revcnt].pos = pos;
							revcnt++;
						}
					}
				}
			}

			//////////////////////開放度算出部/////////////////////
			//	System.out.printf("--------開放値算出部-----------%n");
			int[] opval = new int[array.size()];
			for(int a=0;a<array.size();a++){
				int pos[] = array.get(a);
				//	System.out.printf("置いたマス--------(%d,%d)-------------------%n",pos[0],pos[1]);
				for(int b=0;ReList[a][b].pos[0]!=-1;b++){
					int px = ReList[a][b].pos[0]; int py = ReList[a][b].pos[1];
					//	System.out.printf(" 反転したマス(x,y) = (%d,%d) %n",px,py);
					for(int i=px-1;i<=px+1;i++){
						for(int j=py-1;j<=py+1;j++){
							if(0<=i && i <=7 &&0 <= j && j<=7)
							if( nextstate[a].data[i][j] == 0){
								opval[a]+=1;
								//			System.out.printf(" 開放マス(x,y) = (%d,%d) %n",i,j);
							}
						}
					}
				}
				//	System.out.printf("openval=%d%n",opval[a]);
			}

			//開放度の最小となる位置のindexを求める
			int min = opval[0];
			ArrayList<int[]> minList = new ArrayList<int[]>();
			//開放度の最小値算出
			for(int i=0;i<array.size();i++){
				if(min > opval[i]){
					min = opval[i];
				}
			}
			//開放度が最小となる位置保存
			for(int i=0;i<array.size();i++){
				if(min == opval[i]){
					int pos[] = array.get(i);
					minList.add(pos);
				}
			}

			///////////////重み最大の位置保存//////////////////
			int postmp[] = minList.get(0);
			int x = postmp[0]; int y = postmp[1];
			int max = WT1[x][y];
			//最大重み探索
			for(int i=0;i<minList.size();i++){
				int pos[] = minList.get(i);
				x = pos[0]; y = pos[1];
				if(max <= WT1[x][y] ){
					max = WT1[x][y]; //重みの最大値更新
					index = i; //重み最大のインデックス更新
				}
			}
			
		}else if(cpulevel == 2){

			///////////////１手後の盤面作成///////////////////////
			//打ち込み可能場所の先頭の重み保存(max探索の為)
		  	int ptmp[] = array.get(0);
			int cx = ptmp[0]; int cy = ptmp[1];
			int maxval = WT1[cx][cy];
			//最大重み探索
			for(int i=0;i<array.size();i++){
				int pos[] = array.get(i);
				cx = pos[0]; cy = pos[1];
				if(maxval <= WT1[cx][cy] ){
					maxval = WT1[cx][cy]; //重みの最大値更新			
				}
			}
			//重み最大の位置保存
			ArrayList<int[]> maxList = new ArrayList<int[]>();
			for(int i=0;i<array.size();i++){
			   	int pos[] = array.get(i);
					int x = pos[0]; int y = pos[1];		
					if(maxval == WT1[x][y] ){
					   	int tpos[] = {x,y};
						maxList.add(tpos);
				}
			}
			//次の盤面作成
			GameState[] nextstate = new GameState[maxList.size()];
			for(int i=0;i<maxList.size();i++){
				nextstate[i] = new GameState();
			}
			for(int ban=0;ban<maxList.size();ban++){
				for(int i=0;i<8;i++){
					for(int j=0;j<8;j++){
						nextstate[ban].data[i][j] = state.data[i][j];
					}
				}
			}
			//次の盤面状態
			//	System.out.printf("次の盤面の可能性%n");
			for(int ban=0;ban<maxList.size();ban++){
				int pos[] = maxList.get(ban);
				int x = pos[0]; int y = pos[1];
				nextstate[ban].player *= -1; //プレーヤー反転
				nextstate[ban].put(x,y); //候補手を置く
				//	System.out.printf("-------%d-------%n",ban);
					//	System.out.printf("b %d %d  %n",x,y);
				for(int j=0;j<8;j++){
					for(int i=0;i<8;i++){
						//	System.out.printf("%d,",nextstate[ban].data[i][j]);
					}
					//	System.out.printf("%n");
				}
			}
			///////////////次の手で反転するコマ/////////////////////
			Rev_Status[][] ReList = new Rev_Status[maxList.size()][18];
			for(int j=0;j<18;j++){
				for(int i=0;i<maxList.size();i++){
					ReList[i][j] = new Rev_Status();
 				}
			}
			//反転したコマの位置保存
			for(int ban=0;ban<maxList.size();ban++){
				int revcnt=0;
				for(int y=0; y<8; y++){
					for(int x=0; x<8; x++){
						//反転した場所を保存
						int put[] = maxList.get(ban); //置いた場所
						int tx=put[0]; int ty=put[1];
						//反転してかつ置いた場所以外の場所保存
						//	System.out.printf("c %d %d  %n",tx,ty);
						if(nextstate[ban].data[x][y] != state.data[x][y] && (x!=put[0] || y!=put[1]) ){
							int pos[] = {x,y};
							ReList[ban][revcnt].pos = pos;
							revcnt++;
						}
					}
				}
			}
			//////////////////////開放度算出部/////////////////////
			//		System.out.printf("--------開放値算出部-----------%n");
			int[] opval = new int[maxList.size()];
			for(int a=0;a<maxList.size();a++){
				int pos[] = maxList.get(a);
				//	System.out.printf("置いたマス--------(%d,%d)-------------------%n",pos[0],pos[1]);
				for(int b=0;ReList[a][b].pos[0]!=-1;b++){
					int px = ReList[a][b].pos[0]; int py = ReList[a][b].pos[1];
					//	System.out.printf(" 反転したマス(x,y) = (%d,%d) %n",px,py);
					for(int i=px-1;i<=px+1;i++){
						for(int j=py-1;j<=py+1;j++){
							if(0<=i && i <=7 &&0 <= j && j<=7)
							if( nextstate[a].data[i][j] == 0){
								opval[a]+=1;
								//			System.out.printf(" 開放マス(x,y) = (%d,%d) %n",i,j);
							}
						}
					}
				}
			}

			//開放度の最小となる位置のindexを求める
			int min = opval[0];
			ArrayList<int[]> minList = new ArrayList<int[]>();
			//開放度の最小値算出
			for(int i=0;i<maxList.size();i++){
				if(min > opval[i]){
					min = opval[i];
					index = i;
				}
			}
			return maxList.get(index); //評価値が高く、開放度の低い場所を返す
		}
		return array.get(index);
	}


		// 配列90度回転
   	int[][] Rotation(int[][] block){
		
		int[][] temp = new int[8][8];
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				temp[i][j] = block[j][7-i]; // 回転
			}
		}
		return temp;
	}

	// 白黒反転
	int[][] AllRevers(int[][] block){
		
		int[][] temp = new int[8][8];
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				temp[i][j] = block[i][j]*-1; // 反転
			}
		}
		return temp;
	}
	
	// 定石チェック_メイン
	int[] Check_Opening(GameState state,int[][] block){
		
		int[] put = new int[2]; // 定石 指す位置
		int[] put_t = new int[2]; //転置定石 指す位置
		int[] put_r = new int[2]; //白黒反転定石 指す位置
		
		int[][] op = new int[8][8]; // 定石
		int[][] op_trans = new int[8][8]; // 定石の転置
		int[][] op_revers = new int[8][8]; // 定石の白黒反転
			
		// 定石チェック開始
		//	System.out.println("-- Check Start Opening --");
		//View(block);

		/*
		**   forループより定石構造体をチェック
		**   定石構造体：open.Pt[] (Openingクラスで実装) 
		**   
		**   ☆チェック手順
		**     1.定石を回転してチェック
		**     2.転置定石を回転してチェック
		**     3.白黒反転定石を回転してチェック
                                                        */
		for(int i=0; i<open.MAX_OP; i++){
			
			// 定石初期化
			//System.out.println("-- Initialise Opening : "+ i);
			op = open.Pt[i].opening; // 定石
			op_trans = open.TransposedMatrix(op); // 転置
			op_revers = AllRevers(op); // 白黒反転

			
			// 定石　回転チェック
			put = Check_OpRotation(i,block,op);
			if( put != null ){
				// 定石ヒットした場合
				//System.out.println("-- Hit Opening original --"+ i);
				
				// ヒットした定石が置けるかチェック
				if(state.canReverse(put[0], put[1]) == true)
					return put;
				else{
					//	System.out.println("-- Can't put Opening --");
					return null;
				}
				
			// 定石ヒットしない　->  転置定石　回転チェック
			}else if(Check_OpRotation(i,block,op_trans)!=null){
				//	System.out.println("-- Hit Opening trans --"+ i);
				int[] temp = Check_OpRotation(i,block,op_trans);
				// System.out.println("temp : " + Arrays.toString(temp));

				// 最善手の転置
				put_t[0] = temp[1];
				put_t[1] = temp[0];

				// ヒットした定石が置けるかチェック
				if(state.canReverse(put_t[0], put_t[1]) == true)
					return put_t;
				else{
					System.out.println("-- Can't put Opening --");
					return null;
				}
				
			// 定石,転置定石ヒットしない　->  白黒反転定石　回転チェック
			}else if(Check_OpRotation(i,block,op_revers)!=null){
				//	System.out.println("-- Hit Opening revers --"+ i);
				put_r = Check_OpRotation(i,block,op_revers);
				
				// ヒットした定石が置けるかチェック
				if(state.canReverse(put_r[0], put_r[1]) == true)
					return put_r;
				else{
					//	System.out.println("-- Can't out  Opening --");
					return null;
				}

				
			}else {
				// i番目の定石は ヒットなし
				// System.out.println("-- Not Opening : "+ i); 	
			}
		}
		//	System.out.println("-- Not All Opening ");
		return null;
	}

	// 定石回転チェック
	int[] Check_OpRotation(int num, int[][] block, int[][] check){
		
		int put[]={0,0}; // 定石により求めた指す位置
		int[][] op = new int[8][8]; // 定石
		int[][] op_revers = new int[8][8]; // 定石の白黒反転
		int[][] op_90 = new int[8][8]; // 定石の90度回転
		int[][] op_180 = new int[8][8]; // 定石の180度回転
		int[][] op_270 = new int[8][8]; // 定石の270度回転

		//System.out.println("|| Check OpRotation ||");
		
		// 定石初期化
		//System.out.println("|| Initialise OpRotation ||");
		op = check; // 定石		
		op_90 = Rotation(op); // 90回転
		op_180 = Rotation(op_90); // 180回転
		op_270 = Rotation(op_180); // 270回転

		// 比較　盤面：定石
		if(Arrays.deepEquals(block,op)){ 
			//	System.out.println("|| Hit Opening ||");
			put = open.Pt[num].pos;
			//	System.out.println("Best Pos : " + open.Pt[num].name
			//				   + Arrays.toString(put));
			return put;
			
		// 比較　盤面：定石90
		} else if(Arrays.deepEquals(block,op_90)){
			//	System.out.println("|| Hit Opening90 ||");
			// put 90回転
			put[0] = 7-open.Pt[num].pos[1];
			put[1] = open.Pt[num].pos[0];
			//	System.out.println("Best Pos : " +open.Pt[num].name
			//				   + Arrays.toString(put));
			return put;
			
		// 比較　盤面：定石180
		} else if(Arrays.deepEquals(block,op_180)){
		//	System.out.println("|| Hit Opening180 ||");
			// put 180回転
			put[0] = 7-open.Pt[num].pos[0];
			put[1] = 7-open.Pt[num].pos[1];
			//	System.out.println("Best Pos : " +open.Pt[num].name
			//				   + Arrays.toString(put));
			return put;			 
			
		// 比較　盤面：定石270
		} else if(Arrays.deepEquals(block,op_270)){
		//	System.out.println("|| Hit Opening270 ||");
			// put 270回転
			put[0] = open.Pt[num].pos[1];
			put[1] = 7-open.Pt[num].pos[0];
			//	System.out.println("Best Pos : " +open.Pt[num].name
			//				   + Arrays.toString(put));
			return put;

		} else {
			//System.out.println("|| Not OpRotation ||");
			return null;
		}
	}
}
