package com.example.uidemo.common.bitmapfun;

import java.util.Vector;

public class NetBuff {

	public NetBuff() {

	}

	public static Vector buffName = new Vector();
	static Vector buffData = new Vector();

	public static void addBuff(String name, byte[] data) {
		// System.out.print("buff:" + name + "    ");
		for (int i = 0; i < buffName.size(); i++) {
			if (((String) buffName.elementAt(i)).equals(name)) {
				return;
			}
		}

		buffName.addElement(name);
		buffData.addElement(data);
		// System.gc();
		while (buffData != null && buffData.size() > 0 && Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > 10000 * 1024) {
			if (buffData != null && buffData.size() > 0) {
				buffData.removeElementAt(0);
			}
			if (buffName != null && buffName.size() > 0) {
				buffName.removeElementAt(0);
			}
			// System.gc();
		}
		System.out.println("freeMemory:".concat(String.valueOf(Runtime.getRuntime().freeMemory())));
	}

	public static byte[] readBuff(String name) {
		for (int i = 0; i < buffName.size(); i++) {
			if (((String) buffName.elementAt(i)).equals(name)) {
				return (byte[]) buffData.elementAt(i);
			}
		}

		return null;
	}

	public static void delBuff(String name) {
		for (int i = 0; i < buffName.size(); i++) {
			if (((String) buffName.elementAt(i)).equals(name)) {
				buffName.removeElementAt(i);
				buffData.removeElementAt(i);
			}
		}
	}

	public static void release() {
		buffName.removeAllElements();
		buffData.removeAllElements();
		System.gc();
	}
}
