package cn.com.tw.engine.core.enm;

public enum ModeEnum {
	//以太网主动连接主动采集，
	//以太网被动连接主动采集，
	//gprs被动连接主动采集，
	//gprs被动连接被动采集，
	//wifi被动连接被动接受， 
	//lorawan主动连接主动采集被动返回数据
	//zk_lorawan_active_get 中科院对接，主动连接 主动采集，主动拉数据的方式
	etnet_active_get, etnet_passive_getByGroup, etnet_passive_get, etnet_passive_rev, gprs_passive_get, gprs_passive_rev, wifi_passive_rev, lorawan_active_get, zk_lorawan_active_get, ali_mq_lorawan_get_get, etnet_gprs_passive_rev;
}
