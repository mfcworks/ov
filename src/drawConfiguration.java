import java.awt.Color;
import java.awt.Graphics; // uses the abstract windowing toolkit
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * drawConfiguration : 描画クラス
 *
 * Originally written by T教授
 * Modified by Tomoki Miyazaki
 *
 * Changes:
 *   * 閉じるボタンで終了するようにした
 *   * なめらかに描画されるようにした
 */
public class drawConfiguration extends JPanel {
//    static Graphics g;
    static JFrame gWin;
    double screenWidth, roadLength;
    int frameTop, frameLeft; // platform-dependent width of frame's top and left
    int marginX=20, marginY=20;

    // パラメータの一時格納変数
    private double[] x;
    private int numberOfCar;


    // コンストラクタ
    drawConfiguration(int fSize, double rLength) {
        screenWidth = (double) fSize;
        roadLength = rLength;
        // シミュレーション本体画面の初期化
        gWin = new JFrame("OV Model");
        gWin.setLocation(20, 150); // screen coordinates of top left corner
        gWin.setResizable(false);
        gWin.setVisible(true); // show it!
        Insets theInsets = gWin.getInsets();
        gWin.setSize(fSize + theInsets.left + theInsets.right + marginX*2,
        		fSize + theInsets.top + marginY*2+ theInsets.bottom/*+30*/); /* 下30は文字出力用*/
        this.frameTop = theInsets.top;
        this.frameLeft = theInsets.left;

        // 閉じるボタンで終了する
        gWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gWin.setLayout(null);
        gWin.getContentPane().setBackground(Color.WHITE);
        gWin.getContentPane().add(this);
        this.setBounds(marginX, 0, fSize, fSize);
    }

    // 呼び出し用APIインターフェース
    public void drawCars(double[] _x, int _numberOfCar) {
    	x = _x;
    	numberOfCar = _numberOfCar;

    	this.repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);

    	this.__drawCars(g);
    }


    // 車の描画（下請け化）
    private void __drawCars(Graphics g) {
        double x_coordinate, y_coordinate;
        double pi2 = Math.PI * 2.0;
        double radius = 0.9 * screenWidth /2.0;

        // 描画する画面(Graphics)を特定する
        //this.g = gWin.getGraphics();

        // 画面全体をクリア
        g.setColor(new Color(255,255,255));
        g.fillRect(0, 0, getWidth(), getHeight());

        int x_center = getWidth()/2;
        int y_center = x_center;

//        System.out.println(x_center+ ", " + y_center +", "  +  gWin.getHeight());

        // 車の位置を描画
        g.setColor(new Color(0,0,255));
        for(int i=0; i< numberOfCar; i++) {
            x_coordinate = radius * Math.cos(pi2*x[i]/roadLength) + x_center;
            y_coordinate = radius * Math.sin(pi2*x[i]/roadLength)  + y_center;
//            System.out.println(x_coordinate + ", " +y_coordinate);
            g.fillOval((int) x_coordinate -5, (int) y_coordinate -5, 10, 10); // 半径10
        }
    }

}