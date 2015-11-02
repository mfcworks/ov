class ov {
    // グローバル変数のセット
    protected static double sensitivity, beta;   // セルの数、初期の車台数、パラメータ
    protected static double[]  x, v;    // 車の位置、速度

    protected static final int N = 50;			// 車の台数
    protected static final double L = 100.0;	// 道路長
    protected static final double DT = 0.0001;	// 時間刻み幅

    // 係数パラメータ
    protected static double c = 2.0;
    protected static double a = 1.0;

    protected static drawConfiguration gr;

    ///////////// メインメソッド //////////////////
    public static void main(String[] args) {
        int step=0;  // 時間

        // 描画インターフェースの構築
        gr = new drawConfiguration(600, L);  // グラフィック用クラスのインスタンス化

        setInitialCondition();
        // 初期値確認
        for (int i = 0; i < x.length; i++) {
        	System.out.println(x[i] + " " + v[i]);
        }

        // 描画
        gr.drawCars(x, N);


        // 時間発展の繰り返し
        //for(int i =0; i < 10000; i++) {
        while (true) {
           update();
           // setBoundaryCondition();
            // 状態の書き出し

           gr.drawCars(x, N);
        }
    }

    //////////// 初期値設定 ///////////////////
    public static void setInitialCondition() {
        // initial_n台の車を等間隔に
        // 速度は乱数により設定
    	x = new double[N + 1];
    	v = new double[N + 1];

    	for (int i = 0; i < N; i++) {
    		x[i] = L * i / N;
    		v[i] = Math.random(); //初期速度範囲 0.0 - 1.0 とする
    	}
    	x[N] = x[0];
    	v[N] = v[0];
    }

    /////////// 時間発展 /////////////////////
    public static void update() {
        // 速度を計算し、それに基づいて車を進める
    	/*
    	 * 微分方程式の数値解法 : オイラー法
    	 *
    	 * x(t+dt) = x(t) + dt * v(t)
    	 * v(t+dt) = v(t) + dt * a * (V(delta_x) - v(t))
    	 */


    	for (int i = 0; i < N; i++) {
    		// 今の時間での x, v

    		// 前の車との車間(車頭)距離
    		double delta_x = x[i + 1] - x[i];
    		if (delta_x < 0.0) delta_x += L;

    		x[i] += DT * v[i];
    		v[i] += DT * a * (V(delta_x) - v[i]);
    	}
    	x[N] = x[0];
    	v[N] = v[0];

    	// 追い越しがないか確認
    	for (int i = 0; i < N; i++) {
    		if (x[i]>x[i+1] && x[i]-x[i+1]<L/2) { // とりあえず
    			System.out.println("追い越しが発生しました at x[" + i + "]");
    			System.exit(1);
    		}
    	}
    }

    ////////// 境界条件処理（車の生成、消滅） ////
    public static void setBoundaryCondition() {

    }

    public static double V(double dx) {
    	return Math.tanh(dx - c) + Math.tanh(c);
    }

}