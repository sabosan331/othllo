import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameState extends Observable{

	int data[][];
	int turn;
	int player;
	int black;
	int white;
	int flag_pass = 0;
	int tmp_player = 0;
	
	public GameState(){
		
		data = new int[8][8];

		//ゲームスタート初期値
		data[3][3] = -1;
		data[3][4] = 1;
		data[4][3] = 1;
		data[4][4] = -1;

		turn = 0;
		player = 1;
		black = 2;
		white = 2;
		
		//削除対象ファイルのFileオブジェクトを生成する
	}
	
	public boolean put(int x, int y){

		/*
		if( isOut(x,y) == true ){
			System.out.println("盤面外です");
			return false;
		}
		*/
		
		//else{
			//すでに駒があるところには置けない
			if(data[x][y] != 0){
				return false;
			}
			//リバースできないところには置けない
			if(reverse(x,y,true) == false){
				return false;
			}
			
			//駒を置く
			data[x][y] = player;
			player *= -1;
			turn++;
			countDisc();
			
			setChanged();
			notifyObservers();
			
			return true;
			//	}
	}
	
	public boolean reverse(int x,int y, boolean doReverse ){
		int dir[][] = {
				{-1,-1}, {0,-1}, {1,-1},
				{-1, 0},         {1, 0},
				{-1, 1}, {0, 1}, {1, 1}
		};
		
		boolean reversed = false;
		
		for(int i=0; i<8; i++){
			//隣のマス
			int x0 = x+dir[i][0];
			int y0 = y+dir[i][1];
			if(isOut(x0,y0) == true){
				continue;
			}
			int nextState =data[x0][y0];
			if(nextState == player){
				//	System.out.println("Next state is player: " +x0 +","+ y0);
				continue;
			}else if(nextState == 0){
				//	System.out.println("Next state is null: " +x0 +","+ y0);
				continue;
			}else{
				//	System.out.println("Next state is enemy: " +x0 +","+ y0);
			}
			
			//隣の隣から端まで走査して、自分の色があればリバース
			int j = 2;
			while(true){
			
				int x1 = x + (dir[i][0]*j);
				int y1 = y + (dir[i][1]*j);
				if(isOut(x1,y1) == true){
					break;
				}
				
				//自分の駒があったら、リバース
				if(data[x1][y1]==player){
					//	System.out.println("Player cell!: " +x1 +","+ y1);
					
					if(doReverse){
						for(int k=1; k<j; k++){
							int x2 = x + (dir[i][0]*k);
							int y2 = y + (dir[i][1]*k);
							data[x2][y2] *= -1;
							//		System.out.println("reverse: " +x2 +","+ y2);
						}
					}
					reversed = true;
					break;
				}
				
				//空白があったら、終了
				if(data[x1][y1]==0){
					break;
				}				
				j++;				
			}			
		}
		
		return reversed;
	}
	
	
	
	public boolean canReverse(int x, int y){
		return reverse(x, y, false);
	}
	
	public boolean isOut(int x, int y){
		if(x<0 || y<0 || x>=8 || y>=8){
			return true;
		}
		return false;
	}
	
	public boolean checkPass(){
		
		//コピーデータの全升目に対して、リバースできるかチェック
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				
				//すでに駒があるところはチェックしない
				if(data[x][y] != 0){
					continue;
				}
				
				//リバースできる（した）とき、元に戻してfalseを返す
				if(canReverse(x,y) == true){
					return false;
				}
				
			}
		}
		
		return true;
	}
	
	public void countDisc(){
		
		black = 0;
		white = 0;
		
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				if(data[x][y] == 1){
					black++;
				}else if(data[x][y] == -1){
					white++;
				}
			}
		}
	}


	//------------------------------------履歴管理メソッド-------------------
	
	//終了条件判定メソッド　終了ならtrue それ以外ならfalseを返す
	public boolean checkEnd(){
	
		int flag_black = 0;
		int flag_white = 0;
		
	   	//(1)先手後手ともにパスの状態になった場合
		if(flag_pass == 2){
			//先手後手が連続してパスになる
			if( player != tmp_player){
				System.out.println("先手後手ともにパスの状態になりました");
				flag_pass = 0;//フラグパスを初期化
				return true;
			}
			//同じプレイヤーが連続してパスになる
			else if( player == tmp_player ) {
				flag_pass = 0;
			}
		}
		
		//パスの状態になった場合
		if(checkPass() == true){
			flag_pass += 1;
			tmp_player = player;
			System.out.println( player + "のターンがパスされました");
		}
		//パスじゃない場合
		else if(checkPass() == false){
			flag_pass = 0;
		}
		
		
		//(2)すべての色が同色になった場合
		int count = 0;
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				if(data[x][y] != 0){
					count++;
				}
				//黒があるとき
			    if(data[x][y] == 1){
					flag_black = 1;
				}
				//白があるとき
				else if(data[x][y] == -1){
					flag_white = 1;
				}
			}
		}
		
		//黒一色の場合
		if ( flag_black == 1 && flag_white == 0 )
			return true;
		//白一色の場合
		else if ( flag_black == 0 && flag_white == 1 )
			return true;

		//(3)盤面が全部埋まっている場合
		if (count == 64){
			System.out.println("盤面が全部埋まっている");
			return true;
		}
		
		return false;
	}
	

	/////////////////////////////////////////////////////////////////////

	
	public void writeHistory(boolean TF){
		
        try {
            //出力先を作成する
			//false:上書き true:追記
            FileWriter fw = new FileWriter("./history.csv", TF);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            //ターン数、ターンプレイヤー、黒コマ数、白コマ数の出力
            pw.print(turn);
            pw.print(",");
            pw.print(player);
            pw.print(",");
            pw.print(black);
            pw.print(",");
            pw.print(white);			
            pw.print(",");

			//全コマ配置の出力
			for(int i=0; i<8; i++){
				for(int j=0; j<8; j++){			
					pw.print(data[i][j]);
					pw.print(",");
				}
			}

			//改行
	        pw.println();
            //ファイルに書き出す
            pw.close();

        } catch (IOException ex) {
            //例外時処理
            ex.printStackTrace();
        }

	}

	public void readHistory(){

        try {
            //ファイルを読み込む
            FileReader fr = new FileReader("./history.csv");
            BufferedReader br = new BufferedReader(fr);

            //読み込んだファイルを１行ずつ処理する
            String line;
            StringTokenizer token;
            while ((line = br.readLine()) != null) {
                //区切り文字","で分割する
                token = new StringTokenizer(line, ",");
				
	            //ターン数、ターンプレイヤー、黒コマ数、白コマ数の読み込み
			   	turn = Integer.parseInt(token.nextToken());
				player = Integer.parseInt(token.nextToken());
				black = Integer.parseInt(token.nextToken());
				white = Integer.parseInt(token.nextToken());
				
                //全コマ配置の読み込み
                while (token.hasMoreTokens()) {					
					for(int i=0; i<8; i++){
						for(int j=0; j<8; j++){			
							data[i][j] = Integer.parseInt(token.nextToken());
						}
					}
                }
            }

            //終了処理
            br.close();

			setChanged();
			notifyObservers();

        } catch (IOException ex) {
            //例外発生時処理
            ex.printStackTrace();
        }			   

	}
	
	public void undo(int p){		

					if(turn >= p){
			
			try {
				//ファイルを読み込む
				FileReader fr = new FileReader("./history.csv");
				BufferedReader br = new BufferedReader(fr);
				//出力先を作成する
				FileWriter fw = new FileWriter("./tmp.csv", true);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				
				//読み込んだファイルを１行ずつ処理する
				String line;

				int u = 0;
				while (u <= turn-p) {
					if ((line = br.readLine()) != null)
						pw.println(line);
					u += 1;
				}
				
				//終了処理
				br.close();
				pw.close();
				
				//削除対象ファイルのFileオブジェクトを生成する
				File f = new File("./history.csv");
				
				if (f.exists()) {
					//削除実行
					f.delete();
					//System.out.println("ファイルを削除しました。");
					
				} else {
					//	System.out.println("ファイルが存在しません。");
				}
				
				
				//Fileオブジェクトを生成する
				File fOld = new File("./tmp.csv");
				File fNew = new File("./history.csv");
				
				if (fOld.exists()) {
					//ファイル名変更実行
					fOld.renameTo(fNew);
					//System.out.println("ファイル名を変更しました。");
					
				} else {
					//System.out.println("ファイルが存在しません。");
				}
				
				readHistory();
				setChanged();
				notifyObservers();
				
			} catch (IOException ex) {
				//例外時処理
				ex.printStackTrace();
			}
			
		}
	}
	
	public void checkHistory(int turnH){
        try {
            //ファイルを読み込む
            FileReader fr = new FileReader("./history.csv");
            BufferedReader br = new BufferedReader(fr);
			
            //読み込んだファイルを１行ずつ処理する
            String line;
            StringTokenizer token;
            while ((line = br.readLine()) != null) {
                //区切り文字","で分割する
                token = new StringTokenizer(line, ",");
				
				if(turnH != Integer.parseInt(token.nextToken())){
					continue;
				}
				else{
					//ターン数、ターンプレイヤー、黒コマ数、白コマ数の読み込み
					player = Integer.parseInt(token.nextToken());
					black = Integer.parseInt(token.nextToken());
					white = Integer.parseInt(token.nextToken());
					
					//全コマ配置の読み込み
					while (token.hasMoreTokens()) {					
						for(int i=0; i<8; i++){
							for(int j=0; j<8; j++){			
								data[i][j] = Integer.parseInt(token.nextToken());
							}
						}
					}
				}
            }
			
            //終了処理
            br.close();

			setChanged();
			notifyObservers();
			
        } catch (IOException ex) {
            //例外発生時処理
            ex.printStackTrace();
        }
	}


	public void deleteHistory(){
		//削除対象ファイルのFileオブジェクトを生成する
        File f = new File("./history.csv");
		
        if (f.exists()) {
            //削除実行
            f.delete();
            System.out.println("ファイルを削除しました。");
			
        } else {
            System.out.println("ファイルが存在しません。");
        }
	}
	//----------------------------------------------------------------------------------
	
}
