package kstn.game.app.network;

import android.util.Log;

import kstn.game.app.event.LLBaseEventType;
import kstn.game.app.event.LLEventData;
import kstn.game.app.event.LLEventManager;
import kstn.game.app.event.LLListener;
import kstn.game.app.network.events.UDPReceiveData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.UDPManager;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static kstn.game.app.network.NetworkUtil.bigEndianToByte;

public class UDPBaseManager implements UDPManager, Runnable {
	private int port;
	int sendPort; // normal is equal to port, but may be different when test
    private int hostIpAddress;
	private int broadcastAddr;
	private byte[] tmpAddr = new byte[4];

	// Can be null
	private DatagramSocket socket = null;
	private final Thread thread;
	private volatile boolean running;
	private final Map<Integer, EventData.Parser> parserMap = new HashMap<>();

	// Only use in game thread
	private ByteArrayOutputStream broadcastByteArray = new ByteArrayOutputStream();
	private final LLEventManager llEventManager;

    private OnReceiveDataListener receiveDataListener = null;

	UDPBaseManager(int hostIP, int port, int mask,
                   Map<EventType, EventData.Parser> parserMap,
                   LLEventManager llEventManager) throws SocketException {
		this.port = port;
		this.sendPort = port;
		this.hostIpAddress = hostIP;
		this.llEventManager = llEventManager;

        this.llEventManager.addListener(LLBaseEventType.UDP_RECEIVE_DATA,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        EventData eventData = ((UDPReceiveData) event).getEvent();
                        if (receiveDataListener != null) {
                            receiveDataListener.onReceiveData(eventData);
                        }
                    }
                });

		// Copy parser maps
		for (Map.Entry<EventType, EventData.Parser> entry: parserMap.entrySet()) {
		    this.parserMap.put(entry.getKey().getValue(), entry.getValue());
        }

		this.broadcastAddr = (hostIP & mask) | ~mask;
		socket = new DatagramSocket(port);
		socket.setBroadcast(true);
		thread = new Thread(this);
		this.running = true;
		thread.start();
	}

	@Override
	public void shutdown() {
        try {
            broadcastByteArray.close();
        } catch (IOException e) {
        }

        if (socket != null)
			socket.close();

		this.running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
	}

	@Override
    public void setReceiveDataListener(OnReceiveDataListener listener) {
        this.receiveDataListener = listener;
    }

	// Run on another thread
	@Override
	public void run() {
		while (this.running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				continue; // Simply ignore
			}

			byte[] bytesIp = packet.getAddress().getAddress();
			int ipAddress = NetworkUtil.bytesToInt(bytesIp);
			if (hostIpAddress == ipAddress && port == packet.getPort())
			    continue;

            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(packet.getData(),
                            packet.getOffset(), packet.getLength());
            DataInputStream eventIdReader = new DataInputStream(inputStream);

            int eventId;
            try {
                eventId = eventIdReader.readInt();
            } catch (IOException e) {
                continue;
            }
            EventData.Parser parser = parserMap.get(eventId);
            if (parser == null)
                throw new RuntimeException("Can't find parser");

            EventData event;
            try {
                event = parser.parseFrom(inputStream);
            } catch (IOException e) {
                continue;
            }
            llEventManager.queue(new UDPReceiveData(event));
		}
	}

	@Override
	public void broadcast(EventData event) {
		bigEndianToByte(broadcastAddr, tmpAddr);
		InetAddress ip = null;
		try {
			ip = InetAddress.getByAddress(tmpAddr);
		} catch (UnknownHostException e) {
			// Simply ignore
			return;
		}

		broadcastByteArray.reset();
        DataOutputStream eventIdWriter = new DataOutputStream(broadcastByteArray);
		try {
            eventIdWriter.writeInt(event.getEventType().getValue());
			event.serialize(broadcastByteArray);
		} catch (IOException e) {
			// Simply ignore
			return;
		}

		byte[] data = broadcastByteArray.toByteArray();

		DatagramPacket send = 
				new DatagramPacket(data, data.length, ip, sendPort);
		
		try {
			socket.send(send);
		} catch (IOException e) {
			// Simply ignore
			return;
		}
	}

}
