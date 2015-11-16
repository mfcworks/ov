import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * 軌跡を描画するクラス
 *
 */
public class LocationTimeView extends JPanel {
	private JFrame frame;
	private int areaX, areaY;
	private int clientX, clientY;
//	private double[] xs; // 今回の車の位置
	private double[] xsOld; // 前回の車の位置
	public int delta = 5; // 1ステップあたりの描画する横幅
	private int posX; // 現在の横軸の座標

	// 描画マージン (top,left,bottom,right)
	private Insets margins = new Insets(20, 20, 30, 20);

	private BufferedImage bi;


	/**
	 * コンストラクタ
	 *
	 * @param areaX 描画エリアのXサイズ[px]
	 * @param areaY 描画エリアのYサイズ[px]
	 */
	public LocationTimeView(int areaX, int areaY) {
		this.areaX = areaX;
		this.areaY = areaY;

		// クライアント領域のサイズ
		clientX = areaX + margins.left + margins.right;
		clientY = areaY + margins.top + margins.bottom;

		/*
		 * イメージバッファの作成
		 */
		bi = new BufferedImage(clientX, clientY, BufferedImage.TYPE_INT_RGB);
		clear();
		drawFrame();


		/*
		 * ウィンドウの作成
		 */
		frame = new JFrame("LocationTimeView");
		// 閉じるボタンで終了
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// OSが提供する表示位置を使う
		frame.setLocationByPlatform(true);
		// サイズ不可変
		frame.setResizable(false);

		// このJPanelをメインウィンドウに追加
		frame.add(this);
		// サイズをぴったり合わせる
		frame.pack();

		// ここで表示する
		frame.setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(clientX, clientY);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// イメージバッファをJPanelに転写
		g.drawImage(bi, 0, 0, null);
	}


	/**
	 * バッファをクリアします。
	 */
	public void clear() {
		Graphics g = bi.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

		repaint();
	}

	/**
	 * バッファにフレームを描画します。
	 */
	public void drawFrame() {
		Graphics g = bi.getGraphics();
		g.setColor(Color.BLACK);
		g.drawLine(margins.left, margins.top, margins.left, margins.top + areaY);
		g.drawLine(margins.left + areaX, margins.top, margins.left + areaX, margins.top + areaY);
		g.drawLine(margins.left, margins.top, margins.left + areaX, margins.top);
		g.drawLine(margins.left, margins.top + areaY, margins.left + areaX, margins.top + areaY);

		repaint();
	}

	/**
	 * 描画の原点を設定する
	 *
	 */
	public void setOrigin(double[] xs) {
		int n = xs.length - 1; // 最後は最初と同じなので無視
		xsOld = new double[n];

		for (int i = 0; i < n; i++) {
			xsOld[i] = xs[i];
		}

		// areaXの中における値
		posX = 0;
	}

	/**
	 * 線を描画する<br>
	 * <br>
	 * 正直この実装はスマートではない気がする。まぁテストということで(･_･;)

	 * @param xs 各車の位置の座標(n台→n個の配列を渡すこと)
	 * @param L  道幅の値
	 */
	public void drawLines(double[] xs, double L) {

		// 初回のみ
		if (xsOld == null) {
			setOrigin(xs);
			return;
		}

		int xFrom = posX; // 今のY座標

		// 描画範囲をオーバーランしていなければ描画
		if (posX >= areaX) return;

		if (posX + delta >= areaX)
			posX = areaY;

		posX += delta;

		int xTo = margins.right + posX;

		Graphics g = bi.getGraphics();
		g.setColor(Color.BLUE);

		for (int i = 0; i < xs.length - 1; i++) {
			int yFrom = (int)((1-xsOld[i]/L)*areaY);
			int yTo = (int)((1-xs[i]/L)*areaY);

			if (xsOld[i] > xs[i]) {
				yFrom = areaY;
			}

			g.drawLine(margins.left + xFrom, margins.top + yFrom, margins.left + posX, margins.top + yTo);
		}

		for (int i = 0; i < xsOld.length; i++) {
			xsOld[i] = xs[i];
		}
		repaint();
	}






	// test
	public static void main(String[] args) {
		LocationTimeView view = new LocationTimeView(640, 480);
		System.out.println("Ready");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
