import com.dhht.common.CRC7;


public class Test5 {
	 static byte[] HEAD = new byte[]{(byte)0xAA,(byte)0x55};//报头
	 static byte FUNC_ADDCARD= (byte)0x08;//新增卡号--功能码
	 
    public static void main(String[] args) {  //12345678
         String hexStr = "07B0B26E";  //12345678转16进制后为07B0B26E
         byte[] bt = hexStringToByte(hexStr);
    	 System.out.println(bt.length);
    	 for(byte b:bt){
    		 System.out.println(Integer.toHexString((int)b)+"\t"+b);
    	 }
    	 System.out.println(BinaryToHexString(bt));
	}
    
    public static byte[] hexStringToByte(String hex) {
    	hex = hex.toUpperCase();
        int len = (hex.length() / 2);   
        byte[] result = new byte[len];   
        char[] achar = hex.toCharArray();   
        for (int i = 0; i < len; i++) {   
         int pos = i * 2;   
         result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));   
        }   
        return result;   
    }  
    private static byte toByte(char c) {   
        byte b = (byte) "0123456789ABCDEF".indexOf(c);   
        return b;   
    }
    
    public static String BinaryToHexString(byte[] bytes) {  
        String hexStr = "0123456789ABCDEF";  
        String result = "";  
        String hex = "";  
        for (byte b : bytes) {  
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));  
            hex += String.valueOf(hexStr.charAt(b & 0x0F));  
            result += hex;  
        }  
        return result;  
    } 
    
    
    public static byte[] addCard(String cardNo){
  		String hexStr = "";
  		String first = "";
  		first = Integer.toHexString(Integer.parseInt(cardNo.substring(0, 3)));
  		String completion1 ="";
  		if(first.length()<2){
			for(int i=1;i<=2-first.length();i++){
				completion1 += "0";
			}
			first = completion1+first;
		}
  		String second = "";
  		second = Integer.toHexString(Integer.parseInt(cardNo.substring(3, 8)));
  		String completion2 ="";
  		if(second.length()<4){
			for(int i=1;i<=4-second.length();i++){
				completion2 += "0";
			}
			second = completion2+second;
		}
  		hexStr = first+second;
		byte[] cardByte = hexStringToByte(hexStr);
		byte[] sendByte = new byte[8];
		sendByte[0] = HEAD[0];
		sendByte[1] = HEAD[1];
		sendByte[2] = FUNC_ADDCARD;
		sendByte[3] = (byte)0x03;
		System.arraycopy(cardByte, 0, sendByte, 4, cardByte.length);
		byte[] crcdata = new byte[5];
		crcdata[0] = sendByte[2];
		crcdata[1] = sendByte[3];
		System.arraycopy(cardByte, 0, crcdata, 2, cardByte.length);
		sendByte[7] = CRC7.CheckCode_CRC7(crcdata);
		return sendByte;
	}
}
