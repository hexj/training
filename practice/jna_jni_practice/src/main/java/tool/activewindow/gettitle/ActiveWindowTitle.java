package tool.activewindow.gettitle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class ActiveWindowTitle {

	public static void main(String[] args) {
		System.out.println("Title is :"
				+ ActiveWindowTitle.getActiveWindowTitle());
	}

	public static String getActiveWindowTitle() {
		String titleStr = "";
		if (Platform.isWindows()) {
			HWND fgWindow = User32.INSTANCE.GetForegroundWindow();
			int titleLength = User32.INSTANCE.GetWindowTextLength(fgWindow) + 1;
			char[] title = new char[titleLength];
			User32.INSTANCE.GetWindowText(fgWindow, title, titleLength);
			titleStr = Native.toString(title);
		} else if (Platform.isLinux()) {
			// Possibly most of the Unix systems
			// will work here too, e.g. FreeBSD
			final X11 x11 = X11.INSTANCE;
			final XLib xlib = (XLib) XLib.INSTANCE;
			X11.Display display = x11.XOpenDisplay(null);
			X11.Window window = new X11.Window();
			xlib.XGetInputFocus(display, window, Pointer.NULL);
			X11.XTextProperty name = new X11.XTextProperty();
			x11.XGetWMName(display, window, name);
			titleStr = name.toString();
		} else if (Platform.isMac()) {
			final String script = "tell application \"System Events\"\n"
					+ "\tname of application processes whose frontmost is tru\n"
					+ "end";
			ScriptEngine appleScript = new ScriptEngineManager()
					.getEngineByName("AppleScript");
			try {
				titleStr = (String) appleScript.eval(script);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		} else {
			titleStr = "Plamtform is not Support";
		}
		return titleStr;
	}

	interface XLib extends X11 {
		void XGetInputFocus(Display display, Window window, Pointer pointer);
	}
}
