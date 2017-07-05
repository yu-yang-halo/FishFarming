package com.farmingsocket.manager;

import java.util.Vector;

public class UIManager {
	private static UIManager instance=null;
	private boolean changed = false;
	private Vector<IDataObserver> obs;
	public static UIManager getInstance(){
		synchronized (UIManager.class) {
			if(instance==null){
				instance=new UIManager();
			}
		}
		return instance;
	}


	private UIManager() {
		obs = new Vector<>();
	}

	public synchronized void addObserver(IDataObserver o) {
		if (o == null)
			throw new NullPointerException();
		if (!obs.contains(o)) {
			obs.addElement(o);
		}
	}

	public synchronized void deleteObserver(IDataObserver o) {
		obs.removeElement(o);
	}

	public void notifyObservers() {
		notifyDataObservers(null,-1);
	}

	public void notifyDataObservers(Object arg,int command) {
		Object[] arrLocal;
		synchronized (this) {
			if (!changed)
				return;
			arrLocal = obs.toArray();
			clearChanged();
		}

		for (int i = arrLocal.length - 1; i >= 0; i--)
			((IDataObserver) arrLocal[i]).update(this, arg,command);
	}

	public synchronized void deleteObservers() {
		obs.removeAllElements();
	}

	public synchronized void setChanged() {
		changed = true;
	}

	protected synchronized void clearChanged() {
		changed = false;
	}

	public synchronized boolean hasChanged() {
		return changed;
	}

	public synchronized int countObservers() {
		return obs.size();
	}
}
