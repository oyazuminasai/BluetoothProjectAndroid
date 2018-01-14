package com.example.camilo.bluetoothserver;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    int contVideo1,contVideo2,contVideo3,contVideo4,contVideo5,contVideo6,contVideo7,contVideo8,contVideo9;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_ENABLE_BT = 1;
    private final int numOfAllowDisp= 6;
    private BluetoothAdapter mBluetoothAdapter;
    ArrayList<BluetoothSocket> connectedDevices = new ArrayList();
    ArrayList<String> connectedDevicesString = new ArrayList();
    ArrayAdapter<String> aConnectedDevices;
    Handler handlerContDevices;
    ImageButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
    Button btnReporte;
    private int handlerConnected = 1;
    ListView listviewConnectedDevices;
    boolean isAfolder = false;
    AcceptThread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MAiNACTIVTY INICIO: ", "");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        verifyStoragePermissions(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            this.finish();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        handlerContDevices = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerConnected) {
                    Log.d("HANDLER MAINACTIVITY: ","SOCKET AÑADIDO");
                    BluetoothSocket socket = (BluetoothSocket) msg.obj;
                    connectedDevices.add(socket);
                    connectedDevicesString.add(socket.getRemoteDevice().getAddress());
                    aConnectedDevices = new ArrayAdapter<>(getBaseContext(),R.layout.device_item,connectedDevicesString);
                    listviewConnectedDevices.setAdapter(aConnectedDevices);

                }
            }
        };
        btnReporte = (Button) findViewById(R.id.btn_reporte);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*try {File file = new File(getBaseContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "rep");
                    File file2 = new File(file,"REPORTE.txt");
                    String contentFile = "Video 1 : " + contVideo1 + "\n" + "Video 2 : " + contVideo2;
                    FileWriter writer = new FileWriter(file2);
                    writer.append(contentFile);
                    writer.flush();
                    writer.close();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("/");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file2));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                File file = new File (Environment.getExternalStorageDirectory(),"REPORTE.txt");
                Log.d("MAINACTIVITY : FILE", file.getAbsolutePath());
                try {
                    file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    String contentFile = "Video 1 : " + contVideo1 + "\n" + "Video 2 : " + contVideo2;
                    outputStream.write(contentFile.getBytes());
                    outputStream.close();
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("*/*");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    String title = getResources().getString(R.string.chooser_title);
                    // Create intent to show the chooser dialog
                    Intent chooser = Intent.createChooser(sendIntent, title);
                    // Verify the original intent will resolve to at least one activity
                    if (sendIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(chooser);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        listviewConnectedDevices = (ListView) findViewById(R.id.listConnectedDevices);
        btn1 = (ImageButton) findViewById(R.id.numero_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contVideo1++;
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("1");
                }
            }
        });
        btn2 = (ImageButton) findViewById(R.id.numero_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contVideo2++;
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("2");
                }
            }
        });
        btn3 = (ImageButton) findViewById(R.id.numero_3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("3");
                }
            }
        });
        btn4 = (ImageButton) findViewById(R.id.numero_4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("4");
                }
            }
        });
        btn5 = (ImageButton) findViewById(R.id.numero_5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("5");
                }
            }
        });
        btn6 = (ImageButton) findViewById(R.id.numero_6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("6");
                }
            }
        });
        btn7 = (ImageButton) findViewById(R.id.numero_7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("7");
                }
            }
        });
        btn8 = (ImageButton) findViewById(R.id.numero_8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("8");
                }
            }
        });
        btn9 = (ImageButton) findViewById(R.id.numero_9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothSocket socket : connectedDevices){
                    ConnectedThread ct = new ConnectedThread(socket);
                    ct.write("9");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED){
            String files[] = Environment.getExternalStorageDirectory().list();
            for (int i = 0; i < files.length; i++){
                if(files[i].equals("SecurityFolder")){
                    isAfolder = true;
                }
            }
            if(!isAfolder){
                this.finish();
            }
        }
        if(mBluetoothAdapter.isEnabled()) {
            t = new AcceptThread();
            t.start();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        t.cancel();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private final UUID uuid = UUID.fromString("ecebdad1-89b2-4519-884d-411fe22ed15b");

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(this.getName(), uuid);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {

            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    //connectedDevicesString.add(socket.getRemoteDevice().getAddress());
                    //Integer size = connectedDevices.size();
                    Log.d("MainActivity"," " + connectedDevices.size());
                    handlerContDevices.obtainMessage(handlerConnected,socket).sendToTarget();
                    // Do work to manage the connection (in a separate thread)
                    //manageConnectedSocket(socket);
                    try {
                        if(connectedDevices.size() == numOfAllowDisp){
                            Log.d("MainActivity","IF CLOSE SOCKET");
                            mmServerSocket.close();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                      //      .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String bytes) {
            try {
                mmOutStream.write(bytes.getBytes());
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
