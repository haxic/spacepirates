package test;

public class IntToByteConversionTest {
	public static void main(String[] args) {
		byte[] sendData = new byte[504];
		int value = Integer.parseInt("24");
		System.out.println((byte) ((value >> 24) & 0xFF));
		byte[] inInt = intToByteArray(value, 0);
		for (int i = 0; i < 3; i++) {
			sendData[i] = inInt[i];
			System.out.println(sendData[i]);
		}
		int test = byteArrayToInt(new byte[] { sendData[0], sendData[1], sendData[2], sendData[3] }, 0);
		System.out.println("Value:" + value + " inInt.length:" + inInt.length + " Test:" + test);
	}

	public static int byteArrayToInt(byte[] encodedValue, int start) {
		int value = (encodedValue[start + 3] << (Byte.SIZE * 3));
		value |= (encodedValue[start + 2] & 0xFF) << (Byte.SIZE * 2);
		value |= (encodedValue[start + 1] & 0xFF) << (Byte.SIZE * 1);
		value |= (encodedValue[start] & 0xFF);
		return value;
	}

	public static byte[] intToByteArray(int value, int start) {
		byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
		encodedValue[start + 3] = (byte) (value >> Byte.SIZE * 3);
		encodedValue[start + 2] = (byte) (value >> Byte.SIZE * 2);
		encodedValue[start + 1] = (byte) (value >> Byte.SIZE);
		encodedValue[start] = (byte) value;
		return encodedValue;
	}
}
