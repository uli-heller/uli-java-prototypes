package org.uli.hexdump;

public class HexDump {
    static private final int DEFAULT_BYTES_PER_LINE=16;
    private int bytesPerLine;

    static public String hexDump(byte[] array) {
	return hexDump(array, 0, array.length);
    }

    static public String hexDump(byte[] array, int start, int length) {
	HexDump hd = new HexDump();
	return hd.dump(array, start, length);
    }

    public HexDump() {
	this(DEFAULT_BYTES_PER_LINE);
    }

    public HexDump(int bytesPerLine) {
	this.bytesPerLine = bytesPerLine;
    }

    private String dump(byte[] array, int start, int length) {
	StringBuilder sb = new StringBuilder(length*3+100);
	int remainingLength = length;
	int thisLineStart   = start;
	while (remainingLength > 0) {
	    StringBuffer hexBuffer = new StringBuffer(3*this.bytesPerLine);
	    StringBuffer textBuffer = new StringBuffer(this.bytesPerLine);
	    sb.append(address(thisLineStart));
	    sb.append(": ");
	    int i=0;
	    for (; i<remainingLength && i<this.bytesPerLine; i++) {
		if (i%4 == 0) {
		  hexBuffer.append(" ");
		}
		hexBuffer.append(String.format("%02x", array[thisLineStart+i]));
		textBuffer.append(text(array[thisLineStart+i]));
	    }
	    for ( ; i<this.bytesPerLine; i++) {
		if (i%4 == 0) {
		  hexBuffer.append(" ");
		}
		hexBuffer.append("  ");
	    }
	    sb.append(hexBuffer);
	    sb.append("  '");
	    sb.append(textBuffer);
	    sb.append("'");
	    sb.append(System.getProperty("line.separator"));
	    thisLineStart += this.bytesPerLine;
	    remainingLength -= this.bytesPerLine;
	}
	return sb.toString();
    }

    private String address(int start) {
	return String.format("%08x", start);
    }

    private String text(byte n) {
	byte[] b = new byte[1];
	b[0] = n;
	return new String(b);
    }

    public static void main(String[] args) {
	System.out.println(HexDump.hexDump(args[0].getBytes()));
    }
}
