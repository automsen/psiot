package cn.com.tw.common.utils.tools.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtils {  
	
	/**
	 * 定义密钥位数（512/768/1024/2048位），最小位数要大于等于512位
	 */
	public static final int KEY_BIT_LOW_SEC = 512;
	public static final int KEY_BIT_MID_SEC= 768;
	public static final int KEY_BIT_NORMAL_SEC = 1024;
	public static final int KEY_BIT_HIGH_SEC = 2048;
	
	/**
	 * 定义公钥/私钥键名称
	 */
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";
	
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> map = getKeyPair();
		Key key = (Key) map.get(PUBLIC_KEY);
		System.out.println(key); 
		String keystr = (new BASE64Encoder()).encodeBuffer(key.getEncoded());
		System.out.println(keystr); 
		
		byte[] keystrd = new BASE64Decoder().decodeBuffer(keystr);
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keystrd);
		//PKCS8EncodedKeySpec pkc = new PKCS8EncodedKeySpec(keystrd);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
		Key key1 = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		System.out.println(key1);
		/*
		Key key = (Key) map.get(PUBLIC_KEY); 
        byte[] publicKey = key.getEncoded();
        System.out.println(key);
        Key key1 = (Key) map.get(PRIVATE_KEY);
        String enStr = Base64.encodeBase64String(key1.getEncoded());
		System.out.println(Base64.encodeBase64String(key1.getEncoded()));
		byte[] decodeStr = Base64.decodeBase64(enStr);
		System.out.println(new String());*/
	    //saveKey(map.get(PUBLIC_KEY), "e:/serPublic.key");
		//saveKey(map.get(PUBLIC_KEY), "serv_key_Public.key");
		//saveKey(map.get(PRIVATE_KEY), "serv_key_Private.key");
		//saveKey(map.get(PRIVATE_KEY), "e:/serPrivate.key");
		//readKey("c://serPublic.key");
	}
	
	/**
	 * 通过字符串秘钥生成秘钥对象
	 * @param securKey
	 * @return
	 */
	public static PublicKey getPublicKeyByStr(String securKey){
		if(StringUtils.isEmpty(securKey)){
			return null;
		}
		
		try {
			byte[] keystrd = new BASE64Decoder().decodeBuffer(securKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keystrd);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
			Key key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			return (PublicKey) key;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 通过字符串秘钥生成秘钥对象
	 * @param securKey
	 * @return
	 */
	public static PrivateKey getPrivateKeyByStr(String securKey){
		
		if(StringUtils.isEmpty(securKey)){
			return null;
		}
		
		try {
			byte[] keystrd = new BASE64Decoder().decodeBuffer(securKey);
			PKCS8EncodedKeySpec pkc = new PKCS8EncodedKeySpec(keystrd);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
			Key key = (RSAPrivateKey) keyFactory.generatePrivate(pkc);
			return (PrivateKey) key;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
  
    /** 
     * 生成公钥和私钥（默认密钥长度1024位）
     * @throws NoSuchAlgorithmException  無效算法異常
     * @throws InvalidKeyLengthException 無效密鑰長度異常
     * 
     */  
    public static HashMap<String, Object> getKeyPair(){  
        try {
			return RSAUtils.getKeyPair(KEY_BIT_NORMAL_SEC);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        return null;
    }  
    
	/**
	 * 生成公钥和私钥
	 * 
	 * @param keyLength  密钥位数（512/768/1024/2048位，位数越小效率越高，但加密强度就越低）
	 * @return HashMap<String, Object> 包含公钥和私钥的HashMap
	 * @throws NoSuchAlgorithmException 抛出无效算法的异常
	 * @throws InvalidKeyLengthException 抛出无效密钥位数异常
	 */
	public static HashMap<String, Object> getKeyPair(int keyLength)
			throws NoSuchAlgorithmException, InvalidKeyLengthException {
		if (keyLength != RSAUtils.KEY_BIT_LOW_SEC
				& keyLength != RSAUtils.KEY_BIT_MID_SEC
				& keyLength != RSAUtils.KEY_BIT_NORMAL_SEC
				& keyLength != RSAUtils.KEY_BIT_HIGH_SEC) {
			throw new InvalidKeyLengthException();
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");//指定生成RSA非对称密钥对
			keyPairGen.initialize(keyLength);//指定密钥位数
			KeyPair keyPair = keyPairGen.generateKeyPair();//生成密钥对
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();//取得公钥
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();//取得私钥
			map.put(PUBLIC_KEY, publicKey);//将公钥放入map中
			map.put(PRIVATE_KEY, privateKey);//将私钥放入map中
			return map;
		}
    }
	
	/**
	 * 保存密钥到文件中
	 * @param key key
	 * @param keyName 保存文件名
	 * @throws Exception Exception
	 */
	public static void saveKey(Object key,String keyName) throws Exception{ 
		//写入文件中
	    FileOutputStream fosKey=new FileOutputStream(keyName);  
	    ObjectOutputStream oosKey=new ObjectOutputStream(fosKey);  
	    oosKey.writeObject(key);  
	    oosKey.close();  
	    fosKey.close();  
	}  
	
	 /**  
     * 从文件中读取密钥 
     * @param keyName  
     * @return Key  
     * @throws Exception  
     */  
    public static Object readKey(String keyName) throws Exception{  
        FileInputStream fisKey=new FileInputStream(keyName);  
        ObjectInputStream oisKey=new ObjectInputStream(fisKey);  
        Object key=oisKey.readObject();  
        oisKey.close();  
        fisKey.close();  
        return key;  
    }  
	
    /** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus  模 
     * @param exponent 指数 
     * @return RSAPublicKey RSA公钥
     */  
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {  
            BigInteger modulusBI = new BigInteger(modulus); //取得模
            BigInteger exponentBI = new BigInteger(exponent);  //取得公钥指数
            try {
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
				RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulusBI, exponentBI);  //通过模和公钥指数创建一个新的RSA公钥规格
				return (RSAPublicKey) keyFactory.generatePublic(keySpec);  //从所提供的密钥规格生成一个公钥对象
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    }  
    
    /** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus  模 
     * @param exponent 指数 
     * @return RSAPublicKey RSA公钥
     */  
    public static RSAPublicKey getPublicKey(byte[] modulus, byte[] exponent) {  
            BigInteger modulusBI = new BigInteger(1, modulus); //取得模
            BigInteger exponentBI = new BigInteger(1, exponent);  //取得公钥指数
            try {
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
				RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulusBI, exponentBI);  //通过模和公钥指数创建一个新的RSA公钥规格
				return (RSAPublicKey) keyFactory.generatePublic(keySpec);  //从所提供的密钥规格生成一个公钥对象
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    } 
    
    public static String encrypt(String data, String modulus, String exponent) {   
        try {   
            byte[] modulusBytes = Base64Utils.decode(modulus);   
            byte[] exponentBytes = Base64Utils.decode(exponent);   
            BigInteger modulusBI = new BigInteger(1, modulusBytes);   
            BigInteger exponentBI = new BigInteger(1, exponentBytes);   
  
            RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulusBI, exponentBI);   
            KeyFactory fact = KeyFactory.getInstance("RSA");   
            PublicKey pubKey = fact.generatePublic(rsaPubKey);   
  
            System.out.println(pubKey);
            Cipher cipher = Cipher.getInstance("RSA");   
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);   
  
            byte[] cipherData = cipher.doFinal(data.getBytes());   
            
            return StringUtil.bcd2Str(cipherData);   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        return null;   
  
    }   
    
  
    /** 
     * 使用模和指数生成RSA私钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus  模 
     * @param exponent 指数 
     * @return RSAPrivateKey RSA私钥
     */  
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {  
            BigInteger modulusBI = new BigInteger(modulus);  //取得模
            BigInteger exponentBI = new BigInteger(exponent);  //取得私钥指数
            try {
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
				RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulusBI, exponentBI);  //通过模和私钥指数创建一个新的RSA私钥规格
				return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  //从所提供的密钥规格生成一个私钥对象
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
     }  
  
	/**
	 * 使用公钥加密
	 * 
	 * @param data 待加密的明文数据
	 * @param publicKey 公钥
	 * @return String 加密后的密文数据
	 * @throws NoSuchAlgorithmException 无此算法异常
	 * @throws NoSuchPaddingException 无此padding异常
	 * @throws InvalidKeyException 无效密钥异常
	 * @throws IllegalBlockSizeException 非法的块大小异常
	 * @throws BadPaddingException 坏padding异常
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException
			 {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);//指定用公钥加密
		int keyLength = publicKey.getModulus().bitLength() / 8;// 模长（密钥长度）
		// 加密数据长度 <= 模长-11（这是因为非对称加密对数据长度有限制，一般就是模长-11）
		String[] datas = StringUtil.splitString(data, keyLength - 11);
		String passwordText = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			passwordText += StringUtil.bcd2Str(cipher.doFinal(s.getBytes()));//加密
		}
		return passwordText;
	}
  
	/**
	 * 使用私钥解密
	 * 
	 * @param data 待解密的密文数据
	 * @param privateKey 私钥
	 * @return String 解密后的明文数据
	 * @throws NoSuchAlgorithmException 无此算法异常
	 * @throws NoSuchPaddingException 无此padding异常
	 * @throws InvalidKeyException 无效密钥异常
	 * @throws IllegalBlockSizeException 非法的块大小异常
	 * @throws BadPaddingException 坏padding异常
	 */
	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		int keyLength = privateKey.getModulus().bitLength() / 8;		// 模长
		byte[] bytes = data.getBytes();
		byte[] bcd = StringUtil.ASCII_To_BCD(bytes, bytes.length);
		//System.err.println(bcd.length);
		String plainText = "";
		// 如果密文长度大于模长则要分组解密
		byte[][] arrays = StringUtil.splitArray(bcd, keyLength);
		for (byte[] arr : arrays) {
			plainText += new String(cipher.doFinal(arr));
		}
		return plainText;
	}  
	
	/**
	 * 使用私钥加密（即数字签名）
	 * @param data 待加密的数据
	 * @param privateKey 私钥
	 * @return String 加密后的数据
	 * @throws NoSuchAlgorithmException 无此算法异常
	 * @throws NoSuchPaddingException 无此padding异常
	 * @throws InvalidKeyException 无效密钥异常
	 * @throws IllegalBlockSizeException 非法的块大小异常
	 * @throws BadPaddingException 坏padding异常
	 */
	public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		int keyLength = privateKey.getModulus().bitLength() / 8;// 模长（密钥长度）
		// 加密数据长度 <= 模长-11（这是因为非对称加密对数据长度有限制，一般就是模长-11）
		String[] datas = StringUtil.splitString(data, keyLength - 11);
		String passwordText = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			passwordText += StringUtil.bcd2Str(cipher.doFinal(s.getBytes()));// 加密
		}
		return passwordText;
	}
	
	/**
	 * 使用公钥解密（即验证数字签名）
	 * @param data 待解密的数据
	 * @param publicKey 公钥
	 * @return String 解密后的数据
	 * @throws NoSuchAlgorithmException 无此算法异常
	 * @throws NoSuchPaddingException 无此padding异常
	 * @throws InvalidKeyException 无效密钥异常
	 * @throws IllegalBlockSizeException 非法的块大小异常
	 * @throws BadPaddingException 坏padding异常
	 */
	public static String decryptByPublicKey(String data, RSAPublicKey publicKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
			IllegalBlockSizeException, BadPaddingException{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		int keyLength = publicKey.getModulus().bitLength() / 8;		// 模长
		byte[] bytes = data.getBytes();
		byte[] bcd = StringUtil.ASCII_To_BCD(bytes, bytes.length);
		//System.err.println(bcd.length);
		String plainText = "";
		// 如果密文长度大于模长则要分组解密
		byte[][] arrays = StringUtil.splitArray(bcd, keyLength);
		for (byte[] arr : arrays) {
			plainText += new String(cipher.doFinal(arr));
		}
		return plainText;
	}
	
	/**
	 * 使用私鑰簽名
	 * @param message 待簽名的信息
	 * @param key RSA私鑰
	 * @return byte[] 簽名
	 * @throws NoSuchAlgorithmException 無效算法異常
	 * @throws InvalidKeyException 無效密鑰異常
	 * @throws SignatureException 簽名異常
	 * @throws UnsupportedEncodingException 不支持的編碼異常
	 */
	public static byte[] sign(String message, RSAPrivateKey key)
			throws NoSuchAlgorithmException, InvalidKeyException,
			SignatureException, UnsupportedEncodingException {
		Signature signetcheck = Signature.getInstance("MD5withRSA");
		signetcheck.initSign(key);
		signetcheck.update(message.getBytes("ISO-8859-1"));
		return signetcheck.sign();
	}
	
	/**
	 * 使用公鑰驗證簽名
	 * @param message 待驗證的信息
	 * @param signStr 簽名
	 * @param key 公鑰
	 * @return boolean 驗證是否成功
	 * @throws MissParameterException 參數缺失異常
	 * @throws NoSuchAlgorithmException 無效算法異常
	 * @throws SignatureException 簽名異常
	 * @throws UnsupportedEncodingException 不支持的編碼異常
	 * @throws InvalidKeyException 無效密鑰異常
	 */
	public boolean verifySign(String message, String signStr, RSAPublicKey key)
			throws NoSuchAlgorithmException,
			SignatureException, UnsupportedEncodingException,
			InvalidKeyException {
		if (message == null || signStr == null || key == null) {
			throw new RuntimeException();
		}
		Signature signetcheck = Signature.getInstance("MD5withRSA");//默認使用MD5對信息進行摘要，然後使用RSA對信息摘要進行加密
		signetcheck.initVerify(key);
		signetcheck.update(message.getBytes("ISO-8859-1"));//轉換成字節數組時默認使用ISO-8859-1字符集
		return signetcheck.verify(StringUtil.hex2byte(signStr));//將簽名還原成字節數組後進行驗證
	}
	
	/**
	 * 將密鑰保存到文件中
	 * @param key 公鑰/私鑰
	 * @param fileName 要保存密鑰的文件名
	 * @throws FileNotFoundException 文件沒找到異常
	 * @throws IOException I/O異常
	 */
	public void saveToFile(Object key, String fileName)
			throws FileNotFoundException, IOException {
		ObjectOutputStream output = new ObjectOutputStream(
				new FileOutputStream(fileName));
		output.writeObject(key);
		output.close();
	}
	
	/**
	 * 從文件中裝載密鑰
	 * @param fileName 要提取公鑰/私鑰的文件
	 * @return Object 提取到的公鑰/私鑰
	 * @throws IOException I/O異常
	 * @throws FileNotFoundException 文件沒找到異常 
	 * @throws ClassNotFoundException 類沒找到異常
	 */
	public Object loadFromFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(
		fileName));
		Object obj = input.readObject();
		input.close();
		return obj;
	}

	/**
	 * 处理xml
	 * @param xml
	 * @return
	 */
	/*public static RSAPublicKey decodePublicKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, Base64Utils.decode(StringHelper
                .GetMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1,
        		Base64Utils.decode(StringHelper.GetMiddleString(xml,
                        "<Exponent>", "</Exponent>")));

       RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus,
                publicExponent);

       KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey)keyf.generatePublic(rsaPubKey);
        } catch (Exception e) {
            return null;
        }
    }*/

}
