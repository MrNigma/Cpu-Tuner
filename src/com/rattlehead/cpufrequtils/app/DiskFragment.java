package com.rattlehead.cpufrequtils.app;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.rattlehead.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.rattlehead.cpufrequtils.app.utils.Constants;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;
import com.rattlehead.cpufrequtils.app.utils.RootUtils;

public class DiskFragment extends SherlockFragment {
	View mView;
	Context mContext;
	AbstractWheel diskScheduler, readAhead;
	String[] availableSchedulers, readAheadValues;
	ArrayWheelAdapter<String> schedulerAdapter, readAheadAdapter;
	String currentScheduler;
	ArrayList<String> schedulers = new ArrayList<String>();
	ArrayList<String> availableReadAheadValues = new ArrayList<String>();
	Button applyButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		availableSchedulers = CpuUtils.getAvailableIOScheduler();
		schedulers = CpuUtils.toArrayList(availableSchedulers);
		currentScheduler = CpuUtils.getCurrentIOScheduler();
		readAheadValues = new String[] { "32", "64", "128", "256", "512",
				"1024", "2048", "3072", "4096" };
		availableReadAheadValues = CpuUtils.toArrayList(readAheadValues);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.disk_control_fragment, container,
				false);
		mContext = mView.getContext();
		applyButton = (Button) mView.findViewById(R.id.disk_apply);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		diskScheduler = (AbstractWheel) mView.findViewById(R.id.ioscheduler);
		readAhead = (AbstractWheel) mView.findViewById(R.id.readahead1);
		schedulerAdapter = new ArrayWheelAdapter<String>(mContext,
				availableSchedulers);
		schedulerAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
		schedulerAdapter.setItemTextResource(R.id.text);
		diskScheduler.setViewAdapter(schedulerAdapter);
		diskScheduler.setCurrentItem(schedulers.indexOf(currentScheduler));

		readAheadAdapter = new ArrayWheelAdapter<String>(mContext,
				readAheadValues);
		readAheadAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
		readAheadAdapter.setItemTextResource(R.id.text);
		readAhead.setViewAdapter(readAheadAdapter);
		readAhead.setCurrentItem(availableReadAheadValues.indexOf(CpuUtils
				.getDefaultReadAhead()));

		applyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (RootUtils.isRooted()) {
					CpuUtils.setDiskSchedulerandReadAhead(schedulers
							.get(diskScheduler.getCurrentItem()),
							availableReadAheadValues.get(readAhead
									.getCurrentItem()));
				}
				else {
					new RootNotFoundAlertDialog().show(getFragmentManager(),
							Constants.App_Tag);
				}
			}
		});

	}
}
