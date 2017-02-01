class Pt_Status{
	// 定石　構造体
	public String name; //　名前
	public int[] pos; // 最善手	
	public int[][] opening; //　配置
	
	public Pt_Status(){}
	public Pt_Status(String name, int[] pos, int[][] opening){
		this.name = name;
		this.pos = pos;
		this.opening = opening;
	}
}

public class Opening {
	public static final int MAX_OP = 13; // 定石数
	
	public Opening(){}
	
	//　構造体Pt に 定石パターン を転置して挿入
	Pt_Status[] Pt = {
		new Pt_Status(Pt1_name, Pt1_pos, TransposedMatrix(Pt1)),
		new Pt_Status(Pt2_name, Pt2_pos, TransposedMatrix(Pt2)),
		new Pt_Status(Pt3_name, Pt3_pos, TransposedMatrix(Pt3)),
		new Pt_Status(Pt4_name, Pt4_pos, TransposedMatrix(Pt4)),
		new Pt_Status(Pt5_name, Pt5_pos, TransposedMatrix(Pt5)),
		new Pt_Status(Pt6_name, Pt6_pos, TransposedMatrix(Pt6)),
		new Pt_Status(Pt7_name, Pt7_pos, TransposedMatrix(Pt7)),
		new Pt_Status(Pt8_name, Pt8_pos, TransposedMatrix(Pt8)),
		new Pt_Status(Pt9_name, Pt9_pos, TransposedMatrix(Pt9)),
		new Pt_Status(Pt10_name, Pt10_pos, TransposedMatrix(Pt10)),
		new Pt_Status(Pt11_name, Pt11_pos, TransposedMatrix(Pt11)),
		new Pt_Status(Pt12_name, Pt12_pos, TransposedMatrix(Pt12)),
		new Pt_Status(Pt0_name, Pt0_pos, TransposedMatrix(Pt0))
	};

	// 転置
	int[][] TransposedMatrix(int[][] block){

		int[][] temp = new int[8][8];
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				temp[i][j] = block[j][i];
			}
		}
		return temp;
	}

	// 定石パターン

	// Pt1-5 牛定石 http://bassy84.net/zyouseki-usikihon.html
	// Pt6-9 兎定石 http://othello.mezasou.com/cop129.html
	// Pt10-12 鼠定石 http://othello.mezasou.com/cop101.html
	// Pt0   dbg(コピー用)

	// Pt1 黒1手目 -> f5
	public static final String Pt1_name = "kuro1";
	public static final int[] Pt1_pos = {5,4};
	public static final int[][] Pt1 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0, 1,-1, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt2 白2手目　牛定石　-> f6
	public static final String Pt2_name = "ushi2";
	public static final int[] Pt2_pos = {5,5};
	public static final int[][] Pt2 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0, 1, 1, 1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};
	
	// Pt3 黒3手目　牛定石　-> e6
	public static final String Pt3_name = "ushi3";
	public static final int[] Pt3_pos = {4,5};
	public static final int[][] Pt3 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0, 1,-1, 1, 0, 0},
									   { 0, 0, 0, 0, 0,-1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};
	
	// Pt4 白4手目　牛定石 ->f4
	public static final String Pt4_name = "ushi4";
	public static final int[] Pt4_pos = {5,3};
	public static final int[][] Pt4 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0, 1, 1, 1, 0, 0},
									   { 0, 0, 0, 0, 1,-1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};
	
	// Pt5 白6手目　牛定石 -> c5
	public static final String Pt5_name = "ushi6";
	public static final int[] Pt5_pos = {2,4};
	public static final int[][] Pt5 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 1, 0, 0, 0},
									   { 0, 0, 0,-1, 1,-1, 0, 0},
									   { 0, 0, 0, 1, 1,-1, 0, 0},
									   { 0, 0, 0, 0, 1,-1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};	

	// Pt6 黒3手目　兎定石 -> c6
	public static final String Pt6_name = "usagi3";
	public static final int[] Pt6_pos = {2,5};
	public static final int[][] Pt6 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 1, 0, 0},
									   { 0, 0, 0,-1, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt7 白4手目　兎定石 -> f4
	public static final String Pt7_name = "usagi4";
	public static final int[] Pt7_pos = {5,3};
	public static final int[][] Pt7 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0, 1, 1, 1, 0, 0},
									   { 0, 0, 1,-1, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt8 黒5手目　兎定石 -> e6
	public static final String Pt8_name = "usagi5";
	public static final int[] Pt8_pos = {4,5};
	public static final int[][] Pt8 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1,-1,-1, 0, 0},
									   { 0, 0, 0, 1,-1, 1, 0, 0},
									   { 0, 0, 1,-1, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt9 白6手目　兎定石 -> G6
	public static final String Pt9_name = "usagi6";
	public static final int[] Pt9_pos = {6,5};
	public static final int[][] Pt9 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1,-1,-1, 0, 0},
									   { 0, 0, 0, 1,-1, 1, 0, 0},
									   { 0, 0, 1, 1, 1, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt10 黒3手目 鼠定石 -> E3
	public static final String Pt10_name = "nezumi3";
	public static final int[] Pt10_pos = {4,2};
	public static final int[][] Pt10 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1,-1,-1, 0, 0},
									   { 0, 0, 0, 1, 1, 1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt11 白4手目 鼠定石 -> F6
	public static final String Pt11_name = "nezumi4";
	public static final int[] Pt11_pos = {5,5};
	public static final int[][] Pt11 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 1, 0, 0, 0},
									   { 0, 0, 0,-1, 1,-1, 0, 0},
									   { 0, 0, 0, 1, 1, 1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};

	// Pt12 黒5手目 鼠定石 -> D3
	public static final String Pt12_name = "nezumi5";
	public static final int[] Pt12_pos = {3,2};
	public static final int[][] Pt12 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 1, 0, 0, 0},
									   { 0, 0, 0,-1, 1,-1, 0, 0},
									   { 0, 0, 0, 1,-1,-1, 0, 0},
									   { 0, 0, 0, 0, 0,-1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};
	

	// Pt0 dbg 白黒 手目 定石
	public static final String Pt0_name = "dbg";
	public static final int[] Pt0_pos = {3,3};
	public static final int[][] Pt0 = {{ 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0,-1, 1, 0, 0, 0},
									   { 0, 0, 0, 1,-1, 1, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0},
									   { 0, 0, 0, 0, 0, 0, 0, 0}};
}
