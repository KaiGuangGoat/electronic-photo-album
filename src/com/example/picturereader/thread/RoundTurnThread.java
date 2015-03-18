package com.example.picturereader.thread;

import android.os.Handler;

public class RoundTurnThread extends Thread {
	private Handler handler;
	private long roundTurnTime = 5000;// 轮转图片的等待时间，设置为5秒
	private boolean canRoundTurn = true;
	private boolean finish = false;

	public RoundTurnThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		while (!finish) {
			try {
				sleep(roundTurnTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(canRoundTurn)
				handler.sendEmptyMessage(0);
		}
	}

	public void setRoundTurn(boolean canRoundTurn) {
		this.canRoundTurn = canRoundTurn;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}
}
