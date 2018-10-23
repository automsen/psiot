package cn.com.tw.common.utils.tools.license;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bouncycastle.crypto.digests.MD5Digest;

import com.verhas.utils.Sugar;

public class HardwareBinder {
	private boolean useHostName;
	private boolean useNetwork;
	private boolean useArchitecture;
	private final Set<String> allowedInterfaceNames;
	private final Set<String> deniedInterfaceNames;

	public HardwareBinder() {
		this.useHostName = true;

		this.useNetwork = true;

		this.useArchitecture = true;
		
		this.allowedInterfaceNames = new HashSet<String>();
		this.deniedInterfaceNames = new HashSet<String>();
	}

	public void ignoreHostName() {
		this.useHostName = false;
	}

	public void ignoreNetwork() {
		this.useNetwork = false;
	}

	public void ignoreArchitecture() {
		this.useArchitecture = false;
	}
	
	private int numberOfInterfaces() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		int interfaceCounter = 0;
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = (NetworkInterface) networkInterfaces
					.nextElement();
			if (weShouldUseForTheCalculationThis(networkInterface)) {
				++interfaceCounter;
			}
		}
		return interfaceCounter;
	}

	public void interfaceAllowed(String regex) {
		this.allowedInterfaceNames.add(regex);
	}

	public void interfaceDenied(String regex) {
		this.deniedInterfaceNames.add(regex);
	}

	private boolean matchesRegexLists(NetworkInterface networkInterface) {
		String interfaceName = networkInterface.getDisplayName();

		return ((!(Sugar.matchesAny(interfaceName, this.deniedInterfaceNames))) && (((this.allowedInterfaceNames
				.size() == 0) || (Sugar.matchesAny(interfaceName,
				this.allowedInterfaceNames)))));
	}

	private boolean weShouldUseForTheCalculationThis(
			NetworkInterface networkInterface) throws SocketException {
		return ((!(networkInterface.isLoopback()))
				&& (!(networkInterface.isVirtual()))
				&& (!(networkInterface.isPointToPoint())) && (matchesRegexLists(networkInterface)));
	}

	private NetworkInterfaceData[] networkInterfaceData()
			throws SocketException {
		NetworkInterfaceData[] networkInterfaceArray = new NetworkInterfaceData[numberOfInterfaces()];
		int index = 0;

		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = (NetworkInterface) networkInterfaces
					.nextElement();
			if (weShouldUseForTheCalculationThis(networkInterface)) {
				networkInterfaceArray[index] = new NetworkInterfaceData(
						networkInterface);

				++index;
			}
		}
		return networkInterfaceArray;
	}

	private void sortNetworkInterfaces(
			NetworkInterfaceData[] networkInterfaceData) {
		Arrays.sort(networkInterfaceData, new HardComparator());
	}

	private void updateWithNetworkData(MD5Digest md5,
			NetworkInterfaceData[] networkInterfaces)
			throws UnsupportedEncodingException {
		for (NetworkInterfaceData ni : networkInterfaces) {
			
			if (ni.hwAddress != null){
				//System.out.println(bytesToString(ni.hwAddress));
				md5.update(ni.name.getBytes("utf-8"), 0, ni.name.getBytes("utf-8").length);
				md5.update(ni.hwAddress, 0, ni.hwAddress.length);
			}
		}
	}

	private void updateWithNetworkData(MD5Digest md5)
			throws UnsupportedEncodingException, SocketException {
		NetworkInterfaceData[] networkInterfaces = networkInterfaceData();
		sortNetworkInterfaces(networkInterfaces);
		updateWithNetworkData(md5, networkInterfaces);
	}

	private void updateWithHostName(MD5Digest md5) throws UnknownHostException,
			UnsupportedEncodingException {
		String hostName = InetAddress.getLocalHost().getHostName();
		md5.update(hostName.getBytes("utf-8"), 0,
				hostName.getBytes("utf-8").length);
	}

	private void updateWithArchitecture(MD5Digest md5)
			throws UnsupportedEncodingException {
		String architectureString = System.getProperty("os.arch");
		md5.update(architectureString.getBytes("utf-8"), 0,
				architectureString.getBytes("utf-8").length);
	}
	
	public UUID getMachineId() throws Exception {
		MD5Digest md5 = new MD5Digest();
		md5.reset();
		if (this.useNetwork) {
			updateWithNetworkData(md5);
		}
		if (this.useHostName) {
			updateWithHostName(md5);
		}
		if (this.useArchitecture) {
			updateWithArchitecture(md5);
		}
		byte[] digest = new byte[16];
		md5.doFinal(digest, 0);
		return UUID.nameUUIDFromBytes(digest);
	}

	public String getMachineIdString() throws Exception {
		UUID uuid = getMachineId();
		if (uuid != null) {
			return uuid.toString();
		}
		return null;
	}

	public boolean assertUUID(UUID uuid) throws Exception {
		UUID machineUUID = getMachineId();
		if (machineUUID == null) {
			return false;
		}
		return machineUUID.equals(uuid);
	}

	public boolean assertUUID(String uuid) {
		try {
			return assertUUID(UUID.fromString(uuid));
		} catch (Exception e) {
		}
		return false;
	}
	
	public String getOsName() {
        String os = "";
        os = System.getProperty("os.name");
        return os;
    }

	public static void main(String[] args) throws Exception {
		HardwareBinder hb = new HardwareBinder();
		System.out.print(hb.getMachineIdString());
	}

	private class NetworkInterfaceData {
		String name;
		byte[] hwAddress;

		public NetworkInterfaceData(NetworkInterface paramNetworkInterface)
				throws SocketException {
			this.name = paramNetworkInterface.getName();
			this.hwAddress = paramNetworkInterface.getHardwareAddress();
		}
	}
	
	@SuppressWarnings("unused")
	private static String bytesToString(byte[] bytes){
	    if (bytes == null || bytes.length == 0) {
	        return null ;
	    }
	    StringBuilder buf = new StringBuilder();
	    for (byte b : bytes) {
	        buf.append(String.format("%02X:", b));
	    }
	    if (buf.length() > 0) {
	        buf.deleteCharAt(buf.length() - 1);
	    }
	    return buf.toString();
	}

	
	private class HardComparator implements Comparator<HardwareBinder.NetworkInterfaceData>{

		@Override
		public int compare(HardwareBinder.NetworkInterfaceData a,
				HardwareBinder.NetworkInterfaceData b) {
			return a.name.compareTo(b.name);
		}

	}
}