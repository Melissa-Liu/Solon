package com.bme.solon;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bme.solon.bluetooth.BluetoothService;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for {@link RecyclerView} specifically for scanned devices
 */
public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ScanListViewHolder> {
    private static final String TAG = "ScanListAdapter";

    private List<BluetoothDevice> deviceList;
    private AlertDialog dialog;
    private BluetoothService btService;

    /**
     * ViewHolder specific for scanned devices.
     */
    public static class ScanListViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;

        /**
         * Constructor
         * @param itemView  View object
         */
        public ScanListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.connect_scan_list_name);
            address = itemView.findViewById(R.id.connect_scan_list_address);
        }

        /**
         * Bind device data to the appropriate TextViews.
         * @param device    Device to show
         */
        public void bindData(BluetoothDevice device) {
            Log.d(TAG, "bindData");
            if (device != null) {
                Log.d(TAG, "bindData: device - " + device.getName() + " " + device.getAddress());
                if (device.getName() == null || device.getName().length() <= 0) {
                    name.setText(R.string.connect_scan_no_name);
                }
                else {
                    name.setText(device.getName());
                }
                address.setText(device.getAddress());
                //TODO: refer to database to lookup names
            }
        }
    }

    /**
     * OnClickListener for each List item in the RecyclerView.
     */
    public class ScanListListener implements View.OnClickListener {
        private int position;

        /**
         * Constructor
         * @param position      Position in RecyclerView list
         */
        public ScanListListener(int position) {
            this.position = position;
        }

        /**
         * Try to connect with device
         * @param view  View that was clicked
         */
        @Override
        public void onClick(View view) {
            dialog.dismiss(); //this should cancel Bluetooth discovery automatically

            //Start the connection
            BluetoothDevice device = deviceList.get(position);
            btService.connectToDevice(device);
            Log.d(TAG,"onClick: device = " + device.toString());
        }
    }

    /**
     * Constructor
     * @param dialog    The dialog this adapter is in
     * @param btService Bound service
     */
    public ScanListAdapter(AlertDialog dialog, BluetoothService btService) {
        super();
        deviceList = new ArrayList<>();
        this.dialog = dialog;
        this.btService = btService;
    }

    /**
     * Create a new ScanListViewHolder
     * @param parent        The ViewGroup that the new View will be added to
     * @param viewType      View type of the new View
     * @return              New ScanListViewHolder
     */
    @Override
    @NonNull
    public ScanListViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        Log.v(TAG, "onCreateViewHolder: viewType " + viewType);
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_scan_list, parent, false);
        return new ScanListViewHolder(view);
    }

    /**
     * Bind data to existing ScanListViewHolder and attach click listener
     * @param holder    Holder to bind data to
     * @param position  Position of data to bind
     */
    @Override
    public void onBindViewHolder(@NonNull final ScanListViewHolder holder, final int position) {
        Log.v(TAG, "onBindViewHolder: position " + position);
        holder.bindData(deviceList.get(position));
        holder.itemView.setOnClickListener(new ScanListListener(position));
    }

    /**
     * Get the total number of BluetoothDevices
     * @return  Number of BluetoothDevices
     */
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: # is " + deviceList.size());
        return deviceList.size();
    }

    /**
     * Add new BluetoothDevice to the list
     * @param device    Device to add
     */
    public void addDevice(BluetoothDevice device) {
        Log.d(TAG, "addDevice");
        if (!deviceList.contains(device)) {
            Log.d(TAG, "addDevice: new device " + device.toString() + " added");
            deviceList.add(device);
        }
    }
}
