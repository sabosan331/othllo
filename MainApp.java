import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class MainApp extends JFrame {

	public MainApp(){
		
		setTitle("Othello");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainPanel panel = new MainPanel();
		add(panel);		
		pack();
		initCloseOperation();
	}
	
	public static void main(String[] args){
	
		MainApp app = new MainApp();
		app.setVisible(true);
	}

	private void initCloseOperation() {
		addWindowListener(new WindowAdapter() {
				// ウィンドウが閉じるときに呼ばれる
				public void windowClosing(WindowEvent e) {
					System.out.println("ゲーム終了");
					//削除対象ファイルのFileオブジェクトを生成する
					File f = new File("./history.csv");
					if (f.exists()) {
						//削除実行
						f.delete();
						System.out.println("ゲームログを破棄しました。");
						
					} else {
						System.out.println("ゲームログが存在しません。");
					}					
					System.exit(0);
				}
			});		
	}
	
}
