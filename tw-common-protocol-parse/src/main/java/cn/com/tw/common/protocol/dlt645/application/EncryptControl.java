package cn.com.tw.common.protocol.dlt645.application;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 加密控制类指令
 * 
 * @author Cheng Qi Peng
 *
 */
public class EncryptControl {
	/**
	 * 下发编码
	 * 
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException {
		// 数据标识
		String dataMarker = (String) params[2];
		// 密码
		String password = (String) params[3];
		// 回路(默认01)
		// NN=01代表主回路 （对单回路表、双回路表、三回路表有效）
		// NN=02代表副回路 （对双回路表和三回路表有效）
		// NN=03代表外置继电器 （对三回路表有效）
		// NN=00代表全部继电器 （对双回路表和三回路表有效）
		String loop = "01";
		if (params.length > 4) {
			loop = (String) params[4];
		}
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "00000000";
		// 操作类型
		String controlType = dataMarker.substring(6, 8);

		// "563412"为固定值
		String dataValues = loop + "563412" + controlType;
		return ByteUtils.inverted(dataMarker) + ByteUtils.inverted(password) + operatorCode + dataValues;
	}

	/**
	 * 仪表重置
	 * 
	 * @param params
	 * @return
	 * @throws ProtocolException
	 */
	public static String reset(Object... params) throws ProtocolException {
		// 数据标识
		String dataMarker = (String) params[2];
		// 密码
		String password = (String) params[3];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "00000000";
		// "00000000"为固定值
		String dataValues = "00000000";
		return ByteUtils.inverted(dataMarker) + ByteUtils.inverted(password) + operatorCode + dataValues;
	}

	/**
	 * 正常返回无数据项 无需解析
	 * 
	 * @param param
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) {
		return param;
	}
}
