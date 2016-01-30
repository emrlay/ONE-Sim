/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package ui;

import java.util.Vector;

import report.Report;
import core.ApplicationListener;
import core.ConnectionListener;
import core.MessageListener;
import core.MovementListener;
import core.Settings;
import core.SettingsError;
import core.SimClock;
import core.SimError;
import core.SimScenario;
import core.UpdateListener;
import core.World;

/**
 * Abstract superclass for user interfaces; contains also some simulation
 * settings.
 * DTNSimUI是抽象类，具体的实现有命令行和GUI两种
 */
public abstract class DTNSimUI {
	/** 
	 * Number of reports -setting id ({@value}). Defines how many reports
	 * are loaded. 
	 */
	public static final String NROF_REPORT_S = "Report.nrofReports";
	/** 
	 * Report class name -setting id prefix ({@value}). Defines name(s) of
	 * the report classes to load. Must be suffixed with numbers starting from
	 * one.
	 */
	public static final String REPORT_S = "Report.report";
	/**
	 * Movement model warmup time -setting id ({@value}). Defines how many
	 * seconds of movement simulation is run without connectivity etc. checks 
	 * before starting the real simulation.
	 */
	public static final String MM_WARMUP_S = 
		movement.MovementModel.MOVEMENT_MODEL_NS + ".warmup";
	
	/** report class' package name */
	private static final String REPORT_PAC = "report.";

	/** The World where all actors of the simulator are */
	protected World world;
	
	/** Reports that are loaded for this simulation */
	protected Vector<Report> reports;
	
	/** has simulation terminated normally */
	protected boolean simDone;
	
	/** is simulation termination requested */
	protected boolean simCancelled;
	
	/** Scenario of the current simulation */
	protected SimScenario scen;
	
	/** simtime of last UI update */
	protected double lastUpdate;
	
	/**
	 * Constructor.
	 */
	public DTNSimUI() {
		this.lastUpdate = 0;
		this.reports = new Vector<Report>();
		this.simDone = false;
		this.simCancelled = false;
	}
	
	/**
	 * Starts the simulation.
	 * 启动simulation，有两步，其一是初始化模型，其二是runSim
	 */
	public void start() {
		initModel();
		runSim();
	}
	
	/**
	 * Runs simulation after the model has been initialized.
	 * runSim是根据UI类型的不同，到具体的子类中实现的
	 */
	protected abstract void runSim();
	
	/**
	 * Initializes the simulator model.
	 */
	private void initModel() {
		Settings settings = null;
				
		try {
			//是创建了一个新的settings对象
			settings = new Settings();
			//SimScenario.getInstance方法获得了用于模拟的场景对象，这一方法确保场景是singleton的
			this.scen = SimScenario.getInstance();

			// add reports
			//获取到所有的report类（将类名读到String），并添加到report类的vector中去
			//同时，根据其类型的不同（具体是哪一种Listener），添加到场景中去
			for (int i=1, n = settings.getInt(NROF_REPORT_S); i<=n; i++){
				String reportClass = settings.getSetting(REPORT_S + i);
				addReport((Report)settings.createObject(REPORT_PAC + 
						reportClass));	
			}

			double warmupTime = 0;
			//如果移动模型有“预热时间”，那么把模拟器的时钟回拨，以实现预热效果
			if (settings.contains(MM_WARMUP_S)) {
				warmupTime = settings.getDouble(MM_WARMUP_S);
				if (warmupTime > 0) {
					SimClock c = SimClock.getInstance();
					c.setTime(-warmupTime);
				}
			}

			//world对象在场景被创建时，即在构造器中创建了
			this.world = this.scen.getWorld();
			//移动模型的预热
			world.warmupMovementModel(warmupTime);
		}
		catch (SettingsError se) {
			System.err.println("Can't start: error in configuration file(s)");
			System.err.println(se.getMessage());
			System.exit(-1);			
		}
		catch (SimError er) {
			System.err.println("Can't start: " + er.getMessage());
			System.err.println("Caught at " + er.getStackTrace()[0]);
			System.exit(-1);
		}		
	}
	
	/**
	 * Runs maintenance jobs that are needed before exiting.
	 *模拟执行完毕，此时，执行（输出）所有的报告
	 */
	public void done() {
		for (Report r : this.reports) {
			r.done();
		}
	}
	
	/**
	 * Adds a new report for simulator
	 * @param r Report to add
	 */
	protected void addReport(Report r) {
		if (r instanceof MessageListener) {
			scen.addMessageListener((MessageListener)r);
		}
		if (r instanceof ConnectionListener) {
			scen.addConnectionListener((ConnectionListener)r);
		}
		if (r instanceof MovementListener) {
			scen.addMovementListener((MovementListener)r);
		}
		if (r instanceof UpdateListener) {
			scen.addUpdateListener((UpdateListener)r);
		}
		if (r instanceof ApplicationListener) {
			scen.addApplicationListener((ApplicationListener)r);
		}

		this.reports.add(r);
	}
}
