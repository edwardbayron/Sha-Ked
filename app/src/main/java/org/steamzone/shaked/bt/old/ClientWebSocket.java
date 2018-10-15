package org.steamzone.shaked.bt.old;


//FIXME:!!!!!!!! Решить проблему с переподключением
//FIXME:!!!Добавить таймаут
//FIXME; Добавить пинг до сервера и по нему смотреть подключение

public class ClientWebSocket {
//
//    private static final String TAG = "Websocket";
//
//    //FIXME: добавить таймаут на подключение и ответ от сервера
//    private static final int TIMEOUT = 5000;
//    private static final int PING_INTERVAL = 1000;
//
//    boolean flag_connect_to_server;
//    boolean connect_to_server;
//    private Timer ReConnectTimer;
//    private TimerTask ReConnectTimerTask;
//
//
//  public enum socketConnectStatus
//   {
//       SOCKET_STATUS_DISCONNECT,
//       SOCKET_STATUS_CONNECT,
//       SOCKET_STATUS_CONNECT_ERR
//   }
//
//
//
//
//    WebSocket ws;
//
//    Activity activity;
//
//    private MessageListener listener;
//    String host;
//    String UserName;
//    String password;
//
//    public boolean send_string(String string)
//    {
//        //FIXME: проверять возможность отправки!! наличие подключения итп
//        Log.i(TAG, "Data Sending..");
//        ws.sendText(string);
//        return true;
//    }
//
//
//    public ClientWebSocket(Activity activity, MessageListener listener, String host, String UserName, String password) {
//        this.listener = listener;
//        this.host = host;
//        this.UserName = UserName;
//        this.password = password;
//        this.activity = activity;
//    }
//
//    public void connect()
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run() {
//                    try {
//                        ws = new WebSocketFactory().setConnectionTimeout(TIMEOUT).createSocket(host);
//                        ws.setUserInfo(UserName, password);
//                        // ws.addHeader("access-token", token);
//                        ws.addListener(new SoketListener());
//                        ws.setPingInterval(PING_INTERVAL);
//                        ws.connect();
//                        Log.i(TAG, "Connecting..");
//
//                        flag_connect_to_server = true;
//                        ReConnectTimerInit();
//                    }
//
//
//                    catch (OpeningHandshakeException e)
//                    {
//                        // A violation against the WebSocket protocol was detected
//                        // during the opening handshake.
//
//                        // Status line.
//                        StatusLine sl = e.getStatusLine();
//                        System.out.println("=== Status Line ===");
//                        System.out.format("HTTP Version  = %s\n", sl.getHttpVersion());
//                        System.out.format("Status Code   = %d\n", sl.getStatusCode());
//                        System.out.format("Reason Phrase = %s\n", sl.getReasonPhrase());
//
//                        listener.onSocketConnectStatus(SOCKET_STATUS_CONNECT_ERR, sl.getStatusCode());
//
//                        Log.e(TAG, "Socket connect error");
//
//                        // HTTP headers.
//                        Map<String, List<String>> headers = e.getHeaders();
//                        System.out.println("=== HTTP Headers ===");
//                        for (Map.Entry<String, List<String>> entry : headers.entrySet())
//                        {
//                            // Header name.
//                            String name = entry.getKey();
//
//                            // Values of the header.
//                            List<String> values = entry.getValue();
//
//                            if (values == null || values.size() == 0)
//                            {
//                                // Print the name only.
//                                System.out.println(name);
//                                continue;
//                            }
//
//                            for (String value : values)
//                            {
//                                // Print the name and the value.
//                                System.out.format("%s: %s\n", name, value);
//                            }
//                        }
//                    }
//                    catch (HostnameUnverifiedException e)
//                    {
//                        // The certificate of the peer does not match the expected hostname.
//                    }
//                    catch (WebSocketException e)
//                    {
//                        // Failed to establish a WebSocket connection.
//                    }
//
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//
//    private void reconnect() {
//        Log.e(TAG, "reconnect");
//        try {
//            ws = ws.recreate().connect();
//        } catch (WebSocketException e) {
//            if (flag_connect_to_server){
//                if(connect_to_server == false){
//                    //Log.e(TAG, "reconnect_F1");
//                     //reconnect();
//                }
//            }
//            e.printStackTrace();
//        } catch (IOException e) {
//            if (flag_connect_to_server){
//                if(connect_to_server == false){
//                    //Log.e(TAG, "reconnect_F2");
//                   // reconnect();
//                }
//            }
//            e.printStackTrace();
//        }
//    }
//
//    public WebSocket getConnection() {
//        return ws;
//    }
//
//    public void close() {
//        ws.disconnect();
//        flag_connect_to_server = false;
//        ReConnectTimerStop();
//    }
//
//
//    public class SoketListener extends WebSocketAdapter {
//
//        @Override
//        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
//            super.onConnected(websocket, headers);
//            Log.i(TAG, "onConnected");
//            connect_to_server = true;
//            listener.onSocketConnectStatus(SOCKET_STATUS_CONNECT, 0);
//        }
//
//        public void onTextMessage(WebSocket websocket, String message) {
//            listener.onSocketMessage(message);
//            Log.i(TAG, "Message --> " + message);
//        }
//
//        @Override
//        public void onError(WebSocket websocket, WebSocketException cause) {
//            Log.i(TAG, "Error -->" + cause.getMessage());
//            listener.onSocketConnectStatus(SOCKET_STATUS_DISCONNECT, 1);
//            reconnect();
//        }
//
//        @Override
//        public void onDisconnected(WebSocket websocket,
//                                   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
//                                   boolean closedByServer) {
//            Log.i(TAG, "onDisconnected");
//            connect_to_server = false;
//            listener.onSocketConnectStatus(SOCKET_STATUS_DISCONNECT, 0);
//            if (closedByServer) {
//                reconnect();
//            }
//        }
//
//        @Override
//        public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
//            Log.i(TAG, "Error -->" + cause.getMessage());
//            listener.onSocketConnectStatus(SOCKET_STATUS_DISCONNECT, 2);
//            reconnect();
//        }
//
//        @Override
//        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
//            super.onPongFrame(websocket, frame);
//            websocket.sendPing("Are you there?");
//        }
//    }
//
//
//    public interface MessageListener {
//        void onSocketMessage(String message);
//        void onSocketConnectStatus(socketConnectStatus socketConnectStatus, int Error);
//    }
//
//
//    private void ReConnectTimerInit()
//    {
//        ReConnectTimer = new Timer();
//        ReConnectTimerTask = new ReConnectTimerTaskH();
//
//        // delay 500ms, repeat in 1000ms
//        ReConnectTimer.schedule(ReConnectTimerTask, 5000, 5000);
//    }
//
//    private void ReConnectTimerStop()
//    {
//        if(ReConnectTimer != null){ReConnectTimer.cancel();}
//    }
//
//    class ReConnectTimerTaskH extends TimerTask {
//        @Override
//        public void run() {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //todo
//                    if (flag_connect_to_server){
//                        if(connect_to_server == false){
//                            Log.e(TAG, "Timer recconnect");
//                            Message msg = new Message();
//                            msg.arg1=1;	msg.arg2=0; handler.sendMessage(msg);
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//    Handler handler = new Handler(new Handler.Callback() {
//
//        @Override
//        public boolean handleMessage(Message msg)
//        {
//            if(msg.arg1 == 1) {
//                ws.disconnect();
//                //reconnect();
//                connect();
//            }
//
//            return true;
//        }
//    });
}
