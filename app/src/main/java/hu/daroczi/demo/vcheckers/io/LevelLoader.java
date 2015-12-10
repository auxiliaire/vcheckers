package hu.daroczi.demo.vcheckers.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class LevelLoader {

	private static final String LEVEL_EXTENSION = ".xml";
	private static final String LEVEL_PREFIX = "level_";
	private static final String ns = null;

	private int level;

	public static class Cell {
		public final int row;
		public final int col;
		public final int value;

		private Cell(String row, String col, String value) {
			this.row = Integer.parseInt(row);
			this.col = Integer.parseInt(col);
			this.value = Integer.parseInt(value);
		}
	}

	public LevelLoader(int level) {
		this.setLevel(level);
	}

	public List<Cell> loadLevel(Context context) {
		InputStream in = null;
		String filename = getFilename();
		try {
			// TODO: res/raw alá átrakni
			in = context.getAssets().open(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Cell> table = null;
		try {
			table = parse(in);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return table;
	}

	public int getLevel() {
		return level;
	}

	private String getFilename() {
		return (LEVEL_PREFIX + String.valueOf(getLevel()) + LEVEL_EXTENSION);
	}

	public List<Cell> parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readTable(parser);
		} finally {
			in.close();
		}
	}

	private List<Cell> readTable(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Cell> rows = new ArrayList<Cell>();

		parser.require(XmlPullParser.START_TAG, ns, "table");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals("row")) {
				rows.addAll(readRow(parser));
			} else {
				skip(parser);
			}
		}
		return rows;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

	private List<Cell> readRow(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Cell> cells = new ArrayList<Cell>();
		parser.require(XmlPullParser.START_TAG, ns, "row");
		String row = parser.getAttributeValue(null, "position");
		// MyLog.logMessage("row: " + row);
		String col = null;
		String value = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("cell")) {
				col = readCol(parser);
				// MyLog.logMessage("col: " + col);
				value = readValue(parser);
				// MyLog.logMessage("value: " + value);
				cells.add(new Cell(row, col, value));
			} else {
				skip(parser);
			}
		}
		return cells;
	}

	private String readValue(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "cell");
		String value = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "cell");
		return value;
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private String readCol(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "cell");
		String col = parser.getAttributeValue(null, "position");
		return col;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
