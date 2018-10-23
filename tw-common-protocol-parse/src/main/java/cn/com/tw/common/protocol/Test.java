package cn.com.tw.common.protocol;

import cn.com.tw.common.protocol.utils.ByteUtils;

public class Test {

	public static void main(String[] args) {
//		
//		 HashMap<String,String> test=InstructionFormat.formatMap();
//		for(String temp:test.keySet()){
//			System.out.println(test.get(temp));
//		}
//		ParseOpera opera = ParseFactory.build(ParseEnum.Dlt6452007);

		ParseOpera opera = ParseFactory.build(ParseEnum.GEHUA);
		
//		//加密充值
//		opera.encode("000000200508","03","070101FF","20","13");
		//加密通电
//		opera.encode("000000247293","03","ABABABAA","00000002","01");
//		opera.encode("000000247293","03","ABABABBB","00000002","01");
//		opera.encode("000000247293","03","ABABABAA","00000002","02");
//		opera.encode("000000247293","03","ABABABBB","00000002","02");
//		opera.encode("000000247293","03","ABABABAA","00000002","03");
//		opera.encode("000000247293","03","ABABABBB","00000002","03");
//		opera.encode("000000247293","03","04100001","00000002","1806250100");
//		opera.encode("000000247293","11","04100001");
		
//		opera.encode("000000247293","03","ABABABAA","00000002","00");
//		opera.encode("000000247293","03","ABABABBB","00000002","00");
//		opera.encode("000000247293","11","04601002");
//		opera.encode("020000000003","14","0460AA02","00000002","AAAA");
		//不加密断电
//		opera.encode("111111111111","1C","00000002","1C");
//		opera.encode("111111111111","1C","00000002","1A");
		//读取总电能
//		opera.encode("000000243019","11","04601001");
//		opera.encode("110000000001","14","04700102","00000002","10");
//		opera.encode("110000000001","11","04700102");
//		opera.encode("110000000001","11","04700103");
//		opera.encode("110000000001","14","04700104","00000002","1","1","2","000000211930","1","000000211931","1");
//		opera.encode("110000000001","14","04700105","00000002","1","1","1","000000211930","1");
//		opera.encode("110000000001","14","04700106","00000002","1");
//		opera.encode("110000000001","11","04700107","1");
//		opera.encode("110000000001","11","04700108","1","1","2");
//		opera.encode("110000000001","14","04700109","00000002","1","1","2","000000211930","1","000000211931","1");
//		opera.encode("110000000001","14","0470010A","00000002","1","1","1","000000211930","1","000000211932","1");
//		opera.encode("110000000001","14","04700201","00000002","2","0000FF00","0201FF00");
//		opera.encode("110000000001","11","04700201");
		
//		String rep0 = "6801000000001168d10526496768352b16";
		String rep = "21025b9b5608000000000000ffffffffffffffffffffff";
//		String rep = "fefefefe6803000000000268940a35dd9337933333333433e216";
//		opera.decode(ByteUtils.toByteArray(rep0));
//		
		opera.decode(ByteUtils.toByteArray(rep));
//		opera.decode(ByteUtils.toByteArray(rep1));

	}
}
