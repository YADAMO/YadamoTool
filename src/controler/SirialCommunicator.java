package controler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SirialCommunicator {
	private CommPortIdentifier portID;
	private SerialPort port;
	private BufferedReader reader;

	/**
	 * COM??????????????????????????????
	 * @return portArray
	 */
	public List<String> getComPort(){
		List<String> portArray = new ArrayList<String>();

		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier portID;
		while(portList.hasMoreElements()){
			// ???????????????????????????????????????
			portID = (CommPortIdentifier)portList.nextElement();

			if(!portID.isCurrentlyOwned()){
				if(portID.getPortType() == CommPortIdentifier.PORT_SERIAL){
					portArray.add(portID.getName());
				}
			}
		}

		return portArray;
	}

	/**
	 * ??????????????????
	 * @param portNum
	 * @return
	 */
	public Boolean openComport(String portNum){
		try {
			portID =  CommPortIdentifier.getPortIdentifier(portNum);
			port = (SerialPort) portID.open("YADAMO", 5000);

			//??????????????????????????????????????????????????????????????????????????????????????????
			port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE );

			//???????????????????????????
			port.setFlowControlMode( SerialPort.FLOWCONTROL_NONE );
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			return false;
		} catch (PortInUseException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
			return false;
		}
		port.setDTR(true);
		port.setRTS(false);
		return true;
	}

	public void close(){
		if(port != null){
			port.close();
		}
	}

	/**
	 * ??????
	 * @param text
	 */
	public void submit(String text){
		OutputStream output = null;
		try {
			byte[] data = text.getBytes();
			output = port.getOutputStream();
			//???????????????????????????????????????????????????????????????
			Thread.sleep(1000);
			output.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			port.close();
		}
	}

	public void read(){
		CreateFile file = new CreateFile();
		int data[] = new int[12];
		InputStream in = null;
		int timeOffset = 0;
		boolean flag = false;
		try {
			in = port.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(port.getInputStream())); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		int numRead[] = new int[34];

		int i = 0;
		while (true) {

			if(i >= 34){
				System.out.println(" ");
				i = 0;
				file.addData(data);
				for(int j = 0; j < data.length; j++){

					System.out.print(data[j] + " ");
					data[j] = 0;
				}
			}

			try {
				numRead[i] = in.read();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (numRead[i] == -1) {
				break;
			} else if (numRead[i] == 0) {
			}

			switch(i){
			case 2:
				data[0] += numRead[i];
				break;
			case 3:
				data[0] += (numRead[i] * 256);
				break;
			case 4:
				data[0] += (numRead[i] * 256 * 256);
				break;
			case 5:
				data[0] += (numRead[i] * 256 * 256 * 256);
				if(!flag){
					timeOffset = data[0];
				}
				data[0] -= timeOffset;
				flag = true;
				break;
			case 6:
				data[1] += numRead[i];
				break;
			case 7:
				data[2] += numRead[i];
				break;
			case 8:
				data[3] += numRead[i];
				break;
			case 9:
				data[3] += numRead[i] * 256;
				break;
			case 10:
				data[4] += numRead[i];
				break;
			case 11:
				data[4] += numRead[i] * 256;
				break;
			case 12:
				data[4] += numRead[i] * 256 * 256;
				break;
			case 13:
				data[4] += numRead[i] * 256 * 256 * 256;
				break;
			case 14:
				data[5] += numRead[i];
				break;
			case 15:
				data[5] += numRead[i] * 16;
				break;
			case 16:
				data[5] += numRead[i] * 16 * 16;
				break;
			case 17:
				data[5] += numRead[i] * 16;
				break;
			case 18:
				data[6] += numRead[i];
				break;
			case 19:
				data[6] += numRead[i] * 256;
				break;
			case 20:
				data[6] += numRead[i] * 256 * 256;
				break;
			case 21:
				data[6] += numRead[i] * 256 * 256 * 256;
				break;
			case 22:
				data[7] += numRead[i];
				
				break;
			case 23:
				data[7] += numRead[i] * 256;
				data[7] = (short)data[7];
				break;
			case 24:
				data[8] += numRead[i];
				break;
			case 25:
				data[8] += numRead[i] * 256;
				data[8] = (short)data[8];
				break;
			case 26:
				data[9] += numRead[i];
				break;
			case 27:
				data[9] += numRead[i] * 256;
				break;
			case 28:
				data[10] += numRead[i];
				break;
			case 29:
				data[10] += numRead[i] * 256;
				break;
			case 30:
				data[11] += numRead[i];
				break;
			case 31:
				data[11] += numRead[i]*256;
				break;
			case 32:
				data[11] += numRead[i]*256 * 256;
				break;
			case 33:
				data[11] += numRead[i]*256 * 256 * 256;
				break;
			}
			i++;
		}
	}
}
